package org.ln.noortools.util;

import java.io.File;

/**
 * Utility class for file name and extension operations.
 */
public final class FileUtil {

    private FileUtil() {
        // utility class, no instances
    }

    /**
     * Gets the extension of a file (without the dot).
     * Example: "file.txt" -> "txt"
     */
    public static String getExtension(File file) {
        if (file == null || file.getName() == null) return "";
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex >= 0 && dotIndex < name.length() - 1)
                ? name.substring(dotIndex + 1)
                : "";
    }

    /**
     * Gets the file name without extension.
     * Example: "file.txt" -> "file"
     */
    public static String getNameWithoutExtension(File file) {
        if (file == null || file.getName() == null) return "";
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex >= 0) ? name.substring(0, dotIndex) : name;
    }
}
