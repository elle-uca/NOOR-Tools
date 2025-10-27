package org.ln.noortools.tag;



import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubsTest {

    @Test
    void testBasicSubstring() {
        assertEquals("Fil", Subs.getSafeSubstring("Filename.txt", 0, 3)); // <Subs:1:3>
        assertEquals("nam", Subs.getSafeSubstring("Filename.txt", 4, 7)); // <Subs:5:8>
    }

    @Test
    void testFullString() {
        String s = "HelloWorld";
        assertEquals("HelloWorld", Subs.getSafeSubstring(s, 0, s.length()));
    }

    @Test
    void testOutOfRangeIndices() {
        assertEquals("", Subs.getSafeSubstring("Test", 100, 200)); // completely out of range
        assertEquals("", Subs.getSafeSubstring("Test", -5, -1));   // negative indices only
    }

    @Test
    void testPartialOutOfRange() {
        assertEquals("Test", Subs.getSafeSubstring("Test", 0, 100)); // end > length → normalized
        assertEquals("st", Subs.getSafeSubstring("Test", 2, 100));
    }

    @Test
    void testInvalidCases() {
        assertEquals("", Subs.getSafeSubstring(null, 0, 3));   // null input
        assertEquals("", Subs.getSafeSubstring("", 0, 3));     // empty string
        assertEquals("", Subs.getSafeSubstring("Data", 3, 3)); // start == end
        assertEquals("", Subs.getSafeSubstring("Data", 4, 2)); // start > end
    }
}
