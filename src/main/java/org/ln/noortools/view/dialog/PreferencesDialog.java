//package org.ln.noortools.view.dialog;
//
//import java.awt.BorderLayout;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JSpinner;
//import javax.swing.SpinnerNumberModel;
//import javax.swing.SwingUtilities;
//
//import org.ln.noortools.i18n.I18n;
//import org.ln.noortools.preferences.Prefs;
//
//@SuppressWarnings("serial")
//public class PreferencesDialog extends JDialog {
//
//    private final JComboBox<ThemeChoice> themeCombo;
//    private final JSpinner fillValueSpinner;
//    private boolean saved;
//
//    public PreferencesDialog(JFrame owner, I18n i18n, Prefs prefs) {
//        super(owner, true);
//        setTitle(i18n.get("preferences.title"));
//
//        themeCombo = new JComboBox<>(new ThemeChoice[] {
//                new ThemeChoice("light", i18n.get("preferences.theme.light")),
//                new ThemeChoice("dark", i18n.get("preferences.theme.dark"))
//        });
//        themeCombo.setSelectedItem(ThemeChoice.fromKey(prefs.getTheme(), themeCombo));
//
//        fillValueSpinner = new JSpinner(new SpinnerNumberModel(prefs.getFillValue(), 0, 10, 1));
//
//        add(buildFormPanel(i18n), BorderLayout.CENTER);
//        add(buildButtonPanel(i18n, prefs), BorderLayout.SOUTH);
//
//        pack();
//        setLocationRelativeTo(owner);
//    }
//
//    private JPanel buildFormPanel(I18n i18n) {
//        JPanel panel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(8, 10, 8, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 0;
//
//        panel.add(new JLabel(i18n.get("preferences.theme.label")), gbc);
//
//        gbc.gridx = 1;
//        gbc.weightx = 1;
//        panel.add(themeCombo, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.weightx = 0;
//        panel.add(new JLabel(i18n.get("preferences.fillValue.label")), gbc);
//
//        gbc.gridx = 1;
//        gbc.weightx = 1;
//        panel.add(fillValueSpinner, gbc);
//
//        return panel;
//    }
//
//    private JPanel buildButtonPanel(I18n i18n, Prefs prefs) {
//        JPanel panel = new JPanel();
//
//        JButton cancel = new JButton(i18n.get("preferences.cancel"));
//        cancel.addActionListener(e -> dispose());
//
//        JButton save = new JButton(i18n.get("preferences.save"));
//        save.addActionListener(e -> {
//            prefs.setTheme(getSelectedTheme());
//            prefs.setFillValue(getSelectedFillValue());
//            saved = true;
//            dispose();
//        });
//
//        panel.add(cancel);
//        panel.add(save);
//        if (SwingUtilities.getRootPane(save) != null) {
//            SwingUtilities.getRootPane(save).setDefaultButton(save);
//        }
//        return panel;
//    }
//
//    public boolean isSaved() {
//        return saved;
//    }
//
//    public String getSelectedTheme() {
//        ThemeChoice choice = (ThemeChoice) themeCombo.getSelectedItem();
//        return choice != null ? choice.key() : "light";
//    }
//
//    public int getSelectedFillValue() {
//        return (Integer) fillValueSpinner.getValue();
//    }
//
//    private record ThemeChoice(String key, String label) {
//        @Override
//        public String toString() {
//            return label;
//        }
//
//        static ThemeChoice fromKey(String key, JComboBox<ThemeChoice> combo) {
//            for (int i = 0; i < combo.getItemCount(); i++) {
//                ThemeChoice choice = combo.getItemAt(i);
//                if (choice.key().equalsIgnoreCase(key)) {
//                    return choice;
//                }
//            }
//            return combo.getItemAt(0);
//        }
//    }
//}
