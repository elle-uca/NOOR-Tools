package org.ln.noortools;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.view.panel.AccordionPanel;
import org.ln.noortools.view.panel.AddPanel;
import org.ln.noortools.view.panel.CasePanel;
import org.ln.noortools.view.panel.RemovePanel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RenamerDemoApp {

    public static void main(String[] args) {
    	
        // ⚡ Assicurati che non sia headless
        System.setProperty("java.awt.headless", "false");
        
        // Avvia il contesto Spring Boot
        ConfigurableApplicationContext context =
                SpringApplication.run(NoorToolsApplication.class, args);
        
        // Recupera i bean Spring
        I18n i18n = context.getBean(I18n.class);

        // Recupera i servizi Spring
        //ReplaceRuleService ruleService = context.getBean(ReplaceRuleService.class);
        RenamerService renamerService = context.getBean(RenamerService.class);
        
        // Avvio Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Renamer Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);


            // --- Tabella a destra
            FileTableModel tableModel = new FileTableModel();
            JTable table = new JTable(tableModel);

            // Listener -> aggiorna la tabella quando cambia la lista
            renamerService.addListener(tableModel::setFiles);

            // --- Mock files iniziali
            List<RenamableFile> initial = List.of(
                    new RenamableFile("fileOne.txt"),
                    new RenamableFile("helloWorld.java"),
                    new RenamableFile("TestFile.pdf")
            );
            renamerService.setFiles(new ArrayList<>(initial));

            // --- Accordion a sinistra con un AddPanel
            AccordionPanel accordion = new AccordionPanel();
            accordion.addPanel("Add Text",
                    new AddPanel(accordion, i18n,  renamerService));
            accordion.addPanel("Replace Text",
                    new RemovePanel(accordion, i18n,  renamerService));
            accordion.addPanel("Case Text",
                    new CasePanel(accordion, i18n,  renamerService));
            
            // --- Layout con split
            JSplitPane split = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    accordion,
                    new JScrollPane(table)
            );
            split.setDividerLocation(250);

            frame.add(split, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
    
    // --- Modello tabella minimale
    @SuppressWarnings("serial")
	static class FileTableModel extends AbstractTableModel {
        private List<RenamableFile> files = new ArrayList<>();
        private final String[] cols = {"Original", "Destination"};

        public void setFiles(List<RenamableFile> files) {
            this.files = files != null ? files : new ArrayList<>();
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return files.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int r, int c) {
            RenamableFile f = files.get(r);
            return switch (c) {
                case 0 -> f.getSource().getName();
                case 1 -> f.getDestinationName();
                default -> "";
            };
        }
    }
}
