package org.ln.noortools.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.ln.noortools.tag.AbstractTag;

import com.formdev.flatlaf.extras.FlatSVGIcon;

/**
 * Custom ListCellRenderer for JList that displays AbstractTag objects.
 * It renders each list item as a "card" with an icon, a bold title, and a secondary tag string.
 * It also implements custom hover and selection coloring using UIManager colors and FlatLaf icons.
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
public class TagListCellRenderer extends JPanel implements ListCellRenderer<AbstractTag> {

    // Labels for the three main parts of the cell content
    private final JLabel iconLabel = new JLabel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel tagLabel = new JLabel();

    /**
     * Constructor sets up the renderer's layout and initial component styling.
     */
    public TagListCellRenderer() {
        // Use BorderLayout for the main panel: Icon (WEST) and Text (CENTER).
        setLayout(new BorderLayout(8, 0)); 
        // Set initial inner padding/margin for the cell.
        setBorder(new EmptyBorder(6, 10, 6, 10)); 

        // Primary title styling (Bold, slightly larger font)
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 13f));

        // Secondary tag styling (Monospaced, slightly smaller, disabled-like color)
        tagLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        tagLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        // Panel to stack the two text labels (title and tag) vertically.
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false); // Make sure the nested panel is transparent.
        textPanel.add(titleLabel);
        textPanel.add(tagLabel);

        // Add components to the main renderer panel.
        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        // Keep the panel background initially non-opaque (will be set dynamically).
        setOpaque(false); 
    }

    /**
     * This method is called to configure the renderer component for a specific cell.
     */
    @Override
    public Component getListCellRendererComponent(
            JList<? extends AbstractTag> list,
            AbstractTag value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // --- 1. Load Data ---
        if (value != null) {
            iconLabel.setIcon(resolveIcon(value));      // Set icon based on tag type.
            titleLabel.setText(value.getDescription()); // Set the main description.
            tagLabel.setText(value.getTagString());     // Set the secondary tag string.
        }

        // --- 2. Determine Colors based on State (Selection/Hover) ---
        Color bg;
        Color fg;

        if (isSelected) {
            // Selected state uses standard list selection colors.
            bg = UIManager.getColor("List.selectionBackground");
            fg = UIManager.getColor("List.selectionForeground");
        } else if (isHover(list, index)) {
            // Custom hover state: blend the list background and selection background for a soft effect.
            bg = blend(UIManager.getColor("List.background"), UIManager.getColor("List.selectionBackground"), 0.1f);
            fg = UIManager.getColor("List.foreground");
        } else {
            // Normal, unselected state uses standard list colors.
            bg = UIManager.getColor("List.background");
            fg = UIManager.getColor("List.foreground");
        }

        // --- 3. Apply Colors and Border Style ---

        // Apply foreground color to the main title.
        titleLabel.setForeground(fg);
        
        // Apply secondary tag color: bright selection foreground if selected, otherwise disabled foreground.
        tagLabel.setForeground(isSelected
                ? UIManager.getColor("List.selectionForeground").brighter()
                : UIManager.getColor("Label.disabledForeground"));

        // Apply background color and make the panel opaque to display the color.
        setBackground(bg);
        setOpaque(true);
        
        // Apply a CompoundBorder to achieve a "card" look:
        // 1. LineBorder: A soft, slightly rounded border (opacity based on selection).
        // 2. EmptyBorder: Inner padding for the content.
        setBorder(new CompoundBorder(
                new LineBorder(new Color(0, 0, 0, isSelected ? 60 : 20), 1, true), 
                new EmptyBorder(6, 10, 6, 10)
        ));

        return this; // Return the configured JPanel as the component to be painted.
    }

    /** * Helper method to check if the mouse is currently hovering over the specified list item. 
     */
    private boolean isHover(JList<?> list, int index) {
        Point mouse = list.getMousePosition();
        if (mouse == null) return false;
        // Convert mouse coordinates to a list index.
        int hoverIndex = list.locationToIndex(mouse); 
        return hoverIndex == index;
    }

    /** * Helper method to blend two colors for soft visual effects (like hover). 
     * * @param c1 The first color.
     * * @param c2 The second color.
     * * @param ratio The blending ratio (0.0 to 1.0).
     */
    private static Color blend(Color c1, Color c2, float ratio) {
        if (c1 == null || c2 == null) return c1;
        float ir = 1.0f - ratio;
        int r = (int) (c1.getRed() * ir + c2.getRed() * ratio);
        int g = (int) (c1.getGreen() * ir + c2.getGreen() * ratio);
        int b = (int) (c1.getBlue() * ir + c2.getBlue() * ratio);
        return new Color(r, g, b);
    }

    /** * Helper method to resolve and load the appropriate SVG icon based on the tag type. 
     * (Uses FlatSVGIcon from the FlatLaf extras library).
     */
    private Icon resolveIcon(AbstractTag tag) {
        String name = tag.getTagName().toLowerCase();
        String iconPath = switch (name) {
            // Map specific tag names to their corresponding SVG icon file paths.
            case "incn", "decn" -> "icons/file-digit.svg";
            case "randl", "randn" -> "icons/dices.svg";
            case "subs" -> "icons/tag-subs.svg";
            case "word" -> "icons/tag-word.svg";
            default -> "icons/hash.svg"; // Default icon for unrecognized tags.
        };
        // Load the icon and scale it to 16x16 pixels.
        return new FlatSVGIcon(iconPath, 16, 16); 
    }
}