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

class ModifyDateTest {

    /**
     * Verifica uso del pattern personalizzato passato al costruttore.
     */
    @Test
    void testInitWithCustomPattern() throws IOException {
        Path temp = Files.createTempFile("md_custom_", ".tmp");
        RenamableFile rf = new RenamableFile(temp.toFile());

        // Set modifica nota
        LocalDateTime known = LocalDateTime.now().minusHours(5);
        Files.setLastModifiedTime(
                temp,
                java.nio.file.attribute.FileTime.from(
                        known.atZone(java.time.ZoneId.systemDefault()).toInstant()
                )
        );

        String userPattern = "yyyy-MM-dd HH:mm";
        String javaPattern = DateTimeFormatMapper.toJavaPattern(userPattern);
        String expected = DateTimeFormatMapper.format(known, javaPattern);

        ModifyDate tag = new ModifyDate(null, userPattern);
        tag.setFilesContext(List.of(rf));
        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(1, out.size());
        assertEquals(expected, out.get(0));
    }

    /**
     * Verifica comportamento con pattern DEFAULT definito nel tag.
     */
    @Test
    void testInitWithDefaultPattern() throws IOException {
        Path temp = Files.createTempFile("md_default_", ".tmp");
        RenamableFile rf = new RenamableFile(temp.toFile());

        LocalDateTime known = FileMetadataUtil.getModificationDate(temp);

        // Il pattern di default del tag ModifyDate è: "yyyy-mm-dd hh:nn"
        String userPatternDefault = "yyyy-mm-dd hh:nn";
        String javaPattern = DateTimeFormatMapper.toJavaPattern(userPatternDefault);
        String expected = DateTimeFormatMapper.format(known, javaPattern);

        ModifyDate tag = new ModifyDate(null /*I18n*/, new Object[0]);
        tag.setFilesContext(List.of(rf));
        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(1, out.size());
        assertEquals(expected, out.get(0));
    }

    /**
     * Verifica comportamento con più file.
     */
    @Test
    void testInitWithMultipleFiles() throws IOException {
        Path t1 = Files.createTempFile("md_multi1_", ".tmp");
        Path t2 = Files.createTempFile("md_multi2_", ".tmp");

        RenamableFile rf1 = new RenamableFile(t1.toFile());
        RenamableFile rf2 = new RenamableFile(t2.toFile());

        String pattern = "yyyy-MM-dd";
        String jp = DateTimeFormatMapper.toJavaPattern(pattern);

        LocalDateTime m1 = FileMetadataUtil.getModificationDate(t1);
        LocalDateTime m2 = FileMetadataUtil.getModificationDate(t2);

        String e1 = DateTimeFormatMapper.format(m1, jp);
        String e2 = DateTimeFormatMapper.format(m2, jp);

        ModifyDate tag = new ModifyDate(null, pattern);
        tag.setFilesContext(List.of(rf1, rf2));
        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(2, out.size());
        assertEquals(e1, out.get(0));
        assertEquals(e2, out.get(1));
    }

    /**
     * Verifica comportamento quando dt = null (caso raro ma possibile).
     */
    @Test
    void testInitHandlesNullDatesGracefully() {
        // Simulazione: creiamo un ModifyDate e richiamiamo newAdd(null)
        // perché FileMetadataUtil non dovrebbe mai ritornare null su lastModifiedTime,
        // ma testiamo comunque la robustezza.
        ModifyDate tag = new ModifyDate(null, "yyyy-MM-dd");
        tag.newClear();
        tag.newAdd(null);

        List<String> out = tag.getNewNames();
        assertEquals(1, out.size());
        assertNull(out.get(0)); // il tag non deve crashare
    }
}
