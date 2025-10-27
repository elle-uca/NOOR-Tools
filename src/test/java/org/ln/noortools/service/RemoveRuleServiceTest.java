//package org.ln.noortools.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RemoveRuleServiceTest {
//
//    private RemoveRuleService service;
//
//    @BeforeEach
//    void setUp() {
//        service = new RemoveRuleService();
//    }
//
//    @Test
//    @DisplayName("Remove middle characters")
//    void testRemoveMiddle() {
//        List<String> result = service.apply(List.of("filename"), 2, 3);
//        assertEquals(List.of("fname"), result); // "ile" removed
//    }
//
//    @Test
//    @DisplayName("Remove from start")
//    void testRemoveFromStart() {
//        List<String> result = service.apply(List.of("filename"), 1, 4);
//        assertEquals(List.of("name"), result);
//    }
//
//    @Test
//    @DisplayName("Remove from end")
//    void testRemoveFromEnd() {
//        List<String> result = service.apply(List.of("filename"), 6, 10);
//        assertEquals(List.of("filen"), result);
//    }
//
//    @Test
//    @DisplayName("Position > string length → unchanged")
//    void testPositionOutOfRange() {
//        List<String> result = service.apply(List.of("short"), 10, 2);
//        assertEquals(List.of("short"), result);
//    }
//
//    @Test
//    @DisplayName("Count <= 0 → unchanged")
//    void testZeroOrNegativeCount() {
//        List<String> result1 = service.apply(List.of("test"), 2, 0);
//        List<String> result2 = service.apply(List.of("test"), 2, -5);
//        assertEquals(List.of("test"), result1);
//        assertEquals(List.of("test"), result2);
//    }
//
//    @Test
//    @DisplayName("Position <= 0 → treated as 1")
//    void testNegativeOrZeroPosition() {
//        List<String> result = service.apply(List.of("hello"), 0, 2);
//        assertEquals(List.of("llo"), result); // remove "he"
//    }
//
//    @Test
//    @DisplayName("Null input list → empty result")
//    void testNullList() {
//        List<String> result = service.apply(null, 2, 2);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Null element inside list → preserved")
//    void testNullElementInList() {
//        List<String> result = service.apply(
//                java.util.Arrays.asList("abc", null, "xyz"),
//                2, 1
//        );
//        assertEquals(3, result.size());
//        assertEquals("ac", result.get(0)); // removed "b"
//        assertNull(result.get(1));        // null preserved
//        assertEquals("xz", result.get(2)); // removed "y"
//    }
//
//    @Test
//    @DisplayName("Empty string input")
//    void testEmptyString() {
//        List<String> result = service.apply(List.of(""), 1, 3);
//        assertEquals(List.of(""), result);
//    }
//}
