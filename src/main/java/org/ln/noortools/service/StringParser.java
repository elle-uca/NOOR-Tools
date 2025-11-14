package org.ln.noortools.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ln.noortools.SpringContext;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.tag.FileAwareTag;

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

    /**
     * Parses a template and applies tags based on the given RenameMode.
     * Returns a new list of files; does not modify the original one.
     */
    public static List<RenamableFile> parse(
            String template,
            List<RenamableFile> files,
            RenameMode mode
    ) {
        if (template == null || files == null || files.isEmpty() || !isParsable(template))
            return files;

        // 1️⃣ Split the string into components (tags or plain text)
        List<Object> components = tokenize(template);

        // 2️⃣ Prepare name lists for tag initialization
        List<String> oldNames = getOldStrings(files, mode);
        List<String> newNames = getNewStrings(files, mode);
        
        
       // List<String> newNames = new ArrayList<>(oldNames);

     // 3️⃣ Initialize all tag objects
        for (Object comp : components) {
            if (comp instanceof AbstractTag tag) {
//                tag.setOldNames(oldNames);
//                tag.setNewNames(newNames);
            	tag.setOldNames(new ArrayList<>(oldNames));   // opzionale
            	tag.setNewNames(new ArrayList<>(oldNames));   // copia separata

                // NEW: passa la lista dei file ai tag che ne hanno bisogno
                if (tag instanceof FileAwareTag fat) {
                    fat.setFilesContext(files);
                   
                }

                tag.init();
            }
        }


        // 4️⃣ Build a new list (immutability guaranteed)
        List<RenamableFile> updated = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            RenamableFile file = files.get(i);
            RenamableFile copy = new RenamableFile(file.getSource());

            String sourceName = file.getSource().getName();
            String base = baseNameOf(sourceName);
            String ext  = extensionOf(sourceName);

            StringBuilder sb = new StringBuilder();
            for (Object comp : components) {
                if (comp instanceof AbstractTag tag) {
                    sb.append(tag.getNewName(i));
                } else {
                    sb.append(comp.toString());
                }
            }

            String result;
            switch (mode) {
                case FULL -> result = sb.toString();
                case NAME_ONLY -> result = sb + (ext.isEmpty() ? "" : "." + ext);
                case EXT_ONLY -> result = base + "." + sb;
                default -> result = sourceName;
            }

            copy.setDestinationName(result);
            updated.add(copy);
        }
        return updated;
    }

    // ========================================================================================
    // Internal utilities
    // ========================================================================================

    /**
     * Splits the template into plain text and tag components.
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
     */
