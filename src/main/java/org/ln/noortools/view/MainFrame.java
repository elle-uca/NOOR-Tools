package org.ln.noortools.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenameController;
import org.ln.noortools.service.ruleservice.RenamerService;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.panel.AccordionFactory;
import org.ln.noortools.view.panel.AccordionPanel;
import org.ln.noortools.view.panel.FileTablePanel;
import org.ln.noortools.view.panel.PanelFactory;
import org.ln.noortools.view.panel.RuleButtonBar;
import org.ln.noortools.view.component.StatusBarPanel;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

@SuppressWarnings("serial")
@Component
public class MainFrame extends JFrame {

	private final I18n i18n;
	private final RenamerService renamerService;
	private final PanelFactory panelFactory;
	private final RenameController renameController;

	private StatusBarPanel statusBarPanel;
	private JTable table;
	private AccordionPanel accordion;



	public MainFrame(I18n i18n,
			RenamerService renamerService,
			PanelFactory panelFactory,
			AccordionFactory accordionFactory,
			ConfigurableApplicationContext context,
			RenameController renameController) {
		super("NOOR Tools (Not Only an Ordinary Renamer) by Luke");
		this.i18n = i18n;
		this.renamerService = renamerService;
		this.panelFactory = panelFactory;
		this.accordion = accordionFactory.createAccordion();
		this.renameController = renameController;

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
		statusBarPanel = new StatusBarPanel(
				e -> switchTheme(),
				e -> handleUndo(),
				new ImageIcon(getClass().getResource("/icons/undo.png")));
		renameController.addUndoStateListener(available -> statusBarPanel.setUndoEnabled(available));

		getContentPane().add(statusBarPanel, BorderLayout.SOUTH);

		setMenuBar();
		updateStatusBar();
	}


	private void setMenuBar() {
		// TODO Auto-generated method stub

	}

	private JPanel createMethodPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		accordion = new AccordionPanel();
		accordion.setLayout(new BoxLayout(accordion, BoxLayout.Y_AXIS));
		accordion.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
		accordion.setBorder(BorderFactory.createEmptyBorder());
		panel.add(accordion, BorderLayout.NORTH);

		JPanel ruleButtonBar = new RuleButtonBar(panelFactory, accordion, this::updateStatusBar);
		panel.add(ruleButtonBar, BorderLayout.SOUTH);

		return panel;
	}
	private JPanel createTablePanel() {
		FileTablePanel tablePanel = new FileTablePanel(
				renamerService,
				this::showFileChooser,
				this::showDirChooser,
				this::rename);
		table = tablePanel.getTable();
		return tablePanel;
	}

	private void showFileChooser() {
		File[] chosen = SwingUtil.showOpenDialog(this, JFileChooser.FILES_ONLY, true);
		if (chosen.length > 0) {
			List<RenamableFile> files = new ArrayList<>();
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
			List<RenamableFile> files = new ArrayList<>();
			for (File f : chosen) {
				//System.out.println("Selected: " + f.getAbsolutePath());
				RenamableFile file = new RenamableFile(f);
				files.add(file);
			}
			renamerService.setFiles(files);
		}
	}
	private void updateStatusBar() {
		String theme = (statusBarPanel != null && statusBarPanel.isDarkModeSelected()) ? "🌙 Dark mode" : "🌞 Light mode";
		int ruleCount = (accordion != null) ? accordion.getPanelCount() : 0;
		statusBarPanel.setStatusText(String.format("%s — %d rule%s loaded", theme, ruleCount, ruleCount == 1 ? "" : "s"));
	}

	private void switchTheme() {
		try {
			if (statusBarPanel.isDarkModeSelected()) {
				UIManager.setLookAndFeel(new FlatDarkLaf());
				statusBarPanel.updateThemeSymbol(true);
			} else {
				UIManager.setLookAndFeel(new FlatLightLaf());
				statusBarPanel.updateThemeSymbol(false);
			}
			SwingUtilities.updateComponentTreeUI(this);
			updateStatusBar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void rename()  {
		try {
			renameController.renameFiles(renamerService.getFiles());
			JOptionPane.showMessageDialog(this, "✅ Rinomina completata!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "❌ Errore durante la rinomina:\n" + e.getMessage());
		}
	}

	private void handleUndo() {
		try {
			renameController.undoLastRename();
			JOptionPane.showMessageDialog(this, "↩️ Ultima rinomina annullata.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "❌ Errore durante la rinomina:\n" + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
