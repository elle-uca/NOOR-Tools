package org.ln.noortools.view.dialog;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Dialog shown after all ActionTags have been executed.
 * Displays a detailed log.
 */
public class ActionLogDialog {

    public static void show(String log) {

        JTextArea area = new JTextArea(log);
        area.setEditable(false);
        area.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(550, 380));

        JOptionPane.showMessageDialog(
                null,
                scroll,
                "Execution Report",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
