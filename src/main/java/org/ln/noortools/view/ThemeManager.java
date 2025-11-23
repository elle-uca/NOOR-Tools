package org.ln.noortools.view;


import java.awt.Window;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class ThemeManager {

    /**
     * Applica il tema in base alla chiave salvata nelle preferenze.
     */
	public static void applyTheme(String themeKey) {
	    if (themeKey == null) themeKey = "light";
	    themeKey = themeKey.toLowerCase(Locale.ROOT);

	    try {

	        switch (themeKey) {
	            case "dark" -> {
	                FlatDarkLaf.setup();
	            }

	            case "light" -> {
	                FlatLightLaf.setup();
	            }

	            case "nimbus" -> {
	                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	            }

	            case "metal" -> {
	                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	            }

	            case "motif" -> {
	                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	            }

	            case "system" -> {
	                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            }

	            default -> {
	                FlatLightLaf.setup();
	            }
	        }

	        // Aggiorna tutte le finestre aperte
	        for (Window window : Window.getWindows()) {
	            SwingUtilities.updateComponentTreeUI(window);
	        }

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        FlatLightLaf.setup();
	    }
	}


    /** Applica il tema di default (light) all'avvio, se serve */
    public static void setupDefault() {
        FlatLightLaf.setup();
    }
}
