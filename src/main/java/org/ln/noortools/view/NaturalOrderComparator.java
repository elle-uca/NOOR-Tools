package org.ln.noortools.view;

import java.util.Comparator;

/**
 * Implements a custom Comparator for Strings that sorts them in "natural order" 
 * (also known as alphanumeric or sensible sorting). This means that numbers 
 * within the strings are compared numerically (e.g., "file2" comes before "file10").
 * 
 * Author: Luca Noale
 */
public class NaturalOrderComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        if (s1 == null) return -1;
        if (s2 == null) return 1;

        int i = 0, j = 0;
        int len1 = s1.length();
        int len2 = s2.length();

        while (i < len1 && j < len2) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(j);

            // If both chars are digits, compare the whole numeric chunks
            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                int start1 = i;
                int start2 = j;

                while (i < len1 && Character.isDigit(s1.charAt(i))) i++;
                while (j < len2 && Character.isDigit(s2.charAt(j))) j++;

                String chunk1 = s1.substring(start1, i);
                String chunk2 = s2.substring(start2, j);

                // Strip leading zeros to compare by length first
                String trimmed1 = chunk1.replaceFirst("^0+", "");
                String trimmed2 = chunk2.replaceFirst("^0+", "");

                // If all zeros, keep one zero to avoid empty strings
                if (trimmed1.isEmpty()) trimmed1 = "0";
                if (trimmed2.isEmpty()) trimmed2 = "0";

                if (trimmed1.length() != trimmed2.length()) {
                    return Integer.compare(trimmed1.length(), trimmed2.length());
                }

                int numCompare = trimmed1.compareTo(trimmed2);
                if (numCompare != 0) return numCompare;

                // If numeric values are equal but lengths differ (e.g. 01 vs 1), shorter comes first
                if (chunk1.length() != chunk2.length()) {
                    return Integer.compare(chunk1.length(), chunk2.length());
                }
            } else {
                // Case-insensitive comparison of non-digit chars
                int cmp = Character.compare(Character.toLowerCase(c1), Character.toLowerCase(c2));
                if (cmp != 0) return cmp;
                i++;
                j++;
            }
        }

        // If we reached the end of one string, the shorter one comes first
        return Integer.compare(len1 - i, len2 - j);
    }
}