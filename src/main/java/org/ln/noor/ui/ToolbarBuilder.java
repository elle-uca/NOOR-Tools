package org.ln.noor.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.ln.noor.core.i18n.I18n;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class ToolbarBuilder {

    public static JToolBar buildToolbar(
            I18n i18n,
            Runnable onAddFile,
            Runnable onAddDir,
            Runnable onRename
    ) {
        // 🔹 Toolbar base
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setBackground(UIManager.getColor("Panel.background"));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 🎨 Colori adattivi (chiaro/scuro)
        boolean dark = FlatLaf.isLafDark();

        Color fileColor   = dark ? new Color(0x64B5F6) : new Color(0x1E88E5);
        Color folderColor = dark ? new Color(0xFFB74D) : new Color(0xFB8C00);
        Color renameColor = dark ? new Color(0x81C784) : new Color(0x43A047);

        Color hoverFile   = adjustBrightness(fileColor, 1.3f);
        Color hoverFolder = adjustBrightness(folderColor, 1.3f);
        Color hoverRename = adjustBrightness(renameColor, 1.3f);

        // 🔹 Crea pulsanti colorati
        JButton btnFile = createToolbarButton(
                i18n.get("toolbar.button.file"),
                i18n.get("toolbar.button.file.tooltip"),
                new FlatSVGIcon("icons/file.svg", 32, 32),
                fileColor,
                hoverFile,
                onAddFile
        );

        JButton btnDir = createToolbarButton(
                i18n.get("toolbar.button.directory"),
                i18n.get("toolbar.button.directory.tooltip"),
                new FlatSVGIcon("icons/folder.svg", 32, 32),
                folderColor,
                hoverFolder,
                onAddDir
        );

        JButton btnRename = createToolbarButton(
                i18n.get("toolbar.button.rename"),
                i18n.get("toolbar.button.rename.tooltip"),
                new FlatSVGIcon("icons/rename.svg", 32, 32),
                renameColor,
                hoverRename,
                onRename
        );

        // 🔹 Uniforma dimensioni
        makeUniformSize(btnFile, btnDir, btnRename);

        // 🔹 Aggiungi alla toolbar
        toolBar.add(btnFile);
        toolBar.add(btnDir);
        toolBar.addSeparator(new Dimension(15, 0));
        toolBar.add(btnRename);

        return toolBar;
    }

    // 🔸 Crea un pulsante con colore dinamico e hover
    private static JButton createToolbarButton(
            String text,
            String tooltip,
            FlatSVGIcon icon,
            Color baseColor,
            Color hoverColor,
            Runnable action
    ) {
        JButton button = new JButton(text, icon);
        button.setToolTipText(tooltip);
        button.setFocusable(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setBackground(UIManager.getColor("Panel.background"));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 🎨 Colore iniziale icona
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> baseColor));

        // 🖱️ Hover dinamico
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> hoverColor));
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> baseColor));
                button.repaint();
            }
        });

        // ⚙️ Azione (lambda)
        if (action != null) {
            button.addActionListener(e -> action.run());
        }

        return button;
    }

    // 🔸 Calcola versione più chiara/scura del colore
    private static Color adjustBrightness(Color color, float factor) {
        int r = Math.min(255, Math.round(color.getRed() * factor));
        int g = Math.min(255, Math.round(color.getGreen() * factor));
        int b = Math.min(255, Math.round(color.getBlue() * factor));
        return new Color(r, g, b);
    }

    // 🔸 Uniforma dimensioni pulsanti
    private static void makeUniformSize(JButton... buttons) {
        int maxWidth = 0;
        int maxHeight = 0;

        for (JButton b : buttons) {
            Dimension pref = b.getPreferredSize();
            maxWidth = Math.max(maxWidth, pref.width);
            maxHeight = Math.max(maxHeight, pref.height);
        }

        Dimension uniform = new Dimension(maxWidth, maxHeight);
        for (JButton b : buttons) {
            b.setPreferredSize(uniform);
            b.setMinimumSize(uniform);
            b.setMaximumSize(uniform);
        }
    }
}
