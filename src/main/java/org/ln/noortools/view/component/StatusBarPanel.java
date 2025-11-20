package org.ln.noortools.view.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Status bar displayed at the bottom of the {@code MainFrame}. It exposes
 * methods to update the displayed status and to react to theme/undo actions
 * without leaking the underlying buttons to callers.
 */
public class StatusBarPanel extends JPanel {

    private final JLabel statusBarLabel;
    private final JToggleButton themeToggle;
    private final JButton undoButton;

    public StatusBarPanel(ActionListener themeSwitchListener, ActionListener undoListener, Icon undoIcon) {
        super(new FlowLayout(FlowLayout.LEADING));

        statusBarLabel = new JLabel("🌞 Light mode — 0 rules");
        statusBarLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBarLabel.setFont(statusBarLabel.getFont().deriveFont(Font.PLAIN, 12f));

        themeToggle = new JToggleButton("🌞");
        if (themeSwitchListener != null) {
            themeToggle.addActionListener(themeSwitchListener);
        }

        undoButton = new JButton("Undo");
        undoButton.putClientProperty("JButton.buttonType", "toolBarButton");
        undoButton.putClientProperty("JButton.focusedBackground", null);
        undoButton.setIcon(undoIcon);
        undoButton.setToolTipText("Undo last rename operation");
        undoButton.setEnabled(false);
        if (undoListener != null) {
            undoButton.addActionListener(undoListener);
        }

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));

        add(undoButton);
        add(themeToggle);
        add(statusBarLabel);
    }

    public void setStatusText(String text) {
        statusBarLabel.setText(text);
    }

    public void setUndoEnabled(boolean enabled) {
        undoButton.setEnabled(enabled);
    }

    public boolean isDarkModeSelected() {
        return themeToggle.isSelected();
    }

    public void updateThemeSymbol(boolean darkMode) {
        themeToggle.setText(darkMode ? "🌙" : "🌞");
        themeToggle.setSelected(darkMode);
    }

    public JLabel getStatusBarLabel() {
        return statusBarLabel;
    }

    public JToggleButton getThemeToggle() {
        return themeToggle;
    }

    public JButton getUndoButton() {
        return undoButton;
    }
}
