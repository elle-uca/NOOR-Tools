package org.ln.noor.tools.directory;

import javax.swing.SwingUtilities;

import org.ln.noor.directory.view.DirectoryToolView;
/**
 * DirectoryToolLauncher.
 *
 * @author Luca Noale
 */

public final class DirectoryToolLauncher {

    private DirectoryToolLauncher() {}

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            new DirectoryToolView().setVisible(true);
        });
    }
}
