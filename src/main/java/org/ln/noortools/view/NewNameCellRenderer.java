package org.ln.noortools.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.ln.noortools.enums.FileStatus;

@SuppressWarnings("serial")
public class NewNameCellRenderer extends DefaultTableCellRenderer {
//	private static final Color COLOR_SAME = new Color(230, 230, 230); // grigio chiaro se uguale
//	private static final Color COLOR_CHANGED = new Color(200, 255, 200); // verde chiaro se cambia
//	private static final Color COLOR_ERROR = new Color(255, 220, 220); // rosato per errori

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component c = super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);

		// Evita errori su righe vuote
		if (row >= table.getRowCount() || table.getModel().getRowCount() == 0) {
			return c;
		}

		// Recupera i valori di "Name" (col 0) e "New Name" (col 1)
		Object nameObj = table.getValueAt(row, 0);
		Object newNameObj = table.getValueAt(row, 1);

		String name = nameObj != null ? nameObj.toString() : "";
		String newName = newNameObj != null ? newNameObj.toString() : "";

		// Colore base (se non selezionato)
		if (!isSelected) {
			if (name.isEmpty() && newName.isEmpty()) {
				c.setForeground(Color.black);
			} else if (name.equals(newName)) {
				c.setForeground(Color.GRAY);
			} else {
				c.setForeground(Color.BLUE);
			}
		} else {
			// colore selezione di default
			//c.setBackground(table.getSelectionBackground());
		}

		// opzionale: errore evidenziato se "Errore" (col 3) contiene qualcosa
		Object errorObj = table.getValueAt(row, 3);
		if (errorObj instanceof FileStatus) {
			FileStatus status = (FileStatus) errorObj;

			if (!status.equals(FileStatus.OK)) {
				c.setForeground(Color.red);
			}
		}

		return c;
	}

}