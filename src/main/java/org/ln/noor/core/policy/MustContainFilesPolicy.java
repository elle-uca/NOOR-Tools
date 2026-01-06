package org.ln.noor.core.policy;

import java.io.File;
/**
 * Validates that a directory contains at least one regular file before tool
 * workflows proceed. This guards operations that assume a non-empty file list.
 *
 * @author Luca Noale
 */

public class MustContainFilesPolicy implements DirectoryContentPolicy {

    @Override
    public boolean isValid(File directory) {
        File[] files = directory.listFiles(File::isFile);
        return files != null && files.length > 0;
    }

    @Override
    public String getErrorMessageKey() {
        return "error.directory.noFiles";
    }
}
