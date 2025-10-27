//package org.ln.noortools.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.ln.noortools.enums.ModeCase;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class TagProcessorServiceRuleTest {
//
//    @Autowired
//    private TagProcessorService processor;
//
//    @ParameterizedTest(name = "Transform \"{0}\" with mode {1} → \"{2}\"")
//    @CsvSource({
//            // Uppercase
//            "hello world, UPPER, HELLO WORLD",
//            "Java Code, UPPER, JAVA CODE",
//
//            // Lowercase
//            "HELLO World, LOWER, hello world",
//
//            // Title case
//            "hello world, TITLE_CASE, Hello World",
//            "java programming language, TITLE_CASE, Java Programming Language",
//
//            // Capitalize first
//            "hELLO wORLD, CAPITALIZE_FIRST, Hello world",
//
//            // Toggle case
//            "Hello, TOGGLE_CASE, hELLO",
//            "jAvA123, TOGGLE_CASE, JaVa123"
//    })
//    @DisplayName("Should correctly apply case transformation rules")
//    void testCaseRule(String input, ModeCase mode, String expected) {
//        List<String> result = processor.processRule("caseruleservice", List.of(input), mode);
//
//        assertEquals(1, result.size(), "Result should contain one element");
//        assertEquals(expected, result.get(0));
//    }
//
//    @Test
//    @DisplayName("Null input should return null")
//    void testNullInput() {
//        List<String> input = new ArrayList<>();
//        input.add(null);
//
//        List<String> result = processor.processRule("caseruleservice", input, ModeCase.UPPER);
//
//        assertEquals(1, result.size());
//        assertNull(result.get(0));
//    }
//
//    @Test
//    @DisplayName("Empty input should return empty string")
//    void testEmptyInput() {
//        List<String> result = processor.processRule("caseruleservice", Arrays.asList((String) null), ModeCase.UPPER);
//        assertEquals("", result.get(0));
//    }
//
//    @Test
//    @DisplayName("Batch input should apply rule to all elements")
//    void testBatchInput() {
//        List<String> input = List.of("hello", "world");
//        List<String> result = processor.processRule("caseruleservice", input, ModeCase.UPPER);
//
//        assertEquals(List.of("HELLO", "WORLD"), result);
//    }
//}
