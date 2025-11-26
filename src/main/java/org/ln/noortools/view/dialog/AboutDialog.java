package org.ln.noortools.view.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.ln.noortools.i18n.I18n;

import net.miginfocom.swing.MigLayout;

/**
 * Simple "About" dialog showing application metadata.
 */
public final class AboutDialog {

    private AboutDialog() {}
    
    public static void show(Component parent, I18n i18n) {
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = new JLabel(new ImageIcon(AboutDialog.class.getResource("/img/noor-splash.png")));
        content.add(logoLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new MigLayout("", "[fill]"));

        JLabel titleLabel = new JLabel(i18n.get("about.title"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));

        JTextArea description = new JTextArea(i18n.get("about.description"));
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setOpaque(false);
        description.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        
        infoPanel.add(titleLabel, "wrap");
        infoPanel.add(new JLabel(i18n.get("about.version")), "wrap");
        infoPanel.add(new JLabel(i18n.get("about.author")), "wrap");
        infoPanel.add(description, "wrap");

        content.add(infoPanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                parent,
                content,
                i18n.get("menu.help.about"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
