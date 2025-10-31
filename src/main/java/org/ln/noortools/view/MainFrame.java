package org.ln.noortools.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.ln.noortools.NoorToolsApplication;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.panel.AccordionPanel;
import org.ln.noortools.view.panel.AddPanel;
import org.ln.noortools.view.panel.CasePanel;
import org.ln.noortools.view.panel.RemovePanel;
import org.ln.noortools.view.panel.ReplacePanel;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
//@Component
public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea tagArea;
    private JComboBox<String> fillCombo;
    private JButton addFileBtn, addDirBtn, renameBtn;
    private JToggleButton themeToggle;
    
	private JButton renameButton;
	private JButton addFileButton;
	private JButton addDirButton;
	private JLabel infoLabel;
	
	 I18n i18n;
	 RenamerService renamerService;
	 AccordionPanel accordion;

    public MainFrame() {
        super("NOOR Tools (Not Only an Ordinary Renamer) by Luke");
        

        // ⚡ Assicurati che non sia headless
        System.setProperty("java.awt.headless", "false");
        String args = "";
		// Avvia il contesto Spring Boot
        ConfigurableApplicationContext context =
                SpringApplication.run(NoorToolsApplication.class, args );

        i18n = context.getBean(I18n.class);


        // Recupera i servizi Spring
        renamerService = context.getBean(RenamerService.class);

        // ✅ Avvio in Light mode
        FlatLightLaf.setup();

        initComponents();
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JSplitPane splitPane = new JSplitPane(
        		JSplitPane.HORIZONTAL_SPLIT, 
				createMethodPanel(), 
				createTablePanel());
		splitPane.setDividerLocation(400);
		getContentPane().add(splitPane);
		setMenuBar();
    }

    private JToolBar getTooBar() {
        // Toolbar moderna
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // no drag
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addFileBtn = new JButton("File", UIManager.getIcon("FileView.fileIcon"));
        addFileBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        addFileBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        addFileBtn.addActionListener(e -> showFileChooser());

        addDirBtn = new JButton("Directory", UIManager.getIcon("FileView.directoryIcon"));
        addDirBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        addDirBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        renameBtn = new JButton("Rinomina", UIManager.getIcon("OptionPane.informationIcon"));
        renameBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        renameBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        toolBar.add(addFileBtn);
        toolBar.add(addDirBtn);
        toolBar.add(renameBtn);
        toolBar.addSeparator(new Dimension(20, 0));
		return toolBar;
	}
    
    private JToolBar createTooBar() {
    	JToolBar toolBar = new JToolBar();
    	 toolBar.setFloatable(false);
         toolBar.setRollover(true);
         toolBar.setBackground(UIManager.getColor("Panel.background"));
         toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

         // 🔹 Crea pulsanti
         JButton btnFile = createToolbarButton("File", new FlatSVGIcon("icons/file.svg", 32, 32));
         JButton btnDir  = createToolbarButton("Directory", new FlatSVGIcon("icons/folder.svg", 32, 32));
         JButton btnRename = createToolbarButton("Rinomina", new FlatSVGIcon("icons/rename.svg", 32, 32));

         // 🔹 Uniforma dimensioni
         makeUniformSize(btnFile, btnDir, btnRename);

         // 🔹 Aggiungi alla toolbar
         toolBar.add(btnFile);
         toolBar.add(btnDir);
         toolBar.addSeparator(new Dimension(15, 0));
         toolBar.add(btnRename);
		return toolBar;
	} 
    
    private static JButton createToolbarButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFocusable(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setBackground(UIManager.getColor("Panel.background"));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static void makeUniformSize(JButton... buttons) {
        int maxWidth = 0;
        int maxHeight = 0;

        // Trova le dimensioni massime tra tutti
        for (JButton b : buttons) {
            Dimension pref = b.getPreferredSize();
            maxWidth = Math.max(maxWidth, pref.width);
            maxHeight = Math.max(maxHeight, pref.height);
        }

        Dimension uniform = new Dimension(maxWidth, maxHeight);
        for (JButton b : buttons) {
            b.setPreferredSize(uniform);
            b.setMinimumSize(uniform);
            b.setMaximumSize(uniform);
        }
    }

	private void setMenuBar() {
		// TODO Auto-generated method stub
		
	}

	private JPanel createMethodPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		TextButton addButton = new TextButton("Aggiungi", "ADD");
		TextButton removeButton = new TextButton("Rimuovi", "REMOVE");
		TextButton replaceButton = new TextButton("Replace", "REPLACE");
		TextButton caseButton = new TextButton("Case", "CASE");
		
		buttonPanel.setLayout(new GridLayout(0, 4));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(replaceButton);
		buttonPanel.add(caseButton);
		
		accordion = new AccordionPanel();
		accordion.setLayout(new BoxLayout(accordion, BoxLayout.Y_AXIS));
		accordion.setAlignmentX(Component.LEFT_ALIGNMENT);
		accordion.setBorder(BorderFactory.createEmptyBorder());
		
		addButton.addActionListener(e -> addPanel(e));
		removeButton.addActionListener(e -> addPanel(e));
		caseButton.addActionListener(e -> addPanel(e));
		replaceButton.addActionListener(e -> addPanel(e));
		
		panel.add(accordion, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.SOUTH);


		return panel;
	}

    private void addPanel(ActionEvent e) {
    	 JButton button = (JButton) e.getSource();
		
		switch (button.getActionCommand()) {
	    case "ADD" -> accordion.addPanel(button.getText(), 
	    		new AddPanel(accordion, i18n,  renamerService));
	    case "REMOVE" -> accordion.addPanel(button.getText(), 
	    		new RemovePanel(accordion, i18n,  renamerService));
	    case "REPLACE" -> accordion.addPanel(button.getText(), 
	    		new ReplacePanel(accordion, i18n,  renamerService));
	    case "CASE" -> accordion.addPanel(button.getText(), 
	    		new CasePanel(accordion, i18n,  renamerService));
//	    case "Nuovo nome" -> accordion.addPanel(name, new TagPanel(accordion));
//	    case "Split" -> accordion.addPanel(name, new SplitPanel(accordion));
//	    case "Merge" -> accordion.addPanel(name, new MergePanel(accordion));
	    default -> throw new IllegalArgumentException("Operazione non riconosciuta: " + button.getActionCommand());
		}
	}

	private JPanel createTablePanel() {
		JPanel container = new JPanel();
		JPanel north = new JPanel();
		JPanel south = new JPanel();
		JScrollPane tableScrollPane = new JScrollPane();
        RenamableFileTableModel tableModel = new RenamableFileTableModel();
        renamerService.addListener(tableModel);
        JTable table = new JTable(tableModel);
        tableScrollPane.setViewportView(table);
		table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new NewNameCellRenderer());
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        sorter.setComparator(1, new NaturalOrderComparator()); // colonna Name
        sorter.setComparator(2, new NaturalOrderComparator()); // colonna New Name
        table.setRowSorter(sorter);
        
		addFileButton = new JButton(i18n.get("tabPanel.button.addFile"));
		addDirButton = new JButton(i18n.get("tabPanel.button.addDirectory"));
		renameButton = new JButton(i18n.get("tabPanel.button.rename"));
		addFileButton.addActionListener(e -> showFileChooser());		
		infoLabel = new JLabel("Label");
		
		north.setLayout(new MigLayout("", "[grow]", "[]"));
		north.add(addFileButton);
		north.add(addDirButton);
		north.add(renameButton);

		south.setLayout(new MigLayout("", "[grow]", "[]"));
		south.add(infoLabel);
		
		container.setLayout(new MigLayout("", "[grow]", "[]"));
		container.add(ToolbarBuilder.buildToolbar(), 		"wrap");
		container.add(north, 			"wrap");
		container.add(tableScrollPane, 	"grow, wrap");
		container.add(south);
		return container;
	}
    
    private void showFileChooser() {
    	File[] chosen = SwingUtil.showOpenDialog(this, JFileChooser.FILES_ONLY, true);
    	if (chosen.length > 0) {
    		List<RenamableFile> files = new ArrayList<RenamableFile>();
    	    for (File f : chosen) {
    	        System.out.println("Selected: " + f.getAbsolutePath());
    	        RenamableFile file = new RenamableFile(f);
    	        files.add(file);
    	    }
    	    renamerService.setFiles(files);
    	}
	}

	private void switchTheme() {
        try {
            if (themeToggle.isSelected()) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                themeToggle.setText("🌙");
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
                themeToggle.setText("🌞");
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per testare la GUI
    public static void main(String[] args) {

    	
    	
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
