package org.ln.noor.core.policy;

import java.io.File;
/**
 * Contract for validating directory content before tools operate on a path. A
 * policy articulates expectations about the filesystem state while keeping
 * validation separate from tool orchestration.
 *
 * @author Luca Noale
 */

public interface DirectoryContentPolicy {

    /**
     * Evaluates whether the given directory satisfies the policy requirements.
     * Implementations may inspect the filesystem to make this determination.
     *
     * @param directory directory candidate to validate
     * @return {@code true} when the directory matches the policy
     */
    boolean isValid(File directory);

    /**
     * Provides the i18n key describing why validation failed. Does not touch the
     * filesystem.
     *
     * @return message key explaining the violation, or {@code null} when no
     *         error should be reported
     */
    String getErrorMessageKey();
}
