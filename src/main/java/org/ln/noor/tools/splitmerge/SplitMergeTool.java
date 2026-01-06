package org.ln.noor.tools.splitmerge;



import javax.swing.JFrame;

import org.ln.noor.core.tool.NoorTool;
import org.ln.noor.tools.splitmerge.ui.MergeDialog;
import org.ln.noor.tools.splitmerge.ui.SplitDialog;
/**
 * Swing entry point for coordinating split and merge operations.
 * <p>
 * This tool registers the UI flow used by the split/merge module and opens
 * the selection dialog that routes the user to the desired simulation.
 * It does not perform filesystem changes directly; dialogs handle any
 * apply actions.
 *
 * @author Luca Noale
 */

public class SplitMergeTool implements NoorTool {

    private final JFrame owner;

    /**
     * Builds the tool bound to the owning frame without touching the filesystem.
     *
     * @param owner parent window used for dialog placement
     */
    public SplitMergeTool(JFrame owner) {
        this.owner = owner;
    }

    /**
     * @return stable identifier for the split and merge tool
     */
    @Override
    public String getId() {
        return "splitmerge";
    }

    /**
     * @return display label used in the launcher menu
     */
    @Override
    public String getDisplayName() {
        return "Split & Merge";
    }

    /**
     * Opens the operation chooser dialog and routes to the selected simulation.
     */
    @Override
    public void open() {
        Object[] options = { "Split", "Merge", "Annulla" };

        int choice = javax.swing.JOptionPane.showOptionDialog(
                owner,
                "Scegli l'operazione:",
                "Split & Merge",
                javax.swing.JOptionPane.DEFAULT_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            new SplitDialog(owner).setVisible(true);
        } else if (choice == 1) {
            new MergeDialog(owner).setVisible(true);
        }
    }
}

