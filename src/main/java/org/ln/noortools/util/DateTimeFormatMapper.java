package org.ln.noortools.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeFormatMapper {

    /**
     * Converts a custom format string (like "dddd, dd mmmm yyyy") 
     * into a valid Java DateTimeFormatter pattern.
     *
     * Supported custom tokens:
     *  - yy    : 2-digit year
     *  - yyyy  : 4-digit year
     *  - m     : month (1-12)
     *  - mm    : 2-digit month
     *  - mmm   : short month name
     *  - mmmm  : full month name
     *  - d     : day of month
     *  - dd    : 2-digit day
     *  - ddd   : short day name
     *  - dddd  : full day name
     *  - h     : hour (0-23)
     *  - hh    : 2-digit hour
     *  - n/nn  : minute(s)
     *  - s/ss  : second(s)
     */
    public static String toJavaPattern(String customPattern) {
        return customPattern
                .replace("yyyy", "yyyy")
                .replace("yy", "yy")
                .replace("dddd", "EEEE")
                .replace("ddd", "EEE")
                .replace("dd", "dd")
                .replace("d", "d")
                .replace("mmmm", "MMMM")
                .replace("mmm", "MMM")
                .replace("mm", "MM")
                .replace("m", "M")
                .replace("hh", "HH")
                .replace("h", "H")
                .replace("nn", "mm")
                .replace("n", "m")
                .replace("ss", "ss")
                .replace("s", "s");
    }

    /**
     * Creates a DateTimeFormatter from a custom pattern and a Locale.
     *
     * @param customPattern custom date format string
     * @param locale        desired locale (e.g., Locale.ENGLISH, Locale.ITALIAN)
     * @return DateTimeFormatter
     */
    public static DateTimeFormatter formatter(String customPattern, Locale locale) {
        String javaPattern = toJavaPattern(customPattern);
        return DateTimeFormatter.ofPattern(javaPattern, locale);
    }

    /**
     * Overload with default JVM locale.
     */
    public static DateTimeFormatter formatter(String customPattern) {
        return formatter(customPattern, Locale.getDefault());
    }
    
    public static String format(LocalDateTime dt, String pattern) {
        if (dt == null) return "";

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
            return dt.format(fmt);
        } catch (IllegalArgumentException ex) {
            // Fallback se il pattern è sbagliato
            DateTimeFormatter fallback = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return dt.format(fallback);
        }
    }
}
