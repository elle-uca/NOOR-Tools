package org.ln.noor.tools.splitmerge.ui;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.ln.noor.tools.rename.ui.IntegerSpinner;
import org.ln.noor.tools.splitmerge.util.SplitMergeUtils;

import net.miginfocom.swing.MigLayout;
/**
 * Dialog for preparing split simulations before applying changes.
 * <p>
 * Exposes controls to split a file list by fixed count or size, configure the
 * destination name pattern, and launch a preview. The dialog itself does not
 * modify the filesystem; it only prepares data for an apply step triggered by
 * the user.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class SplitDialog extends SplitMergeDialog {

    private JTextField textField;
    private JLabel textLabel;
    private JLabel numberLabel;
    private JLabel sizeLabel;
    private IntegerSpinner numberSpinner;
    private IntegerSpinner sizeSpinner;
    private ButtonGroup group;
    private JRadioButton jrbNumber;
    private JRadioButton jrbSize;

    /**
     * Builds the dialog with split controls without touching the filesystem.
     *
     * @param owner parent window used for modality and placement
     */
    public SplitDialog(JFrame owner) {
        super(owner);
        setTitle("Split file in Directory");
    }

    /**
     * Initializes split-specific controls and preview handlers.
     */
    protected void initComponents() {
        textField = new JTextField(i18n.get("splitPanel.field.text"));
        textLabel = new JLabel(i18n.get("splitPanel.label.text"));
        numberLabel = new JLabel(i18n.get("splitPanel.label.number"));
        sizeLabel = new JLabel(i18n.get("splitPanel.label.size"));
        numberSpinner = new IntegerSpinner(1, 1, 500, 1);
        sizeSpinner = new IntegerSpinner(1, 1, 500, 1);
        sizeSpinner.setEnabled(false);
        jrbNumber = new JRadioButton(i18n.get("splitPanel.radioButton.number"), true);
        jrbSize = new JRadioButton(i18n.get("splitPanel.radioButton.size"));
        group = new ButtonGroup();
        group.add(jrbNumber);
        group.add(jrbSize);
        jrbSize.addActionListener(e -> updateView());
        go.addActionListener(e -> runSplitSimulation());

        content.setLayout(new MigLayout("", "[][grow]", "20[][][][][][][]20"));
        content.add(stp, "cell 0 0 2 1, growx ");
        content.add(jrbNumber, "cell 0 1 2 1");
        content.add(numberLabel, "cell 0 2");
        content.add(numberSpinner, "cell 1 2, growx ");
        content.add(jrbSize, "cell 0 3 2 1");
        content.add(sizeLabel, "cell 0 4");
        content.add(sizeSpinner, "cell 1 4, growx ");
        content.add(textLabel, "cell 0 5");
        content.add(textField, "cell 1 5, growx");
        content.add(go, "cell 0 6");
    }

    private void runSplitSimulation() {
        String sourcePath = stp.getSourceFieldText();
        String targetPathRaw = stp.getTargetFieldText();

        // Block simulation when the source is missing so the preview is meaningful
        if (sourcePath == null || sourcePath.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Seleziona la directory origine",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Default to the source folder when no destination name is provided
        String targetPath = (targetPathRaw == null || targetPathRaw.isEmpty())
                ? sourcePath
                : targetPathRaw;

        // Capture the resolved path for the preview callback
        final String finalTargetPath = targetPath;

        // Simulate split according to the selected strategy
        Map<String, List<File>> simulation;

        if (jrbNumber.isSelected()) {
            simulation = SplitMergeUtils.simulateSplitByCount(
                    sourcePath,
                    numberSpinner.getIntValue(),
                    textField.getText()
            );
        } else {
            simulation = SplitMergeUtils.simulateSplitBySize(
                    sourcePath,
                    sizeSpinner.getIntValue(),
                    textField.getText()
            );
        }

        final Map<String, List<File>> finalSimulation = simulation;

        // Present the preview asynchronously to keep the UI responsive
        SwingUtilities.invokeLater(() ->
                SplitMergeUtils.showSimulationTable(finalTargetPath, finalSimulation)
        );
    }

    void updateView() {
        sizeSpinner.setEnabled(true);
        numberSpinner.setEnabled(false);

        if (jrbNumber.isSelected()) {
            sizeSpinner.setEnabled(false);
            numberSpinner.setEnabled(true);
        }
    }
}
