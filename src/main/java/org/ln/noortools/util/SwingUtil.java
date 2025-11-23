package org.ln.noortools.util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ln.noortools.preferences.Prefs;

/**
 * Swing utility methods.
 * Provides helpers for consistent, reusable UI components.
 *
 * Author: Luca Noale
 */
public final class SwingUtil {

    private SwingUtil() {
        // utility class: prevent instantiation
    }

    /**
     * Creates a configured {@link JFileChooser} starting from the last used directory.
     *
     * @param mode  selection mode (e.g. JFileChooser.FILES_ONLY)
     * @param multi whether to allow multiple selections
     * @return configured file chooser
     */
    public static JFileChooser getFileChooser(int mode, boolean multi) {
        String lastPath = Prefs.loadLastDir();
        File startDir = (lastPath != null && !lastPath.isBlank())
                ? new File(lastPath)
                : new File(System.getProperty("user.home"));

        JFileChooser fc = new JFileChooser(startDir);
        fc.setFileSelectionMode(mode);
        fc.setMultiSelectionEnabled(multi);
        fc.setAcceptAllFileFilterUsed(true);

        // Consistent look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        return fc;
    }

    /**
     * Shows an "Open" dialog and automatically remembers the last used directory.
     *
     * @param parent optional parent component
     * @param mode   file selection mode (FILES_ONLY, DIRECTORIES_ONLY, etc.)
     * @param multi  whether multiple selection is allowed
     * @return selected file(s), or empty array if cancelled
     */
    public static File[] showOpenDialog(Component parent, int mode, boolean multi) {
        JFileChooser fc = getFileChooser(mode, multi);
        int result = fc.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Save last directory for next use
            File selected = fc.getSelectedFile();
            if (selected != null) {
                Prefs.saveLastDir(selected.getParent());
            }

            if (multi) {
                return fc.getSelectedFiles();
            } else {
                return new File[]{fc.getSelectedFile()};
            }
        }

        return new File[0]; // user cancelled
    }

    /**
     * Convenience method: file chooser for text files.
     */
    public static JFileChooser getTextFileChooser() {
        JFileChooser fc = getFileChooser(JFileChooser.FILES_ONLY, false);
        fc.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
        return fc;
    }

    /**
     * Convenience method: directory chooser only.
     */
    public static JFileChooser getDirectoryChooser() {
        return getFileChooser(JFileChooser.DIRECTORIES_ONLY, false);
    }
}
