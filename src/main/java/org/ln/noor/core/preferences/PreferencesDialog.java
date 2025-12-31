package org.ln.noor.core.preferences;

import java.awt.Frame;

import javax.swing.JDialog;
/**
 * PreferencesDialog.
 *
 * @author Luca Noale
 */

@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog {

    public PreferencesDialog(Frame owner, PreferencesService prefs) {
        super(owner, "Preferenze", true);

        setContentPane(new PreferencesPanel(prefs));
        pack();
        setLocationRelativeTo(owner);
    }
}
