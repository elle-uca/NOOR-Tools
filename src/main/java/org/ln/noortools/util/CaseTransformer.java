package org.ln.noortools.util;

import org.ln.noortools.enums.ModeCase;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Utility class for transforming the case of strings based on {@link ModeCase}.
 */
public class CaseTransformer {

    /**
     * Transforms the case of the provided input string based on the specified {@link ModeCase}.
     * <p>
     * Supported transformations:
     * <ul>
     *   <li>{@code UPPER}: Converts the entire string to uppercase.</li>
     *   <li>{@code LOWER}: Converts the entire string to lowercase.</li>
     *   <li>{@code TITLE_CASE}: Converts each word to Title Case
     *       (e.g., "hello world" -> "Hello World").</li>
     *   <li>{@code CAPITALIZE_FIRST}: Capitalizes the first letter of the entire string,
     *       rest in lowercase (e.g., "hELLO wORLD" -> "Hello world").</li>
     *   <li>{@code TOGGLE_CASE}: Inverts the case of each character
     *       (e.g., "Hello" -> "hELLO").</li>
     * </ul>
     *
     * @param input    The string to be transformed. Can be {@code null}.
     * @param modeCase The transformation mode.
     * @return The transformed string, or {@code null} if input is {@code null}.
     * @throws IllegalArgumentException if {@code modeCase} is not supported.
     */
    public static String transformCase(String input, ModeCase modeCase) {
        if (input == null) return null;

        return switch (modeCase) {
            case UPPER -> input.toUpperCase(Locale.ROOT);

            case LOWER -> input.toLowerCase(Locale.ROOT);

            case TITLE_CASE -> Arrays.stream(input.toLowerCase(Locale.ROOT).split("\\s+"))
                    .filter(word -> !word.isBlank())
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                    .collect(Collectors.joining(" "));

            case CAPITALIZE_FIRST ->
                    input.isEmpty()
                            ? input
                            : Character.toUpperCase(input.charAt(0)) +
                              input.substring(1).toLowerCase(Locale.ROOT);

            case TOGGLE_CASE -> input.chars()
                    .mapToObj(c -> {
                        char ch = (char) c;
                        if (Character.isUpperCase(ch)) return String.valueOf(Character.toLowerCase(ch));
                        if (Character.isLowerCase(ch)) return String.valueOf(Character.toUpperCase(ch));
                        return String.valueOf(ch);
                    })
                    .collect(Collectors.joining());

            default -> throw new IllegalArgumentException("Unsupported mode: " + modeCase);
        };
    }
}
