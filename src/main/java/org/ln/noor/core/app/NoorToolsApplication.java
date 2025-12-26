package org.ln.noor.core.app;

import javax.swing.SwingUtilities;

import org.ln.noor.core.enums.Theme;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.core.preferences.PreferencesService;
import org.ln.noor.core.service.ThemeManager;
import org.ln.noor.tools.rename.service.RenameController;
import org.ln.noor.tools.rename.service.RenamerService;
import org.ln.noor.tools.rename.ui.panel.AccordionFactory;
import org.ln.noor.tools.rename.ui.panel.PanelFactory;
import org.ln.noor.ui.BootProgressListener;
import org.ln.noor.ui.BootSplash;
import org.ln.noor.ui.MainFrame;
import org.ln.noor.ui.SplashScreen;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formdev.flatlaf.FlatLightLaf;


@SpringBootApplication(
	    scanBasePackages = {
	        "org.ln.noor.core",
	        "org.ln.noor.tools"
	    }
	)
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
            //MainFrame frame = context.getBean(MainFrame.class);
            MainFrame frame = new MainFrame(
            		context.getBean(I18n.class),
            		context.getBean(RenamerService.class),
            		context.getBean(PanelFactory.class),
            		context.getBean(AccordionFactory.class),
            		context.getBean(RenameController.class),
            		context.getBean(PreferencesService.class),
            		context::close         );
            splash.setProgress(100, "Caricamento completato.");
            splash.close();
            frame.setVisible(true);
        });
    }
}