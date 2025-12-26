package org.ln.noor.tools.rename.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.table.TableRowSorter;

import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.RenamerService;
import org.ln.noor.tools.rename.ui.FileTransferHandler;
import org.ln.noor.tools.rename.ui.NaturalOrderComparator;
import org.ln.noor.tools.rename.ui.NewNameCellRenderer;
import org.ln.noor.tools.rename.ui.RenamableFileTableModel;
import org.ln.noor.tools.rename.ui.StatusCellRenderer;
import org.ln.noor.ui.ToolbarBuilder;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class FileTablePanel extends JPanel {

    private final RenamerService renamerService;
    private final I18n i18n;
    private final JTable table;
    private final JLabel infoLabel;
    private final JLabel fileInfoLabel;
    RenamableFileTableModel tableModel;

    public FileTablePanel(RenamerService renamerService, 
    		I18n i18n, 
    		Runnable onFileChooser, 
    		Runnable onDirChooser, 
    		Runnable onRename) {
        super(new BorderLayout());
        this.renamerService = renamerService;
        this.i18n = i18n;

        JPanel container = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[][grow][]"));
        JScrollPane tableScrollPane = new JScrollPane();
        tableModel = new RenamableFileTableModel(i18n);
        this.renamerService.addListener(tableModel);
        table = new JTable(tableModel);
        table.putClientProperty("Table.alternateRowColor", null);
        tableScrollPane.setViewportView(table);

        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new NewNameCellRenderer());

        TableRowSorter<RenamableFileTableModel> sorter = new TableRowSorter<>(tableModel);
        sorter.setComparator(1, new NaturalOrderComparator());
        sorter.setComparator(2, new NaturalOrderComparator());
        table.setRowSorter(sorter);
        

        FileTransferHandler dnd = new FileTransferHandler(
                table,
                this::handleDroppedFiles
        );       
        
     // Tasto DELETE
        KeyStroke delete = KeyStroke.getKeyStroke("DELETE");

        table.getInputMap(JComponent.WHEN_FOCUSED).put(delete, "deleteRow");

        table.getActionMap().put("deleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;

                tableModel.removeRow(row);
            }
        });
        
        
        infoLabel = new JLabel(i18n.get("table.noFiles"));
        fileInfoLabel = new JLabel(i18n.get("table.noFileSelected"));

        table.getSelectionModel().addListSelectionListener(e -> updateFileInfo());
        this.renamerService.addListener(this::updateGlobalInfo);

        JToolBar toolBar = ToolbarBuilder.buildToolbar(i18n, onFileChooser, onDirChooser, onRename);

        container.add(toolBar, "wrap");

        tableScrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(5, 0, 5, 0))
        );
        container.add(tableScrollPane, "grow, push, wrap");

        table.setTransferHandler(dnd);
        tableScrollPane.setTransferHandler(dnd);
 
        
        JPanel south = new JPanel(new MigLayout("insets 5 10 5 10, fillx", "[grow]", "[]5[]"));
        south.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));

        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.PLAIN, 12f));
        fileInfoLabel.setFont(fileInfoLabel.getFont().deriveFont(Font.ITALIC, 12f));
        fileInfoLabel.setForeground(new Color(100, 100, 100));

        south.add(infoLabel, "wrap");
        south.add(fileInfoLabel, "growx");
        container.add(south, "growx");

        add(container, BorderLayout.CENTER);
    }
    
    private void handleDroppedFiles(List<File> files) {
        for (File f : files) {
            if (f.isFile()) {
                tableModel.addFile(new RenamableFile(f));
            }
        }
    }



	private void updateGlobalInfo(List<RenamableFile> files) {
        if (files == null || files.isEmpty()) {
            infoLabel.setText(i18n.get("table.noFiles"));
            return;
        }

        String dir = files.getFirst().getSource().getParent();
        infoLabel.setText(i18n.get("table.filesLoaded", files.size(), dir));
    }

    private void updateFileInfo() {
        int row = table.getSelectedRow();
        if (row < 0) {
            fileInfoLabel.setText(i18n.get("table.noFileSelected"));
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        RenamableFileTableModel model = (RenamableFileTableModel) table.getModel();
        RenamableFile file = model.getFileAt(modelRow);
        if (file == null) {
            fileInfoLabel.setText(i18n.get("table.noFileSelected"));
            return;
        }

        java.io.File src = file.getSource();
        String name = src.getName();
        String ext = file.getExtension().isEmpty() ? i18n.get("table.noExtension") : file.getExtension();
        long size = src.length() / 1024;
        String readableSize = size == 0 ? i18n.get("table.size.lessThanOneKb") : size + " KB";

        fileInfoLabel.setText(i18n.get("table.fileInfo", name, ext, readableSize));
    }

    public JTable getTable() {
        return table;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    public JLabel getFileInfoLabel() {
        return fileInfoLabel;
    }
}
