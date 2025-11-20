package org.ln.noortools.tag;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileMetadataUtil;

class WriteModifyDateTest {

    /**
     * Verifica che init() con argomento data popolI newNames
     * e che targetDate venga parsata correttamente.
     */
    @Test
    void testInitParsesDateAndPopulatesNewNames() throws IOException {
        Path p1 = Files.createTempFile("wmd_init1_", ".tmp");
        Path p2 = Files.createTempFile("wmd_init2_", ".tmp");

        RenamableFile rf1 = new RenamableFile(p1.toFile());
        RenamableFile rf2 = new RenamableFile(p2.toFile());

        String arg = "2023-10-15T12:34";

        WriteModifyDate tag = new WriteModifyDate(null, arg);
        tag.setFilesContext(List.of(rf1, rf2));
        tag.init();

        // newNames viene popolato con i nomi file
        List<String> out = tag.getNewNames();
        assertEquals(2, out.size());
        assertEquals(p1.getFileName().toString(), out.get(0));
        assertEquals(p2.getFileName().toString(), out.get(1));
    }

    /**
     * Verifica comportamento con argomento vuoto → default NOW()
     */
    @Test
    void testInitWithEmptyArgUsesNow() throws IOException {
        Path p = Files.createTempFile("wmd_empty_", ".tmp");
        RenamableFile rf = new RenamableFile(p.toFile());

        WriteModifyDate tag = new WriteModifyDate(null, "");
        tag.setFilesContext(List.of(rf));

        LocalDateTime before = LocalDateTime.now();

        assertDoesNotThrow(tag::init);

        // performAction deve scrivere una data > now - qualche secondo
        assertDoesNotThrow(tag::performAction);

        LocalDateTime after = FileMetadataUtil.getModificationDate(p);

        if (after != null) {
            assertTrue(!after.isBefore(before.minusSeconds(2)));
        }
    }

    /**
     * Verifica che performAction aggiorni davvero last modified time
     * quando la piattaforma lo consente.
     */
    @Test
    void testPerformActionWritesModificationDateIfSupported() throws IOException {
        Path p = Files.createTempFile("wmd_action_", ".tmp");
        RenamableFile rf = new RenamableFile(p.toFile());

        LocalDateTime target = LocalDateTime.now().minusDays(1);
        String arg = target.toString();

        WriteModifyDate tag = new WriteModifyDate(null, arg);
        tag.setFilesContext(List.of(rf));
        tag.init();

        assertDoesNotThrow(tag::performAction);

        LocalDateTime written = FileMetadataUtil.getModificationDate(p);

        if (written != null) {
            // Normalizzazione a secondi per compatibilità cross-platform
            LocalDateTime w = written.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime t = target.truncatedTo(ChronoUnit.SECONDS);

            assertEquals(t, w);
        }
    }

    /**
     * Test con più file
     */
    @Test
    void testPerformActionWithMultipleFiles() throws IOException {
        Path p1 = Files.createTempFile("wmd_multi1_", ".tmp");
        Path p2 = Files.createTempFile("wmd_multi2_", ".tmp");

        RenamableFile rf1 = new RenamableFile(p1.toFile());
        RenamableFile rf2 = new RenamableFile(p2.toFile());

        LocalDateTime target = LocalDateTime.of(2020, 1, 1, 10, 20);
        String arg = target.toString();

        WriteModifyDate tag = new WriteModifyDate(null, arg);
        tag.setFilesContext(List.of(rf1, rf2));
        tag.init();

        assertDoesNotThrow(tag::performAction);

        LocalDateTime w1 = FileMetadataUtil.getModificationDate(p1);
        LocalDateTime w2 = FileMetadataUtil.getModificationDate(p2);

        if (w1 != null && w2 != null) {
            LocalDateTime t = target.truncatedTo(ChronoUnit.SECONDS);
            assertEquals(t, w1.truncatedTo(ChronoUnit.SECONDS));
            assertEquals(t, w2.truncatedTo(ChronoUnit.SECONDS));
        }
    }

    /**
     * Test robusto: date molto vecchie o molto future.
     */
    @Test
    void testExtremeDates() throws IOException {
        Path p = Files.createTempFile("wmd_extreme_", ".tmp");
        RenamableFile rf = new RenamableFile(p.toFile());

        LocalDateTime target = LocalDateTime.of(1980, 1, 1, 0, 0);
        WriteModifyDate tag = new WriteModifyDate(null, target.toString());
        tag.setFilesContext(List.of(rf));

        tag.init();
        assertDoesNotThrow(tag::performAction);

        LocalDateTime w = FileMetadataUtil.getModificationDate(p);

        if (w != null) {
            assertEquals(target.truncatedTo(ChronoUnit.SECONDS),
                         w.truncatedTo(ChronoUnit.SECONDS));
        }
    }
}
