package org.ln.noor.core.policy;

import java.io.File;
/**
 * DirectoryContentPolicy.
 *
 * @author Luca Noale
 */

public interface DirectoryContentPolicy {

    boolean isValid(File directory);

    String getErrorMessageKey();
}
