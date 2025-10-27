//package org.ln.noortools.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.ln.noortools.enums.ReplacementType;
//
//class ReplaceRuleServiceTest {
//
//    private final ReplaceRuleService service = new ReplaceRuleService();
//
//    @Test
//    @DisplayName("FIRST occurrence replacement")
//    void testReplaceFirst() {
//        List<String> result = service.apply(
//                List.of("hello world hello"),
//                "hello", "hi",
//                ReplacementType.FIRST, true
//        );
//        assertEquals(List.of("hi world hello"), result);
//    }
//
//    @Test
//    @DisplayName("LAST occurrence replacement")
//    void testReplaceLast() {
//        List<String> result = service.apply(
//                List.of("hello world hello"),
//                "hello", "hi",
//                ReplacementType.LAST, true
//        );
//        assertEquals(List.of("hello world hi"), result);
//    }
//
//    @Test
//    @DisplayName("ALL occurrences replacement")
//    void testReplaceAll() {
//        List<String> result = service.apply(
//                List.of("hello world hello"),
//                "hello", "hi",
//                ReplacementType.ALL, true
//        );
//        assertEquals(List.of("hi world hi"), result);
//    }
//
//    @Test
//    @DisplayName("Case-insensitive replacement")
//    void testCaseInsensitive() {
//        List<String> result = service.apply(
//                List.of("Hello world HELLO"),
//                "hello", "hi",
//                ReplacementType.ALL, false // case-insensitive
//        );
//        assertEquals(List.of("hi world hi"), result);
//    }
//
//    @Test
//    @DisplayName("No match → unchanged string")
//    void testNoMatch() {
//        List<String> result = service.apply(
//                List.of("filename"),
//                "xyz", "123",
//                ReplacementType.ALL, true
//        );
//        assertEquals(List.of("filename"), result);
//    }
//
//    @Test
//    @DisplayName("Null or empty input list → returns empty list")
//    void testNullInputList() {
//        assertTrue(service.apply(null, "a", "b", ReplacementType.ALL, true).isEmpty());
//        assertTrue(service.apply(List.of(), "a", "b", ReplacementType.ALL, true).isEmpty());
//    }
//
//    @Test
//    @DisplayName("Null element inside input list → preserved as null")
//    void testNullElementInList() {
//        List<String> input = Arrays.asList("abc", null, "abc"); // ✅ consente null
//        List<String> result = service.apply(
//                input,
//                "a", "x",
//                ReplacementType.ALL, true
//        );
//
//        assertEquals(Arrays.asList("xbc", null, "xbc"), result);
//    }
//
//
//    @Test
//    @DisplayName("Null or empty search string → input unchanged")
//    void testNullOrEmptySearch() {
//        List<String> result1 = service.apply(List.of("test"), null, "x", ReplacementType.ALL, true);
//        List<String> result2 = service.apply(List.of("test"), "", "x", ReplacementType.ALL, true);
//
//        assertEquals(List.of("test"), result1);
//        assertEquals(List.of("test"), result2);
//    }
//}
