package org.ln.noortools.view;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.ln.noortools.model.RenamableFile;

@SuppressWarnings("serial")
public class DisabledRowRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        RenamableFileTableModel model = (RenamableFileTableModel) table.getModel();
        int modelRow = table.convertRowIndexToModel(row);
        RenamableFile file = model.getFileAt(modelRow);

        if (!file.isSelected()) {
            c.setForeground(Color.GRAY);
        } else {
            c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        }

        return c;
    }
}
