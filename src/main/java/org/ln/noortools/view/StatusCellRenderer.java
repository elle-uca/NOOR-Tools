/**
 * Package containing the view components (UI elements) for the noortools application.
 */
package org.ln.noortools.view;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import org.ln.noortools.enums.FileStatus;
import org.ln.noortools.model.RenamableFile;

/**
 * Custom renderer for displaying the status of a file within a JTable cell.
 * It shows an icon (OK/KO) based on the FileStatus enumeration value.
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
public class StatusCellRenderer extends  DefaultTableCellRenderer  {

    // Initializes the 'OK' icon by loading the image resource.
    private final Icon okIcon  = new ImageIcon(getClass().getResource("/icons/ok.png"));
    // Initializes the 'KO' (Knock Out/Error) icon by loading the image resource.
    private final Icon koIcon  = new ImageIcon(getClass().getResource("/icons/ko.png"));

    /**
     * Constructor for StatusCellRenderer.
     * Sets up the default look and alignment for the JLabel.
     */
    public StatusCellRenderer() {
        // Ensures the background color is visible.
        setOpaque(true);
        // Centers the icon/text horizontally within the cell.
        setHorizontalAlignment(CENTER);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        RenamableFileTableModel model = (RenamableFileTableModel) table.getModel();
        int modelRow = table.convertRowIndexToModel(row);
        RenamableFile f = model.getFileAt(modelRow);

        if (!f.isSelected()) {
            setText("");
            setIcon(null);
            setForeground(UIManager.getColor("Label.disabledForeground"));
            return this;
        }

        // riga attiva → usa il FileStatus del value
        FileStatus status = (value instanceof FileStatus fs) ? fs : FileStatus.OK;

        // qui il tuo disegno originale (icona verde/rossa ecc.)
        setText(switch (status) {
            case OK -> "No conflicts";
            case KO -> "Conflict";
            default -> "";
        });

        // … setIcon(...) coerente
        return this;
    }
    
    

    /**
     * Called by the JTable when rendering a cell.
     * @param table The JTable drawing the renderer.
     * @param value The value of the cell (expected to be a FileStatus).
     * @param isSelected True if the cell is selected.
     * @param hasFocus True if the cell has focus.
     * @param row The row index.
     * @param column The column index.
     * @return The component used for drawing the cell.
     */
//    @Override
//    public Component getTableCellRendererComponent(
//            JTable table, Object value, boolean isSelected,
//            boolean hasFocus, int row, int column) {
//
//        // Casts the cell value to the expected FileStatus enumeration.
//        FileStatus status = (FileStatus) value;
//
//        if (status == FileStatus.OK) {
//            // Set the 'OK' icon.
//            setIcon(okIcon);
//            // Optionally set descriptive text (optional)
//            setText("No conflicts"); 
//            // Set the tooltip text for detailed info.
//            setToolTipText("No conflicts");
//        } else if (status == FileStatus.KO) {
//            // Set the 'KO' icon.
//            setIcon(koIcon);
//            // Optionally set descriptive text (optional)
//            setText("Name duplicated");
//            // Set the tooltip text for detailed info.
//            setToolTipText("Name duplicated or invalid");
//        } else {
//            // For any other status (e.g., FileStatus.PENDING), clear the icon/tooltip.
//            setIcon(null);
//            setToolTipText(null);
//        }
//
//        RenamableFile file = ((RenamableFileTableModel) table.getModel()).getFileAt(row);
//
//        if (!file.isSelected()) {
//            setForeground(UIManager.getColor("Label.disabledForeground"));
//        } else {
//            setForeground(UIManager.getColor("Label.foreground"));
//        }
////        // ✅ Maintain theme colors (e.g., FlatLaf friendly)
////        if (isSelected) {
////            // Use the selection background color if the cell is selected.
////            setBackground(table.getSelectionBackground());
////        } else {
////            // Use the default table background color.
////            setBackground(table.getBackground());
////        }
//
//        // Return this JLabel instance to render the cell.
//        return this;
//    }
}





















