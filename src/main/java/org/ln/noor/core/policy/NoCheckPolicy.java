package org.ln.noor.core.policy;

import java.io.File;
/**
 * Skips directory validation while satisfying the {@link DirectoryContentPolicy}
 * contract. Useful when a tool must operate regardless of the initial
 * filesystem state.
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
