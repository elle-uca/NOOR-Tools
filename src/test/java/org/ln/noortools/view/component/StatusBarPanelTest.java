package org.ln.noortools.view.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatusBarPanelTest {

    @BeforeAll
    static void configureHeadless() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void shouldToggleThemeAndUndoStates() throws Exception {
        AtomicBoolean undoTriggered = new AtomicBoolean(false);
        AtomicBoolean themeTriggered = new AtomicBoolean(false);
        AtomicReference<StatusBarPanel> panelRef = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> panelRef.set(
                new StatusBarPanel(
                        e -> themeTriggered.set(true),
                        e -> undoTriggered.set(true),
                        new ImageIcon())));

        StatusBarPanel panel = panelRef.get();

        SwingUtilities.invokeAndWait(() -> {
            panel.setStatusText("Custom status");
            panel.setUndoEnabled(true);
            panel.getUndoButton().doClick();
            panel.getThemeToggle().doClick();
            panel.updateThemeSymbol(true);
        });

        assertThat(panel.getStatusBarLabel().getText()).isEqualTo("Custom status");
        assertThat(panel.getUndoButton().isEnabled()).isTrue();
        assertThat(undoTriggered.get()).isTrue();
        assertThat(themeTriggered.get()).isTrue();
        assertThat(panel.isDarkModeSelected()).isTrue();
    }
}
