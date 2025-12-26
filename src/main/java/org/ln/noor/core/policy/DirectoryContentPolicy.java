package org.ln.noor.core.policy;

import java.io.File;

public interface DirectoryContentPolicy {

    boolean isValid(File directory);

    String getErrorMessageKey();
}
