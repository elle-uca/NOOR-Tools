//package org.ln.noortools.view.component;
//
//import javax.swing.*;
//import java.awt.Color;
//import java.awt.datatransfer.*;
//import java.io.File;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class TableFileDropHandler extends TransferHandler {
//
//    private final Consumer<List<File>> onFilesDropped;
//    private final JComponent highlightComp;
//    private final Color originalBg;
//
//    public TableFileDropHandler(JComponent highlightComp,
//                                Consumer<List<File>> onFilesDropped) {
//        this.highlightComp = highlightComp;
//        this.onFilesDropped = onFilesDropped;
//        this.originalBg = highlightComp.getBackground();
//        System.out.println("TransferHandler attivato su JTable!");
//    }
//
//    @Override
//    public boolean canImport(TransferSupport support) {
//        boolean ok = support.isDrop()
//                && support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
//
//        if (ok) {
//            highlightComp.setBackground(new Color(180, 210, 255));
//        } else {
//            highlightComp.setBackground(originalBg);
//        }
//
//        return ok;
//    }
//
//    @Override
//    public boolean importData(TransferSupport support) {
//        highlightComp.setBackground(originalBg);
//
//        if (!canImport(support))
//            return false;
//
//        try {
//            @SuppressWarnings("unchecked")
//            List<File> files = (List<File>) support.getTransferable()
//                    .getTransferData(DataFlavor.javaFileListFlavor);
//
//            onFilesDropped.accept(files);
//            return true;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//    }
//}
