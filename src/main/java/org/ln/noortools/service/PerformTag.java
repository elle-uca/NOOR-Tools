package org.ln.noortools.service;

/**
 * Tag that performs a side-effect action when the rename is executed
 * (e.g. writing metadata to file system or audio/photo tags).
 */
public interface PerformTag {

    /**
     * Human-readable description of the action,
     * used in confirmation dialogs/logs.
     */
    String getActionDescription();

    /**
     * Executes the action on the given files.
     * This is called only when the user confirms the rename.
     */
    //void performAction();
}
