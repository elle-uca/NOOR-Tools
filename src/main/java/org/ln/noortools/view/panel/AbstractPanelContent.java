package org.ln.noortools.view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.ln.noortools.i18n.I18n;

/**
 * Base class for all accordion panel contents.
 * Provides:
 *  - shared renameField
 *  - access to parent AccordionPanel
 *  - automatic updateView() call on any input change
 * 
 * Each subclass must implement:
 *  - initComponents(): build the UI
 *  - updateView(): update preview / data when user input changes
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
public abstract class AbstractPanelContent extends JPanel
        implements ChangeListener, DocumentListener, ActionListener {

    protected final AccordionPanel accordion;
    protected final JTextField renameField;
   
    protected final I18n i18n;      
	 

	 public AbstractPanelContent(AccordionPanel accordion, I18n i18n) {
		this.i18n = i18n;
        this.accordion = accordion;
        this.renameField = new JTextField();

        // Hook changes
        renameField.getDocument().addDocumentListener(this);

        // Build UI
        initComponents();
    }

    /** Subclasses must implement component initialization */
    protected abstract void initComponents();

    /** Subclasses must implement how data is updated when inputs change */
    protected abstract void updateView();

    // ---- Utility methods ----
    public JTextField getRenameField() {
        return renameField;
    }

    public String getRenameText() {
        return renameField.getText();
    }

    // ---- Listeners: all trigger updateView() ----
    @Override
    public void insertUpdate(DocumentEvent e) {
        updateView();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateView();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateView();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateView();
    }
}
