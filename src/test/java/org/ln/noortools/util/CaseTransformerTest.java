package org.ln.noortools.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.ln.noortools.enums.ModeCase;

import static org.junit.jupiter.api.Assertions.*;

class CaseTransformerTest {

    @ParameterizedTest(name = "ModeCase {1} → \"{0}\"")
    @DisplayName("Transform case with different modes")
    @CsvSource({
            // UPPER
            "hello world, UPPER, HELLO WORLD",
            "Java Code, UPPER, JAVA CODE",

            // LOWER
            "HELLO World, LOWER, hello world",

            // TITLE_CASE
            "hello world, TITLE_CASE, Hello World",
            "java programming language, TITLE_CASE, Java Programming Language",
            "MULTI   SPACE test, TITLE_CASE, Multi Space Test",

            // CAPITALIZE_FIRST
            "hELLO wORLD, CAPITALIZE_FIRST, Hello world",
            "java, CAPITALIZE_FIRST, Java",
            "JAVA, CAPITALIZE_FIRST, Java",

            // TOGGLE_CASE
            "Hello, TOGGLE_CASE, hELLO",
            "jAvA123, TOGGLE_CASE, JaVa123"
    })
    void testTransformCase(String input, ModeCase mode, String expected) {
        String result = StringCaseUtil.transformCase(input, mode);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Null input should return null")
    @CsvSource({
            "UPPER", "LOWER", "TITLE_CASE", "CAPITALIZE_FIRST", "TOGGLE_CASE"
    })
    void testNullInput(ModeCase mode) {
        assertNull(StringCaseUtil.transformCase(null, mode));
    }

    @ParameterizedTest
    @DisplayName("Empty input should return empty")
    @CsvSource({
            "UPPER", "LOWER", "TITLE_CASE", "CAPITALIZE_FIRST", "TOGGLE_CASE"
    })
    void testEmptyInput(ModeCase mode) {
        assertEquals("", StringCaseUtil.transformCase("", mode));
    }
}
