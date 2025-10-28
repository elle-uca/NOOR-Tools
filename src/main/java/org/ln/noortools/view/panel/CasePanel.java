package org.ln.noortools.view.panel;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.ln.noortools.enums.ModeCase;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.RenamerService;

/**
 * Panel <Case>
 * 
 * Provides options to transform filenames according to a chosen case rule:
 * - Uppercase
 * - Lowercase
 * - Title case
 * - Capitalize first letter
 * - Toggle case
 *
 * This panel updates the renaming preview by delegating to RenamerService.
 */
@SuppressWarnings("serial")
public class CasePanel extends AbstractPanelContent {

    private ButtonGroup buttonGroup;       // Group to ensure only one option is selected
    private JRadioButton jrbUpper;         // Option: UPPERCASE
    private JRadioButton jrbLower;         // Option: lowercase
    private JRadioButton jrbCapAll;        // Option: Title Case (capitalize every word)
    private JRadioButton jrbCapFirst;      // Option: Capitalize only the first letter
    private JRadioButton jrbInvert;        // Option: Toggle case (invert each letter)

    private final RenamerService renamerService; // Service to apply rules and update file list

    /**
     * Creates a new CasePanel.
     *
     * @param accordion       parent accordion container
     * @param i18n            internationalization helper
     * @param renamerService  service for applying renaming rules
     */
    public CasePanel(AccordionPanel accordion, I18n i18n, RenamerService renamerService) {
        super(accordion, i18n);
        this.renamerService = renamerService;
    }

    /**
     * Initializes the UI components of the panel.
     */
    @Override
    protected void initComponents() {
        // Create radio buttons with localized labels
        jrbUpper    = new JRadioButton(i18n.get("casePanel.radioButton.upper"), true);
        jrbLower    = new JRadioButton(i18n.get("casePanel.radioButton.lower"));
        jrbCapAll   = new JRadioButton(i18n.get("casePanel.radioButton.titleCase"));
        jrbCapFirst = new JRadioButton(i18n.get("casePanel.radioButton.capitalizeFirst"));
        jrbInvert   = new JRadioButton(i18n.get("casePanel.radioButton.toggle"));

        // Add them to a button group
        buttonGroup = new ButtonGroup();
        buttonGroup.add(jrbUpper);
        buttonGroup.add(jrbLower);
        buttonGroup.add(jrbCapAll);
        buttonGroup.add(jrbCapFirst);
        buttonGroup.add(jrbInvert);

        // Register event listeners
        jrbUpper.addActionListener(this);
        jrbLower.addActionListener(this);
        jrbCapAll.addActionListener(this);
        jrbCapFirst.addActionListener(this);
        jrbInvert.addActionListener(this);

        // Layout: vertical with spacing
        setLayout(new MigLayout("", "30[]", "20[][][][][]20"));
        add(jrbUpper,    "wrap");
        add(jrbLower,    "wrap");
        add(jrbCapAll,   "wrap");
        add(jrbCapFirst, "wrap");
        add(jrbInvert,   "wrap");
    }

    /**
     * Called whenever the user interacts with the panel.
     * Detects which case option is selected and applies it
     * via the central RenamerService.
     */
    @Override
    protected void updateView() {
        ModeCase modeCase = ModeCase.UPPER;
        if (jrbLower.isSelected()) modeCase = ModeCase.LOWER;
        if (jrbCapAll.isSelected()) modeCase = ModeCase.TITLE_CASE;
        if (jrbCapFirst.isSelected()) modeCase = ModeCase.CAPITALIZE_FIRST;
        if (jrbInvert.isSelected()) modeCase = ModeCase.TOGGLE_CASE;

        // 👉 Delegate to RenamerService (dispatches to CaseRuleService internally)
        renamerService.applyRule("case", modeCase);
    }
}
