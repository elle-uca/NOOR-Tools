package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;

/**
 * Tag <Subs>
 *
 * Extracts a substring from the original file name.
 *
 * - First argument (optional): start position (1-based, default = 1, must be > 0).
 * - Second argument (optional): end position/length (default = 1, must be > 0).
 * - If the indices are invalid or exceed the string length, it returns an empty string.
 *
 * Example:
 *   <Subs:1:3> applied to "Filename.txt" → "Fil"
 *
 * Author: Luca Noale
 */
public class Subs extends AbstractTag {

    public Subs(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Subs";
        this.type = TagType.STRING;
    }

    @Override
    public void init() {
        // Ensure start and step are positive, with defaults
        start = Math.max(getIntArg(0, 1), 1);
        step  = Math.max(getIntArg(1, 1), 1);

        newClear();
        for (String name : getOldNames()) {
            String sub = getSafeSubstring(name, start - 1, step);
            newAdd(sub);
        }
    }

    @Override
    public String getDescription() {
        return "Subs";//i18n.get("tag.subs.description");
    }

    /**
     * Safely extracts a substring of the given string.
     *
     * @param str   the source string
     * @param start the starting index (inclusive, 0-based)
     * @param end   the ending index (exclusive)
     * @return      the substring, or an empty string if indices are invalid
     */
    public static String getSafeSubstring(String str, int start, int end) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        int len = str.length();

        // Normalize indices
        if (start < 0) start = 0;
        if (end > len) end = len;

        // Validate indices
        if (start >= end || start >= len || end <= 0) {
            return "";
        }

        return str.substring(start, end);
    }
}
