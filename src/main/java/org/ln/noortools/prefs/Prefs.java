package org.ln.noortools.prefs;

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
 *   - Legacy: use RnPrefs.getProp("key"), RnPrefs.saveWindow(800,600), etc.
 */
@Component
public class Prefs {

    // Instance reference for legacy static access
    private static Prefs instance;

    // Local user preferences (system registry or .plist/.ini depending on OS)
    private final Preferences userPrefs = Preferences.userNodeForPackage(Prefs.class);

    // Global config values from application.properties
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
        return language;
    }

    public String getTheme() {
        return theme;
    }

    public int getFillValue() {
        return fillValue;
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
        if ("language".equals(key)) return getInstance().getLanguage();
        if ("theme".equals(key)) return getInstance().getTheme();
        if ("fill.value".equals(key)) return String.valueOf(getInstance().getFillValue());
        return def;
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
