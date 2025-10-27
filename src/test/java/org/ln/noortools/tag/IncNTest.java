package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncNTest {

    @Test
    void testBasicIncrement() {
        IncN tag = new IncN(1, 2); // start=1, step=2
        tag.setOldNames(Arrays.asList("a", "b", "c", "d"));
        tag.init();

        List<String> result = tag.getNewNames();
        assertEquals(Arrays.asList("1", "3", "5", "7"), result);
    }

    @Test
    void testDefaultArgs() {
        IncN tag = new IncN(); // defaults start=1, step=1
        tag.setOldNames(Arrays.asList("x", "y", "z"));
        tag.init();

        assertEquals(Arrays.asList("1", "2", "3"), tag.getNewNames());
    }
}