//    @SuppressWarnings("unchecked")
//    private static AbstractTag createTag(String token) {
//        try {
//            // 1️⃣ Extract tag class name
//        	
//        	Matcher nameM = Pattern.compile("(?<=<)[A-Za-z][A-Za-z0-9_]*(?=[:>])").matcher(token);
//        	if (!nameM.find()) return null;
//        	String className = nameM.group();
////            Matcher nameM = Pattern.compile("(?<=<)[a-zA-Z]+(?=:)").matcher(token);
////            if (!nameM.find()) return null;
////            String className = nameM.group();
//
//            // 2️⃣ Extract numeric parameters (e.g., :1, :2)
//            Matcher argsM = Pattern.compile("(?<=:)\\d+(?=[>:])").matcher(token);
//            List<Object> args = new ArrayList<>();
//            while (argsM.find()) args.add(Integer.parseInt(argsM.group()));
//
//            // 3️⃣ Get I18n bean from Spring context
//            I18n i18n = SpringContext.getBean(I18n.class);
//            Object[] arr = args.toArray();
//
//            // 4️⃣ Get constructor from cache or via reflection
//            Constructor<? extends AbstractTag> ctor = TAG_CACHE.get(className);
//
//            if (ctor == null) {
//                Class<?> clazz = Class.forName(TAG_PACKAGE + className);
//                try {
//                    ctor = (Constructor<? extends AbstractTag>)
//                            clazz.getDeclaredConstructor(I18n.class, Object[].class);
//                } catch (NoSuchMethodException e) {
//                    ctor = (Constructor<? extends AbstractTag>)
//                            clazz.getDeclaredConstructor(); // fallback
//                }
//                TAG_CACHE.put(className, ctor);
//
//                // optional debug log
//                System.out.println("[StringParser] Cached new tag class: " + className);
//            }
//
//            // 5️⃣ Instantiate the tag
//            if (ctor.getParameterCount() == 2)
//                return ctor.newInstance(i18n,  arr);
//            else
//                return ctor.newInstance();
//
//            
//        } catch (Exception e) {
//            System.err.println("[StringParser] Failed to create tag: " + token + " → " + e.getMessage());
//            return null;
//        }
//    }

    
//    @SuppressWarnings("unchecked")
//    private static AbstractTag createTag(String token) {
//        try {
//            // Rimuovi i delimitatori <...>
//            if (!token.startsWith("<") || !token.endsWith(">")) return null;
//            String inner = token.substring(1, token.length() - 1).trim();
//
//            // 1) Nome classe: fino al primo ':' (se c'è), altrimenti tutto
//            int colon = inner.indexOf(':');
//            String className = (colon >= 0) ? inner.substring(0, colon) : inner;
//            if (className.isBlank()) return null;
//
//            // 2) Argomenti (se presenti), separati da ':'
//            List<Object> args = new ArrayList<>();
//            if (colon >= 0 && colon + 1 < inner.length()) {
//                String rest = inner.substring(colon + 1);
//                // supporta più argomenti: <Foo:a:b:3>
//                for (String raw : rest.split(":")) {
//                    String s = raw.trim();
//                    if (s.isEmpty()) continue; // ignora vuoti
//                    if (s.matches("-?\\d+")) {
//                        // intero
//                        args.add(Integer.valueOf(s));
//                    } else {
//                        // stringa
//                        args.add(s);
//                    }
//                }
//            }
//
//            // 3) I18n dal contesto Spring
//            I18n i18n = SpringContext.getBean(I18n.class);
//            Object[] arr = args.toArray();
//
//            // 4) Costruttore da cache o via reflection
//            Constructor<? extends AbstractTag> ctor = TAG_CACHE.get(className);
//            if (ctor == null) {
//                Class<?> clazz = Class.forName(TAG_PACKAGE + className);
//                try {
//                    ctor = (Constructor<? extends AbstractTag>)
//                            clazz.getDeclaredConstructor(I18n.class, Object[].class);
//                } catch (NoSuchMethodException e) {
//                    ctor = (Constructor<? extends AbstractTag>)
//                            clazz.getDeclaredConstructor(); // fallback
//                }
//                ctor.setAccessible(true);
//                TAG_CACHE.put(className, ctor);
//                System.out.println("[StringParser] Cached new tag class: " + className);
//            }
//
//            // 5) Istanziazione
//            if (ctor.getParameterCount() == 2) {
//                return ctor.newInstance(i18n, arr);
//            } else {
//                return ctor.newInstance();
//            }
//
//        } catch (Exception e) {
//            System.err.println("[StringParser] Failed to create tag: " + token + " → " + e.getMessage());
//            return null;
//        }
//    }

    @SuppressWarnings("unchecked")
    private static AbstractTag createTag(String token) {
        try {
            // 1️⃣ Nome della classe del tag, es. "WriteAlbum" da <WriteAlbum:Pippo>
            Matcher nameM = Pattern.compile("(?<=<)[A-Za-z][A-Za-z0-9_]*(?=[:>])").matcher(token);
            if (!nameM.find()) return null;
            String className = nameM.group();

            // 2️⃣ Estrazione argomenti (possono essere numeri o stringhe)
            // Esempi validi:
            // <IncN:1:2> → ["1", "2"]
            // <Date:dd-MMM-yyyy> → ["dd-MMM-yyyy"]
            // <WriteAlbum:Pippo> → ["Pippo"]
            Matcher argsM = Pattern.compile("(?<=:)\\s*([^>:]+)\\s*(?=[:>])").matcher(token);
            List<Object> args = new ArrayList<>();

            while (argsM.find()) {
                String raw = argsM.group(1).trim();
                // prova a convertirlo in numero, altrimenti tienilo come stringa
                try {
                    args.add(Integer.parseInt(raw));
                } catch (NumberFormatException e) {
                    args.add(raw);
                }
            }

            // 3️⃣ Ottieni il bean I18n da Spring
            I18n i18n = SpringContext.getBean(I18n.class);
            Object[] arr = args.toArray();

            // 4️⃣ Recupera (o crea) il costruttore dal cache
            Constructor<? extends AbstractTag> ctor = TAG_CACHE.get(className);

            if (ctor == null) {
                Class<?> clazz = Class.forName(TAG_PACKAGE + className);
                try {
                    ctor = (Constructor<? extends AbstractTag>)
                            clazz.getDeclaredConstructor(I18n.class, Object[].class);
                } catch (NoSuchMethodException e) {
                    ctor = (Constructor<? extends AbstractTag>)
                            clazz.getDeclaredConstructor(); // fallback
                }
                TAG_CACHE.put(className, ctor);
                System.out.println("[StringParser] Cached new tag class: " + className);
            }

            // 5️⃣ Istanzia il tag
            if (ctor.getParameterCount() == 2)
                return ctor.newInstance(i18n, (Object) arr);
            else
                return ctor.newInstance();

        } catch (Exception e) {
            System.err.println("[StringParser] Failed to create tag: " + token + " → " + e.getMessage());
            return null;
        }
    }

    
    
    // ========================================================================================
    // Helper methods
    // ========================================================================================

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
