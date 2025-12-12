package org.ln.noortools.view.dialog;

import java.awt.Dialog;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.ln.noortools.SpringContext;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.SplitMergeUtils;
import org.ln.noortools.util.SplitMergeUtils.MergeResult;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.component.SourceTargetPanel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MergeDialog extends JDialog {

	private SourceTargetPanel stp;
    private ButtonGroup group;
    private JRadioButton jrbMove;
    private JRadioButton jrbCopy;
    private JButton go;
    private boolean move = true;
    



	public MergeDialog(JFrame owner) {
		super(owner);
		setTitle("Merge directory in file ");
		initComponents();
	}


	/**
	 *
	 */
	void initComponents() {
		I18n i18n =  SpringContext.getBean(I18n.class);
		JPanel content = new JPanel();
        stp = new SourceTargetPanel();
        jrbMove = new JRadioButton(i18n.get("mergePanel.radioButton.move"), true);
        jrbCopy = new JRadioButton(i18n.get("mergePanel.radioButton.copy"));

        group = new ButtonGroup();
        group.add(jrbMove);
        group.add(jrbCopy);

        go = new JButton(i18n.get("mergePanel.button.go"));

        // Consumer Listener 
        stp.onSourceChosen(t -> chooseDirectorySource());
        stp.onTargetChosen(t -> chooseDirectoryTarget());

        jrbMove.addActionListener(e ->updateView());
        jrbCopy.addActionListener(e ->updateView());

        go.addActionListener(e -> runMergeSimulation());

        content.setLayout(new MigLayout("", "[][grow][]", "20[][][]20"));
        content.add(stp, 		"cell 0 0 3 1, growx");
        content.add(jrbMove, 	"cell 0 1");
        content.add(jrbCopy, 	"cell 1 1");
        content.add(go, 		"cell 0 2");
		add(content);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(700, 480));
		setLocationRelativeTo(getOwner());

	}  


	private void runMergeSimulation() {
        try {
            MergeResult simulation = SplitMergeUtils.simulateMerge(
                    stp.getSourceFieldText(), stp.getTargetFieldText());
            SwingUtilities.invokeLater(() -> SplitMergeUtils.showSimulation(simulation, move));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante la simulazione:\n" + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }	


	void updateView() {
		 move = jrbMove.isSelected();
	}
	
	private void chooseDirectorySource() {
		chooseDirectory(stp::setSourceFieldText, true);
	}

	private void chooseDirectoryTarget() {
		chooseDirectory(stp::setTargetFieldText, false);
	}
	
	private void chooseDirectory(Consumer<String> pathConsumer, boolean checkFiles) {
	    File[] res = SwingUtil.showOpenDialog(this, JFileChooser.DIRECTORIES_ONLY, false);
	    
	    if(res.length == 0) return; // user cancelled
	    
	    String path = res[0].getAbsolutePath();
	    if (checkFiles) {
	        File dir = new File(path);
	        File[] files = dir.listFiles(File::isDirectory);

	        if (files == null || files.length == 0) {
	            JOptionPane.showMessageDialog(
	                    null,
	                    "La directory non contiene directory",
	                    "Errore",
	                    JOptionPane.ERROR_MESSAGE
	            );
	            return;
	        }
	    }

	    pathConsumer.accept(path);
	}
}


