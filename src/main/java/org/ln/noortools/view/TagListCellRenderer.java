package org.ln.noortools.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.ln.noortools.tag.AbstractTag;
import com.formdev.flatlaf.extras.FlatSVGIcon;

@SuppressWarnings("serial")
public class TagListCellRenderer extends JPanel implements ListCellRenderer<AbstractTag> {

    private final JLabel iconLabel = new JLabel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel tagLabel = new JLabel();

    public TagListCellRenderer() {
        setLayout(new BorderLayout(8, 0));
        setBorder(new EmptyBorder(6, 8, 6, 8));

        // Titolo principale
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 13f));

        // Tag string secondaria (più piccola e grigia)
        tagLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        tagLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(tagLabel);

        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends AbstractTag> list,
            AbstractTag value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (value != null) {
            iconLabel.setIcon(resolveIcon(value));
            titleLabel.setText(value.getDescription());
            tagLabel.setText(value.getTagString());
        }

        // Colori FlatLaf dinamici
        Color bg = isSelected
                ? UIManager.getColor("List.selectionBackground")
                : UIManager.getColor("List.background");
        Color fg = isSelected
                ? UIManager.getColor("List.selectionForeground")
                : UIManager.getColor("List.foreground");

        setBackground(bg);
        titleLabel.setForeground(fg);
        tagLabel.setForeground(isSelected
                ? UIManager.getColor("List.selectionForeground").brighter()
                : UIManager.getColor("Label.disabledForeground"));

        return this;
    }

    /** Restituisce un’icona SVG coerente col tipo di tag */
    private Icon resolveIcon(AbstractTag tag) {
        String name = tag.getTagName().toLowerCase();
        String iconPath = switch (name) {
            case "incn", "decn" -> "icons/file-digit.svg";
            case "randl", "randn" -> "icons/dices.svg";
            case "subs" -> "icons/tag-subs.svg";
            case "word" -> "icons/tag-word.svg";
            default -> "icons/hash.svg";
        };
        return new FlatSVGIcon(iconPath, 16, 16);
    }
}












































//package org.ln.noortools.view;
//
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//
//import org.ln.noortools.tag.AbstractTag;
//
//@SuppressWarnings("serial")
//public class TagListCellRenderer extends JPanel implements ListCellRenderer<AbstractTag> {
//
//    private final JLabel iconLabel = new JLabel();
//    private final JLabel titleLabel = new JLabel();
//    private final JLabel tagLabel = new JLabel();
//
//    public TagListCellRenderer() {
//        setLayout(new BorderLayout(8, 0)); // spazio tra icona e testo
//        setBorder(new EmptyBorder(6, 8, 6, 8));
//
//        // Titolo (descrizione leggibile)
//        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
//
//        // Tag stringa (più piccolo e monospaziato)
//        tagLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
//        tagLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
//
//        // Colonna di testo
//        JPanel textPanel = new JPanel(new GridLayout(2, 1));
//        textPanel.setOpaque(false);
//        textPanel.add(titleLabel);
//        textPanel.add(tagLabel);
//
//        add(iconLabel, BorderLayout.WEST);
//        add(textPanel, BorderLayout.CENTER);
//    }
//
//    @Override
//    public Component getListCellRendererComponent(
//            JList<? extends AbstractTag> list,
//            AbstractTag value,
//            int index,
//            boolean isSelected,
//            boolean cellHasFocus) {
//
//        if (value != null) {
//            // --- ICONA ---
//            iconLabel.setIcon(resolveIcon(value));
//
//            // --- TESTO ---
//            titleLabel.setText(value.getDescription());
//            tagLabel.setText(value.getTagString());
//        }
//
//        // --- COLORI FlatLaf ---
//        Color bg, fg;
//        if (isSelected) {
//            bg = UIManager.getColor("List.selectionBackground");
//            fg = UIManager.getColor("List.selectionForeground");
//        } else {
//            bg = UIManager.getColor("List.background");
//            fg = UIManager.getColor("List.foreground");
//        }
//
//        setBackground(bg);
//        titleLabel.setForeground(fg);
//        tagLabel.setForeground(isSelected
//                ? UIManager.getColor("List.selectionForeground").brighter()
//                : UIManager.getColor("Label.disabledForeground"));
//
//        return this;
//    }
//
//    /** Icona diversa in base al tipo di tag */
//    private Icon resolveIcon(AbstractTag tag) {
//        String name = tag.getTagName().toLowerCase();
//        String iconName = switch (name) {
//            case "incn", "decn" -> "🧮";  // numerico
//            case "randl", "randn" -> "🎲"; // casuale
//            case "sub", "subs" -> "✂️";
//            case "word" -> "🔤";
//            default -> "🏷️";
//        };
//        // piccola etichetta emoji come icona
//        return new TextIcon(iconName);
//    }
//
//    /** Piccola utility: disegna una emoji come Icon */
//    private static class TextIcon implements Icon {
//        private final String text;
//        public TextIcon(String text) { this.text = text; }
//        public void paintIcon(Component c, Graphics g, int x, int y) {
//            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
//            g.drawString(text, x, y + 12);
//        }
//        public int getIconWidth() { return 16; }
//        public int getIconHeight() { return 16; }
//    }
//}
