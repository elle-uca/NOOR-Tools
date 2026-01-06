package org.ln.noor.ui;
/**
 * Displays a temporary splash screen to guide users through startup phases.
 * This interface isolates the splash lifecycle so the bootstrap sequence can
 * reveal, update, and dismiss progress messaging without coupling to a specific
 * UI widget.
 *
 * @author Luca Noale
 */

public interface BootSplash {
    /**
     * Opens the splash screen for the active startup session. Does not touch the
     * filesystem.
     */
    void showSplash();

    /**
     * Updates the progress indicator and user-facing message for the current
     * startup step. Does not touch the filesystem.
     *
     * @param value   progress percentage from 0 to 100
     * @param message message describing the active startup phase
     */
    void setProgress(int value, String message);

    /**
     * Closes the splash screen once initialization completes. Does not touch the
     * filesystem.
     */
    void close();
}
