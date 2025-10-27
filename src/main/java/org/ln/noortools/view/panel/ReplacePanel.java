//package org.ln.noortools.view.panel;
//
//import java.util.List;
//
//import javax.swing.ButtonGroup;
//import javax.swing.JLabel;
//import javax.swing.JRadioButton;
//import javax.swing.JTextField;
//
//import org.ln.noortools.enums.ReplacementType;
//import org.ln.noortools.model.RenamableFile;
//import org.ln.noortools.service.ReplaceRuleService;
//
//import net.miginfocom.swing.MigLayout;
//
///**
// * Panel <Replace>
// * Replace text in file names.
// */
//@SuppressWarnings("serial")
//public class ReplacePanel extends AbstractPanelContent {
//
//    private JLabel textLabel;
//    private JLabel replaceLabel;
//    private JLabel occurrenceLabel;
//
//    private JTextField textField;
//    private JTextField replaceField;
//
//    private JRadioButton jrbCase;
//    private ButtonGroup group;
//    private JRadioButton jrbFirst;
//    private JRadioButton jrbLast;
//    private JRadioButton jrbAll;
//
//    private final ReplaceRuleService replaceRuleService = new ReplaceRuleService();
//
//    public ReplacePanel(AccordionPanel accordion, java.util.ResourceBundle bundle) {
//        super(accordion, bundle);
//    }
//
//    @Override
//    protected void initComponents() {
//        textLabel      = new JLabel(bundle.getString("replacePanel.label.text"));
//        replaceLabel   = new JLabel(bundle.getString("replacePanel.label.replace"));
//        occurrenceLabel = new JLabel(bundle.getString("replacePanel.label.occurence"));
//
//        textField    = new JTextField();
//        replaceField = new JTextField();
//
//        textField.getDocument().addDocumentListener(this);
//        replaceField.getDocument().addDocumentListener(this);
//
//        jrbCase  = new JRadioButton(bundle.getString("replacePanel.radioButton.case"));
//
//        group    = new ButtonGroup();
//        jrbFirst = new JRadioButton(bundle.getString("replacePanel.radioButton.first"), true);
//        jrbLast  = new JRadioButton(bundle.getString("replacePanel.radioButton.last"));
//        jrbAll   = new JRadioButton(bundle.getString("replacePanel.radioButton.all"));
//
//        group.add(jrbFirst);
//        group.add(jrbLast);
//        group.add(jrbAll);
//
//        jrbCase.addActionListener(this);
//        jrbFirst.addActionListener(this);
//        jrbLast.addActionListener(this);
//        jrbAll.addActionListener(this);
//
//        setLayout(new MigLayout("", "[][grow]", "20[][][][][]20"));
//        add(textLabel,      "cell 0 0");
//        add(textField,      "cell 1 0, grow");
//        add(replaceLabel,   "cell 0 1");
//        add(replaceField,   "cell 1 1, grow");
//        add(jrbCase,        "cell 0 2");
//        add(occurrenceLabel,"cell 0 3");
//        add(jrbFirst,       "cell 1 3");
//        add(jrbLast,        "cell 0 4");
//        add(jrbAll,         "cell 1 4");
//    }
//
//    @Override
//    protected void updateView() {
//        ReplacementType type = ReplacementType.FIRST;
//        if (jrbLast.isSelected()) type = ReplacementType.LAST;
//        if (jrbAll.isSelected()) type = ReplacementType.ALL;
//
//        List<RenamableFile> updated = replaceRuleService.applyRule(
//            accordion.getTableData(),
//            textField.getText(),
//            replaceField.getText(),
//            type,
//            jrbCase.isSelected()
//        );
//
//        accordion.setTableData(updated);
//    }
//}
