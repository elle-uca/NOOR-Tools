package org.ln.noortools.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

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
            case 0 -> Boolean.class;  // checkbox
            default -> String.class; // all other columns as text
        };
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	if (rowIndex >= data.size()) {
    		return switch (columnIndex) {
    		case 0 -> Boolean.FALSE; // checkbox spento di default
    		default -> "";           // stringa vuota per le altre colonne
    		};
    	}
    	
    	RenamableFile file = data.get(rowIndex);

    	return switch (columnIndex) {
    	case 0 -> file.isSelected();
    	case 1 -> file.getSource().getName();
    	case 2 -> (file.getDestinationName() == null || 
    			file.getDestinationName().isEmpty()) ? 
    			file.getSource().getName() : 
    			file.getDestinationName();
    	case 3 -> file.getSource().getParent();
    	case 4 -> file.getFileStatus();
    	default -> "";
    	};
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; // ✅ solo la colonna checkbox è editabile
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0 && rowIndex < data.size()) {
            RenamableFile file = data.get(rowIndex);
            if (aValue instanceof Boolean) {
                file.setSelected((Boolean) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }
    
	@Override
	public void onFilesUpdated(List<RenamableFile> updatedFiles) {
        this.data = new ArrayList<>(updatedFiles);
        fireTableDataChanged(); // 🔄 refresh JTable
        System.out.println("onFilesUpdated");
    }		
	


}
