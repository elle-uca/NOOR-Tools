package org.ln.noortools.view;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalOrderComparator implements Comparator<String> {
    private static final Pattern PATTERN = Pattern.compile("(\\d+)");

    @Override
    public int compare(String s1, String s2) {
        if (s1 == null) return -1;
        if (s2 == null) return 1;

        Matcher m1 = PATTERN.matcher(s1);
        Matcher m2 = PATTERN.matcher(s2);

        int lastEnd1 = 0, lastEnd2 = 0;
        while (m1.find() && m2.find()) {
            // confronto parte testuale
            String text1 = s1.substring(lastEnd1, m1.start());
            String text2 = s2.substring(lastEnd2, m2.start());
            int textCompare = text1.compareToIgnoreCase(text2);
            if (textCompare != 0) return textCompare;

            // confronto parte numerica
            int num1 = Integer.parseInt(m1.group());
            int num2 = Integer.parseInt(m2.group());
            if (num1 != num2) return Integer.compare(num1, num2);

            lastEnd1 = m1.end();
            lastEnd2 = m2.end();
        }

        // Se una delle due stringhe ha finito i numeri, confronto il resto
        return s1.substring(lastEnd1).compareToIgnoreCase(s2.substring(lastEnd2));
    }
}
