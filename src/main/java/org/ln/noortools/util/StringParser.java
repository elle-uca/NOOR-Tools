//package org.ln.noortools.util;
//
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.ln.noortools.model.RenamableFile;
//import org.ln.noortools.tag.AbstractTag;
//
//public class StringParser {
//
//    private static final String TAG_PACKAGE = "org.ln.noortools.tag.";
//
//    /**
//     * Parses a template containing tags like <RandN:3> and updates the file list.
//     * The template is applied to the base name (without extension); the original
//     * extension is appended back to build the final destination name.
//     */
//    public static List<RenamableFile> parse(String template, List<RenamableFile> files) {
//        if (template == null || files == null) return files;
//
//        // Split into chunks: <tag> or plain text
//        List<Object> components = tokenize(template);
//
//        // Build vector inputs (base names)
//        List<String> oldNames = getOldBaseNames(files);
//        List<String> currentNames = getCurrentBaseNames(files); // from destinationName if present, else base
//
//        // Initialize tags and collect components
//        for (Object comp : components) {
//            if (comp instanceof AbstractTag tag) {
//                tag.setOldNames(oldNames);
//                tag.setNewNames(currentNames);
//                tag.init();
//            }
//        }
//
//        // Recompose each file's destination name: resultBase + '.' + ext
//        for (int i = 0; i < files.size(); i++) {
//            RenamableFile f = files.get(i);
//            String name = f.getSource().getName();
//            String base = baseNameOf(name);
//            String ext  = extensionOf(name); // without dot
//
//            StringBuilder sb = new StringBuilder();
//            for (Object comp : components) {
//                if (comp instanceof AbstractTag tag) {
//                    sb.append(tag.getNewName(i));
//                } else {
//                    sb.append(comp.toString());
//                }
//            }
//
//            String finalBase = sb.toString();
//            String finalName = ext.isEmpty() ? finalBase : (finalBase + "." + ext);
//            f.setDestinationName(finalName);
//        }
//
//        return files;
//    }
//
//    // --------------- helpers ---------------
//
//    private static List<Object> tokenize(String template) {
//        List<Object> parts = new ArrayList<>();
//        Pattern p = Pattern.compile("<[^>]+>|[^<]+");
//        Matcher m = p.matcher(template);
//
//        while (m.find()) {
//            String token = m.group();
//            if (token.startsWith("<")) {
//                AbstractTag tag = createTag(token);
//                if (tag != null) parts.add(tag);
//            } else {
//                parts.add(token);
//            }
//        }
//        return parts;
//    }
//
//    /** Create a tag instance from a token like "<IncN:1:1>" using reflection. */
//    private static AbstractTag createTag(String token) {
//        try {
//            // tag name
//            Matcher nameM = Pattern.compile("(?<=<)[a-zA-Z]+(?=:)").matcher(token);
//            if (!nameM.find()) return null;
//            String className = nameM.group();
//
//            // numeric params
//            Matcher argsM = Pattern.compile("(?<=:)\\d+(?=[>:])").matcher(token);
//            List<Integer> args = new ArrayList<>();
//            while (argsM.find()) args.add(Integer.parseInt(argsM.group()));
//            Integer[] arr = args.toArray(Integer[]::new);
//
//            Class<?> clazz = Class.forName(TAG_PACKAGE + className);
//            return (AbstractTag) clazz.getDeclaredConstructor(Integer[].class)
//                                      .newInstance((Object) arr);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static boolean isParsable(String str) {
//        return str != null && !str.isEmpty() && count(str, '<') == count(str, '>');
//    }
//
//    private static int count(String s, char c) {
//        int k = 0;
//        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == c) k++;
//        return k;
//    }
//
//    // ---- name splitting (no external utils) ----
//
//    private static String baseNameOf(String filename) {
//        int dot = filename.lastIndexOf('.');
//        return (dot <= 0) ? filename : filename.substring(0, dot);
//    }
//
//    private static String extensionOf(String filename) {
//        int dot = filename.lastIndexOf('.');
//        if (dot <= 0 || dot == filename.length() - 1) return "";
//        return filename.substring(dot + 1); // no dot
//    }
//
//    // ---- vectors for tags ----
//
//    /** Original base names (no extension) */
//    private static List<String> getOldBaseNames(List<RenamableFile> files) {
//        List<String> list = new ArrayList<>(files.size());
//        for (RenamableFile f : files) {
//            String name = f.getSource().getName();
//            list.add(baseNameOf(name));
//        }
//        return list;
//    }
//
//    /** Current base names (prefer destinationName if set; else original base) */
//    private static List<String> getCurrentBaseNames(List<RenamableFile> files) {
//        List<String> list = new ArrayList<>(files.size());
//        for (RenamableFile f : files) {
//            String dest = f.getDestinationName();
//            if (dest != null && !dest.isBlank()) {
//                list.add(baseNameOf(dest));
//            } else {
//                list.add(baseNameOf(f.getSource().getName()));
//            }
//        }
//        return list;
//    }
//}
