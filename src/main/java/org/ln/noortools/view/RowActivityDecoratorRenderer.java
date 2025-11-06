package org.ln.noortools.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.ln.noortools.model.RenamableFile;

public class RowActivityDecoratorRenderer implements TableCellRenderer {

    private final TableCellRenderer delegate;

    private static final Color INACTIVE_BG = new Color(45, 45, 45);
    private static final Color INACTIVE_FG = new Color(150, 150, 150);

    public RowActivityDecoratorRenderer(TableCellRenderer delegate) {
        this.delegate = delegate;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        RenamableFileTableModel model = (RenamableFileTableModel) table.getModel();
        RenamableFile file = model.getFileAt(table.convertRowIndexToModel(row));

        if (file != null && !file.isSelected()) {
            // riga disattivata
            c.setForeground(INACTIVE_FG);
            c.setBackground(isSelected ? INACTIVE_BG.darker() : INACTIVE_BG);
        } else {
            // riga attiva → lascia i colori del renderer originale
            if (!isSelected) {
                c.setForeground(table.getForeground());
                c.setBackground(table.getBackground());
            }
        }

        return c;
    }
}
