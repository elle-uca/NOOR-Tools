package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class RandNTest {

    @Test
    void testRandomNumbersDigits() {
        RandN tag = new RandN(3); // 3 digits
        tag.setOldNames(Arrays.asList("a", "b", "c"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(3, result.size());
        result.forEach(s -> assertEquals(3, s.length())); // all 3-digit numbers
    }

    @Test
    void testInvalidDigitsThrows() {
        assertThrows(IllegalArgumentException.class, () -> RandN.generateRandomNumber(0));
        assertThrows(IllegalArgumentException.class, () -> RandN.generateRandomNumber(10));
    }
}
