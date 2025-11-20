package org.ln.noortools.util;

/**
 * Utility class for working with file names and extensions.
 *
 * Provides safe methods to split and combine base name and extension.
 */
public final class FileNameUtil {

    private FileNameUtil() {}
        // prevent instantiation
    

    /**
     * Returns the base name of a file (without extension).
     *
     * Examples:
     *   "document.txt" -> "document"
     *   "archive.tar.gz" -> "archive.tar"
     *   "file" -> "file"
     */
    public static String getBaseName(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName; // no extension
        }
        return fileName.substring(0, dotIndex);
    }

    /**
     * Returns the extension of a file (without dot).
     *
     * Examples:
     *   "document.txt" -> "txt"
     *   "archive.tar.gz" -> "gz"
     *   "file" -> ""
     */
    public static String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // no extension or trailing dot
        }
        return fileName.substring(dotIndex + 1);
    }

    /**
     * Combines a base name and extension into a full file name.
     *
     * @param base the base name (e.g., "document")
     * @param extension the extension (e.g., "txt" or ".txt")
     * @return combined name (e.g., "document.txt")
     */
    public static String combine(String base, String extension) {
        if (base == null) base = "";
        if (extension == null || extension.isEmpty()) {
            return base;
        }

        // remove leading dot if present
        if (extension.startsWith(".")) {
            extension = extension.substring(1);
        }

        return base + "." + extension;
    }

    /**
     * Checks if a file name has an extension.
     */
    public static boolean hasExtension(String fileName) {
        return !getExtension(fileName).isEmpty();
    }
}
