package org.ln.noortools.view.panel;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.ln.noortools.enums.ReplacementType;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.RenamerService;

import net.miginfocom.swing.MigLayout;

/**
 * Panel <Replace>
 *
 * Provides a UI for replacing substrings inside filenames.
 * 
 * Users can configure:
 * - the text to search,
 * - the replacement text,
 * - whether the search is case-sensitive,
 * - and whether to replace the first, last, or all occurrences.
 *
 * The panel interacts with {@link RenamerService}, which updates
 * the file list and notifies the table view on the right.
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
public class ReplacePanel extends AbstractPanelContent {

    private JLabel textLabel;
    private JLabel replaceLabel;
    private JLabel occurrenceLabel;

    private JTextField textField;
    private JTextField replaceField;

    private JRadioButton jrbCase;
    private ButtonGroup group;
    private JRadioButton jrbFirst;
    private JRadioButton jrbLast;
    private JRadioButton jrbAll;

    private final RenamerService renamerService;


    /**
     * Creates a ReplacePanel instance.
     *
     * @param accordion          parent accordion container
     * @param i18n               internationalization support
     * @param renamerService     service responsible for applying renaming rules
     */
    public ReplacePanel(AccordionPanel accordion,  I18n i18n, RenamerService renamerService){
        super(accordion, i18n);
        this.renamerService = renamerService;
    }

    @Override
    protected void initComponents() {
        textLabel      = new JLabel(i18n.get("replacePanel.label.text"));
        replaceLabel   = new JLabel(i18n.get("replacePanel.label.replace"));
        occurrenceLabel = new JLabel(i18n.get("replacePanel.label.occurence"));

        textField    = new JTextField();
        replaceField = new JTextField();

        textField.getDocument().addDocumentListener(this);
        replaceField.getDocument().addDocumentListener(this);

        jrbCase  = new JRadioButton(i18n.get("replacePanel.radioButton.case"));

        group    = new ButtonGroup();
        jrbFirst = new JRadioButton(i18n.get("replacePanel.radioButton.first"), true);
        jrbLast  = new JRadioButton(i18n.get("replacePanel.radioButton.last"));
        jrbAll   = new JRadioButton(i18n.get("replacePanel.radioButton.all"));

        group.add(jrbFirst);
        group.add(jrbLast);
        group.add(jrbAll);

        jrbCase.addActionListener(this);
        jrbFirst.addActionListener(this);
        jrbLast.addActionListener(this);
        jrbAll.addActionListener(this);

        setLayout(new MigLayout("", "[][grow]", "20[][][][][]20"));
        add(textLabel,      "cell 0 0");
        add(textField,      "cell 1 0, grow");
        add(replaceLabel,   "cell 0 1");
        add(replaceField,   "cell 1 1, grow");
        add(jrbCase,        "cell 0 2");
        add(occurrenceLabel,"cell 0 3");
        add(jrbFirst,       "cell 1 3");
        add(jrbLast,        "cell 0 4");
        add(jrbAll,         "cell 1 4");
    }

    /**
     * Called whenever the user interacts with the panel.
     * Detects which case option is selected and applies it
     * via the central {@link RenamerService}.
     * 
     */
    @Override
    protected void updateView() {
        ReplacementType type = ReplacementType.FIRST;
        if (jrbLast.isSelected()) type = ReplacementType.LAST;
        if (jrbAll.isSelected()) type = ReplacementType.ALL;

        
        // 🔑 CHIAMA SOLO RenamerService → userà la chiave "replace"
        renamerService.applyRule(
                "replace",
                textField.getText(),
                replaceField.getText(),
                type,
                jrbCase.isSelected()
        );
    }
}
