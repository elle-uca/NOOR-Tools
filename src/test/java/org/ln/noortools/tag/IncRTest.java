package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class IncRTest {

    @Test
    void testRomanIncrement() {
        IncR tag = new IncR(1, 2); // start=1, step=2
        tag.setOldNames(Arrays.asList("a", "b", "c", "d"));
        tag.init();

        assertEquals(Arrays.asList("I", "III", "V", "VII"), tag.getNewNames());
    }

    @Test
    void testDefaultArgs() {
        IncR tag = new IncR(); // defaults start=1, step=1
        tag.setOldNames(Arrays.asList("x", "y", "z"));
        tag.init();

        assertEquals(Arrays.asList("I", "II", "III"), tag.getNewNames());
    }
}
