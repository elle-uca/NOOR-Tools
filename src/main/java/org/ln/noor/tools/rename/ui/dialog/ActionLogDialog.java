package org.ln.noor.tools.rename.ui.dialog;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Dialog shown after all ActionTags have been applied.
 * Displays a detailed log without touching the filesystem.
 *
 * @author Luca Noale
 */
public class ActionLogDialog {

    /**
     * Shows the apply log dialog for the current rename preview.
     * This method does not touch the filesystem.
     *
     * @param log text to display
     */
    public static void show(String log) {

        JTextArea area = new JTextArea(log);
        area.setEditable(false);
        area.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(550, 380));

        JOptionPane.showMessageDialog(
                null,
                scroll,
                "Apply Report",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
