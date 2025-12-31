package org.ln.noor.core.policy;

import java.io.File;
/**
 * NoCheckPolicy.
 *
 * @author Luca Noale
 */

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
