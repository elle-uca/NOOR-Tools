package org.ln.noortools.tag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.DateTimeFormatMapper;

/**
 * Tag <Date:format>
 *
 * Generates the current date formatted using the provided pattern.
 * Example:
 *   <Date:yyyy-mm-dd> -> "2025-10-26"
 *   <Date:dd/mm/yyyy> -> "26/10/2025"
 *
 * Author: Luca Noale
 */
public class Date extends AbstractTag {

    public Date(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "Date";
    }

    @Override
    public void init() {
        String pattern = getStringArg(0, "dd-mmm-yyyy"); // default custom
        String javaPattern = DateTimeFormatMapper.toJavaPattern(pattern);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(javaPattern);

        newClear();
        for (int i = 0; i < oldSize(); i++) {
            newAdd(LocalDate.now().format(formatter));
        }
    }

    @Override
    public String getDescription() {
        return "Current date with custom format (default: yyyy-mm-dd)";
    }
}
