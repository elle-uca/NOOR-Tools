package org.ln.noortools.tag;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.DateTimeFormatMapper;

/**
 * Tag <Time:format>
 *
 * Generates the current time formatted using the provided pattern.
 * Example:
 *   <Time:hh:nn> -> "23:45"
 *   <Time:HH:nn:ss> -> "23:45:12"
 *
 * Author: Luca Noale
 */
public class Time extends AbstractTag {

    public Time(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "Time";
    }

    @Override
    public void init() {
        String pattern = getStringArg(0, "hh:nn:ss"); // default custom
        String javaPattern = DateTimeFormatMapper.toJavaPattern(pattern);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(javaPattern);

        newClear();
        for (int i = 0; i < oldSize(); i++) {
            newAdd(LocalTime.now().format(formatter));
        }
    }

    @Override
    public String getDescription() {
        return "Current time with custom format (default: hh:nn:ss)";
    }
}
