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

/**
 * Boots the NOOR Tools desktop application by wiring the Spring context and
 * constructing the initial Swing frame. This class defines the lifecycle entry
 * point and ensures the UI starts in a desktop-only configuration without
 * exposing any web endpoints.
 *
 * @author Luca Noale
 */
@SpringBootApplication(
    scanBasePackages = {
        "org.ln.noor.core",
        "org.ln.noor.tools"
    }
)
public class NoorToolsApplication {



    /**
     * Launches the application, preparing the splash screen, initializing the
     * Spring container, and presenting the main window. This method schedules UI
     * creation on the Event Dispatch Thread and does not directly touch the
     * filesystem.
     *
     * @param args runtime arguments supplied by the user
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();

        BootSplash splash = new SplashScreen();
        splash.showSplash();
        splash.setProgress(5, "Starting…");

        // Build the Spring application in non-web (desktop) mode
        SpringApplicationBuilder builder =
                new SpringApplicationBuilder(NoorToolsApplication.class)
                        .headless(false)
                        .web(WebApplicationType.NONE);

        // Attach a listener to update splash progress during boot
        builder.listeners(new BootProgressListener(splash));

        // Start the Spring context
        ConfigurableApplicationContext context = builder.run(args);


        // Load preferences early so the saved look and feel applies before any
        // window is created.
        PreferencesService prefs = context.getBean(PreferencesService.class);

        // Apply the preferred theme before constructing Swing components to avoid
        // repaint flicker during startup.
        ThemeManager.applyTheme(Theme.fromKey(prefs.getTheme()));

        // Create and show the main window on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            MainFrame frame = new MainFrame(
                    context.getBean(I18n.class),
                    context.getBean(RenamerService.class),
                    context.getBean(PanelFactory.class),
                    context.getBean(AccordionFactory.class),
                    context.getBean(RenameController.class),
                    context.getBean(PreferencesService.class),
                    context::close
            );

            // Finalize splash screen and show the UI
            splash.setProgress(100, "Loading completed.");
            splash.close();
            frame.setVisible(true);
        });
    }
}
