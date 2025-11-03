package org.ln.noortools.view.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.ln.noortools.enums.RenameMode;
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

	protected  AccordionPanel accordion;
	protected final JTextField renameField;
	protected final JComboBox<RenameMode> modeCombo;
	protected final I18n i18n;      


	public AbstractPanelContent( I18n i18n) {
		//public AbstractPanelContent(AccordionPanel accordion, I18n i18n) {
		this.i18n = i18n;
		this.accordion = accordion;
		this.renameField = new JTextField();
		this.modeCombo = new JComboBox<>(RenameMode.values());
		// Default: NAME_ONLY
		this.modeCombo.setSelectedItem(RenameMode.NAME_ONLY);
		modeCombo.addActionListener(this);

		// Hook changes
		renameField.getDocument().addDocumentListener(this);

		// layout base con area per i controlli + combo in basso
		setLayout(new BorderLayout());

		JPanel contentArea = new JPanel(); 
		contentArea.setLayout(new GridBagLayout()); // i figli concreti useranno GridBag o MigLayout
		add(contentArea, BorderLayout.CENTER);

		// pannello footer con combo
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		footer.add(new JLabel("Rename Mode:"));
		footer.add(modeCombo);
		add(footer, BorderLayout.SOUTH);

		// delega a sottoclasse per costruire controlli specifici
		initComponents(contentArea);

		// Build UI
		//initComponents();
	}

	/**
	 * Allows delayed injection of the parent AccordionPanel
	 * (useful when the panel is managed by Spring and built before accordion exists).
	 */
	public void setAccordion(AccordionPanel accordion) {
		this.accordion = accordion;
	}

	/**
	 * Subclasses must implement this to add their specific UI components.
	 */
	protected abstract void initComponents(JPanel contentArea);


	/** Subclasses must implement how data is updated when inputs change */
	protected abstract void updateView();

	// ---- Utility methods ----
	public JTextField getRenameField() {
		return renameField;
	}

	public String getRenameText() {
		return renameField.getText();
	}

	/** Get the rename mode chosen by user */
	public RenameMode getRenameMode() {
		return (RenameMode) modeCombo.getSelectedItem();
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


	protected void initComponents() {
		// TODO Auto-generated method stub

	}
}
