package org.ln.noor.tools.rename.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.ln.noor.core.enums.FileStatus;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.RenamerServiceListener;

@SuppressWarnings("serial")
public class RenamableFileTableModel extends AbstractTableModel implements RenamerServiceListener{

	private List<RenamableFile> data = new ArrayList<RenamableFile>();

	private final String[] columnNames;

	public RenamableFileTableModel(I18n i18n) {
		this.columnNames = new String[] {
				i18n.get("table.column.selected"),
				i18n.get("table.column.original"),
				i18n.get("table.column.new"),
				i18n.get("table.column.path"),
				i18n.get("table.column.status")
		};
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


	public void addFiles(List<RenamableFile> files) {
		data.clear();
		data.addAll(files);
		fireTableDataChanged();
	}

	public void addFile(RenamableFile file) {
	    // controllo duplicato
	    if(containsFile(file)) {
	    	 return; // IGNORA
	    }

	    int row = data.size();
	    data.add(file);
	    fireTableRowsInserted(row, row);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public boolean containsFile(RenamableFile f) {
	    String abs = f.getSource().getAbsolutePath();

	    for (RenamableFile rf : data) {
	        if (rf.getSource().getAbsolutePath().equals(abs))
	            return true;
	    }
	    return false;
	}

	
	public void removeRow(int rowIndex) {
	    if (rowIndex < 0 || rowIndex >= data.size()) return;

	    data.remove(rowIndex);
	    fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	public void clear() {
	    int size = data.size();
	    if (size == 0) return;

	    data.clear();
	    fireTableRowsDeleted(0, size - 1);
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
	}		




	public RenamableFile getFileAt(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= data.size()) return null;
		return data.get(rowIndex);
	}

}
