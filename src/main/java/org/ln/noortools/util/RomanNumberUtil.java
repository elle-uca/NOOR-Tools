package org.ln.noortools.util;

/**
 * Utility class for Roman numeral conversions.
 * 
 * Supports conversion from integers (1–3999) to Roman numeral strings.
 */
public final class RomanNumberUtil {

    private RomanNumberUtil() {
        // prevent instantiation
    }

    /**
     * Converts an integer (1–3999) to its Roman numeral representation.
     *
     * @param num integer value (must be >= 1 and <= 3999)
     * @return Roman numeral string
     * @throws IllegalArgumentException if num is out of range
     */
    public static String intToRoman(int num) {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("Roman numerals support values from 1 to 3999. Got: " + num);
        }

        String[] thousands = { "", "M", "MM", "MMM" };
        String[] hundreds  = { "", "C", "CC", "CCC", "CD",
                                  "D", "DC", "DCC", "DCCC", "CM" };
        String[] tens      = { "", "X", "XX", "XXX", "XL",
                                  "L", "LX", "LXX", "LXXX", "XC" };
        String[] ones      = { "", "I", "II", "III", "IV",
                                  "V", "VI", "VII", "VIII", "IX" };

        return thousands[num / 1000]
             + hundreds[(num % 1000) / 100]
             + tens[(num % 100) / 10]
             + ones[num % 10];
    }
}
