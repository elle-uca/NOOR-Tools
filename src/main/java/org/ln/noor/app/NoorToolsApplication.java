package org.ln.noor.app;

import javax.swing.SwingUtilities;

import org.ln.noor.core.enums.Theme;
import org.ln.noor.core.preferences.PreferencesService;
import org.ln.noor.service.ThemeManager;
import org.ln.noor.ui.component.BootProgressListener;
import org.ln.noor.ui.component.BootSplash;
import org.ln.noor.ui.component.SplashScreen;
import org.ln.noor.ui.view.MainFrame;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formdev.flatlaf.FlatLightLaf;

@SpringBootApplication
public class NoorToolsApplication {


	
    public static void main(String[] args) {
    	FlatLightLaf.setup();
         
        BootSplash splash = new SplashScreen(); 
        splash.showSplash();
        splash.setProgress(5, "Avvio…");
        
        SpringApplicationBuilder builder =
                new SpringApplicationBuilder(NoorToolsApplication.class)
                        .headless(false)
                        .web(WebApplicationType.NONE);
        
        builder.listeners(new BootProgressListener(splash));
        
        ConfigurableApplicationContext context = builder.run(args);
        
        
        // 1) recupera le preferenze
        PreferencesService prefs = context.getBean(PreferencesService.class);

        // 2) applica il tema PRIMA di creare le finestre
        ThemeManager.applyTheme(Theme.fromKey(prefs.getTheme()));
        System.out.println("main   "+prefs.getTheme());
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = context.getBean(MainFrame.class);
            splash.setProgress(100, "Caricamento completato.");
            splash.close();
            frame.setVisible(true);
        });
    }
}