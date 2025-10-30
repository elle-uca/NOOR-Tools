package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class IncHTest {

    @Test
    void testBasicIncrement() {
        IncH tag = new IncH(10); // start at decimal 10 -> hex "A"
        tag.setOldNames(Arrays.asList("a", "b", "c", "d"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("A", "B", "C", "D"), result);
    }

    @Test
    void testStartBelowOneDefaultsToOne() {
        IncH tag = new IncH(0); // start < 1 -> defaults to 1
        tag.setOldNames(Arrays.asList("foo", "bar"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("1", "2"), result);
    }

    @Test
    void testDefaultStart() {
        IncH tag = new IncH(); // no args -> default start=1
        tag.setOldNames(Arrays.asList("x", "y", "z"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("1", "2", "3"), result);
    }

    @Test
    void testHexBeyond9() {
        IncH tag = new IncH(14); // start=14 -> "E"
        tag.setOldNames(Arrays.asList("a", "b", "c"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("E", "F", "10"), result);
    }
}
