package org.ln.noor.tools.directory;

import javax.swing.SwingUtilities;
import org.ln.noor.directory.view.CrocodileView;

public final class DirectoryToolLauncher {

    private DirectoryToolLauncher() {}

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            new CrocodileView().setVisible(true);
        });
    }
}
