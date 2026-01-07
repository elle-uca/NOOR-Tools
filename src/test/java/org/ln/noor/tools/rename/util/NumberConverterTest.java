package org.ln.noor.tools.rename.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class NumberConverterTest {

    private NumberConverter converter;

    @BeforeEach
    void setUp() {
        converter = new NumberConverter();
    }

    // -------------------------------------------------
    // BASIC CONVERSIONS
    // -------------------------------------------------

    @Test
    @DisplayName("Decimal to Binary")
    void decimalToBinary() {
        assertEquals("1010", converter.convert("10", 10, 2));
    }

    @Test
    @DisplayName("Binary to Decimal")
    void binaryToDecimal() {
        assertEquals("10", converter.convert("1010", 2, 10));
    }

    @Test
    @DisplayName("Decimal to Hexadecimal")
    void decimalToHex() {
        assertEquals("FF", converter.convert("255", 10, 16));
    }

    @Test
    @DisplayName("Hexadecimal to Decimal")
    void hexToDecimal() {
        assertEquals("255", converter.convert("FF", 16, 10));
    }

    // -------------------------------------------------
    // IDENTITY & ZERO
    // -------------------------------------------------

    @Test
    @DisplayName("Same base returns original number")
    void sameBaseReturnsInput() {
        assertEquals("12345", converter.convert("12345", 10, 10));
        assertEquals("ABC", converter.convert("ABC", 16, 16));
    }

    @Test
    @DisplayName("Zero conversion is stable across bases")
    void zeroConversion() {
        assertEquals("0", converter.convert("0", 10, 2));
        assertEquals("0", converter.convert("0", 2, 16));
        assertEquals("0", converter.convert("0", 16, 10));
    }

    // -------------------------------------------------
    // CASE INSENSITIVITY
    // -------------------------------------------------

    @Test
    @DisplayName("Hexadecimal input is case-insensitive")
    void hexCaseInsensitive() {
        assertEquals("255", converter.convert("ff", 16, 10));
        assertEquals("255", converter.convert("Ff", 16, 10));
        assertEquals("FF", converter.convert("255", 10, 16));
    }

    // -------------------------------------------------
    // LARGE NUMBERS
    // -------------------------------------------------

    @Test
    @DisplayName("Large decimal number conversion")
    void largeNumberConversion() {
        String binary = converter.convert("1024", 10, 2);
        assertEquals("10000000000", binary);

        String back = converter.convert(binary, 2, 10);
        assertEquals("1024", back);
    }

    // -------------------------------------------------
    // ROUND TRIP CONSISTENCY
    // -------------------------------------------------

    @Nested
    @DisplayName("Round-trip consistency")
    class RoundTripTests {

        @Test
        void decimalBinaryDecimal() {
            String value = "12345";
            String binary = converter.convert(value, 10, 2);
            String back = converter.convert(binary, 2, 10);
            assertEquals(value, back);
        }

        @Test
        void decimalHexDecimal() {
            String value = "4095";
            String hex = converter.convert(value, 10, 16);
            String back = converter.convert(hex, 16, 10);
            assertEquals(value, back);
        }

        @Test
        void binaryHexBinary() {
            String binary = "110101101011";
            String hex = converter.convert(binary, 2, 16);
            String back = converter.convert(hex, 16, 2);
            assertEquals(binary, back);
        }
    }

    // -------------------------------------------------
    // STRESS / STABILITY
    // -------------------------------------------------

    @RepeatedTest(10)
    @DisplayName("Repeated conversions are stable")
    void repeatedConversions() {
        String value = "9999";
        String hex = converter.convert(value, 10, 16);
        String bin = converter.convert(hex, 16, 2);
        String back = converter.convert(bin, 2, 10);
        assertEquals(value, back);
    }

    // -------------------------------------------------
    // EDGE CONDITIONS
    // -------------------------------------------------

    @Test
    @DisplayName("Single digit values across bases")
    void singleDigitValues() {
        assertEquals("1", converter.convert("1", 2, 10));
        assertEquals("1", converter.convert("1", 10, 16));
        assertEquals("1", converter.convert("1", 16, 2));
    }
}
