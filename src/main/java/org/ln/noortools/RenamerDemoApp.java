package org.ln.noortools;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.view.NaturalOrderComparator;
import org.ln.noortools.view.NewNameCellRenderer;
import org.ln.noortools.view.RenamableFileTableModel;
import org.ln.noortools.view.StatusCellRenderer;
import org.ln.noortools.view.panel.AccordionPanel;
import org.ln.noortools.view.panel.AddPanel;
import org.ln.noortools.view.panel.CasePanel;
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
        RenamerService renamerService = context.getBean(RenamerService.class);
        
        // Avvio Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Renamer Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);


            // --- Tabella a destra
            RenamableFileTableModel tableModel = new RenamableFileTableModel();
             JTable table = new JTable(tableModel);

            // Listener -> aggiorna la tabella quando cambia la lista
            renamerService.addListener(tableModel);
            
    		table.setAutoCreateRowSorter(true);
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
            table.getColumnModel().getColumn(2).setCellRenderer(new NewNameCellRenderer());
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            sorter.setComparator(1, new NaturalOrderComparator()); // colonna Name
            sorter.setComparator(2, new NaturalOrderComparator()); // colonna New Name
            table.setRowSorter(sorter);
            
            
            

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
//            accordion.addPanel("Replace Text",
//                    new RemovePanel(accordion, i18n,  renamerService));
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
    
}
