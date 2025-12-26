package org.ln.noor.tools.splitmerge;



import javax.swing.JFrame;

import org.ln.noor.core.tool.NoorTool;
import org.ln.noor.tools.splitmerge.ui.MergeDialog;
import org.ln.noor.tools.splitmerge.ui.SplitDialog;

public class SplitMergeTool implements NoorTool {

    private final JFrame owner;

    public SplitMergeTool(JFrame owner) {
        this.owner = owner;
    }

    @Override
    public String getId() {
        return "splitmerge";
    }

    @Override
    public String getDisplayName() {
        return "Split & Merge";
    }

    @Override
    public void open() {
        // Dialog principale di scelta
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

