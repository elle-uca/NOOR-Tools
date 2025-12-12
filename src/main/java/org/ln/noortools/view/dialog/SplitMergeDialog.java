package org.ln.noortools.view.dialog;

import java.awt.Frame;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.ln.noortools.SpringContext;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.view.component.SourceTargetPanel;

@SuppressWarnings("serial")
public class SplitMergeDialog extends JDialog {
	
	private SourceTargetPanel stp;
	private JButton go;
	private JPanel content;
	private I18n i18n ;


	public SplitMergeDialog(Frame owner) {
		super(owner);
		content = new JPanel();
		i18n =  SpringContext.getBean(I18n.class);
		stp = new SourceTargetPanel();
		go = new JButton(i18n.get("splitPanel.button.go"));
		stp.onSourceChosen(t -> chooseDirectorySource());
		stp.onTargetChosen(t -> chooseDirectoryTarget());
	}

	private void chooseDirectorySource() {
		chooseDirectory(stp::setSourceFieldText, true);
	}

	private void chooseDirectoryTarget() {
		chooseDirectory(stp::setTargetFieldText, false);
	}
	
	protected void chooseDirectory(Consumer<String> pathConsumer, boolean checkFiles) {
//	    File[] res = SwingUtil.showOpenDialog(this, JFileChooser.DIRECTORIES_ONLY, false);
//	    
//	    if(res.length == 0) return; // user cancelled
//	    
//	    String path = res[0].getAbsolutePath();
//	    if (checkFiles) {
//	        File dir = new File(path);
//	        File[] files = dir.listFiles(File::isDirectory);
//
//	        if (files == null || files.length == 0) {
//	            JOptionPane.showMessageDialog(
//	                    null,
//	                    "La directory non contiene directory",
//	                    "Errore",
//	                    JOptionPane.ERROR_MESSAGE
//	            );
//	            return;
//	        }
//	    }
//
//	    pathConsumer.accept(path);
	}
}