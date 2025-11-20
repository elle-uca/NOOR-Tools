package org.ln.noortools.view.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ln.noortools.i18n.I18n;
import org.springframework.context.support.StaticMessageSource;

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

        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("status.initial", Locale.getDefault(), "initial");
        messageSource.addMessage("status.theme.toggle.light", Locale.getDefault(), "🌞");
        messageSource.addMessage("status.theme.toggle.dark", Locale.getDefault(), "🌙");
        messageSource.addMessage("status.undo.label", Locale.getDefault(), "Undo");
        messageSource.addMessage("status.undo.tooltip", Locale.getDefault(), "Undo tooltip");
        I18n i18n = new I18n(messageSource);

        SwingUtilities.invokeAndWait(() -> panelRef.set(
                new StatusBarPanel(
                        i18n,
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
