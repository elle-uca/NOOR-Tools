//package org.ln.noortools.view.panel;
//
//import java.util.List;
//
//import javax.swing.JLabel;
//
//import org.ln.noortools.model.RenamableFile;
//import org.ln.noortools.service.RemoveRuleService;
//import org.ln.noortools.view.JIntegerSpinner;
//
//import net.miginfocom.swing.MigLayout;
//
///**
// * Panel <Remove>
// * Remove characters from file names.
// */
//@SuppressWarnings("serial")
//public class RemovePanel extends AbstractPanelContent {
//
//    private JLabel posLabel;
//    private JLabel numLabel;
//    private JIntegerSpinner posSpinner;
//    private JIntegerSpinner numSpinner;
//
//    private final RemoveRuleService removeRuleService = new RemoveRuleService();
//
//    public RemovePanel(AccordionPanel accordion, java.util.ResourceBundle bundle) {
//        super(accordion, bundle);
//    }
//
//    @Override
//    protected void initComponents() {
//        posLabel = new JLabel(bundle.getString("removePanel.label.position"));
//        numLabel = new JLabel(bundle.getString("removePanel.label.number"));
//
//        posSpinner = new JIntegerSpinner();
//        numSpinner = new JIntegerSpinner();
//
//        posSpinner.addChangeListener(this);
//        numSpinner.addChangeListener(this);
//
//        setLayout(new MigLayout("", "[][grow]", "20[][]20"));
//        add(numLabel);
//        add(numSpinner, "growx, wrap");
//        add(posLabel);
//        add(posSpinner, "growx, wrap");
//    }
//
//    @Override
//    protected void updateView() {
//        List<RenamableFile> updated = removeRuleService.applyRule(
//            accordion.getTableData(),
//            posSpinner.getIntValue(),
//            numSpinner.getIntValue()
//        );
//
//        accordion.setTableData(updated);
//    }
//}
