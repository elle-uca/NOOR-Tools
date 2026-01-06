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
 * Application entry point for NOOR Tools.
 * <p>
 * {@code NoorToolsApplication} bootstraps the desktop application by:
 * <ul>
 *   <li>Initializing the Look & Feel</li>
 *   <li>Displaying the splash screen and tracking boot progress</li>
 *   <li>Starting the Spring application context in non-web mode</li>
 *   <li>Applying user preferences (theme)</li>
 *   <li>Creating and showing the main application window</li>
 * </ul>
 *
 * This class intentionally disables any web environment and runs
 * as a pure desktop Swing application.
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
     * Main entry point of the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        // Install a default Look & Feel early to avoid UI flicker
        FlatLightLaf.setup();

        // Create and display the splash screen
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

        // Retrieve user preferences from the context
        PreferencesService prefs =
                context.getBean(PreferencesService.class);

        // Apply the preferred theme BEFORE creating any Swing windows
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
