package org.ln.noortools.view;
import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class ToolbarBuilder {

    public static JToolBar buildToolbar() {
        // 🔹 Toolbar base
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setBackground(UIManager.getColor("Panel.background"));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 🔹 Crea pulsanti
        JButton btnFile = createToolbarButton("File", new FlatSVGIcon("icons/file.svg", 32, 32));
        JButton btnDir  = createToolbarButton("Directory", new FlatSVGIcon("icons/folder.svg", 32, 32));
        JButton btnRename = createToolbarButton("Rinomina", new FlatSVGIcon("icons/rename.svg", 32, 32));

        // 🔹 Uniforma dimensioni
        makeUniformSize(btnFile, btnDir, btnRename);

        // 🔹 Aggiungi alla toolbar
        toolBar.add(btnFile);
        toolBar.add(btnDir);
        toolBar.addSeparator(new Dimension(15, 0));
        toolBar.add(btnRename);

        return toolBar;
    }

    private static JButton createToolbarButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFocusable(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setBackground(UIManager.getColor("Panel.background"));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static void makeUniformSize(JButton... buttons) {
        int maxWidth = 0;
        int maxHeight = 0;

        // Trova le dimensioni massime tra tutti
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
