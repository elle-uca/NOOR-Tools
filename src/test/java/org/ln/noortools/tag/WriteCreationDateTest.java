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

class WriteCreationDateTest {

    /**
     * Verifica che init():
     *  - parsI la data passata come stringa
     *  - popoli newNames con i nomi file
     */
    @Test
    void testInitPopulatesNewNames() throws IOException {
        Path temp1 = Files.createTempFile("wcd_init1_", ".tmp");
        Path temp2 = Files.createTempFile("wcd_init2_", ".tmp");

        RenamableFile rf1 = new RenamableFile(temp1.toFile());
        RenamableFile rf2 = new RenamableFile(temp2.toFile());

        // formato ISO-8601 compatibile con parseDateTime (LocalDateTime.parse)
        String dateArg = "2024-01-02T03:04";

        WriteCreationDate tag = new WriteCreationDate(null, dateArg);
        tag.setFilesContext(List.of(rf1, rf2));

        tag.init();

        List<String> out = tag.getNewNames();
        assertEquals(2, out.size());
        assertEquals(temp1.getFileName().toString(), out.get(0));
        assertEquals(temp2.getFileName().toString(), out.get(1));
    }

    /**
     * Verifica che performAction:
     *  - non lanci eccezioni
     *  - quando possibile, imposti davvero la creation date
     */
    @Test
    void testPerformActionTriesToWriteCreationDate() throws IOException {
        Path temp = Files.createTempFile("wcd_action_", ".tmp");
        RenamableFile rf = new RenamableFile(temp.toFile());

        String dateArg = "2024-05-06T07:08";
        LocalDateTime expected = LocalDateTime.parse(dateArg);

        // data "prima" (per capire se cambia qualcosa)
        LocalDateTime before = FileMetadataUtil.getCreationDate(temp);

        WriteCreationDate tag = new WriteCreationDate(null, dateArg);
        tag.setFilesContext(List.of(rf));
        tag.init();

        // non deve lanciare eccezioni
        assertDoesNotThrow(tag::performAction);

        LocalDateTime after = FileMetadataUtil.getCreationDate(temp);

        // su alcuni FS (es. certi Linux) non è possibile scrivere la creation date:
        // in quel caso after rimane null o uguale a before
        if (after != null && (before == null || !after.equals(before))) {
            // Normalizziamo a secondi per evitare problemi di precisione
            LocalDateTime expTrunc = expected.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime aftTrunc = after.truncatedTo(ChronoUnit.SECONDS);

            assertEquals(expTrunc, aftTrunc,
                    "La creation date scritta non corrisponde a quella attesa");
        }
    }

    /**
     * Verifica comportamento con argomento vuoto:
     * deve usare LocalDateTime.now() come default
     * (non possiamo leggere il campo private, ma possiamo verificare che
     *  almeno newNames venga popolato correttamente e che non ci siano errori).
     */
    @Test
    void testInitWithEmptyArgUsesNowAndPopulatesNames() throws IOException {
        Path temp = Files.createTempFile("wcd_empty_", ".tmp");
        RenamableFile rf = new RenamableFile(temp.toFile());

        WriteCreationDate tag = new WriteCreationDate(null, "");
        tag.setFilesContext(List.of(rf));

        assertDoesNotThrow(tag::init);

        List<String> out = tag.getNewNames();
        assertEquals(1, out.size());
        assertEquals(temp.getFileName().toString(), out.get(0));
    }
}
