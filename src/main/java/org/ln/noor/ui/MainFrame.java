package org.ln.noor.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;

import org.ln.noor.core.enums.Theme;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.core.preferences.PreferencesDialog;
import org.ln.noor.core.preferences.PreferencesService;
import org.ln.noor.core.service.ThemeManager;
import org.ln.noor.core.tool.NoorTool;
import org.ln.noor.tools.directory.DirectoryTool;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.RenameController;
import org.ln.noor.tools.rename.service.RenamerService;
import org.ln.noor.tools.rename.ui.panel.AccordionFactory;
import org.ln.noor.tools.rename.ui.panel.AccordionPanel;
import org.ln.noor.tools.rename.ui.panel.PanelFactory;
import org.ln.noor.tools.rename.ui.panel.RuleButtonBar;
import org.ln.noor.tools.rename.util.SwingUtil;
import org.ln.noor.tools.splitmerge.SplitMergeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Main application window of NOOR Tools.
 * <p>
 * {@code MainFrame} is the central UI container of the application.
 * It assembles and coordinates:
 * <ul>
 *   <li>The rule/method panel (accordion-based)</li>
 *   <li>The file table panel</li>
 *   <li>The application menu bar</li>
 *   <li>The status bar</li>
 * </ul>
 *
 * The frame connects user actions to the underlying services
 * responsible for rename logic, preferences and theme handling.
 * It does not apply changes to the filesystem directly.
 *
 * This class is managed as a Spring component and represents
 * the primary entry point of the desktop UI.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
@Component
public class MainFrame extends JFrame {

    /** Application logger */
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    /** Internationalization service */
    private final I18n i18n;

    /** Service holding the current file list */
    private final RenamerService renamerService;

    /** Factory used to create rule panels */
    private final PanelFactory panelFactory;

    /** Controller responsible for rename and undo operations */
    private final RenameController renameController;

    /** User preferences service */
    private final PreferencesService prefs;

    /** Status bar displayed at the bottom of the frame */
    private StatusBarPanel statusBarPanel;

    /** Accordion containing rename rule panels */
    private AccordionPanel accordion;

