package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class IncLTest {

    @Test
    void testFromLabel() {
        assertEquals(1, IncL.fromLabel("A"));
        assertEquals(26, IncL.fromLabel("Z"));
        assertEquals(27, IncL.fromLabel("AA"));
        assertEquals(28, IncL.fromLabel("AB"));
        assertEquals(52, IncL.fromLabel("AZ"));
        assertEquals(703, IncL.fromLabel("AAA"));
    }

    @Test
    void testToLabel() {
        assertEquals("A", IncL.toLabel(1));
        assertEquals("Z", IncL.toLabel(26));
        assertEquals("AA", IncL.toLabel(27));
        assertEquals("AB", IncL.toLabel(28));
        assertEquals("AZ", IncL.toLabel(52));
        assertEquals("AAA", IncL.toLabel(703));
    }

    @Test
    void testGenerateLabelsFrom() {
        List<String> labels = IncL.generateLabelsFrom("A", 5);
        assertEquals(List.of("A", "B", "C", "D", "E"), labels);

        List<String> labels2 = IncL.generateLabelsFrom("Y", 5);
        assertEquals(List.of("Y", "Z", "AA", "AB", "AC"), labels2);
    }

    @Test
    void testTagInit() {
        IncL tag = new IncL();
        tag.setArgs(1); // start index = 1 → "A"
        tag.setOldNames(List.of("file1", "file2", "file3", "file4"));

        tag.init();

        assertEquals(List.of("A", "B", "C", "D"), tag.getNewNames());
    }
}
