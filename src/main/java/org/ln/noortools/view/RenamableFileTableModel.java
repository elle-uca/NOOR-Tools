package org.ln.noortools.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.ln.noortools.enums.FileStatus;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerServiceListener;

@SuppressWarnings("serial")
public class RenamableFileTableModel extends AbstractTableModel implements RenamerServiceListener{
	
	private List<RenamableFile> data = new ArrayList<>();

	private final String[] columnNames = {"Selected", "Original name", "New name", "Path", "Status" };
	
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
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Boolean.class;      // Selected
            case 4 -> FileStatus.class;   // Status
            default -> String.class;      // Original, New name, Path
        };
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        RenamableFile f = data.get(row);

        return switch (col) {
            case 0 -> f.isSelected();
            case 1 -> f.getSource().getName();
            case 2 -> (f.getDestinationName() == null || f.getDestinationName().isBlank())
                        ? f.getSource().getName()
                        : f.getDestinationName();
            case 3 -> f.getSource().getParent();
            case 4 -> f.getFileStatus(); // ✅ sempre FileStatus
            default -> "";
        };
    }

   

    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; // ✅ solo la colonna checkbox è editabile
    }
    
    
    
    @Override
    public void setValueAt(Object aValue, int row, int col) {
        if (col == 0 && row >= 0 && row < data.size()) {
            boolean sel = (Boolean) aValue;
            data.get(row).setSelected(sel);
            fireTableRowsUpdated(row, row);
        }
    }
    
	@Override
	public void onFilesUpdated(List<RenamableFile> updatedFiles) {
        this.data = new ArrayList<>(updatedFiles);
        fireTableDataChanged(); // 🔄 refresh JTable
        //System.out.println("onFilesUpdated");
    }		
	
	

	
	public RenamableFile getFileAt(int rowIndex) {
	    if (rowIndex < 0 || rowIndex >= data.size()) return null;
	    return data.get(rowIndex);
	}

}
