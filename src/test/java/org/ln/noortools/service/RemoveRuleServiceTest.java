package org.ln.noortools.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;

class RemoveRuleServiceTest {

    private final RemoveRuleService service = new RemoveRuleService();

    @Test
    void testRemoveFromNameOnly() {
        RenamableFile f = new RenamableFile(new File("filename.txt"));
        List<RenamableFile> result = service.applyRule(
                List.of(f), 2, 3, RenameMode.NAME_ONLY);
        assertEquals("fname.txt", result.get(0).getDestinationName());
    }

    @Test
    void testRemoveFromExtensionOnly() {
        RenamableFile f = new RenamableFile(new File("file.txt"));
        List<RenamableFile> result = service.applyRule(
                List.of(f), 2, 1, RenameMode.EXT_ONLY); // togli "x"
        assertEquals("file.tt", result.get(0).getDestinationName());
    }

    @Test
    void testRemoveFromFullName() {
        RenamableFile f = new RenamableFile(new File("file.txt"));
        List<RenamableFile> result = service.applyRule(
                List.of(f), 5, 3, RenameMode.FULL); // rimuove ".tx"
        assertEquals("filet", result.get(0).getDestinationName());
    }

    @Test
    void testRemoveNothingIfPositionTooHigh() {
        RenamableFile f = new RenamableFile(new File("abc.txt"));
        List<RenamableFile> result = service.applyRule(
                List.of(f), 50, 2, RenameMode.NAME_ONLY);
        assertEquals("abc.txt", result.get(0).getDestinationName());
    }

    @Test
    void testRemoveWithNegativeLength() {
        RenamableFile f = new RenamableFile(new File("abc.txt"));
        List<RenamableFile> result = service.applyRule(
                List.of(f), 1, -3, RenameMode.NAME_ONLY);
        assertEquals("abc.txt", result.get(0).getDestinationName());
    }
}
