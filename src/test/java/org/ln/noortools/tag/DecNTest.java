package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class DecNTest {

    @Test
    void testBasicDecrement() {
        DecN tag = new DecN(10, 2); // start=10, step=2
        tag.setOldNames(Arrays.asList("a", "b", "c", "d"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("10", "8", "6", "4"), result);
    }

    @Test
    void testDefaultArgs() {
        DecN tag = new DecN(); // defaults start=1, step=1
        tag.setOldNames(Arrays.asList("x", "y", "z"));
        tag.init();

        assertEquals(Arrays.asList("1", "0", "-1"), tag.getNewNames());
    }
}
