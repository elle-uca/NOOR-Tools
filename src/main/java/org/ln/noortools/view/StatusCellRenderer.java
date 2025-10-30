package org.ln.noortools.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.ln.noortools.enums.FileStatus;

@SuppressWarnings("serial")
public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof FileStatus) {
        	FileStatus status = (FileStatus) value;
        	//System.out.println("====== nuovo status " + status );

            if (!isSelected) { // Mantieni selezione blu di default
                switch (status) {
                    case OK -> c.setForeground(Color.BLACK); 
                    case KO -> c.setForeground(new Color(255, 100, 100));
                    case KO1 -> c.setForeground(new Color(255, 100, 100)); 
                }
                //c.setForeground(Color.BLACK);
            }
        }
         return c;
    }
}