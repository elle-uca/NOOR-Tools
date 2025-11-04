package org.ln.noortools.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.ln.noortools.tag.AbstractTag;
import com.formdev.flatlaf.extras.FlatSVGIcon;

@SuppressWarnings("serial")
public class TagListCellRenderer extends JPanel implements ListCellRenderer<AbstractTag> {

    private final JLabel iconLabel = new JLabel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel tagLabel = new JLabel();

    public TagListCellRenderer() {
        setLayout(new BorderLayout(8, 0));
        setBorder(new EmptyBorder(6, 10, 6, 10));

        // Titolo principale
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 13f));

        // Tag secondario
        tagLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        tagLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(tagLabel);

        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        setOpaque(false); // lasciamo il background trasparente
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

        // Colori coerenti con FlatLaf
        Color bg;
        Color fg;

        if (isSelected) {
            bg = UIManager.getColor("List.selectionBackground");
            fg = UIManager.getColor("List.selectionForeground");
        } else if (isHover(list, index)) {
            bg = blend(UIManager.getColor("List.background"), UIManager.getColor("List.selectionBackground"), 0.1f);
            fg = UIManager.getColor("List.foreground");
        } else {
            bg = UIManager.getColor("List.background");
            fg = UIManager.getColor("List.foreground");
        }

        // Applica i colori
        titleLabel.setForeground(fg);
        tagLabel.setForeground(isSelected
                ? UIManager.getColor("List.selectionForeground").brighter()
                : UIManager.getColor("Label.disabledForeground"));

        // Applica sfondo “card”
        setBackground(bg);
        setOpaque(true);
        setBorder(new CompoundBorder(
                new LineBorder(new Color(0, 0, 0, isSelected ? 60 : 20), 1, true), // bordo arrotondato soft
                new EmptyBorder(6, 10, 6, 10)
        ));

        return this;
    }

    /** Ritorna true se il mouse è sopra l’elemento corrente */
    private boolean isHover(JList<?> list, int index) {
        Point mouse = list.getMousePosition();
        if (mouse == null) return false;
        int hoverIndex = list.locationToIndex(mouse);
        return hoverIndex == index;
    }

    /** Mescola due colori per effetto hover morbido */
    private static Color blend(Color c1, Color c2, float ratio) {
        if (c1 == null || c2 == null) return c1;
        float ir = 1.0f - ratio;
        int r = (int) (c1.getRed() * ir + c2.getRed() * ratio);
        int g = (int) (c1.getGreen() * ir + c2.getGreen() * ratio);
        int b = (int) (c1.getBlue() * ir + c2.getBlue() * ratio);
        return new Color(r, g, b);
    }

    /** Icona coerente col tipo di tag */
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
