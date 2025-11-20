package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.ln.noortools.view.dialog.ActionConfirmationDialog;
import org.ln.noortools.view.dialog.ActionLogDialog;
import org.springframework.stereotype.Component;

/**
 * Manager responsible for collecting and executing ActionTag instances.
 *
 * Ensures:
 *  - no actions are executed during preview
 *  - user confirmation before any destructive operation
 *  - an aggregated detailed log after execution
 *
 * Author: Luca Noale
 */
@Component
public class ActionManager {

    private final List<ActionTag> actionTags = new ArrayList<>();

    /** Called by StringParser each time it creates a tag */
    public void registerActionTag(ActionTag tag) {
        if (tag != null) {
            actionTags.add(tag);
        }
    }

    /** Clears previously registered actions (called before new preview generation) */
    public void reset() {
        actionTags.clear();
    }

    /**
     * Executes all registered actions with:
     *  1. Confirmation dialog
     *  2. Aggregated execution
     *  3. Final log dialog
     *
     * Called only when user presses "Rename".
     */
    public void executeAllIfConfirmed() {

        if (actionTags.isEmpty()) {
            return; // nothing to do
        }

        // 1) Build confirmation message
        StringBuilder sb = new StringBuilder();
        sb.append("The following actions will be executed:\n\n");

        for (ActionTag t : actionTags) {
            sb.append("• ").append(t.getActionDescription()).append("\n");
        }

        // 2) Ask user to confirm
        boolean ok = ActionConfirmationDialog.show(sb.toString());

        if (!ok) return;

        // 3) Execute actions and build log
        StringBuilder resultLog = new StringBuilder();
        resultLog.append("EXECUTION REPORT\n\n");

        for (ActionTag t : actionTags) {
            try {
                t.performAction();
                resultLog.append("[OK] ").append(t.getActionDescription()).append("\n");
            } catch (Exception ex) {
                resultLog.append("[ERROR] ")
                         .append(t.getActionDescription())
                         .append(" → ").append(ex.getMessage()).append("\n");
            }
        }

        // 4) Show final log
        SwingUtilities.invokeLater(() ->
                ActionLogDialog.show(resultLog.toString())
        );
    }

	/**
	 * @return the actionTags
	 */
	public List<ActionTag> getActionTags() {
		return actionTags;
	}
    
    
}
