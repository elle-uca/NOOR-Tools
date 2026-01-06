package org.ln.noor.core.policy;

import java.io.File;
/**
 * Ensures that a directory holds at least one subdirectory before a workflow
 * continues. This protects components that rely on nested paths being present.
 *
 * @author Luca Noale
 */

public class MustContainDirectoriesPolicy implements DirectoryContentPolicy {

    @Override
    public boolean isValid(File directory) {
        File[] dirs = directory.listFiles(File::isDirectory);
        return dirs != null && dirs.length > 0;
    }

    @Override
    public String getErrorMessageKey() {
        return "error.directory.noDirectories";
    }
}
