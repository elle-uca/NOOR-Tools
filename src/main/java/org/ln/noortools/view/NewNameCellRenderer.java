package org.ln.noortools.view;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import org.ln.noortools.model.RenamableFile;

@SuppressWarnings("serial")
public class NewNameCellRenderer extends DefaultTableCellRenderer {
//	private static final Color COLOR_SAME = new Color(230, 230, 230); // grigio chiaro se uguale
//	private static final Color COLOR_CHANGED = new Color(200, 255, 200); // verde chiaro se cambia
//	private static final Color COLOR_ERROR = new Color(255, 220, 220); // rosato per errori

	 @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {

	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	        RenamableFileTableModel model = (RenamableFileTableModel) table.getModel();
	        int modelRow = table.convertRowIndexToModel(row);
	        RenamableFile f = model.getFileAt(modelRow);

	        if (!f.isSelected()) {
	            setText(""); // visivamente vuota
	            setForeground(UIManager.getColor("Label.disabledForeground"));
	        } else {
	            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
	        }

	        setToolTipText(f.getDestinationName()); // comodo
	        return this;
	    }

}