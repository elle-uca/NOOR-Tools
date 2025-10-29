package org.ln.noortools.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.enums.ReplacementType;
import org.ln.noortools.model.RenamableFile;

class ReplaceRuleServiceTest {

    private ReplaceRuleService service;

    @BeforeEach
    void setup() {
        service = new ReplaceRuleService();
    }

    private RenamableFile file(String name) {
        return new RenamableFile(new File(name));
    }

    @Test
    void testReplaceFirstInName() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("file.txt")),
                "f", "X", ReplacementType.FIRST, RenameMode.NAME_ONLY
        );

        assertEquals("Xile.txt", result.get(0).getDestinationName());
    }

    @Test
    void testReplaceLastInName() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("hello.txt")),
                "l", "X", ReplacementType.LAST, RenameMode.NAME_ONLY
        );

        assertEquals("helXo.txt", result.get(0).getDestinationName());
    }

    @Test
    void testReplaceAllInName() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("hello.txt")),
                "l", "X", ReplacementType.ALL, RenameMode.NAME_ONLY
        );

        assertEquals("heXXo.txt", result.get(0).getDestinationName());
    }

    @Test
    void testReplaceFirstInExtension() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("report.txt")),
                "t", "X", ReplacementType.FIRST, RenameMode.EXT_ONLY
        );

        // "txt" → "Xxt"
        assertEquals("report.Xxt", result.get(0).getDestinationName());
    }

    @Test
    void testReplaceLastInExtension() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("report.txt")),
                "t", "X", ReplacementType.LAST, RenameMode.EXT_ONLY
        );

        // "txt" → "txX"
        assertEquals("report.txX", result.get(0).getDestinationName());
    }

    @Test
    void testReplaceAllInExtension() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("report.txt")),
                "t", "X", ReplacementType.ALL,RenameMode.EXT_ONLY
        );

        // "txt" → "XxX"
        assertEquals("report.XxX", result.get(0).getDestinationName());
    }

    @Test
    void testReplaceInFullName() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("file.txt")),
                "e", "E", ReplacementType.ALL, RenameMode.FULL
        );

        assertEquals("filE.txT".toLowerCase(), result.get(0).getDestinationName().toLowerCase());
    }

    @Test
    void testNoMatch() {
        List<RenamableFile> result = service.applyRule(
                List.of(file("data.csv")),
                "z", "X", ReplacementType.ALL, RenameMode.NAME_ONLY
        );

        assertEquals("data.csv", result.get(0).getDestinationName());
    }

    @Test
    void testNullFileList() {
        List<RenamableFile> result = service.applyRule(
                null, "a", "b", ReplacementType.ALL, RenameMode.NAME_ONLY
        );

        assertTrue(result.isEmpty());
    }
}
