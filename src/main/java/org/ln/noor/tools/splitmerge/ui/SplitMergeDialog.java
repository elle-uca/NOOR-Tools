package org.ln.noor.tools.splitmerge.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.ln.noor.core.app.SpringContext;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.core.policy.DirectoryContentPolicy;
import org.ln.noor.core.policy.MustContainFilesPolicy;
import org.ln.noor.core.policy.NoCheckPolicy;
import org.ln.noor.tools.rename.ui.SourceTargetPanel;
import org.ln.noor.tools.rename.util.SwingUtil;
/**
 * Base modal dialog hosting shared split/merge controls and policies.
 * <p>
 * This abstract layer wires the source/target selectors, validation policies,
 * and layout container used by concrete split and merge simulations. It
 * centralizes directory validation without applying any filesystem changes
 * itself.
 *
 * @author Luca Noale
 */

@SuppressWarnings("serial")
public abstract class SplitMergeDialog extends JDialog {
	
	protected SourceTargetPanel stp;
	protected JButton go;
	protected JPanel content;
	protected I18n i18n ;


        /**
         * Creates the shared dialog shell and wires validation hooks without
         * touching the filesystem.
         *
         * @param owner parent window hosting the modal dialog
         */
        public SplitMergeDialog(Frame owner) {
                super(owner);
                content = new JPanel();
                i18n =  SpringContext.getBean(I18n.class);
                stp = new SourceTargetPanel();
                go = new JButton(i18n.get("splitPanel.button.go"));
                stp.onSourceChosen(t -> chooseDirectorySource());
                stp.onTargetChosen(t -> chooseDirectoryTarget());
                initComponents();
                add(content);
                setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                setMinimumSize(new Dimension(700, 480));
                setLocationRelativeTo(getOwner());
        }

        /**
         * Initializes tool-specific controls inside the shared container.
         */
        protected abstract void initComponents() ;

	
        /**
         * Opens the chooser for the source folder and enforces file presence.
         */
        protected void chooseDirectorySource() {
            chooseDirectory(
                stp::setSourceFieldText,
                new MustContainFilesPolicy()
            );
        }

        /**
         * Opens the chooser for the destination folder without additional checks.
         */
        protected void chooseDirectoryTarget() {
            chooseDirectory(
                stp::setTargetFieldText,
                new NoCheckPolicy()
            );
        }
	
        /**
         * Shows a directory chooser and validates the selection according to the
         * provided policy without applying filesystem modifications.
         *
         * @param pathConsumer callback receiving the chosen path
         * @param policy validation applied to the directory contents
         */
        protected void chooseDirectory(
                Consumer<String> pathConsumer,
                DirectoryContentPolicy policy) {
            File[] res = SwingUtil.showOpenDialog(this, JFileChooser.DIRECTORIES_ONLY, false);
            if (res == null || res.length == 0) return;

	    File dir = res[0];

	    if (!policy.isValid(dir)) {
	        JOptionPane.showMessageDialog(
	                this,
	                i18n.get(policy.getErrorMessageKey()),
	                i18n.get("error"),
	                JOptionPane.ERROR_MESSAGE
	        );
	        return;
	    }
	    pathConsumer.accept(dir.getAbsolutePath());
	}
}
