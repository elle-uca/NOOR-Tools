package org.ln.noortools.view;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a custom Comparator for Strings that sorts them in "natural order" 
 * (also known as alphanumeric or sensible sorting). This means that numbers 
 * within the strings are compared numerically (e.g., "file2" comes before "file10").
 * 
 * Author: Luca Noale
 */
public class NaturalOrderComparator implements Comparator<String> {
    
    /**
     * Compiled regular expression pattern to find one or more digits (\d+).
     * This pattern is used to segment the strings into text and numeric parts.
     */
    private static final Pattern PATTERN = Pattern.compile("(\\d+)");

    /**
     * Compares two strings, s1 and s2, for natural order sorting.
     * * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return A negative integer, zero, or a positive integer as the first argument 
     * is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(String s1, String s2) {
        // Handle null values first, treating null as 'less than' any non-null string.
        if (s1 == null) return -1;
        if (s2 == null) return 1;

        // Create matchers to search for the numeric pattern in both strings.
        Matcher m1 = PATTERN.matcher(s1);
        Matcher m2 = PATTERN.matcher(s2);

        // Indices to track where the last matched number ended in each string.
        int lastEnd1 = 0, lastEnd2 = 0;
        
        // Loop as long as numbers can be found in *both* strings.
        while (m1.find() && m2.find()) {
            
            // --- 1. Compare the TEXTUAL part (the segment before the current number) ---
            
            // Extract the text segment from the end of the last number to the start of the current number.
            String text1 = s1.substring(lastEnd1, m1.start());
            String text2 = s2.substring(lastEnd2, m2.start());
            
            // Compare the text segments case-insensitively.
            int textCompare = text1.compareToIgnoreCase(text2);
            
            // If the text parts differ, return the result immediately.
            if (textCompare != 0) return textCompare;

            // --- 2. Compare the NUMERIC part (the number itself) ---
            
            // Convert the matched number string to an integer for numerical comparison.
            int num1 = Integer.parseInt(m1.group());
            int num2 = Integer.parseInt(m2.group());
            
            // If the numbers differ, return the numerical comparison result. 
            // This is the core of natural ordering (e.g., 2 < 10).
            if (num1 != num2) return Integer.compare(num1, num2);

            // Update the indices to the end of the current numbers for the next iteration.
            lastEnd1 = m1.end();
            lastEnd2 = m2.end();
        }

        // --- 3. Compare the remaining tail of the strings ---
        // The loop finished because one or both strings ran out of numbers.
        // Compare the remaining parts of the strings (from the last number's end to the string's end).
        return s1.substring(lastEnd1).compareToIgnoreCase(s2.substring(lastEnd2));
    }
}