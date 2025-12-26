package org.ln.noor.ui.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ln.noor.core.i18n.I18n;

/**
 * Status bar displayed at the bottom of the {@code MainFrame}. It exposes
 * methods to update the displayed status and to react to theme/undo actions
 * without leaking the underlying buttons to callers.
 */
@SuppressWarnings("serial")
public class StatusBarPanel extends JPanel {

    private final JLabel statusBarLabel;
    private final JButton undoButton;

    public StatusBarPanel(I18n i18n, 
    		ActionListener undoListener, 
    		Icon undoIcon) {
        super(new FlowLayout(FlowLayout.LEADING));
        statusBarLabel = new JLabel(i18n.get("status.initial"));
        statusBarLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBarLabel.setFont(statusBarLabel.getFont().deriveFont(Font.PLAIN, 12f));

        undoButton = new JButton(i18n.get("status.undo.label"));
        undoButton.putClientProperty("JButton.buttonType", "toolBarButton");
        undoButton.putClientProperty("JButton.focusedBackground", null);
        undoButton.setIcon(undoIcon);
        undoButton.setToolTipText(i18n.get("status.undo.tooltip"));
        undoButton.setEnabled(false);
        if (undoListener != null) {
            undoButton.addActionListener(undoListener);
        }

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));

        add(undoButton);
        add(statusBarLabel);
    }

    public void setStatusText(String text) {
        statusBarLabel.setText(text);
    }

    public void setUndoEnabled(boolean enabled) {
        undoButton.setEnabled(enabled);
    }



    public JLabel getStatusBarLabel() {
        return statusBarLabel;
    }


    public JButton getUndoButton() {
        return undoButton;
    }
}
