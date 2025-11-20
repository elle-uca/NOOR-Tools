package org.ln.noortools.tag;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.PerformTag;

/**
 * Base class for all renaming tags used by NOOR Tools.
 *
 * Example tags:
 *   <IncN:1:1>, <Date:yyyy-mm-dd>, <Word:3>
 *
 * Each tag performs a specific transformation or generates
 * part of a new filename during the renaming process.
 *
 * @author Luca Noale
 */
public abstract class AbstractTag implements PerformTag {

    public enum TagType {
        NUMERIC,
        STRING,
        DATE_TIME,
        AUDIO,
        CHECKSUM,
        FILE_SYSTEM
    }

    // ============================================================
    // 🔹 Core properties
    // ============================================================

    protected String tagName;

    protected TagType type;

    protected int start;
    protected int step;
    protected int pos;

    protected List<String> oldNames = new ArrayList<>();
    protected List<String> newNames = new ArrayList<>();

    /** Raw arguments as passed to the tag (unprocessed). */
    protected Object[] rawArgs;

    /** Normalized integer arguments, if applicable. */
    protected Integer[] normArgs;

    /** Internationalization helper (Spring-injected). */
    protected final I18n i18n;

    // Tag string formatting constants
    protected static final char OPEN_TAG = '<';
    protected static final char CLOSE_TAG = '>';
    protected static final String SEPARATOR_TAG = ":";

    // ============================================================
    // 🧩 Constructors and argument handling
    // ============================================================

    /**
     * Base constructor for all tags.
     * @param i18n injected I18n instance
     * @param args optional tag arguments
     */
    protected AbstractTag(I18n i18n, Object... args) {
        this.i18n = i18n;
        setArgs(args);
    }

    /**
     * Parses and normalizes all tag arguments.
     */
    public final void setArgs(Object... args) {
        this.rawArgs = args != null ? args : new Object[0];
        this.normArgs = new Integer[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            normArgs[i] = coerceToInt(rawArgs[i], 0);
        }
    }

    /** Safely converts an object to int. */
    protected int coerceToInt(Object val, int def) {
        if (val == null) return def;
        if (val instanceof Number) return ((Number) val).intValue();
        if (val instanceof String) {
            try { return Integer.parseInt((String) val); }
            catch (NumberFormatException e) { return def; }
        }
        return def;
    }

    /** Gets an int argument by index, or a default. */
    protected int getIntArg(int index, int def) {
        return (index < normArgs.length) ? normArgs[index] : def;
    }

    /** Gets a string argument by index, or a default. */
    protected String getStringArg(int index, String def) {
        if (index < rawArgs.length && rawArgs[index] != null)
            return rawArgs[index].toString();
        return def;
    }

    // ============================================================
    // 🔧 Abstract behavior to be implemented by subclasses
    // ============================================================

    /** Initialize the tag and generate its output names. */
    public abstract void init();

    /** Returns a short human-readable description of this tag. */
    public abstract String getDescription();

    // ============================================================
    // 🧱 Generic getters / helpers
    // ============================================================

    public String getTagName() {
        return tagName;
    }

    public List<String> getNewNames() {
        return newNames;
    }

    public void setNewNames(List<String> newNames) {
        this.newNames = newNames;
    }

    public List<String> getOldNames() {
        return oldNames;
    }

    public void setOldNames(List<String> oldNames) {
        this.oldNames = oldNames;
    }

    public int oldSize() {
        return oldNames.size();
    }

    public void oldClear() {
        oldNames.clear();
    }

    public int newSize() {
        return newNames.size();
    }

    public void newClear() {
        newNames.clear();
    }

    public boolean newAdd(String e) {
        return newNames.add(e);
    }

    /** Returns the new name at a given index (safe). */
    public String getNewName(int index) {
        return (newNames != null && index >= 0 && index < newNames.size())
                ? newNames.get(index)
                : "";
    }

    /** Returns all tag arguments. */
    public Object[] getArgs() {
        return rawArgs;
    }

    public TagType getType() { return type; }

    // ============================================================
    // 🏷️ Tag display helpers
    // ============================================================

    /**
     * Returns the string representation of this tag
     * (e.g., "<IncN:1:1>").
     */
    public String getTagString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OPEN_TAG).append(tagName);

        if (rawArgs != null && rawArgs.length > 0) {
            sb.append(SEPARATOR_TAG);
            for (int i = 0; i < rawArgs.length; i++) {
                sb.append(rawArgs[i]);
                if (i < rawArgs.length - 1)
                    sb.append(SEPARATOR_TAG);
            }
        }

        sb.append(CLOSE_TAG);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getTagString() + " - " + getDescription();
    }
}