    /**
     * Creates the main application frame without touching the filesystem.
     *
     * @param i18n              internationalization service
     * @param renamerService    service managing the file list and rename preview
     * @param panelFactory      factory for rule panels
     * @param accordionFactory  factory for the accordion container
     * @param renameController  controller handling rename operations
     * @param service           user preferences service
     * @param onClose           callback invoked when the application is closing
     */
    public MainFrame(
            I18n i18n,
            RenamerService renamerService,
            PanelFactory panelFactory,
            AccordionFactory accordionFactory,
            RenameController renameController,
            PreferencesService service,
            Runnable onClose) {

        // Set the window title using i18n
        super(i18n.get("main.title"));

        this.i18n = i18n;
        this.renamerService = renamerService;
        this.panelFactory = panelFactory;
        this.accordion = accordionFactory.createAccordion();
        this.renameController = renameController;
        this.prefs = service;

        // Build UI components
        initComponents();

        // Frame configuration
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Notify external listener when closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (onClose != null) {
                    onClose.run();
                }
            }
        });
    }

    /**
     * Initializes and lays out all main UI components.
     * <p>
     * This method builds the split pane, status bar and menu bar,
     * and wires UI events to the corresponding controllers.
     */
    private void initComponents() {

        // Main split pane: rules on the left, table on the right
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createMethodPanel(),
                createTablePanel());
        splitPane.setDividerLocation(400);

        getContentPane().add(splitPane);

        // Status bar with undo support
        statusBarPanel = new StatusBarPanel(
                i18n,
                e -> handleUndo(),
                new ImageIcon(getClass().getResource("/icons/undo.png")));

        // Enable/disable undo button based on controller state
        renameController.addUndoStateListener(
                available -> statusBarPanel.setUndoEnabled(available));

        getContentPane().add(statusBarPanel, BorderLayout.SOUTH);

        // Menu bar and initial status update
        setMenuBar();
        updateStatusBar();
    }

    /**
     * Creates and installs the application menu bar.
     * <p>
     * Provides access to file loading, theme selection,
     * additional tools, preferences and about dialog.
     */
    private void setMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // ----- File menu -----
        JMenu fileMenu = new JMenu(i18n.get("menu.file"));

        JMenuItem addFileItem = new JMenuItem(i18n.get("toolbar.button.file"));
        addFileItem.addActionListener(e -> showFileChooser());
        fileMenu.add(addFileItem);

        JMenuItem addDirItem = new JMenuItem(i18n.get("toolbar.button.directory"));
        addDirItem.addActionListener(e -> showDirChooser());
        fileMenu.add(addDirItem);

        JMenuItem exitItem = new JMenuItem(i18n.get("menu.file.exit"));
        exitItem.addActionListener(
                e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(exitItem);

        // ----- View menu (themes) -----
        JMenu viewMenu = new JMenu(i18n.get("menu.view"));
        JMenu themeMenu = new JMenu("Theme");

        Theme activeTheme = Theme.fromKey(prefs.getTheme());
        if (activeTheme == null) {
            activeTheme = Theme.LIGHT;
        }

        ButtonGroup themeGroup = new ButtonGroup();
        EnumMap<Theme, JRadioButtonMenuItem> themeMenuItems =
                new EnumMap<>(Theme.class);

        for (Theme theme : Theme.values()) {
            JRadioButtonMenuItem themeItem =
                    new JRadioButtonMenuItem(theme.toString());
            themeItem.addActionListener(e -> updateTheme(theme));
            themeItem.setSelected(theme == activeTheme);
            themeGroup.add(themeItem);
            themeMenu.add(themeItem);
            themeMenuItems.put(theme, themeItem);
        }

        viewMenu.add(themeMenu);

        // ----- Tools menu -----
        JMenu toolMenu = new JMenu(i18n.get("menu.tool"));

        List<NoorTool> tools = List.of(
                new SplitMergeTool(this),
                new DirectoryTool(this)
        );

        for (NoorTool tool : tools) {
            JMenuItem item = new JMenuItem(tool.getDisplayName());
            item.addActionListener(e -> tool.open());
            toolMenu.add(item);
        }

        // ----- Help menu -----
        JMenu helpMenu = new JMenu(i18n.get("menu.help"));

        JMenuItem preferencesItem =
                new JMenuItem(i18n.get("menu.help.preferences"));
        preferencesItem.addActionListener(e -> {
            PreferencesDialog dialog = new PreferencesDialog(this, prefs);
            dialog.setVisible(true);
        });
        helpMenu.add(preferencesItem);

        JMenuItem aboutItem = new JMenuItem(i18n.get("menu.help.about"));
        aboutItem.addActionListener(e -> AboutDialog.show(this, i18n));
        helpMenu.add(aboutItem);

        // Assemble menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Applies the selected UI theme and persists the choice.
     *
     * @param theme the theme to apply
     */
    private void updateTheme(Theme theme) {
        ThemeManager.applyTheme(theme);
        prefs.setTheme(theme.getKey());
        updateStatusBar();
    }

    /**
     * Creates the left panel containing the rule accordion
     * and the rule button bar.
     *
     * @return the method panel component
     */
    private JPanel createMethodPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        accordion = new AccordionPanel();
        accordion.setLayout(new BoxLayout(accordion, BoxLayout.Y_AXIS));
        accordion.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        accordion.setBorder(BorderFactory.createEmptyBorder());

        panel.add(accordion, BorderLayout.NORTH);

        JPanel ruleButtonBar = new RuleButtonBar(
                panelFactory,
                accordion,
                this::updateStatusBar);

        panel.add(ruleButtonBar, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the file table panel.
     *
     * @return the table panel component
     */
    private JPanel createTablePanel() {
        return new FileTablePanel(
                renamerService,
                i18n,
                this::showFileChooser,
                this::showDirChooser,
                this::rename
        );
    }

    /**
     * Opens a file chooser dialog to select one or more files.
     * <p>
     * Selected files are loaded into the renamer service and
     * rename rules are immediately reapplied.
     */
    private void showFileChooser() {

        File[] chosen =
                SwingUtil.showOpenDialog(this, JFileChooser.FILES_ONLY, true);

        if (chosen.length > 0) {
            List<RenamableFile> files = new ArrayList<>();
            for (File f : chosen) {
                files.add(new RenamableFile(f));
            }
            renamerService.setFiles(files);
            renamerService.reapplyRules();
        }
    }

    /**
     * Opens a directory chooser dialog.
     * <p>
     * Selected directories are loaded as rename targets
     * and processed by the current rules.
     */
    private void showDirChooser() {

        File[] chosen =
                SwingUtil.showOpenDialog(this, JFileChooser.DIRECTORIES_ONLY, true);

        if (chosen.length > 0) {
            List<RenamableFile> files = new ArrayList<>();
            for (File f : chosen) {
                files.add(new RenamableFile(f));
            }
            renamerService.setFiles(files);
            renamerService.reapplyRules();
        }
    }

    /**
     * Updates the status bar with the current theme
     * and the number of active rename rules.
     */
    private void updateStatusBar() {

        String theme = prefs.getTheme();
        int ruleCount = (accordion != null) ? accordion.getPanelCount() : 0;

        String rulesMessage = ruleCount == 1
                ? i18n.get("status.rules.single", ruleCount)
                : i18n.get("status.rules.multiple", ruleCount);

        statusBarPanel.setStatusText(
                i18n.get("status.summary", theme, rulesMessage));
    }

    /**
     * Executes the rename operation on the loaded files.
     * <p>
     * Displays a success or error message depending on the outcome.
     */
    private void rename() {

        if (renamerService.getFiles().isEmpty()) {
            return;
        }

        try {
            renameController.renameFiles(renamerService.getFiles());
            JOptionPane.showMessageDialog(this, i18n.get("rename.success"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this, i18n.get("rename.error", e.getMessage()));
            logger.error("Rename failed", e);
        }
    }

    /**
     * Undoes the last rename operation, if available.
     * <p>
     * Restores the previous filenames and updates the UI.
     */
    private void handleUndo() {

        try {
            renameController.undoLastRename();
            JOptionPane.showMessageDialog(this, i18n.get("undo.success"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, i18n.get("undo.error", e.getMessage()));
            logger.error("Undo failed", e);
        }
    }
}
