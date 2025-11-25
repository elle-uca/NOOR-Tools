package org.ln.noortools.view.panel;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;

/**
 * Horizontal bar with shortcuts to add rule panels inside the accordion.
 */
@SuppressWarnings("serial")
public class RuleButtonBar extends JPanel {

    private final PanelFactory panelFactory;
    private final AccordionPanel accordion;
    private final Runnable statusUpdater;

    public RuleButtonBar(PanelFactory panelFactory, AccordionPanel accordion, Runnable statusUpdater) {
      // super(new FlowLayout(FlowLayout.LEFT, 8, 6));
    	super(new MigLayout("", "[][][]", "[][][]"));
        this.panelFactory = panelFactory;
        this.accordion = accordion;
        this.statusUpdater = statusUpdater;

        add(newRuleButton("New Name", "NEW", "wrench.svg"));
        add(newRuleButton("Aggiungi", "ADD", "plus.svg"));
        add(newRuleButton("Rimuovi", "REMOVE", "x.svg"), "wrap");
        add(newRuleButton("Replace", "REPLACE", "rotate.svg"));
        add(newRuleButton("Case", "CASE", "case-upper.svg"));
    }

    private JButton newRuleButton(String text, String actionCommand, String iconName) {
        JButton b = new JButton(text, new FlatSVGIcon("icons/" + iconName, 16, 16));
        b.setActionCommand(actionCommand);
        b.addActionListener(this::addPanel);
        b.putClientProperty("JButton.buttonType", "toolBarButton");
        b.putClientProperty("JButton.borderless", true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 12f));
        return b;
    }

    private void addPanel(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        switch (button.getActionCommand()) {
        case "ADD" -> accordion.addPanel(button.getText(), panelFactory.createAddPanel());
        case "REMOVE" -> accordion.addPanel(button.getText(), panelFactory.createRemovePanel());
        case "REPLACE" -> accordion.addPanel(button.getText(), panelFactory.createReplacePanel());
        case "CASE" -> accordion.addPanel(button.getText(), panelFactory.createCasePanel());
        case "NEW" -> accordion.addPanel(button.getText(), panelFactory.createTagPanel(accordion));
        default -> throw new IllegalArgumentException("Unknown command: " + button.getActionCommand());
        }

        if (statusUpdater != null) {
            statusUpdater.run();
        }
    }
}
