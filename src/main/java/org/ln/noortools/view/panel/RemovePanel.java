package org.ln.noortools.view.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.view.IntegerSpinner;

import net.miginfocom.swing.MigLayout;



/**
 * Panel <Remove>
 *
 * Provides a UI for removing characters from filenames.
 * 
 * Users can specify:
 * - the starting position,
 * - the number of characters to remove.
 *
 * The panel interacts with {@link RenamerService}, which updates
 * the file list and notifies the table view on the right.
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
public class RemovePanel extends AbstractPanelContent {

    private JLabel posLabel;
    private JLabel numLabel;
    private IntegerSpinner posSpinner;
    private IntegerSpinner numSpinner;

    private final RenamerService renamerService;

    /**
     * Creates a RemovePanel instance.
     *
     * @param accordion       	parent accordion container
     * param i18n               internationalization support				
     * @param renamerService  	service responsible for applying renaming rules
     */
    public RemovePanel(I18n i18n, 
    		 RenamerService renamerService) {
        super(i18n);
        this.renamerService = renamerService;
    }

    /**
     * Initializes UI components: labels and spinners
     * to configure remove position and length.
     */
	@Override
	protected void initComponents(JPanel contentArea) {
        posLabel = new JLabel(i18n.get("removePanel.label.position"));
        numLabel = new JLabel(i18n.get("removePanel.label.number"));
        posSpinner = new IntegerSpinner();
        numSpinner = new IntegerSpinner();

        posSpinner.addChangeListener(this);
        numSpinner.addChangeListener(this);

        contentArea.setLayout(new MigLayout("", "[][grow]", "20[][]20"));
        contentArea.add(numLabel);
        contentArea.add(numSpinner, "growx, wrap");
        contentArea.add(posLabel);
        contentArea.add(posSpinner, "growx, wrap");
	}

    
    /**
     * Called whenever the user interacts with the panel.
     * Detects which case option is selected and applies it
     * via the central {@link RenamerService}.
     * 
     */
    @Override
    protected void updateView() {
        renamerService.applyRule(
                "remove",
                getRenameMode(),
                posSpinner.getIntValue(),
                numSpinner.getIntValue()
        );
    }
}
