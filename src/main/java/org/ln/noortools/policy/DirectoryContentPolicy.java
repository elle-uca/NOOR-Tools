package org.ln.noortools.policy;

import java.io.File;

public interface DirectoryContentPolicy {

    boolean isValid(File directory);

    String getErrorMessageKey();
}
