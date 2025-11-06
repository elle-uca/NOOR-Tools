//package org.ln.noortools.view;
//
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//
//import org.ln.noortools.model.RenamableFile;
//
//public class RenamableFileRowRenderer extends DefaultTableCellRenderer {
//
//    private static final Color INACTIVE_BG = new Color(45, 45, 45);     // dark mode
//    private static final Color INACTIVE_FG = new Color(150, 150, 150);  // dimmed text
//
//    @Override
//    public Component getTableCellRendererComponent(
//            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//
//        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//
//        RenamableFileTableModel model = (RenamableFileTableModel) table.getModel();
//        RenamableFile file = model.getFileAt(table.convertRowIndexToModel(row));
//
//        boolean active = file.isSelected();
//
//        if (!active) {
//            // RIGA DISELEZIONATA
//            c.setForeground(INACTIVE_FG);
//            c.setBackground(isSelected ? INACTIVE_BG.darker() : INACTIVE_BG);
//        } else {
//            // RIGA ATTIVA (usa colori tema FlatLaf)
//            c.setForeground(table.getForeground());
//            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
//        }
//
//        return c;
//    }
//}
//
