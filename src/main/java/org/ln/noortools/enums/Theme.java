package org.ln.noortools.enums;

public enum Theme {

    LIGHT ("light",  "Chiaro (Flat)"),
    DARK  ("dark",   "Scuro (Flat)"),
    NIMBUS("nimbus", "Nimbus (Swing)"),
    METAL ("metal",  "Metal (Swing)"),
    MOTIF ("motif",  "Motif (Swing)"),
    SYSTEM("system", "Tema di Sistema");

    private final String key;
    private final String displayName;

    Theme(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // ricava il tema dalla key salvata in Preferences
    public static Theme fromKey(String key) {
        if (key == null) return LIGHT;
        for (Theme t : values()) {
            if (t.key.equalsIgnoreCase(key)) return t;
        }
        return LIGHT; // fallback
    }
}
