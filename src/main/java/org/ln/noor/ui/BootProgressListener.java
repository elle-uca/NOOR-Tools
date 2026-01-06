package org.ln.noor.ui;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
/**
 * Relays Spring Boot lifecycle milestones to the splash screen so users receive
 * timely progress feedback while the desktop environment initializes.
 *
 * @author Luca Noale
 */

public class BootProgressListener
implements ApplicationListener<SpringApplicationEvent> {

        private final BootSplash splash;

        public BootProgressListener(BootSplash splash) {
                this.splash = splash;
        }

        /**
         * Updates the splash screen as the Spring Boot lifecycle advances. The
         * listener only forwards progress information and does not touch the
         * filesystem.
         *
         * @param e Spring lifecycle event that signals the current startup stage
         */
        @Override
        public void onApplicationEvent(SpringApplicationEvent e) {

                if (e instanceof ApplicationStartingEvent) {
                        splash.showSplash();
                        splash.setProgress(5, "Avvio…");

                } else if (e instanceof ApplicationEnvironmentPreparedEvent) {
                        splash.setProgress(20, "Preparazione ambiente…");

                } else if (e instanceof ApplicationContextInitializedEvent) {
                        splash.setProgress(40, "Inizializzazione contesto…");

                } else if (e instanceof ApplicationPreparedEvent) {
                        splash.setProgress(60, "Creazione componenti…");

                } else if (e instanceof ApplicationStartedEvent) {
                        splash.setProgress(80, "Avvio moduli…");

                } else if (e instanceof ApplicationReadyEvent) {
                        splash.setProgress(100, "Pronto!");
                        splash.close();
                }
        }
}
