package org.ln.noortools.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerServiceListener;

public class FileTableModel extends AbstractTableModel implements RenamerServiceListener {

    private final String[] columnNames = { "Original name", "New name", "Status" };
    private List<RenamableFile> data = new ArrayList<>();

    @Override
    public void onFilesUpdated(List<RenamableFile> updatedFiles) {
        this.data = new ArrayList<>(updatedFiles);
        fireTableDataChanged(); // 🔄 refresh JTable
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RenamableFile file = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> file.getSource().getName();
            case 1 -> file.getDestinationName();
            case 2 -> file.getFileStatus();
            default -> "";
        };
    }
}
