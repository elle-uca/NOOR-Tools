package org.ln.noortools;

import javax.swing.SwingUtilities;

import org.ln.noortools.preferences.PreferencesService;
import org.ln.noortools.view.MainFrame;
import org.ln.noortools.view.SplashScreen;
import org.ln.noortools.view.ThemeManager;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NoorToolsApplication {


	
    public static void main(String[] args) {
        // 1) Mostra lo splash
        SplashScreen splash = new SplashScreen();
        splash.showSplash();
     // Simulazione caricamento while Spring parte (progress finto)
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                splash.setProgress(i, "Caricamento... " + i + "%");
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ignored) {}
            }
        }).start();

        
        ConfigurableApplicationContext context =
            new SpringApplicationBuilder(NoorToolsApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
        // 1) recupera le preferenze
        PreferencesService prefs = context.getBean(PreferencesService.class);

        // 2) applica il tema PRIMA di creare le finestre
        ThemeManager.applyTheme(prefs.getTheme());

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = context.getBean(MainFrame.class);
            splash.close();
            frame.setVisible(true);
        });
    }
}