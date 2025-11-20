package org.ln.noortools.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ln.noortools.SpringContext;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.tag.AbstractTag;

/**
 * Parses a rename template containing tags (e.g., <IncN:1>)
 * and applies them to a list of files according to the selected RenameMode.
 *
 * ✳️ Key features:
 * - Dynamically loads tag classes using reflection
 * - Retrieves the I18n bean from SpringContext
 * - Creates new RenamableFile copies (immutable update)
 * - Caches tag constructors to improve performance
 * 
 * @author Luca Noale
 */
public class StringParser {

    private static final String TAG_PACKAGE = "org.ln.noortools.tag.";
    private static final Map<String, Constructor<? extends AbstractTag>> TAG_CACHE = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(StringParser.class.getName());

    /**
     * Parses a rename template, evaluates every tag inside it and returns a new list
     * of {@link RenamableFile} with updated destination names. The original list is
     * never mutated.
     *
     * @param template the template containing plain text and tag placeholders
     * @param files    the files to rename
     * @param mode     controls whether the template applies to the full name, only the
     *                 base name, or only the extension
     * @return a new list of files with computed destination names; the original
     *         {@code files} list is returned untouched when the template cannot be parsed
     */
    public static List<RenamableFile> parse(
            String template,
            List<RenamableFile> files,
            RenameMode mode
    ) {
        if (template == null || files == null || files.isEmpty() || !isParsable(template)) {
            return files;
        }

        List<Object> templateComponents = tokenize(template);
        List<String> oldNames = getOldStrings(files, mode);
        List<String> newNames = getNewStrings(files, mode);

        initializeTags(templateComponents, files, oldNames, newNames);

        List<RenamableFile> updated = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            RenamableFile file = files.get(i);
            RenamableFile copy = new RenamableFile(file.getSource());

            String sourceName = file.getSource().getName();
            String base = baseNameOf(sourceName);
            String ext  = extensionOf(sourceName);

            StringBuilder destinationBuilder = new StringBuilder();
            for (Object component : templateComponents) {
                if (component instanceof AbstractTag tag) {
                    destinationBuilder.append(tag.getNewName(i));
                } else {
                    destinationBuilder.append(component.toString());
                }
            }

            String destinationName = switch (mode) {
                case FULL -> destinationBuilder.toString();
                case NAME_ONLY -> destinationBuilder + (ext.isEmpty() ? "" : "." + ext);
                case EXT_ONLY -> base + "." + destinationBuilder;
                default -> sourceName;
            };

            copy.setDestinationName(destinationName);
            updated.add(copy);
        }
        return updated;
    }

    private static void initializeTags(
            List<Object> components,
            List<RenamableFile> files,
            List<String> oldNames,
            List<String> newNames
    ) {
        for (Object component : components) {
            if (component instanceof AbstractTag tag) {
                tag.setOldNames(new ArrayList<>(oldNames));
                tag.setNewNames(new ArrayList<>(newNames));

                if (tag instanceof FileAwareTag fileAwareTag) {
                    fileAwareTag.setFilesContext(files);
                }

                if (tag instanceof ActionTag actionTag) {
                    ActionManager actionManager = SpringContext.getBean(ActionManager.class);
                    actionManager.registerActionTag(actionTag);
                }

                if (tag instanceof PerformTag performTag) {
                    PerformManager performManager = SpringContext.getBean(PerformManager.class);
                    performManager.registerActionTag(performTag);
                }

                tag.init();
            }
        }
    }

    // ========================================================================================
    // Internal utilities
    // ========================================================================================

    /**
     * Splits the template into plain text and tag components.
     *
     * @param template the user-provided rename template
     * @return a list containing literal strings and {@link AbstractTag} instances
     */
    private static List<Object> tokenize(String template) {
        List<Object> parts = new ArrayList<>();
        Matcher m = Pattern.compile("<[^>]+>|[^<]+").matcher(template);
        while (m.find()) {
            String token = m.group();
            if (token.startsWith("<")) {
                AbstractTag tag = createTag(token);
                if (tag != null) parts.add(tag);
            } else {
                parts.add(token);
            }
        }
        return parts;
    }


    /**
     * Dynamically creates a tag instance (e.g., <DecN:1> or <RandN:2>).
     * Uses reflection and retrieves I18n from Spring.
     * Constructor lookups are cached to boost performance.
     *
     * @param token the tag token including angle brackets (e.g., "<IncN:1>")
     * @return the concrete {@link AbstractTag} or {@code null} when parsing fails
     */
    @SuppressWarnings("unchecked")
    private static AbstractTag createTag(String token) {
        try {
            Matcher nameMatcher = Pattern.compile("(?<=<)[A-Za-z][A-Za-z0-9_]*(?=[:>])").matcher(token);
            if (!nameMatcher.find()) {
                return null;
            }
            String className = nameMatcher.group();

            Matcher argsMatcher = Pattern.compile("(?<=:)\\s*([^>:]+)\\s*(?=[:>])").matcher(token);
            List<Object> arguments = new ArrayList<>();

            while (argsMatcher.find()) {
                String rawArgument = argsMatcher.group(1).trim();
                try {
                    arguments.add(Integer.parseInt(rawArgument));
                } catch (NumberFormatException e) {
                    arguments.add(rawArgument);
                }
            }

            I18n i18n = SpringContext.getBean(I18n.class);
            Object[] constructorArgs = arguments.toArray();

            Constructor<? extends AbstractTag> constructor = TAG_CACHE.get(className);

            if (constructor == null) {
                Class<?> clazz = Class.forName(TAG_PACKAGE + className);
                try {
                    constructor = (Constructor<? extends AbstractTag>) clazz.getDeclaredConstructor(I18n.class, Object[].class);
                } catch (NoSuchMethodException e) {
                    constructor = (Constructor<? extends AbstractTag>) clazz.getDeclaredConstructor();
                }
                TAG_CACHE.put(className, constructor);
                LOGGER.log(Level.FINE, () -> "Cached new tag class: " + className);
            }

            if (constructor.getParameterCount() == 2) {
                return constructor.newInstance(i18n, (Object) constructorArgs);
            }
            return constructor.newInstance();

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, () -> "Failed to create tag: " + token + " → " + e.getMessage());
            return null;
        }
    }


    // ========================================================================================
    // Helper methods

    public static boolean isParsable(String str) {
        return str != null && !str.isBlank() && count(str, '<') == count(str, '>');
    }

    private static int count(String s, char c) {
        int count = 0;
        for (char ch : s.toCharArray())
            if (ch == c) count++;
        return count;
    }

    private static String baseNameOf(String name) {
        int dot = name.lastIndexOf('.');
        return (dot > 0) ? name.substring(0, dot) : name;
    }

    private static String extensionOf(String name) {
        int dot = name.lastIndexOf('.');
        return (dot > 0 && dot < name.length() - 1) ? name.substring(dot + 1) : "";
    }

    private static List<String> getOldStrings(List<RenamableFile> files, RenameMode mode) {
        List<String> result = new ArrayList<>();
        for (RenamableFile f : files) {
            String src = f.getSource().getName();
            result.add(selectPart(src, mode));
        }
        return result;
    }

    private static List<String> getNewStrings(List<RenamableFile> files, RenameMode mode) {
        List<String> result = new ArrayList<>();
        for (RenamableFile f : files) {
            String dest = f.getDestinationName();
            if (dest == null || dest.isBlank()) dest = f.getSource().getName();
            result.add(selectPart(dest, mode));
        }
        return result;
    }

    private static String selectPart(String filename, RenameMode mode) {
        return switch (mode) {
            case FULL -> filename;
            case NAME_ONLY -> baseNameOf(filename);
            case EXT_ONLY -> extensionOf(filename);
        };
    }

    // ========================================================================================
    // Advanced utilities (for testing / debugging)
    // ========================================================================================

    /** Clears the internal tag constructor cache. */
    public static void clearCache() {
        TAG_CACHE.clear();
    }

    /** Returns the set of currently cached tag class names. */
    public static Set<String> getCachedTags() {
        return TAG_CACHE.keySet();
    }
}
