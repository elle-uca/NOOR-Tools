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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.prefs.Prefs;
import org.ln.noortools.service.RenameController;
import org.ln.noortools.service.ruleservice.RenamerService;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.component.StatusBarPanel;
import org.ln.noortools.view.panel.AccordionFactory;
import org.ln.noortools.view.panel.AccordionPanel;
import org.ln.noortools.view.panel.FileTablePanel;
import org.ln.noortools.view.panel.PanelFactory;
import org.ln.noortools.view.panel.RuleButtonBar;
import org.ln.noortools.view.dialog.AboutDialog;
import org.ln.noortools.view.dialog.PreferencesDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

@SuppressWarnings("serial")
@Component
public class MainFrame extends JFrame {

	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

        private final I18n i18n;
        private final RenamerService renamerService;
        private final PanelFactory panelFactory;
        private final RenameController renameController;
        private final Prefs prefs;

	private StatusBarPanel statusBarPanel;
	private AccordionPanel accordion;



	public MainFrame(I18n i18n,
                        RenamerService renamerService,
                        PanelFactory panelFactory,
                        AccordionFactory accordionFactory,
                        ConfigurableApplicationContext context,
                        RenameController renameController,
                        Prefs prefs) {
                super(i18n.get("main.title"));
                this.i18n = i18n;
                this.renamerService = renamerService;
                this.panelFactory = panelFactory;
                this.accordion = accordionFactory.createAccordion();
                this.renameController = renameController;
                this.prefs = prefs;

                setupInitialTheme();

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
                                i18n,
                                e -> switchTheme(),
                                e -> handleUndo(),
                                new ImageIcon(getClass().getResource("/icons/undo.png")));
                statusBarPanel.updateThemeSymbol(isDarkModePreferred());
                renameController.addUndoStateListener(available -> statusBarPanel.setUndoEnabled(available));

		getContentPane().add(statusBarPanel, BorderLayout.SOUTH);

		setMenuBar();
		updateStatusBar();
	}


	private void setMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(i18n.get("menu.file"));
		JMenuItem addFileItem = new JMenuItem(i18n.get("toolbar.button.file"));
		addFileItem.addActionListener(e -> showFileChooser());
		fileMenu.add(addFileItem);
		JMenuItem addDirItem = new JMenuItem(i18n.get("toolbar.button.directory"));
		addDirItem.addActionListener(e -> showDirChooser());
		fileMenu.add(addDirItem);
		JMenuItem exitItem = new JMenuItem(i18n.get("menu.file.exit"));
		exitItem.addActionListener(e -> dispatchEvent(
				new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
		fileMenu.add(exitItem);

                JMenu viewMenu = new JMenu(i18n.get("menu.view"));
                JMenuItem themeItem = new JMenuItem(i18n.get("menu.view.toggleTheme"));
                themeItem.addActionListener(e -> statusBarPanel.toggleTheme());
                viewMenu.add(themeItem);

                JMenu toolMenu = new JMenu(i18n.get("menu.tool"));

                JMenu helpMenu = new JMenu(i18n.get("menu.help"));
                JMenuItem preferencesItem = new JMenuItem(i18n.get("menu.help.preferences"));
                preferencesItem.addActionListener(e -> showPreferences());
                helpMenu.add(preferencesItem);
                JMenuItem aboutItem = new JMenuItem(i18n.get("menu.help.about"));
                aboutItem.addActionListener(e -> AboutDialog.show(this, i18n));
                helpMenu.add(aboutItem);

                menuBar.add(fileMenu);
                menuBar.add(viewMenu);
                menuBar.add(toolMenu);
                menuBar.add(helpMenu);

		setJMenuBar(menuBar);

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
				i18n,
				this::showFileChooser,
				this::showDirChooser,
				this::rename);
		return tablePanel;
	}

	private void showFileChooser() {
		File[] chosen = SwingUtil.showOpenDialog(this, JFileChooser.FILES_ONLY, true);
		if (chosen.length > 0) {
			List<RenamableFile> files = new ArrayList<>();
			for (File f : chosen) {
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
				RenamableFile file = new RenamableFile(f);
				files.add(file);
			}
			renamerService.setFiles(files);
		}
	}
        private void updateStatusBar() {
                String theme = (statusBarPanel != null && statusBarPanel.isDarkModeSelected())
                                ? i18n.get("status.theme.dark")
                                                : i18n.get("status.theme.light");
		int ruleCount = (accordion != null) ? accordion.getPanelCount() : 0;
		String rulesMessage = ruleCount == 1
				? i18n.get("status.rules.single", ruleCount)
						: i18n.get("status.rules.multiple", ruleCount);
		statusBarPanel.setStatusText(i18n.get("status.summary", theme, rulesMessage));
	}

        private void switchTheme() {
                try {
                        applyTheme(statusBarPanel.isDarkModeSelected());
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void showPreferences() {
                PreferencesDialog dialog = new PreferencesDialog(this, i18n, prefs);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                        applyTheme("dark".equalsIgnoreCase(dialog.getSelectedTheme()));
                        updateStatusBar();
                }
        }

        private void applyTheme(boolean darkMode) {
                try {
                        if (darkMode) {
                                UIManager.setLookAndFeel(new FlatDarkLaf());
                                prefs.setTheme("dark");
                        } else {
                                UIManager.setLookAndFeel(new FlatLightLaf());
                                prefs.setTheme("light");
                        }
                        if (statusBarPanel != null) {
                                statusBarPanel.updateThemeSymbol(darkMode);
                        }
                        SwingUtilities.updateComponentTreeUI(this);
                        updateStatusBar();
                } catch (Exception e) {
                        logger.error("Unable to apply theme", e);
                }
        }

        private boolean isDarkModePreferred() {
                return "dark".equalsIgnoreCase(prefs.getTheme());
        }

        private void setupInitialTheme() {
                if (isDarkModePreferred()) {
                        FlatDarkLaf.setup();
                } else {
                        FlatLightLaf.setup();
                }
        }


	private void rename()  {
		try {
			renameController.renameFiles(renamerService.getFiles());
			JOptionPane.showMessageDialog(this, i18n.get("rename.success"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, i18n.get("rename.error", e.getMessage()));
			logger.error("Rename failed", e);
		}
	}

	private void handleUndo() {
		try {
			renameController.undoLastRename();
			JOptionPane.showMessageDialog(this, i18n.get("undo.success"));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, i18n.get("undo.error", ex.getMessage()));
			logger.error("Undo failed", ex);
		}
	}

}
