package org.ln.noortools.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("serial")
public class SplashScreen extends JWindow {

    private final JProgressBar progressBar;
    private final boolean fadeAllowed;

    public SplashScreen() {

        // Determina se la traslucenza è supportata
        boolean canFade = false;
        try {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice();

            if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
                canFade = true;
            }
        } catch (Exception ignore) {}

        this.fadeAllowed = canFade;

        JPanel content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout());

        // Carica immagine
        ImageIcon icon = loadImage();
        JLabel label = new JLabel(icon);
        content.add(label, BorderLayout.CENTER);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(icon.getIconWidth(), 20));
        content.add(progressBar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        // Se supportato, impostiamo l'opacità iniziale
        if (fadeAllowed) {
            try {
                setOpacity(0f);
            } catch (UnsupportedOperationException ignore) {
                // fallback
            }
        }
    }

    private ImageIcon loadImage() {
        // 1) Cerca nel classpath (resources)
        URL res = getClass().getResource("/img/noor-splash.png");
        if (res != null) return new ImageIcon(res);

        // 2) Cerca nella cartella docs
        Path path = Paths.get("docs", "splash.png").toAbsolutePath();
        return new ImageIcon(path.toString());
    }

    /** Mostra lo splash, con fade-in solo se supportato */
    public void showSplash() {
        setVisible(true);

        if (!fadeAllowed) return;

        Timer fade = new Timer(20, e -> {
            try {
                float opacity = getOpacity();
                opacity += 0.05f;

                if (opacity >= 1f) {
                    setOpacity(1f);
                    ((Timer) e.getSource()).stop();
                } else {
                    setOpacity(opacity);
                }
            } catch (UnsupportedOperationException ex) {
                ((Timer) e.getSource()).stop();
            }
        });

        fade.start();
    }

    public void setProgress(int value, String message) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
            progressBar.setString(message);
        });
    }

    public void close() {
        dispose();
    }
}
