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

import jakarta.annotation.PostConstruct;

/**
 * Abstract base class for all accordion panel contents used inside the renamer UI.
 *
 * <p>This class provides a standard structure that all panels share:
 * <ul>
 *   <li>A text field for rename templates</li>
 *   <li>A combo box for selecting the {@link RenameMode}</li>
 *   <li>A reference to the parent {@link AccordionPanel}</li>
 *   <li>Automatic triggers that invoke {@link #updateView()} whenever the user edits inputs</li>
 * </ul>
 *
 * <p>UI construction happens in two steps:
 * <ol>
 *   <li>The constructor initializes shared components and listeners</li>
 *   <li>{@link #setupUI()} (invoked via {@code @PostConstruct}) creates the layout
 *       and delegates to {@link #initComponents(JPanel)} for panel-specific controls</li>
 * </ol>
 *
 * <p>Subclasses must implement:
 * <ul>
 *   <li>{@code initComponents(JPanel)} – to build the custom UI inside the provided panel</li>
 *   <li>{@code updateView()} – to update preview data whenever inputs change</li>
 * </ul>
 *
 * <p>The class implements {@link DocumentListener}, {@link ChangeListener}, and
 * {@link ActionListener} in order to capture every possible user action that should refresh the preview.
 *
 * <p><b>Note:</b> {@link #setAccordion(AccordionPanel)} is called externally by
 * the {@code PanelFactory} immediately after the panel instance is created.
 *
 * Author: Luca Noale
 */

@SuppressWarnings("serial")
public abstract class AbstractPanelContent extends JPanel
        implements ChangeListener, DocumentListener, ActionListener {

    protected AccordionPanel accordion;
    protected final JTextField renameField;
    protected final JComboBox<RenameMode> modeCombo;
    protected final I18n i18n;      
	 

	 public AbstractPanelContent(I18n i18n) {
		this.i18n = i18n;
        this.renameField = new JTextField();
        this.modeCombo = new JComboBox<>(RenameMode.values());
        // Default: NAME_ONLY
        this.modeCombo.setSelectedItem(RenameMode.NAME_ONLY);
        modeCombo.addActionListener(this);

        // Hook changes
        renameField.getDocument().addDocumentListener(this);
        
        setLayout(new BorderLayout());
    }
	 
	 @PostConstruct
    private void setupUI(){
            JPanel contentArea = new JPanel();
            contentArea.setLayout(new GridBagLayout());
            add(contentArea, BorderLayout.CENTER);

            // pannello footer con combo
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            footer.add(new JLabel(i18n.get("rename.mode")));
            footer.add(modeCombo);
            add(footer, BorderLayout.SOUTH);
            // delega a sottoclasse per costruire controlli specifici
            initComponents(contentArea);
     }
	 
	    /** chiamata da SlidingPanel subito dopo la creazione */
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


}
