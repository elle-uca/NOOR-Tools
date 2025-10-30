package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class DecRTest {

    @Test
    void testRomanDecrement() {
        DecR tag = new DecR(10, 2); // start=10, step=2
        tag.setOldNames(Arrays.asList("a", "b", "c", "d"));
        tag.init();

        assertEquals(Arrays.asList("X", "VIII", "VI", "IV"), tag.getNewNames());
    }

    @Test
    void testDefaultArgs() {
        DecR tag = new DecR(); // defaults start=10, step=1
        tag.setOldNames(Arrays.asList("x", "y", "z"));
        tag.init();

        assertEquals(Arrays.asList("X", "IX", "VIII"), tag.getNewNames());
    }
}
