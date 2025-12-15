package org.ln.noortools.policy;

import java.io.File;

public class NoCheckPolicy implements DirectoryContentPolicy {

    @Override
    public boolean isValid(File directory) {
        return true;
    }

    @Override
    public String getErrorMessageKey() {
        return null;
    }
}
