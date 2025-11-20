package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.DateTimeFormatMapper;
import org.ln.noortools.util.FileMetadataUtil;

class CreationDateTest {

    /**
     * Verifica che CreationDate usi il pattern passato come argomento.
     */
    @Test
    void testInitWithCustomPattern() throws IOException {
        Path temp = Files.createTempFile("cdt_custom_", ".tmp");
        RenamableFile rf = new RenamableFile(temp.toFile());

        // pattern "umano" passato al tag
        String userPattern = "yyyy-MM-dd HH:mm";
        String javaPattern = DateTimeFormatMapper.toJavaPattern(userPattern);

        // data letta dal filesystem (qualunque sia)
        LocalDateTime dt = FileMetadataUtil.getCreationDate(temp);

        // atteso calcolato con lo stesso mapper usato dal tag
        String expected = DateTimeFormatMapper.format(dt, javaPattern);

        CreationDate tag = new CreationDate(null, userPattern);
        tag.setFilesContext(List.of(rf));

        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(1, out.size());
        String result = out.get(0);

        assertEquals(expected, result);
    }

    /**
     * Verifica che, se non passo argomenti, venga usato il pattern di default
     * definito in CreationDate: "yyyy-mm-dd hh:nn" (che poi passa nel mapper).
     */
    @Test
    void testInitWithDefaultPattern() throws IOException {
        Path temp = Files.createTempFile("cdt_default_", ".tmp");
        RenamableFile rf = new RenamableFile(temp.toFile());

        // pattern di default usato in CreationDate.init()
        String userPatternDefault = "yyyy-mm-dd hh:nn";
        String javaPattern = DateTimeFormatMapper.toJavaPattern(userPatternDefault);

        LocalDateTime dt = FileMetadataUtil.getCreationDate(temp);
        String expected = DateTimeFormatMapper.format(dt, javaPattern);

        // nessun argomento → usa pattern di default
        CreationDate tag = new CreationDate(null /* I18n */, new Object[0]);
        tag.setFilesContext(List.of(rf));

        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(1, out.size());
        String result = out.get(0);

        assertEquals(expected, result);
    }

    /**
     * Verifica comportamento con più file nel contesto.
     */
    @Test
    void testInitWithMultipleFiles() throws IOException {
        Path temp1 = Files.createTempFile("cdt_multi1_", ".tmp");
        Path temp2 = Files.createTempFile("cdt_multi2_", ".tmp");

        RenamableFile rf1 = new RenamableFile(temp1.toFile());
        RenamableFile rf2 = new RenamableFile(temp2.toFile());

        String userPattern = "yyyy-MM-dd";
        String javaPattern = DateTimeFormatMapper.toJavaPattern(userPattern);

        LocalDateTime dt1 = FileMetadataUtil.getCreationDate(temp1);
        LocalDateTime dt2 = FileMetadataUtil.getCreationDate(temp2);

        String expected1 = DateTimeFormatMapper.format(dt1, javaPattern);
        String expected2 = DateTimeFormatMapper.format(dt2, javaPattern);

        CreationDate tag = new CreationDate(null, userPattern);
        tag.setFilesContext(List.of(rf1, rf2));

        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(2, out.size());
        assertEquals(expected1, out.get(0));
        assertEquals(expected2, out.get(1));
    }
}
