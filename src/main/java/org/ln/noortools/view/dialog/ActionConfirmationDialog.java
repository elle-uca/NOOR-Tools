package org.ln.noortools.view.dialog;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Standard confirmation dialog used before executing ActionTags.
 */
public class ActionConfirmationDialog {

    public static boolean show(String message) {

        JTextArea area = new JTextArea(message);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(420, 220));

        int res = JOptionPane.showConfirmDialog(
                null,
                scroll,
                "Confirm Actions",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        return res == JOptionPane.OK_OPTION;
    }
}
