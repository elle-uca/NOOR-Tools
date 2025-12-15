package org.ln.noortools.view.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class SplashScreenSafe extends JWindow implements BootSplash {

    private final JProgressBar progressBar;
    private final JLabel statusLabel;

    public SplashScreenSafe() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        panel.setBackground(new Color(245, 245, 245));

        JLabel logo = new JLabel(
                new ImageIcon(getClass().getResource("/img/noor-splash.png")),
                SwingConstants.CENTER
        );

        statusLabel = new JLabel("Avvio…", SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(14f));

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300, 18));
        progressBar.setStringPainted(false);

        panel.add(logo, BorderLayout.NORTH);
        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void showSplash() {
        setVisible(true);
    }

    @Override
    public void setProgress(int value, String message) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
            statusLabel.setText(message);
        });
    }

    @Override
    public void close() {
        dispose();
    }
}

