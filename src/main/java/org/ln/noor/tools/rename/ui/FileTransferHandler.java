package org.ln.noor.tools.rename.ui;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class FileTransferHandler extends TransferHandler {

    private final Consumer<List<File>> onFilesDropped;
    private final JComponent highlightComponent;

    private Color originalBg;
    private Border originalBorder;

    public FileTransferHandler(JComponent highlightComponent,
                               Consumer<List<File>> onFilesDropped) {
        this.highlightComponent = highlightComponent;
        this.onFilesDropped = onFilesDropped;

        this.originalBg = highlightComponent.getBackground();
        this.originalBorder = highlightComponent.getBorder();
    }

    @Override
    public boolean canImport(TransferSupport support) {
        boolean ok = support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);

        if (ok) highlight();
        else unhighlight();

        return ok;
    }

    @Override
    public boolean importData(TransferSupport support) {
        unhighlight(); // fine drag & drop

        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            return false;

        try {
            @SuppressWarnings("unchecked")
            List<File> files =
                (List<File>) support.getTransferable()
                        .getTransferData(DataFlavor.javaFileListFlavor);

            onFilesDropped.accept(files);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        unhighlight();
    }

    private void highlight() {
        highlightComponent.setBackground(new Color(180, 210, 255));
        highlightComponent.setBorder(BorderFactory.createLineBorder(new Color(30, 100, 255), 2));
    }

    private void unhighlight() {
        highlightComponent.setBackground(originalBg);
        highlightComponent.setBorder(originalBorder);
    }
}
