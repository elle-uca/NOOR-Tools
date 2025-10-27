package org.ln.noortools.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTimeFormatMapperTest {

    @Test
    @DisplayName("Should correctly map custom date format to Java pattern")
    void testPatternConversion() {
        assertEquals("dd-MM-yyyy", DateTimeFormatMapper.toJavaPattern("dd-mm-yyyy"));
        assertEquals("dd-MMM-yy", DateTimeFormatMapper.toJavaPattern("dd-mmm-yy"));
        assertEquals("EEEE, d MMMM yyyy", DateTimeFormatMapper.toJavaPattern("dddd, d mmmm yyyy"));
        assertEquals("HH:mm:ss", DateTimeFormatMapper.toJavaPattern("hh:nn:ss"));
    }

    @Test
    @DisplayName("Should correctly format date with mapped pattern")
    void testDateFormatting() {
        LocalDate fixedDate = LocalDate.of(2025, 10, 26);
        
        DateTimeFormatter formatter = DateTimeFormatMapper.formatter("dddd, dd mmmm yyyy", Locale.ENGLISH);

//        String pattern = DateTimeFormatMapper.toJavaPattern("dddd, dd mmmm yyyy");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        String formatted = fixedDate.format(formatter);

        assertEquals("Sunday, 26 October 2025", formatted);
    }

    @Test
    @DisplayName("Should correctly format datetime with mapped pattern")
    void testDateTimeFormatting() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2025, 10, 26, 14, 5, 9);

        String pattern = DateTimeFormatMapper.toJavaPattern("dd/mm/yy hh:nn:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        String formatted = fixedDateTime.format(formatter);

        assertEquals("26/10/25 14:05:09", formatted);
    }
}
