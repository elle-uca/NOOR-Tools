package org.ln.noortools.view.panel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * AccordionPanel manages a list of SlidingPanel instances (rules).
 * It displays the panels stacked vertically in an accordion style.
 * * Spring Configuration:
 * - @Component: Marks this class as a Spring-managed bean.
 * - @Scope("prototype"): Ensures a new instance is created every time it's requested, 
 * which is crucial for UI components that may be used in multiple views.
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype") // ⚡ Ensures a new instance for every use (prototype scope).
public class AccordionPanel extends JPanel {

    // Internal list to keep track of all the added SlidingPanels.
    private final List<SlidingPanel> panels = new ArrayList<>();

    /**
     * Constructor sets up the layout and appearance of the accordion container.
     */
    public AccordionPanel() {
        // Use BoxLayout along the Y_AXIS to stack components vertically.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
        setBackground(Color.WHITE);
    }

    /**
     * Adds a new panel to the accordion container.
     *
     * @param title   The title to be displayed in the header of the SlidingPanel.
     * @param content The content panel (AbstractPanelContent) to be placed inside.
     */
    public void addPanel(String title, AbstractPanelContent content) {
        // Inject a reference to this AccordionPanel into the content.
        content.setAccordion(this); 
        
        // Create the new collapsible panel.
        SlidingPanel panel = new SlidingPanel(title, content, this);

        // Set the "collapse others" behavior: when this panel expands, 
        // it triggers a method to collapse all other panels.
        panel.setCollapseOthers(() -> collapseAllExcept(panel));

        // Add the panel to the internal list and the Swing container.
        panels.add(panel);
        add(panel);

        // Default behavior: collapse all existing panels, then ensure the new one is expanded.
        collapseAllExcept(panel);
        if (!panel.isExpanded()) {
            panel.togglePanel();
        }

        // Refresh the UI.
        updateCountLabels();
        revalidate(); // Recalculate component layout.
        repaint();    // Redraw the components.
    }

    /**
     * Removes a panel from the container.
     * * @param panel The SlidingPanel instance to remove.
     */
    public void removePanel(SlidingPanel panel) {
        // Remove from the internal list and the Swing container.
        panels.remove(panel);
        remove(panel);
        
        // Refresh the UI.
        updateCountLabels();
        revalidate();
        repaint();
    }

    /**
     * Collapses all panels except the one specified.
     * This ensures the single-open-panel accordion functionality.
     * * @param expandedPanel The panel that should remain open (or be toggled open).
     */
    public void collapseAllExcept(SlidingPanel expandedPanel) {
        for (SlidingPanel panel : panels) {
            if (panel != expandedPanel) {
                // Mark the panel as inactive and force it to collapse.
                panel.setActivePanel(false); 
                panel.collapse();
            }
        }
    }

    /**
     * Updates the sequential numeric labels displayed on the panels' headers.
     * (e.g., 1, 2, 3...)
     */
    private void updateCountLabels() {
        for (int i = 0; i < panels.size(); i++) {
            // Sets the label to (index + 1).
            panels.get(i).setCountLabel(i + 1);
        }
    }

    /**
     * Returns the currently active (expanded) panel.
     * * @return The active SlidingPanel, or null if none is currently active.
     */
    public SlidingPanel getActivePanel() {
        // Use Java Streams to efficiently filter and find the first active panel.
        return panels.stream()
                .filter(SlidingPanel::isActivePanel)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the current number of panels in the accordion.
     */
    public int getPanelCount() {
        return panels.size();
    }
}