package org.ln.noortools.util;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.formdev.flatlaf.FlatLaf;

/**
 * Utility class for applying dark theme visual enhancements
 * to tables, buttons, and other components.
 *
 * This improves readability and consistency when using FlatLaf Dark theme.
 *
 * Author: Luca Noale
 */
public class ThemeUtil {

    /**
     * Applies custom dark theme tweaks to the main UI and JTable renderers.
     *
     * @param frame  the main JFrame (can be null)
     * @param table  the JTable showing renamed files
     * @param newNameColumnIndex index of the "New name" column
     */
    public static void applyDarkTheme(JFrame frame, JTable table, int newNameColumnIndex) {
    	
        // Detect current theme → apply tweaks only if Dark
        boolean dark = FlatLaf.isLafDark();
        if (!dark) {
            // Light mode → no special customization
            SwingUtilities.updateComponentTreeUI(frame);
            return;
        }

        // ===========================
        // 🌙 General FlatLaf colors
        // ===========================
        UIManager.put("Panel.background", new Color(0x2b2b2b));
        UIManager.put("Label.foreground", new Color(0xE0E0E0));

        UIManager.put("Table.background", new Color(0x2b2b2b));
        UIManager.put("Table.alternateRowColor", new Color(0x323232));
        UIManager.put("Table.selectionBackground", new Color(0x404040));
        UIManager.put("Table.foreground", new Color(0xDADADA));
        UIManager.put("Table.selectionForeground", Color.WHITE);

        // Button look (for blue "text-buttons")
        UIManager.put("Button.background", new Color(0x3a3f44));
        UIManager.put("Button.foreground", new Color(0xE0E0E0));
        UIManager.put("Button.hoverBackground", new Color(0x40A6FF));
        UIManager.put("Button.hoverForeground", Color.WHITE);
        UIManager.put("Button.arc", 8);
        UIManager.put("Button.focusWidth", 1);

        // Accent color (links, highlights)
        UIManager.put("Component.accentColor", new Color(0x40A6FF));

        FlatLaf.updateUI();

        // ===========================
        // 🎨 JTable “New name” column renderer
        // ===========================
        if (table != null && newNameColumnIndex >= 0) {
            table.getColumnModel().getColumn(newNameColumnIndex)
                    .setCellRenderer(new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(
                                JTable table, Object value, boolean isSelected,
                                boolean hasFocus, int row, int column) {

                            JLabel cell = (JLabel) super.getTableCellRendererComponent(
                                    table, value, isSelected, hasFocus, row, column);

                            if (isSelected) {
                                cell.setBackground(UIManager.getColor("Table.selectionBackground"));
                                cell.setForeground(UIManager.getColor("Table.selectionForeground"));
                            } else {
                                cell.setBackground((row % 2 == 0)
                                        ? UIManager.getColor("Table.background")
                                        : UIManager.getColor("Table.alternateRowColor"));
                                cell.setForeground(UIManager.getColor("Table.foreground"));
                            }

                            // “New name” column → accent color link
                            if (column == newNameColumnIndex && value != null) {
                                cell.setForeground(new Color(0x40A6FF)); // light blue link
                                cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            }

                            cell.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                            return cell;
                        }
                    });
        }

        // ===========================
        // 🪄 Refresh entire UI
        // ===========================
        if (frame != null) {
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }
}
