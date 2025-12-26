package org.ln.noor.ui.component;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ln.noor.app.SpringContext;
import org.ln.noor.core.i18n.I18n;

import net.miginfocom.swing.MigLayout;

/**
 * Reusable panel to select source and destination folders.
 * Does not open JFileChooser, but notifies events via listener or Consumer
 */
@SuppressWarnings("serial")
public class SourceTargetPanel extends JPanel {

    private final JLabel sourceLabel;
    private final JLabel targetLabel;
    private final JTextField sourceField;
    private final JTextField targetField;
    private final JButton fc1;
    private final JButton fc2;

    // callback 
    private Consumer<String> onSourceChosen;
    private Consumer<String> onTargetChosen;

    public SourceTargetPanel() {
        super(new MigLayout("", "[][grow][]", "[][]"));
       
        I18n i18n =  SpringContext.getBean(I18n.class);
        sourceField = new JTextField();
        targetField = new JTextField();
        sourceLabel = new JLabel(i18n.get("sourceTargetPanel.label.sourceDir"));
        targetLabel = new JLabel(i18n.get("sourceTargetPanel.label.targetDir"));
        fc1 = new JButton("...");
        fc2 = new JButton("...");

        add(sourceLabel, "cell 0 0");
        add(sourceField, "cell 1 0, growx, w :150:");
        add(fc1, "cell 2 0");

        add(targetLabel, "cell 0 1");
        add(targetField, "cell 1 1, growx, w :150:");
        add(fc2, "cell 2 1");

        fc1.addActionListener(e -> {
            if (onSourceChosen != null)
                onSourceChosen.accept(sourceField.getText());
        });
        fc2.addActionListener(e -> {
            if (onTargetChosen != null)
                onTargetChosen.accept(targetField.getText());
        });
    }

    // --- Getter e Setter  ---
    public String getSourceFieldText() {
        return sourceField.getText();
    }

    public String getTargetFieldText() {
        return targetField.getText();
    }

    public void setSourceFieldText(String t) {
        sourceField.setText(t);
    }

    public void setTargetFieldText(String t) {
        targetField.setText(t);
    }

    /** Set the same text on both fields */
    public void setText(String t) {
        sourceField.setText(t);
        targetField.setText(t);
    }
    
    public JButton getFc1() {
        return fc1;
    }

    public JButton getFc2() {
        return fc2;
    }

    // --- Classic Listener  ---
    public void addFc1ActionListener(ActionListener l) {
        fc1.addActionListener(l);
    }

    public void addFc2ActionListener(ActionListener l) {
        fc2.addActionListener(l);
    }

    /** Adds the same listener to both buttons */
    public void addFcActionListener(ActionListener l) {
        fc1.addActionListener(l);
        fc2.addActionListener(l);
    }

    // --- Consumer Listener  ---
    public void onSourceChosen(Consumer<String> consumer) {
        this.onSourceChosen = consumer;
    }

    public void onTargetChosen(Consumer<String> consumer) {
        this.onTargetChosen = consumer;
    }

 

}
