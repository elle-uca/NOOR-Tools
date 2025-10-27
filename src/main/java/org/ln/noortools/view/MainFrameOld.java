//package org.ln.noortools.view;
//
//
//import javax.swing.JFrame;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//@SuppressWarnings("serial")
//@Component
//@ConditionalOnProperty(name = "app.ui.enabled", havingValue = "true", matchIfMissing = true)
//public class MainFrameOld extends JFrame {
//
//    private final RenameService renameService;
//
//    public MainFrameOld(RenameService renameService) {
////        super("NOOR Tools - File Renamer");
////        this.renameService = renameService;
////        initUI();
////    }
////
////    private void initUI() {
////        setLayout(new BorderLayout(10, 10));
////        setSize(450, 200);
////        setLocationRelativeTo(null);
////        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////
////        JLabel label = new JLabel("Select a folder to rename files");
////        JButton button = new JButton("Choose Folder");
////        button.addActionListener(e -> chooseFolder());
////
////        JPanel panel = new JPanel(new FlowLayout());
////        panel.add(label);
////        panel.add(button);
////
////        add(panel, BorderLayout.CENTER);
////    }
////
////    private void chooseFolder() {
////        JFileChooser chooser = new JFileChooser();
////        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
////
////        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
////            File dir = chooser.getSelectedFile();
////            int renamed = renameService.renameFiles(dir);
////            JOptionPane.showMessageDialog(this, "Renamed " + renamed + " files.");
////        }
////    }
//}
