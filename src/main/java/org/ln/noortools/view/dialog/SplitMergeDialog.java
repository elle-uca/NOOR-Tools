package org.ln.noortools.view.dialog;

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

import org.ln.noortools.SpringContext;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.policy.DirectoryContentPolicy;
import org.ln.noortools.policy.MustContainFilesPolicy;
import org.ln.noortools.policy.NoCheckPolicy;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.component.SourceTargetPanel;

@SuppressWarnings("serial")
public abstract class SplitMergeDialog extends JDialog {
	
	protected SourceTargetPanel stp;
	protected JButton go;
	protected JPanel content;
	protected I18n i18n ;


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

	protected abstract void initComponents() ;

	
	protected void chooseDirectorySource() {
	    chooseDirectory(
	        stp::setSourceFieldText,
	        new MustContainFilesPolicy()
	    );
	}

	protected void chooseDirectoryTarget() {
	    chooseDirectory(
	        stp::setTargetFieldText,
	        new NoCheckPolicy()
	    );
	}
	
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