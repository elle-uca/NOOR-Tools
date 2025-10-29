package org.ln.noortools.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;

class AddRuleServiceTest {

    private final AddRuleService service = new AddRuleService();

    @Test
    void testAddToNameOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("file.txt"));

        List<RenamableFile> result = service.applyRule(files, "PRE_", 1, RenameMode.NAME_ONLY);

        assertEquals("PRE_file.txt", result.get(0).getDestinationName());
    }

    @Test
    void testAddToExtensionOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("file.txt"));

        List<RenamableFile> result = service.applyRule(files, "bak", 1, RenameMode.EXT_ONLY);

        assertEquals("file.bak", result.get(0).getDestinationName());
    }

    @Test
    void testAddToFullName() {
        List<RenamableFile> files = List.of(new RenamableFile("file.txt"));


        List<RenamableFile> result = service.applyRule(files, "_new", 5, RenameMode.FULL);

        // Inserisce dopo il 5° carattere → "file_new.txt"
        assertEquals("file_new.txt", result.get(0).getDestinationName());
    }
}
