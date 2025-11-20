package org.ln.noortools.view.panel;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.ruleservice.RenamerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.miginfocom.swing.MigLayout;

/**
 * Panel <Add>
 *
 * Provides a UI for adding a substring to filenames.
 * 
 * Users can choose where to insert the text:
 * - at the beginning,
 * - at the end,
 * - or at a specific position.
 *
 * The panel interacts with {@link RenamerService}, which updates
 * the file list and notifies the table view on the right.
 * 
 * Author: Luca Noale
 * 
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class AddPanel extends AbstractPanelContent {

    private final RenamerService renamerService;

    private JLabel textLabel;
    private JLabel whereLabel;
    private JSpinner posSpinner;
    private ButtonGroup group;
    private JRadioButton jrbStart;
    private JRadioButton jrbEnd;
    private JRadioButton jrbPos;

    /**
     * Creates an AddPanel instance.
     *
     * @param accordion       parent accordion container
     * @param i18n            internationalization support
     * @param renamerService  service responsible for applying renaming rules
     */
    public AddPanel(I18n i18n, RenamerService renamerService) {
        super( i18n);
        this.renamerService = renamerService;
    }

        @Override
        protected void initComponents(JPanel contentArea) {
        textLabel = new JLabel(i18n.get("addPanel.label.text"));
        whereLabel = new JLabel(i18n.get("addPanel.label.where"));
        posSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        posSpinner.setEnabled(false);
        posSpinner.addChangeListener(this);

        jrbStart = new JRadioButton(i18n.get("addPanel.radioButton.start"), true);
        jrbEnd   = new JRadioButton(i18n.get("addPanel.radioButton.end"));
        jrbPos   = new JRadioButton(i18n.get("addPanel.radioButton.pos"));

        group = new ButtonGroup();
        group.add(jrbStart);
        group.add(jrbEnd);
        group.add(jrbPos);

        jrbStart.addActionListener(this);
        jrbEnd.addActionListener(this);
        jrbPos.addActionListener(this);

        contentArea.setLayout(new MigLayout("", "[][][grow]", "20[][][][]"));
        contentArea.add(textLabel,   "cell 0 0 3 1");
        contentArea.add(renameField, "cell 0 1 3 1, growx, wrap");
        contentArea.add(whereLabel,  "cell 0 2 1 2");
        contentArea.add(jrbStart,    "cell 1 2");
        contentArea.add(jrbEnd,      "cell 2 2, wrap");
        contentArea.add(jrbPos,      "cell 1 3");
        contentArea.add(posSpinner,  "cell 2 3, growx");
		
	}


    /**
     * Called whenever the user changes text or position.
     * Determines the insertion point and applies the <Add> rule
     * through {@link RenamerService}.
     */
    @Override
    protected void updateView() {
        int position = 1;

        if (jrbEnd.isSelected()) {
            position = Integer.MAX_VALUE; // convention: append at the end
        }
        if (jrbPos.isSelected()) {
            posSpinner.setEnabled(true);
            position = (Integer) posSpinner.getValue();
        } else {
            posSpinner.setEnabled(false);
        }

        // Dispatch to RenamerService → updates model and notifies listeners
        renamerService.applyRule("add", getRenameMode(), renameField.getText(), position);
    }


}
