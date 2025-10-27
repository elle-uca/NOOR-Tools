package org.ln.noortools.tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag <Word>
 * Extracts a word/segment from a string,
 * split by delimiter characters.
 *
 * Example:
 *   input: "file-name_test"
 *   delimiters: ".-_()[]"
 *   result: ["file", "name", "test"]
 *
 * Author: Luca Noale
 */
public class Word extends AbstractTag {

    private static final String DELIMITERS = ".-_()[]";

    public Word(Object... arg) {
        super(arg);
        this.tagName = "Word";
    }

    @Override
    public void init() {
        start = Math.max(1, getIntArg(0, 1)); // ensure start >= 1
        newClear();

        for (String string : getOldNames()) {
            List<String> sub = extractSubstringsFromChars(string, DELIMITERS);
            if (!sub.isEmpty() && start <= sub.size()) {
                getNewNames().add(sub.get(start - 1));
            } else {
                getNewNames().add(""); // fallback if no segment available
            }
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.word.description");
    }

    /**
     * Splits a string into substrings using a delimiter regex.
     *
     * @param inputString     the input string
     * @param delimitersRegex regex for delimiters (e.g., "[._\\-]")
     * @return a list of non-empty substrings
     */
    public static List<String> extractSubstringsByDelimiters(String inputString, String delimitersRegex) {
        List<String> substrings = new ArrayList<>();
        if (inputString == null || inputString.isEmpty()
                || delimitersRegex == null || delimitersRegex.isEmpty()) {
            return substrings;
        }

        String[] parts = inputString.split(delimitersRegex);
        for (String part : parts) {
            if (!part.isEmpty()) {
                substrings.add(part);
            }
        }
        return substrings;
    }

    /**
     * Splits a string into substrings using the provided delimiter characters.
     *
     * @param inputString     the input string
     * @param delimiterChars  string containing all characters to treat as delimiters
     *                        (e.g., "-_()[]" will split by -, _, (, ), [ and ])
     * @return a list of substrings
     */
    public static List<String> extractSubstringsFromChars(String inputString, String delimiterChars) {
        if (delimiterChars == null || delimiterChars.isEmpty()) {
            List<String> result = new ArrayList<>();
            if (inputString != null && !inputString.isEmpty()) {
                result.add(inputString);
            }
            return result;
        }

        // Build a regex class like:  "[.\\-_()\\[\\]]"
        String regex = "[" + delimiterChars.replaceAll("([\\\\\\]\\[\\-])", "\\\\$1") + "]";

        return extractSubstringsByDelimiters(inputString, regex);
    }
}
