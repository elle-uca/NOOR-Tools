package org.ln.noortools.view.panel;

import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.AddRuleService;
import org.ln.noortools.service.RenamerService;

import net.miginfocom.swing.MigLayout;

/**
 * Panel per aggiungere testo ai nomi dei file.
 * Usa AddRuleService per la logica e RenamerService per notificare la tabella.
 */
@SuppressWarnings("serial")
public class AddPanel extends AbstractPanelContent {

    private final AddRuleService addRuleService;
    private final RenamerService renamerService;

    private JLabel textLabel;
    private JLabel whereLabel;
    private JSpinner posSpinner;
    private ButtonGroup group;
    private JRadioButton jrbStart;
    private JRadioButton jrbEnd;
    private JRadioButton jrbPos;

    public AddPanel(AccordionPanel accordion, AddRuleService addRuleService, RenamerService renamerService) {
        super(accordion);
        this.addRuleService = addRuleService;
        this.renamerService = renamerService;
    }

    @Override
    protected void initComponents() {
        renameField.getDocument().addDocumentListener(this);

        textLabel = new JLabel("Text to add:");
        whereLabel = new JLabel("Position:");
        posSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        posSpinner.setEnabled(false);
        posSpinner.addChangeListener(this);

        jrbStart = new JRadioButton("Start", true);
        jrbEnd   = new JRadioButton("End");
        jrbPos   = new JRadioButton("At position");

        group = new ButtonGroup();
        group.add(jrbStart);
        group.add(jrbEnd);
        group.add(jrbPos);

        jrbStart.addActionListener(this);
        jrbEnd.addActionListener(this);
        jrbPos.addActionListener(this);

        setLayout(new MigLayout("", "[][][grow]", "20[][][][]20"));
        add(textLabel,   "cell 0 0 3 1");
        add(renameField, "cell 0 1 3 1, growx, wrap");
        add(whereLabel,  "cell 0 2 1 2");
        add(jrbStart,    "cell 1 2");
        add(jrbEnd,      "cell 2 2, wrap");
        add(jrbPos,      "cell 1 3");
        add(posSpinner,  "cell 2 3, growx");
    }

    @Override
	protected void updateView() {
        int position = 1;

        if (jrbEnd.isSelected()) {
            position = Integer.MAX_VALUE; // convenzione: fine del nome
        }
        if (jrbPos.isSelected()) {
            posSpinner.setEnabled(true);
            position = (Integer) posSpinner.getValue();
        } else {
            posSpinner.setEnabled(false);
        }
        
        

     // Applica la regola usando AddRuleService
        List<RenamableFile> updated =
                addRuleService.applyRule(renamerService.getFiles(), getRenameText(), position);

        // Aggiorna la lista → RenamerService notifica la tabella
        renamerService.setFiles(updated);
    }
}
