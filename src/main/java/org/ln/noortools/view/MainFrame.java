package org.ln.noortools.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import org.ln.noortools.service.RenamerService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

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

	private void setMenuBar() {
		// TODO Auto-generated method stub
		
	}

	private JPanel createMethodPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		return panel;
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
		infoLabel = new JLabel("Label");
		
		north.setLayout(new MigLayout("", "[grow]", "[]"));
		north.add(addFileButton);
		north.add(addDirButton);
		north.add(renameButton);

		south.setLayout(new MigLayout("", "[grow]", "[]"));
		south.add(infoLabel);
		
		container.setLayout(new MigLayout("", "[grow]", "[]"));
		container.add(getTooBar(), 		"wrap");
		container.add(north, 			"wrap");
		container.add(tableScrollPane, 	"grow, wrap");
		container.add(south);
		return container;
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
