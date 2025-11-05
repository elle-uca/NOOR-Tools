package org.ln.noortools.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.panel.AccordionFactory;
import org.ln.noortools.view.panel.AccordionPanel;
import org.ln.noortools.view.panel.PanelFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
//@Component
public class MainFrameOld extends JFrame {

	private final ConfigurableApplicationContext context;
	private JLabel infoLabel;        // global info
	private JLabel fileInfoLabel;    // single file info
	private JToggleButton themeToggle;
	
	private JLabel statusBarLabel;
	private JTable table;
	private AccordionPanel accordion;
	private final  I18n i18n;
	private final  RenamerService renamerService;
	private final AccordionFactory accordionFactory;
	private final PanelFactory panelFactory;
	

	public MainFrameOld(I18n i18n, 
			RenamerService renamerService, 
			PanelFactory panelFactory, 
			AccordionFactory accordionFactory, 
			ConfigurableApplicationContext context) {
		super("NOOR Tools (Not Only an Ordinary Renamer) by Luke");
		this.i18n = i18n;
		this.renamerService = renamerService;
		this.accordionFactory = accordionFactory;
		this.panelFactory = panelFactory;
		this.accordion = accordionFactory.createAccordion();
		this.context = context;

		// ✅ Avvio in Light mode
		FlatLightLaf.setup();

		initComponents();
		
		setSize(950, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// 👇 Chiudi Spring al termine
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                context.close();
            }
        });
	}


	private void initComponents() {


		JSplitPane splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, 
				createMethodPanel(), 
				createTablePanel());
		splitPane.setDividerLocation(400);
		getContentPane().add(splitPane);
		// --- Status bar in basso ---
		statusBarLabel = new JLabel("🌞 Light mode — 0 rules");
		statusBarLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		statusBarLabel.setFont(statusBarLabel.getFont().deriveFont(Font.PLAIN, 12f));

        // 🔘 Toggle Light/Dark
        themeToggle = new JToggleButton("🌞");
        themeToggle.addActionListener(e -> switchTheme());
		
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		statusPanel.add(themeToggle);
		statusPanel.add(statusBarLabel);
		
		statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));

		getContentPane().add(statusPanel, BorderLayout.SOUTH);

		
		setMenuBar();
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
		TextButton tagButton = new TextButton("New Name", "NEW");

		buttonPanel.setLayout(new GridLayout(0, 4));
		buttonPanel.add(tagButton);
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(replaceButton);
		buttonPanel.add(caseButton);
		

		accordion = new AccordionPanel();
		accordion.setLayout(new BoxLayout(accordion, BoxLayout.Y_AXIS));
		accordion.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
		accordion.setBorder(BorderFactory.createEmptyBorder());

		addButton.addActionListener(e -> addPanel(e));
		removeButton.addActionListener(e -> addPanel(e));
		caseButton.addActionListener(e -> addPanel(e));
		replaceButton.addActionListener(e -> addPanel(e));
		tagButton.addActionListener(e -> addPanel(e));

		panel.add(accordion, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.SOUTH);


		return panel;
	}

	private void addPanel(ActionEvent e) {
		JButton button = (JButton) e.getSource();

		// accordion.addPanel(button.getText(), panelFactory.createAddPanel(accordion));

		switch (button.getActionCommand()) {
		case "ADD" ->
		accordion.addPanel(button.getText(), panelFactory.createAddPanel(accordion));
		case "REMOVE" ->
		accordion.addPanel(button.getText(), panelFactory.createRemovePanel(accordion));
		case "REPLACE" ->
		accordion.addPanel(button.getText(), panelFactory.createReplacePanel(accordion));
		case "CASE" ->
		accordion.addPanel(button.getText(), panelFactory.createCasePanel(accordion));
		case "NEW" ->
		accordion.addPanel(button.getText(), panelFactory.createTagPanel(accordion));
		default ->
		throw new IllegalArgumentException("Unknown command: " + button.getActionCommand());
		}



		//	    case "Nuovo nome" -> accordion.addPanel(name, new TagPanel(accordion));
		//	    case "Split" -> accordion.addPanel(name, new SplitPanel(accordion));
		//	    case "Merge" -> accordion.addPanel(name, new MergePanel(accordion));
	}

	private JPanel createTablePanel() {
		JPanel container = new JPanel();
		JPanel south = new JPanel();
		JScrollPane tableScrollPane = new JScrollPane();
		RenamableFileTableModel tableModel = new RenamableFileTableModel();
		renamerService.addListener(tableModel);
		table = new JTable(tableModel);
		tableScrollPane.setViewportView(table);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new NewNameCellRenderer());
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
		sorter.setComparator(1, new NaturalOrderComparator()); // colonna Name
		sorter.setComparator(2, new NaturalOrderComparator()); // colonna New Name
		table.setRowSorter(sorter);
		
		

		// --- Labels
		infoLabel = new JLabel("No files loaded");
		fileInfoLabel = new JLabel("No file selected");
		south.setLayout(new MigLayout("", "[grow]", "[]"));
		south.add(infoLabel,  			"wrap");
		south.add(fileInfoLabel);

		// --- Listeners
		table.getSelectionModel().addListSelectionListener(e -> updateFileInfo(table, tableModel));
		renamerService.addListener(updatedFiles -> updateGlobalInfo(updatedFiles));

		JToolBar toolBar = ToolbarBuilder.buildToolbar(
				this::showFileChooser,   // 👈 riferimento diretto al metodo
				this::showDirChooser,
				this::rename
				);

        //toolBar.addSeparator(new Dimension(20, 0));
        //toolBar.add(themeToggle);
		container.setLayout(new MigLayout(
				"fill, insets 10",     // padding interno di 10px
				"[grow]",              // una sola colonna che cresce
				"[][grow][]"));        // toolbar, tabella (grow), pannello info

		// Toolbar
		container.add(toolBar, "wrap");

		// Tabella (espande in entrambe le direzioni)
		tableScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)),
						BorderFactory.createEmptyBorder(5, 0, 5, 0)
						)
				);
		container.add(tableScrollPane, "grow, push, wrap");

		// South panel con due label e un bordo superiore sottile
		south = new JPanel(new MigLayout("insets 5 10 5 10, fillx", "[grow]", "[]5[]"));
		south.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));

		infoLabel = new JLabel("No files loaded");
		fileInfoLabel = new JLabel("No file selected");

		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.PLAIN, 12f));
		fileInfoLabel.setFont(fileInfoLabel.getFont().deriveFont(Font.ITALIC, 12f));
		fileInfoLabel.setForeground(new Color(100, 100, 100));

		south.add(infoLabel, "wrap");
		south.add(fileInfoLabel, "growx");

		container.add(south, "growx");

		return container;
	}

	private void showFileChooser() {
		File[] chosen = SwingUtil.showOpenDialog(this, JFileChooser.FILES_ONLY, true);
		if (chosen.length > 0) {
			List<RenamableFile> files = new ArrayList<RenamableFile>();
			for (File f : chosen) {
				//System.out.println("Selected: " + f.getAbsolutePath());
				RenamableFile file = new RenamableFile(f);
				files.add(file);
			}
			renamerService.setFiles(files);
		}
	}


	private void showDirChooser() {
		File[] chosen = SwingUtil.showOpenDialog(this, JFileChooser.DIRECTORIES_ONLY, true);
		if (chosen.length > 0) {
			List<RenamableFile> files = new ArrayList<RenamableFile>();
			for (File f : chosen) {
				//System.out.println("Selected: " + f.getAbsolutePath());
				RenamableFile file = new RenamableFile(f);
				files.add(file);
			}
			renamerService.setFiles(files);
		}
	}

	private void updateGlobalInfo(List<RenamableFile> files) {
		if (files == null || files.isEmpty()) {
			infoLabel.setText("No files loaded");
			return;
		}

		// Get directory (first file’s parent)
		String dir = files.get(0).getSource().getParent();
		infoLabel.setText(String.format("%d files loaded from: %s", files.size(), dir));
	}

	private void updateFileInfo(JTable table, RenamableFileTableModel model) {
		int row = table.getSelectedRow();
		if (row < 0) {
			fileInfoLabel.setText("No file selected");
			return;
		}

		int modelRow = table.convertRowIndexToModel(row);
		RenamableFile file = model.getFileAt(modelRow);
		if (file == null) {
			fileInfoLabel.setText("No file selected");
			return;
		}

		File src = file.getSource();
		String name = src.getName();
		String ext = file.getExtension().isEmpty() ? "(no extension)" : file.getExtension();
		long size = src.length() / 1024;
		String readableSize = size == 0 ? "<1 KB" : size + " KB";

		fileInfoLabel.setText(String.format("Selected: %s  —  Type: %s  —  Size: %s", name, ext, readableSize));
	}


	private void updateStatusBar() {
	    String theme = (themeToggle != null && themeToggle.isSelected()) ? "🌙 Dark mode" : "🌞 Light mode";
	    int ruleCount = (accordion != null) ? accordion.getPanelCount() : 0;
	    statusBarLabel.setText(String.format("%s — %d rule%s loaded", theme, ruleCount, ruleCount == 1 ? "" : "s"));
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
			//ThemeUtil.applyDarkTheme(this, table, 2); 
			updateStatusBar(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void rename() {
		renamerService.renameFiles();
	}


}
