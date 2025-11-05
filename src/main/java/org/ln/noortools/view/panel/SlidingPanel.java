package org.ln.noortools.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

/**
 * SlidingPanel = accordion section with collapsible header and smooth animation.
 * Features:
 *  - Rounded gradient header
 *  - Theme-aware colors (FlatLaf light/dark)
 *  - Hover feedback
 *  - Soft shadow card effect
 *  
 *  Author: Luca Noale
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

    private boolean hover = false;

    public SlidingPanel(String title, AbstractPanelContent content, AccordionPanel accordionPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        this.contentComponent = content;

        // --- Custom header with gradient, rounded corners, and shadow ---
        headerPanel = new JPanel(new BorderLayout(5, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();

                boolean dark = FlatLaf.isLafDark();
                Color startColor, endColor, borderColor, shadowColor;

                // 🎨 Theme-aware color palette
                if (dark) {
                    if (hover) {
                        startColor = new Color(70, 90, 130);
                        endColor = new Color(55, 75, 115);
                    } else if (isExpanded) {
                        startColor = new Color(85, 105, 150);
                        endColor = new Color(65, 85, 130);
                    } else {
                        startColor = new Color(60, 70, 100);
                        endColor = new Color(50, 60, 90);
                    }
                    borderColor = new Color(90, 100, 130);
                    shadowColor = new Color(0, 0, 0, 100);
                } else {
                    if (hover) {
                        startColor = new Color(140, 200, 255);
                        endColor = new Color(100, 170, 255);
                    } else if (isExpanded) {
                        startColor = new Color(120, 185, 255);
                        endColor = new Color(90, 155, 240);
                    } else {
                        startColor = new Color(235, 235, 235);
                        endColor = new Color(215, 215, 215);
                    }
                    borderColor = new Color(180, 180, 180);
                    shadowColor = new Color(0, 0, 0, 40);
                }

                // 🌫️ Draw soft shadow first
                int arc = 14;
                int shadowOffset = 3;
                g2.setPaint(shadowColor);
                g2.fillRoundRect(shadowOffset, shadowOffset, width - shadowOffset, height, arc, arc);

                // 🎨 Gradient fill with rounded corners
                GradientPaint gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, width - shadowOffset, height - shadowOffset, arc, arc);

                // 🖋️ Border
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, width - shadowOffset - 1, height - shadowOffset - 1, arc, arc);

                g2.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(6, 10, 6, 10));
        headerPanel.setPreferredSize(new Dimension(100, 42));
        headerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // --- Header elements
        iconLabel = new JLabel(new FlatSVGIcon("icons/chevron-right.svg", 16, 16));

        titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 13f));
        titleLabel.setForeground(FlatLaf.isLafDark() ? new Color(235, 235, 235) : new Color(30, 30, 30));

        countLabel = new JLabel();
        countLabel.setFont(countLabel.getFont().deriveFont(Font.PLAIN, 12f));
        countLabel.setForeground(FlatLaf.isLafDark() ? new Color(200, 200, 200) : new Color(80, 80, 80));

        activeCheck = new JCheckBox();
        activeCheck.setSelected(activePanel);
        activeCheck.setOpaque(false);

        closeButton = new JButton(new FlatSVGIcon("icons/close.svg", 12, 12));
        closeButton.setFocusable(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setToolTipText("Remove this rule");
        closeButton.addActionListener(e -> accordionPanel.removePanel(this));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
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

        // Hover feedback
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                headerPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                headerPanel.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isExpanded && collapseOthers != null)
                    collapseOthers.run();
                togglePanel();
            }
        });

        // --- Content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(contentComponent, BorderLayout.CENTER);
        contentPanel.setPreferredSize(new Dimension(0, 0));
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        contentPanel.setVisible(false);
        contentPanel.setOpaque(false);

        add(headerPanel);
        add(Box.createVerticalStrut(2));
        add(contentPanel);
    }

    // --- Behavior logic ---
    public void collapse() {
        if (isExpanded) togglePanel();
    }

    public void togglePanel() {
        if (animationTimer != null && animationTimer.isRunning()) return;
        isExpanded = !isExpanded;
        iconLabel.setIcon(new FlatSVGIcon(isExpanded ? "icons/chevron-down.svg" : "icons/chevron-right.svg", 16, 16));
        headerPanel.repaint();
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
            if (isExpanded)
                currentHeight = Math.min(currentHeight + ANIMATION_STEP, targetHeight);
            else
                currentHeight = Math.max(currentHeight - ANIMATION_STEP, targetHeight);

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
