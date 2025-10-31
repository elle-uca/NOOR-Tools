package org.ln.noortools.view;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ToolbarBuilder2 {

    public static JToolBar buildToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // blocca lo spostamento
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 🔹 Crea i bottoni principali
        JButton btnFile = createToolbarButton("File", new FlatSVGIcon("icons/file.svg", 32, 32));
        JButton btnDir  = createToolbarButton("Directory", new FlatSVGIcon("icons/folder.svg", 32, 32));
        JButton btnRename = createToolbarButton("Rinomina", new FlatSVGIcon("icons/rename.svg", 32, 32));

        // 🔹 Assegna azioni
        btnFile.addActionListener(e -> System.out.println("Aggiungi file"));
        btnDir.addActionListener(e -> System.out.println("Aggiungi directory"));
        btnRename.addActionListener(e -> System.out.println("Rinomina"));

        // 🔹 Aggiungi alla toolbar
        toolBar.add(btnFile);
        toolBar.add(btnDir);
        toolBar.add(btnRename);

        // 🔹 Uniforma dimensioni (tutti uguali)
        makeButtonsUniform(List.of(btnFile, btnDir, btnRename));

        return toolBar;
    }

    // --- helper per creare pulsanti coerenti ---
    private static JButton createToolbarButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFocusable(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setMargin(new Insets(5, 10, 5, 10));
        return button;
    }

    // --- helper per rendere i bottoni della stessa misura ---
    private static void makeButtonsUniform(List<JButton> buttons) {
        int maxWidth = buttons.stream()
                .mapToInt(b -> b.getPreferredSize().width)
                .max()
                .orElse(100);
        int maxHeight = buttons.stream()
                .mapToInt(b -> b.getPreferredSize().height)
                .max()
                .orElse(60);

        Dimension uniformSize = new Dimension(maxWidth, maxHeight);
        for (JButton b : buttons) {
            b.setPreferredSize(uniformSize);
            b.setMinimumSize(uniformSize);
            b.setMaximumSize(uniformSize);
        }
    }
}
