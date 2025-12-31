package org.ln.noortools.tag;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.tag.Crc32;
import org.ln.noor.tools.rename.tag.Md5;
import org.ln.noor.tools.rename.tag.Sha256;
import org.springframework.context.support.StaticMessageSource;
/**
 * TagChecksumTest.
 *
 * @author Luca Noale
 */

public class TagChecksumTest {

    private I18n i18n;
    private List<RenamableFile> files;

    @BeforeEach
    void setup() throws Exception {
        i18n = stubI18n();

        Path f1 = Files.createTempFile("t1", ".bin");
        Path f2 = Files.createTempFile("t2", ".bin");

        Files.writeString(f1, "HELLO WORLD");
        Files.writeString(f2, "HELLO WORLD");

        files = List.of(
            new RenamableFile(f1.toFile()),
            new RenamableFile(f2.toFile())
        );
    }

    private I18n stubI18n() {
        StaticMessageSource sms = new StaticMessageSource();
        sms.addMessage("tag.crc32.description", Locale.ROOT, "CRC32");
        sms.addMessage("tag.md5.description", Locale.ROOT, "MD5");
        sms.addMessage("tag.sha256.description", Locale.ROOT, "SHA256");
        return new I18n(sms);
    }

    // -------------------------------------------------------------------------
    // TEST 1: CRC32
    // -------------------------------------------------------------------------
    @Test
    void crc32ShouldComputeSameValueForSameContent() {
        Crc32 tag = new Crc32(i18n);
        tag.setFilesContext(files);
        tag.init();

        List<String> out = tag.getNewNames();

        assertThat(out).hasSize(2);
        assertThat(out.get(0)).isEqualTo(out.get(1)); // stessi contenuti → stesso crc32
        assertThat(out.get(0)).matches("[0-9a-fA-F]+");
    }
    

    // -------------------------------------------------------------------------
    // TEST 2: MD5
    // -------------------------------------------------------------------------
    @Test
    void md5ShouldSupportCutting() {
        Md5 tag = new Md5(i18n, 8); // <Md5:8>
        tag.setFilesContext(files);
        tag.init();

        List<String> out = tag.getNewNames();

        assertThat(out).hasSize(2);
        assertThat(out.get(0).length()).isEqualTo(8);
        assertThat(out.get(1).length()).isEqualTo(8);
    }

    // -------------------------------------------------------------------------
    // TEST 3: SHA-256
    // -------------------------------------------------------------------------
    @Test
    void sha256ShouldGenerateFullDigest() {
        Sha256 tag = new Sha256(i18n);
        tag.setFilesContext(files);
        tag.init();

        List<String> out = tag.getNewNames();

        assertThat(out).hasSize(2);
        assertThat(out.get(0)).hasSize(64);  // SHA-256 = 64 hex chars
    }

    // -------------------------------------------------------------------------
    // TEST 4: CACHE
    // -------------------------------------------------------------------------
    @Test
    void checksumShouldBeCached() {
        Md5 tag = new Md5(i18n);
        tag.setFilesContext(files);
        tag.init();

        String first = tag.getNewName(0);

        // Secondo run → deve riusare la cache
        Md5 again = new Md5(i18n);
        again.setFilesContext(files);
        again.init();

        assertThat(again.getNewName(0)).isEqualTo(first);
    }
}
