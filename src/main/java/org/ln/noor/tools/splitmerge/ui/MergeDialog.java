package org.ln.noor.tools.splitmerge.ui;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.ln.noor.core.policy.MustContainDirectoriesPolicy;
import org.ln.noor.tools.splitmerge.util.SplitMergeUtils;
import org.ln.noor.tools.splitmerge.util.SplitMergeUtils.MergeResult;

import net.miginfocom.swing.MigLayout;
/**
 * Dialog guiding merge simulations before applying folder consolidation.
 * <p>
 * Provides controls for selecting the source tree, choosing whether the apply
 * step moves or copies files, and triggering a merge preview. The dialog
 * displays results without touching the filesystem until the user confirms the
 * apply action.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class MergeDialog extends SplitMergeDialog {

    private ButtonGroup group;
    private JRadioButton jrbMove;
    private JRadioButton jrbCopy;
    private boolean move = true;

    /**
     * Builds the dialog and sets the user-facing title without filesystem access.
     *
     * @param owner parent window used for modality and placement
     */
    public MergeDialog(JFrame owner) {
        super(owner);
        setTitle("Merge directory in file ");
    }

    /**
     * Initializes merge-specific controls and binds preview triggers.
     */
    protected void initComponents() {
        jrbMove = new JRadioButton(i18n.get("mergePanel.radioButton.move"), true);
        jrbCopy = new JRadioButton(i18n.get("mergePanel.radioButton.copy"));

        group = new ButtonGroup();
        group.add(jrbMove);
        group.add(jrbCopy);

        jrbMove.addActionListener(e -> updateView());
        jrbCopy.addActionListener(e -> updateView());

        go.addActionListener(e -> runMergeSimulation());

        content.setLayout(new MigLayout("", "[][grow][]", "20[][][]20"));
        content.add(stp, "cell 0 0 3 1, growx");
        content.add(jrbMove, "cell 0 1");
        content.add(jrbCopy, "cell 1 1");
        content.add(go, "cell 0 2");
    }

    private void runMergeSimulation() {
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

        MergeResult simulation = SplitMergeUtils.simulateMerge(
                sourcePath, finalTargetPath);
        SwingUtilities.invokeLater(() -> SplitMergeUtils.showSimulation(simulation, move));
    }

    void updateView() {
        move = jrbMove.isSelected();
    }

    @Override
    protected void chooseDirectorySource() {
        chooseDirectory(
                stp::setSourceFieldText,
                new MustContainDirectoriesPolicy()
        );
    }
}
