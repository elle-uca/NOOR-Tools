package org.ln.noortools.policy;

import java.io.File;

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
