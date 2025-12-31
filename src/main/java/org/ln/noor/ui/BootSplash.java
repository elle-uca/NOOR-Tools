package org.ln.noor.ui;
/**
 * BootSplash.
 *
 * @author Luca Noale
 */

public interface BootSplash {
    void showSplash();

    void setProgress(int value, String message);

    void close();
}
