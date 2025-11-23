package org.ln.noortools.preferences;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Service;

/**
 * Centralized management of user preferences and configuration defaults.
 * <p>
 * This service exposes a singleton instance so that preferences can be
 * accessed from any part of the application, regardless of whether Spring
 * dependency injection is available.
 */
@Service
public class PreferencesService {

    private static final String PREF_KEY_LANGUAGE = "language";
    private static final String PREF_KEY_THEME = "theme";
    private static final String PREF_KEY_FILL_VALUE = "fill.value";
    private static final String PREF_KEY_WINDOW_WIDTH = "window.width";
    private static final String PREF_KEY_WINDOW_HEIGHT = "window.height";
    private static final String PREF_KEY_LAST_FILE = "lastFile";
    private static final String PREF_KEY_LAST_DIR = "lastDir";

    private static volatile PreferencesService instance;

    private final Preferences userPrefs;
    private final String defaultLanguage;
    private final String defaultTheme;
    private final int defaultFillValue;

    public PreferencesService() {
        this(loadDefaults());
        instance = this;
    }

    private PreferencesService(Properties defaults) {
        this.userPrefs = Preferences.userNodeForPackage(PreferencesService.class);
        this.defaultLanguage = defaults.getProperty("app.language", "it");
        this.defaultTheme = defaults.getProperty("app.theme", "light");
        this.defaultFillValue = Integer.parseInt(defaults.getProperty("app.fill.value", "3"));
    }

    private static Properties loadDefaults() {
        Properties props = new Properties();
        try (InputStream stream = PreferencesService.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (stream != null) {
                props.load(stream);
            }
        } catch (IOException ignored) {
            // Use built-in defaults when application.properties is not available
        }
        return props;
    }

    public static PreferencesService getInstance() {
        if (instance == null) {
            synchronized (PreferencesService.class) {
                if (instance == null) {
                    instance = new PreferencesService(loadDefaults());
                }
            }
        }
        return instance;
    }

    // ---------------------------
    // Preference getters/setters
    // ---------------------------
    public String getLanguage() {
        return userPrefs.get(PREF_KEY_LANGUAGE, defaultLanguage);
    }

    public void setLanguage(String language) {
        if (language == null || language.isBlank()) {
            return;
        }
        userPrefs.put(PREF_KEY_LANGUAGE, language);
    }

    public String getTheme() {
        return userPrefs.get(PREF_KEY_THEME, defaultTheme);
    }

    public void setTheme(String theme) {
        if (theme == null || theme.isBlank()) {
            return;
        }
        userPrefs.put(PREF_KEY_THEME, theme.toLowerCase(Locale.ROOT));
    }

    public int getFillValue() {
        return userPrefs.getInt(PREF_KEY_FILL_VALUE, defaultFillValue);
    }

    public void setFillValue(int fillValue) {
        userPrefs.putInt(PREF_KEY_FILL_VALUE, fillValue);
    }

    public void saveWindowSize(int width, int height) {
        userPrefs.putInt(PREF_KEY_WINDOW_WIDTH, width);
        userPrefs.putInt(PREF_KEY_WINDOW_HEIGHT, height);
    }

    public int[] loadWindowSize() {
        int width = userPrefs.getInt(PREF_KEY_WINDOW_WIDTH, 800);
        int height = userPrefs.getInt(PREF_KEY_WINDOW_HEIGHT, 600);
        return new int[]{width, height};
    }

    public void saveLastFile(String path) {
        if (path != null) {
            userPrefs.put(PREF_KEY_LAST_FILE, path);
        }
    }

    public String loadLastFile() {
        return userPrefs.get(PREF_KEY_LAST_FILE, "");
    }

    public void saveLastDirectory(String path) {
        if (path != null) {
            userPrefs.put(PREF_KEY_LAST_DIR, path);
        }
    }

    public String loadLastDirectory() {
        return userPrefs.get(PREF_KEY_LAST_DIR, "");
    }

    // ---------------------------
    // Legacy static-style helpers
    // ---------------------------
    public static String getProp(String key, String def) {
        return getInstance().resolveProperty(key, def);
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

    private String resolveProperty(String key, String def) {
        if (key == null || key.isBlank()) {
            return def;
        }

        String normalized = key.trim()
                .toLowerCase()
                .replace('_', '.');

        if (normalized.startsWith("app.")) {
            normalized = normalized.substring(4);
        }

        return switch (normalized) {
            case PREF_KEY_LANGUAGE -> getLanguage();
            case PREF_KEY_THEME -> getTheme();
            case PREF_KEY_FILL_VALUE -> String.valueOf(getFillValue());
            default -> def;
        };
    }
}
