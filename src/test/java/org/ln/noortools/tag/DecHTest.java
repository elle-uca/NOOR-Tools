package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class DecHTest {

    @Test
    void testBasicDecrement() {
        DecH tag = new DecH(20); // start at decimal 20 -> hex "14"
        tag.setOldNames(Arrays.asList("a", "b", "c", "d"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("14", "13", "12", "11"), result);
    }

    @Test
    void testStopsAtOne() {
        DecH tag = new DecH(3); // start = 3
        tag.setOldNames(Arrays.asList("x", "y", "z", "w", "t"));
        tag.init();

        // should stop at 1 and not go negative
        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("3", "2", "1"), result);
    }

    @Test
    void testStartBelowOneDefaultsToOne() {
        DecH tag = new DecH(0); // start < 1 -> defaults to 1
        tag.setOldNames(Arrays.asList("foo", "bar"));
        tag.init();

        // only "1" can be generated
        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("1"), result);
    }

    @Test
    void testDefaultStart() {
        DecH tag = new DecH(); // no args -> default start=1
        tag.setOldNames(Arrays.asList("a", "b"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("1"), result);
    }
}

