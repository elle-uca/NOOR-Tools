package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class WordTest {

    @Test
    void testBasicExtraction() {
        List<String> parts = Word.extractSubstringsFromChars("file-name_test", ".-_()[]");
        assertEquals(Arrays.asList("file", "name", "test"), parts);
    }

    @Test
    void testMultipleDelimiters() {
        List<String> parts = Word.extractSubstringsFromChars("image(v1)[draft]-final_version", ".-_()[]");
        assertEquals(Arrays.asList("image", "v1", "draft", "final", "version"), parts);
    }

    @Test
    void testConsecutiveDelimiters() {
        List<String> parts = Word.extractSubstringsFromChars("file--name__test", ".-_()[]");
        assertEquals(Arrays.asList("file", "name", "test"), parts);
    }

    @Test
    void testNoDelimiters() {
        List<String> parts = Word.extractSubstringsFromChars("filename", ".-_()[]");
        assertEquals(Arrays.asList("filename"), parts);
    }

    @Test
    void testEmptyAndNullInputs() {
        assertTrue(Word.extractSubstringsFromChars("", ".-_()[]").isEmpty());
        assertTrue(Word.extractSubstringsFromChars(null, ".-_()[]").isEmpty());
    }

    @Test
    void testInitWithStartIndex() {
        Word tag = new Word(2); // start = 2 → take second segment
        tag.setOldNames(Arrays.asList("my-file-name", "test_data"));
        tag.init();

        assertEquals(Arrays.asList("file", "data"), tag.getNewNames());
    }

    @Test
    void testInitFallbackForTooHighStart() {
        Word tag = new Word(10); // start = 10, higher than segments available
        tag.setOldNames(Arrays.asList("only-one"));
        tag.init();

        // should fallback to empty string
        assertEquals(Arrays.asList(""), tag.getNewNames());
    }
}
