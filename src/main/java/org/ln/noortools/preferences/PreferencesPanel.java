package org.ln.noortools.preferences;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.ln.noortools.view.ThemeManager;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel {

    private final PreferencesService prefs;

    private JComboBox<String> languageBox;
    private JComboBox<String> themeBox;

    // Mappa: label visibile -> themeKey
    private final Map<String, String> themeMap = new LinkedHashMap<>();

    public PreferencesPanel(PreferencesService prefs) {
        this.prefs = prefs;
        initThemeMap();
        initUI();
    }

    private void initThemeMap() {
        themeMap.put("Chiaro (Flat)", "light");
        themeMap.put("Scuro (Flat)", "dark");
        themeMap.put("Nimbus (Swing)", "nimbus");
        themeMap.put("Metal (Swing)", "metal");
        themeMap.put("Motif (Swing)", "motif");
        themeMap.put("Tema di Sistema", "system");
    }

    private void initUI() {
        setLayout(new MigLayout(
                "fill, insets 15",     // layout generale
                "[right][grow, fill]", // colonne: label + campo espandibile
                ""));                 // righe automatiche

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ---------------------------------------------------------
        // LINGUA
        // ---------------------------------------------------------
        add(new JLabel("Lingua:"));
        languageBox = new JComboBox<>(new String[]{"it", "en"});
        languageBox.setSelectedItem(prefs.getLanguage());
        add(languageBox, "wrap");

        // ---------------------------------------------------------
        // TEMA
        // ---------------------------------------------------------
        add(new JLabel("Tema:"));
        themeBox = new JComboBox<>(themeMap.keySet().toArray(new String[0]));

        String currentKey = prefs.getTheme();
        String currentLabel = themeMap.entrySet().stream()
                .filter(e -> e.getValue().equalsIgnoreCase(currentKey))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("Chiaro (Flat)");
        themeBox.setSelectedItem(currentLabel);

        add(themeBox, "wrap para");

        // ---------------------------------------------------------
        // PULSANTI
        // ---------------------------------------------------------
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[]10[]", ""));
        JButton applyBtn = new JButton("Applica");
        JButton saveBtn = new JButton("OK");

        applyBtn.addActionListener(e -> applyPreferences(false));
        saveBtn.addActionListener(e -> applyPreferences(true));

        buttonPanel.add(applyBtn);
        buttonPanel.add(saveBtn);

        add(buttonPanel, "span 2, right");
    }

    private void applyPreferences(boolean closeAfter) {

        prefs.setLanguage((String) languageBox.getSelectedItem());

        String selectedLabel = (String) themeBox.getSelectedItem();
        String themeKey = themeMap.getOrDefault(selectedLabel, "light");
        prefs.setTheme(themeKey);

        //prefs.save();
        ThemeManager.applyTheme(themeKey);
        
        if (closeAfter) {
            SwingUtilities.getWindowAncestor(this).dispose();
        }
    }
}
