//package org.ln.noortools.view.panel;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//import javax.swing.JButton;
//import javax.swing.SwingUtilities;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//class RuleButtonBarTest {
//
//    @BeforeAll
//    static void configureHeadless() {
//        System.setProperty("java.awt.headless", "true");
//    }
//
//    @Test
//    void shouldAddPanelsAndNotifyStatusUpdate() throws Exception {
//        PanelFactory factory = mock(PanelFactory.class);
//        AccordionPanel accordion = mock(AccordionPanel.class);
//
//        AddPanel addPanel = mock(AddPanel.class);
//        RemovePanel removePanel = mock(RemovePanel.class);
//        ReplacePanel replacePanel = mock(ReplacePanel.class);
//        CasePanel casePanel = mock(CasePanel.class);
//        TagPanel tagPanel = mock(TagPanel.class);
//
//        when(factory.createAddPanel(any())).thenReturn(addPanel);
//        when(factory.createRemovePanel(any())).thenReturn(removePanel);
//        when(factory.createReplacePanel(any())).thenReturn(replacePanel);
//        when(factory.createCasePanel(any())).thenReturn(casePanel);
//        when(factory.createTagPanel(any())).thenReturn(tagPanel);
//
//        AtomicInteger statusUpdates = new AtomicInteger();
//        RuleButtonBar bar = new RuleButtonBar(factory, accordion, statusUpdates::incrementAndGet);
//
//        SwingUtilities.invokeAndWait(() -> {
//            ((JButton) bar.getComponent(0)).doClick();
//            ((JButton) bar.getComponent(1)).doClick();
//            ((JButton) bar.getComponent(2)).doClick();
//            ((JButton) bar.getComponent(3)).doClick();
//            ((JButton) bar.getComponent(4)).doClick();
//        });
//
//        verify(accordion).addPanel("New Name", tagPanel);
//        verify(accordion).addPanel("Aggiungi", addPanel);
//        verify(accordion).addPanel("Rimuovi", removePanel);
//        verify(accordion).addPanel("Replace", replacePanel);
//        verify(accordion).addPanel("Case", casePanel);
//        assertThat(statusUpdates.get()).isEqualTo(5);
//    }
//}
