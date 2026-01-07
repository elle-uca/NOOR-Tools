package org.ln.noor.tools.rename.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NumberConverterValidationTest {

    private NumberConverter converter;

    @BeforeEach
    void setUp() {
        converter = new NumberConverter();
    }

    @Nested
    @DisplayName("Base validation")
    class BaseValidation {

        @Test
        @DisplayName("baseIn must be between 2 and 16")
        void baseInOutOfRange() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("10", 1, 10));
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("10", 17, 10));
        }

        @Test
        @DisplayName("baseOut must be between 2 and 16")
        void baseOutOutOfRange() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("10", 10, 1));
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("10", 10, 17));
        }
    }

    @Nested
    @DisplayName("Input validation")
    class InputValidation {

        @Test
        @DisplayName("Number must not be null")
        void numberNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert(null, 10, 2));
        }

        @Test
        @DisplayName("Number must not be blank")
        void numberBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("", 10, 2));
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("   ", 10, 2));
        }

        @Test
        @DisplayName("Invalid digit for base 2 should throw")
        void invalidDigitBase2() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("2", 2, 10));
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("102", 2, 10));
        }

        @Test
        @DisplayName("Invalid digit for base 10 should throw")
        void invalidDigitBase10() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("12A", 10, 2));
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("FF", 10, 16));
        }

        @Test
        @DisplayName("Invalid digit for base 16 should throw")
        void invalidDigitBase16() {
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("G", 16, 10));
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("1G", 16, 10));
        }

        @Test
        @DisplayName("Lower/upper case hex digits are allowed, but only up to F")
        void hexCaseAllowedButLimited() {
            // Allowed
            assertDoesNotThrow(() -> converter.convert("a", 16, 10));
            assertDoesNotThrow(() -> converter.convert("fF", 16, 10));

            // Not allowed
            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert("g", 16, 10));
        }
    }
}
