package org.ln.noortools.view.panel;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.formdev.flatlaf.extras.FlatSVGIcon;

/**
 * SlidingPanel = una sezione a fisarmonica con header cliccabile e contenuto espandibile.
 */
@SuppressWarnings("serial")
public class SlidingPanel extends JPanel {

    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final AbstractPanelContent contentComponent;

    private final JLabel iconLabel;
    private final JLabel titleLabel;
    private final JLabel countLabel;
    private final JCheckBox activeCheck;
    private final JButton closeButton;

    private boolean isExpanded = false;
    private boolean activePanel = true;
    private Runnable collapseOthers;

    private Timer animationTimer;
    private int currentHeight = 0;
    private int maxContentHeight = 0;
    private static final int ANIMATION_STEP = 5;

    public SlidingPanel(String title, AbstractPanelContent content, AccordionPanel accordionPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);

        this.contentComponent = content;

        // --- Header
        headerPanel = new JPanel(new BorderLayout(5, 0));
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // icona toggle
        iconLabel = new JLabel(new FlatSVGIcon("icons/chevron-right.svg", 16, 16));

        // titolo + count
        titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        countLabel = new JLabel();

        // checkbox attivazione
        activeCheck = new JCheckBox();
        activeCheck.setSelected(activePanel);
        activeCheck.setOpaque(false);

        // bottone chiudi
        closeButton = new JButton(new FlatSVGIcon("icons/close.svg", 12, 12));
        closeButton.setFocusable(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setToolTipText("Rimuovi questa regola");
        closeButton.addActionListener(e -> accordionPanel.removePanel(this));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(iconLabel);
        leftPanel.add(countLabel);
        leftPanel.add(titleLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(activeCheck);
        rightPanel.add(closeButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // click su header = toggle
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isExpanded && collapseOthers != null) {
                    collapseOthers.run();
                }
                togglePanel();
            }
        });

        // --- Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(contentComponent, BorderLayout.CENTER);
        contentPanel.setPreferredSize(new Dimension(0, 0));
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        contentPanel.setVisible(false);

        // --- Aggiungi al panel principale
        add(headerPanel);
        add(contentPanel);
    }

    // ---------------------------------------
    // Public API
    // ---------------------------------------

    public void collapse() {
        if (isExpanded) togglePanel();
    }

    public void togglePanel() {
        if (animationTimer != null && animationTimer.isRunning()) return;

        isExpanded = !isExpanded;
        iconLabel.setIcon(new FlatSVGIcon(isExpanded ? "icons/chevron-down.svg" : "icons/chevron-right.svg", 16, 16));
        headerPanel.setBackground(isExpanded ? new Color(200, 230, 250) : Color.LIGHT_GRAY);

        if (isExpanded) openContent();
        else closeContentWithAnimation();
    }

    public void setCollapseOthers(Runnable collapseOthers) {
        this.collapseOthers = collapseOthers;
    }

    public void setCountLabel(int index) {
        countLabel.setText(String.valueOf(index));
    }

    public boolean isActivePanel() {
        return activePanel;
    }

    public void setActivePanel(boolean activePanel) {
        this.activePanel = activePanel;
        activeCheck.setSelected(activePanel);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public AbstractPanelContent getContentComponent() {
        return contentComponent;
    }

    // ---------------------------------------
    // Private helpers
    // ---------------------------------------

    private void openContent() {
        contentPanel.setVisible(true);
        setActivePanel(true);
        maxContentHeight = contentComponent.getPreferredSize().height;

        animateToHeight(maxContentHeight);
    }

    private void closeContentWithAnimation() {
        animateToHeight(0);
    }

    private void animateToHeight(int targetHeight) {
        animationTimer = new Timer(5, e -> {
            if (isExpanded) {
                currentHeight = Math.min(currentHeight + ANIMATION_STEP, targetHeight);
            } else {
                currentHeight = Math.max(currentHeight - ANIMATION_STEP, targetHeight);
            }

            contentPanel.setPreferredSize(new Dimension(getWidth(), currentHeight));
            contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, currentHeight));
            revalidate();
            repaint();

            if (currentHeight == targetHeight) {
                if (!isExpanded) contentPanel.setVisible(false);
                ((Timer) e.getSource()).stop();
            }
        });
        animationTimer.start();
    }
}
