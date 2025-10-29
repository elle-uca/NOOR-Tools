package org.ln.noortools.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ln.noortools.enums.ModeCase;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;

class CaseRuleServiceTest {

    private final CaseRuleService service = new CaseRuleService();

    @Test
    @DisplayName("UPPER case - only name")
    void testUpperNameOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("helloWorld.txt"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.UPPER, RenameMode.NAME_ONLY);

        assertEquals("HELLOWORLD.txt", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("UPPER case - only extension")
    void testUpperExtensionOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("helloWorld.txt"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.UPPER, RenameMode.EXT_ONLY);

        assertEquals("helloWorld.TXT", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("UPPER case - full filename")
    void testUpperFull() {
        List<RenamableFile> files = List.of(new RenamableFile("helloWorld.txt"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.UPPER, RenameMode.FULL);

        assertEquals("HELLOWORLD.TXT", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("lowercase - only name")
    void testLowerNameOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("HelloWorld.TXT"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.LOWER, RenameMode.NAME_ONLY);

        assertEquals("helloworld.TXT", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("lowercase - extension only")
    void testLowerExtensionOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("HelloWorld.TXT"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.LOWER, RenameMode.EXT_ONLY);

        assertEquals("HelloWorld.txt", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("Title case - name only")
    void testTitleCaseNameOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("my sample file.TXT"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.TITLE_CASE, RenameMode.NAME_ONLY);

        assertEquals("My Sample File.TXT", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("Capitalize first - name only")
    void testCapitalizeFirstNameOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("hELLOworld.txt"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.CAPITALIZE_FIRST, RenameMode.NAME_ONLY);

        assertEquals("Helloworld.txt", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("Toggle case - name only")
    void testToggleCaseNameOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("ReadMe.txt"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.TOGGLE_CASE, RenameMode.NAME_ONLY);

        assertEquals("rEADmE.txt", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("Toggle case - extension only")
    void testToggleCaseExtensionOnly() {
        List<RenamableFile> files = List.of(new RenamableFile("readme.Md"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.TOGGLE_CASE, RenameMode.EXT_ONLY);

        // toggle della sola estensione
        assertEquals("readme.mD", result.get(0).getDestinationName());
    }

    @Test
    @DisplayName("Toggle case - full filename")
    void testToggleCaseFull() {
        List<RenamableFile> files = List.of(new RenamableFile("readme.md"));
        List<RenamableFile> result = service.applyRule(files, ModeCase.TOGGLE_CASE, RenameMode.FULL);

        assertEquals("rEADME.MD", result.get(0).getDestinationName());
    }
}
