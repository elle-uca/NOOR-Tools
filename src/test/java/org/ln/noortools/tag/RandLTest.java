package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class RandLTest {

    @Test
    void testRandomLettersLength() {
        RandL tag = new RandL(3); // length=3
        tag.setOldNames(Arrays.asList("a", "b", "c"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(3, result.size());
        result.forEach(s -> assertEquals(3, s.length())); // each string length=3
    }

    @Test
    void testInvalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> RandL.generateRandomLetters(0));
    }
}
