package org.ln.noortools.view.panel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * AccordionPanel gestisce una lista di SlidingPanel (regole).
 * Mostra i pannelli uno sotto l’altro, in stile fisarmonica.
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype") // ⚡ Importantissimo!
public class AccordionPanel extends JPanel {

    private final List<SlidingPanel> panels = new ArrayList<>();

    public AccordionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
    }

    /**
     * Aggiunge un nuovo pannello al contenitore.
     *
     * @param title   titolo da mostrare nell’header
     * @param content contenuto (AbstractPanelContent)
     */
    public void addPanel(String title, AbstractPanelContent content) {
    	content.setAccordion(this); // 👈 assegni qui
        SlidingPanel panel = new SlidingPanel(title, content, this);

        // comportamento "chiudi gli altri"
        panel.setCollapseOthers(() -> collapseAllExcept(panel));

        panels.add(panel);
        add(panel);

        // di default espandi il nuovo
        collapseAllExcept(panel);
        if (!panel.isExpanded()) {
            panel.togglePanel();
        }

        updateCountLabels();
        revalidate();
        repaint();
    }

    /**
     * Rimuove un pannello.
     */
    public void removePanel(SlidingPanel panel) {
        panels.remove(panel);
        remove(panel);
        updateCountLabels();
        revalidate();
        repaint();
    }

    /**
     * Collassa tutti i pannelli tranne quello indicato.
     */
    public void collapseAllExcept(SlidingPanel expandedPanel) {
        for (SlidingPanel panel : panels) {
            if (panel != expandedPanel) {
                panel.setActivePanel(false);
                panel.collapse();
            }
        }
    }

    /**
     * Aggiorna le etichette numeriche dei pannelli.
     */
    private void updateCountLabels() {
        for (int i = 0; i < panels.size(); i++) {
            panels.get(i).setCountLabel(i + 1);
        }
    }

    /**
     * Ritorna il pannello attivo.
     */
    public SlidingPanel getActivePanel() {
        return panels.stream()
                .filter(SlidingPanel::isActivePanel)
                .findFirst()
                .orElse(null);
    }

    public int getPanelCount() {
        return panels.size();
    }
}
