package org.ln.noortools.view;


import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.ln.noortools.enums.Theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class ThemeManager {

    /**
     * Applica il tema in base alla chiave salvata nelle preferenze.
     */
	public static void applyTheme(Theme theme) {
	    if (theme == null) theme = Theme.LIGHT;
	
	    try {

	        switch (theme) {
	            case DARK -> {
	                FlatDarkLaf.setup();
	            }

	            case LIGHT -> {
	                FlatLightLaf.setup();
	            }

	            case NIMBUS -> {
	                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	            }

	            case METAL -> {
	                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	            }

	            case MOTIF -> {
	                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	            }

	            case SYSTEM -> {
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
