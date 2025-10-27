//package org.ln.noortools.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.ln.noortools.service.AddRuleService.AddMode;
//
//class AddRuleServiceTest {
//
//    private final AddRuleService service = new AddRuleService();
//
//    @Test
//    @DisplayName("Should add text at the end of each name")
//    void testAddAtEnd() {
//        List<String> input = Arrays.asList("fileOne", "fileTwo");
//        List<String> result = service.apply(input, "123", AddMode.END, 0);
//
//        assertEquals(Arrays.asList("fileOne123", "fileTwo123"), result);
//    }
//
//    @Test
//    @DisplayName("Should add text at the beginning of each name")
//    void testAddAtStart() {
//        List<String> input = Arrays.asList("fileOne", "fileTwo");
//        List<String> result = service.apply(input, "PRE_", AddMode.START, 0);
//
//        assertEquals(Arrays.asList("PRE_fileOne", "PRE_fileTwo"), result);
//    }
//
//    @Test
//    @DisplayName("Should add text at a given position inside each name")
//    void testAddAtPosition() {
//        List<String> input = Arrays.asList("fileOne");
//        List<String> result = service.apply(input, "123", AddMode.POSITION, 3);
//
//        // posizione 3 → "fil" + "123" + "eOne"
//        assertEquals("fil123eOne", result.get(0));
//    }
//
//    @Test
//    @DisplayName("Should handle empty input list")
//    void testEmptyInput() {
//        List<String> input = Collections.emptyList();
//        List<String> result = service.apply(input, "XYZ", AddMode.END, 0);
//
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Should handle null element in list safely")
//    void testNullElementInList() {
//        List<String> input = new ArrayList<>();
//        input.add("file");
//        input.add(null);
//        input.add("test");
//
//        List<String> result = service.apply(input, "X", AddMode.END, 0);
//
//        assertEquals("fileX", result.get(0));
//        assertNull(result.get(1)); // il service non deve esplodere
//        assertEquals("testX", result.get(2));
//    }
//
//    @Test
//    @DisplayName("Should return empty string if input is null")
//    void testNullInputString() {
//        String name = null;
//        List<String> result = service.apply(Arrays.asList(name), "ADD", AddMode.END, 0);
//
//        assertEquals(1, result.size());
//        assertNull(result.get(0));
//    }
//}
