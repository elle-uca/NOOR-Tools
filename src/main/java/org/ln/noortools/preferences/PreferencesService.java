package org.ln.noortools.preferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class PreferencesService {

    private static final String FILE_NAME = "userprefs.properties";

    private final Properties props = new Properties();
    private final Path filePath;
    
    // Instance reference for legacy static access
    private static Prefs instance;
    
    

    public PreferencesService() {
        filePath = Paths.get(FILE_NAME).toAbsolutePath();
        load();
    }

    private void load() {
        if (Files.exists(filePath)) {
            try (FileInputStream in = new FileInputStream(filePath.toFile())) {
                props.load(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
            props.store(out, "NOOR/NOOS User Preferences");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Lingua ----------

    public String getLanguage() {
        return props.getProperty("language", "it");
    }

    public void setLanguage(String lang) {
        props.setProperty("language", lang);
    }

    // ---------- Tema (chiave usata da ThemeManager) ----------

    public String getThemeKey() {
        return props.getProperty("themeKey", "light");
    }

    public void setThemeKey(String key) {
        props.setProperty("themeKey", key);
    }
}
