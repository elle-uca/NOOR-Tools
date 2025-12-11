package org.ln.noortools.view.dialog;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.ln.noortools.SpringContext;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.SplitMergeUtils;
import org.ln.noortools.util.SwingUtil;
import org.ln.noortools.view.component.IntegerSpinner;
import org.ln.noortools.view.component.SourceTargetPanel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SplitDialog extends JDialog {

	private SourceTargetPanel stp;
	private JLabel textLabel;
	private JLabel numberLabel;
	private JLabel sizeLabel;
	private IntegerSpinner numberSpinner;
	private IntegerSpinner sizeSpinner;
	private ButtonGroup group;
	private JRadioButton jrbNumber;
	private JRadioButton jrbSize;
	private JButton go;
	private JTextField textField;



	public SplitDialog(JFrame owner) {
		super(owner);
		setTitle("Split file in Directory");
		initComponents();
	}


	/**
	 *
	 */
	void initComponents() {
		I18n i18n =  SpringContext.getBean(I18n.class);
		JPanel content = new JPanel();
		textField = new JTextField(i18n.get("splitPanel.field.text"));
		//textField.setText(i18n.get("splitPanel.field.text"));
		textLabel = new JLabel(i18n.get("splitPanel.label.text"));
		numberLabel = new JLabel(i18n.get("splitPanel.label.number"));
		sizeLabel = new JLabel(i18n.get("splitPanel.label.size"));
		numberSpinner = new IntegerSpinner(1, 1 ,500 ,1);
		sizeSpinner = new IntegerSpinner(1, 1, 500, 1);
		sizeSpinner.setEnabled(false);
		stp = new SourceTargetPanel();
		jrbNumber = new JRadioButton(i18n.get("splitPanel.radioButton.number"), true);
		jrbSize = new JRadioButton(i18n.get("splitPanel.radioButton.size"));
		group = new ButtonGroup();
		group.add(jrbNumber);
		group.add(jrbSize);
		stp.onSourceChosen(t -> chooseDirectorySource());
		stp.onTargetChosen(t -> chooseDirectoryTarget());		
		jrbNumber.addActionListener(e ->updateView());
		jrbSize.addActionListener(e ->updateView());
		go = new JButton(i18n.get("splitPanel.button.go"));

//		go.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String sourcePath = stp.getSourceFieldText();
//				String targetPath = stp.getTargetFieldText();// aggiungere logica per cartella destinazione
//				
//				if((sourcePath == null || sourcePath.isEmpty())  
//						//|| (targetPath == null || targetPath.isEmpty())
//						){
//					
//					 JOptionPane.showMessageDialog(
//			                    null,
//			                    "Seleziona la directory origine",
//			                    "Errore",
//			                    JOptionPane.ERROR_MESSAGE
//			            );
//			            return;
//				}	
//	
//				// SE NON SI SELEZIONA LA DESTINAZIONE SI ASSUME SIA LA STESSA
//				if(targetPath == null || targetPath.isEmpty())  {
//					targetPath = sourcePath;
//				}
//				
//				Map<String, List<File>> simulation;
//
//				if(jrbNumber.isSelected()) {
//					simulation = SplitMergeUtils.simulateSplitByCount(sourcePath, 
//							numberSpinner.getIntValue(), textField.getText());
//				}
//				else {
//					simulation = SplitMergeUtils.simulateSplitBySize(sourcePath, 
//							sizeSpinner.getIntValue(), textField.getText());
//				}
//
//				SwingUtilities.invokeLater(() -> 
//				SplitMergeUtils.showSimulationTable(targetPath, simulation));
//			}
//		});

		go.addActionListener(e -> {

		    String sourcePath = stp.getSourceFieldText();
		    String targetPathRaw = stp.getTargetFieldText();

		    // Controllo directory origine
		    if (sourcePath == null || sourcePath.isEmpty()) {
		        JOptionPane.showMessageDialog(
		                null,
		                "Seleziona la directory origine",
		                "Errore",
		                JOptionPane.ERROR_MESSAGE
		        );
		        return;
		    }

		    // Se non viene scelta, la destinazione è la stessa dell'origine
		    String targetPath = (targetPathRaw == null || targetPathRaw.isEmpty())
		            ? sourcePath
		            : targetPathRaw;

		    // Rendiamo final per la lambda
		    final String finalTargetPath = targetPath;

		    // Simulazione split
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

		    // Mostra tabella con i risultati della simulazione
		    SwingUtilities.invokeLater(() ->
		            SplitMergeUtils.showSimulationTable(finalTargetPath, finalSimulation)
		    );
		});
		
		content.setLayout(new MigLayout("", "[][grow]", "20[][][][][][][]20"));

		content.add(stp, 			"cell 0 0 2 1, growx ");
		content.add(jrbNumber, 		"cell 0 1 2 1");
		content.add(numberLabel,	"cell 0 2");
		content.add(numberSpinner, 	"cell 1 2, growx ");
		content.add(jrbSize, 		"cell 0 3 2 1");
		content.add(sizeLabel, 		"cell 0 4");
		content.add(sizeSpinner, 	"cell 1 4, growx ");
		content.add(textLabel, 		"cell 0 5");
		content.add(textField, 		"cell 1 5, growx");
		content.add(go,  			"cell 0 6");

		add(content);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(700, 480));
		setLocationRelativeTo(getOwner());
	}  


	void updateView() {
		sizeSpinner.setEnabled(true);
		numberSpinner.setEnabled(false);	

		if(jrbNumber.isSelected()) {
			sizeSpinner.setEnabled(false);
			numberSpinner.setEnabled(true);
		}
	}
	
	private void chooseDirectorySource() {
		chooseDirectory(stp::setSourceFieldText, true);
	}

	private void chooseDirectoryTarget() {
		chooseDirectory(stp::setSourceFieldText, false);
	}
	
	private void chooseDirectory(Consumer<String> pathConsumer, boolean checkFiles) {
	    JFileChooser fc = SwingUtil.getFileChooser(JFileChooser.DIRECTORIES_ONLY, false);
	    int returnVal = fc.showOpenDialog(null);
	    if (returnVal != JFileChooser.APPROVE_OPTION)
	        return;

	    String path = fc.getSelectedFile().getAbsolutePath();

	    if (checkFiles) {
	        File dir = new File(path);
	        File[] files = dir.listFiles(File::isFile);

	        if (files == null || files.length == 0) {
	            JOptionPane.showMessageDialog(
	                    null,
	                    "La directory non contiene file",
	                    "Errore",
	                    JOptionPane.ERROR_MESSAGE
	            );
	            return;
	        }
	    }

	    pathConsumer.accept(path);
	}
}


