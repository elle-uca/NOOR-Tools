package org.ln.noortools.prefs;

import java.util.Locale;
import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Centralized management of program preferences.
 * 
 * - Global config values injected by Spring (@Value from application.properties)
 * - Local user preferences via Java Preferences API
 * - Legacy static methods still available for backward compatibility
 *
 * Usage:
 *   - With Spring: inject RnPrefs as a bean
 *   - Legacy: use Prefs.getProp("key"), Prefs.saveWindow(800,600), etc.
 */
@Component
public class Prefs {

    // Instance reference for legacy static access
    private static Prefs instance;

    // Local user preferences (system registry or .plist/.ini depending on OS)
    private final Preferences userPrefs = Preferences.userNodeForPackage(Prefs.class);

    // Global config values from application.properties
    private static final String PREF_KEY_LANGUAGE = "language";
    private static final String PREF_KEY_THEME = "theme";
    private static final String PREF_KEY_FILL_VALUE = "fill.value";

    @Value("${app.language:it}")
    private String language;

    @Value("${app.theme:light}")
    private String theme;

    @Value("${app.fill.value:3}")
    private int fillValue;

    // Spring will call this constructor and set instance
    public Prefs() {
        instance = this;
    }

    // ---------------------------
    // Non-static getters (Spring style)
    // ---------------------------
    public String getLanguage() {
        return userPrefs.get(PREF_KEY_LANGUAGE, language);
    }

    public void setLanguage(String language) {
        if (language == null || language.isBlank()) {
            return;
        }
        this.language = language;
        userPrefs.put(PREF_KEY_LANGUAGE, language);
    }

    public String getTheme() {
        return userPrefs.get(PREF_KEY_THEME, theme);
    }

    public void setTheme(String theme) {
        if (theme == null || theme.isBlank()) {
            return;
        }
        this.theme = theme.toLowerCase(Locale.ROOT);
        userPrefs.put(PREF_KEY_THEME, this.theme);
    }

    public int getFillValue() {
        return userPrefs.getInt(PREF_KEY_FILL_VALUE, fillValue);
    }

    public void setFillValue(int fillValue) {
        this.fillValue = fillValue;
        userPrefs.putInt(PREF_KEY_FILL_VALUE, fillValue);
    }

    // ---------------------------
    // USER PREFS (Java Preferences API)
    // ---------------------------
    public void saveWindowSize(int width, int height) {
        userPrefs.putInt("window.width", width);
        userPrefs.putInt("window.height", height);
    }

    public int[] loadWindowSize() {
        int width = userPrefs.getInt("window.width", 800); // default 800x600
        int height = userPrefs.getInt("window.height", 600);
        return new int[]{width, height};
    }

    public void saveLastFile(String path) {
        userPrefs.put("lastFile", path);
    }

    public String loadLastFile() {
        return userPrefs.get("lastFile", "");
    }

    public void saveLastDirectory(String path) {
        userPrefs.put("lastDir", path);
    }

    public String loadLastDirectory() {
        return userPrefs.get("lastDir", "");
    }

    // ---------------------------
    // LEGACY STATIC ACCESS
    // ---------------------------
    private static Prefs getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RnPrefs not initialized by Spring");
        }
        return instance;
    }

    public static String getProp(String key, String def) {
        if (key == null || key.isBlank()) {
            return def;
        }

        Prefs prefs = getInstance();

        // Normalise legacy keys (e.g. FILL_VALUE → fill.value)
        String normalized = key.trim()
                .toLowerCase()
                .replace('_', '.');

        // Allow callers to prefix with "app." without breaking lookups
        if (normalized.startsWith("app.")) {
            normalized = normalized.substring(4);
        }

        return switch (normalized) {
            case "language" -> prefs.getLanguage();
            case "theme" -> prefs.getTheme();
            case "fill.value" -> String.valueOf(prefs.getFillValue());
            default -> def;
        };
    }

    public static void saveWindow(int w, int h) {
        getInstance().saveWindowSize(w, h);
    }

    public static int[] loadWindow() {
        return getInstance().loadWindowSize();
    }

    public static void saveLastDir(String path) {
        getInstance().saveLastDirectory(path);
    }

    public static String loadLastDir() {
        return getInstance().loadLastDirectory();
    }
}
