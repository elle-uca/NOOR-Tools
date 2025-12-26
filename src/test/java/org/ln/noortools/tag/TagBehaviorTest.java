package org.ln.noortools.tag;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ln.noor.core.app.SpringContext;
import org.ln.noor.core.enums.FillOption;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.core.preferences.PreferencesService;
import org.ln.noor.tools.rename.factory.TagFactory;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.tag.AbstractTag;
import org.ln.noor.tools.rename.tag.Album;
import org.ln.noor.tools.rename.tag.Artist;
import org.ln.noor.tools.rename.tag.Crc32;
import org.ln.noor.tools.rename.tag.CreationDate;
import org.ln.noor.tools.rename.tag.FileOwner;
import org.ln.noor.tools.rename.tag.Md5;
import org.ln.noor.tools.rename.tag.ModifyDate;
import org.ln.noor.tools.rename.tag.Sha256;
import org.ln.noor.tools.rename.tag.Title;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;

/**
 * Integration-style tests that exercise every tag registered in {@link TagFactory}.
 * The goal is to ensure that initialization never fails and the produced output
 * matches the expected size or known values for deterministic tags.
 */
class TagBehaviorTest {

    private static TagFactory factory;
    private static List<String> baseNames;
    private static List<RenamableFile> tempFiles;
    
    @BeforeAll
    static void disableJaudiotaggerLogs() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.jaudiotagger");
        logger.setLevel(java.util.logging.Level.OFF);
        logger.setUseParentHandlers(false);
    }

    @BeforeAll
    static void setupFactory() throws Exception {
        factory = new TagFactory(new I18n(minimalMessageSource()));
        baseNames = List.of("first-song.mp3", "second-track.wav", "document.pdf");
        tempFiles = createTempFiles();

        // Mock Spring context to provide PreferencesService used by NumberSequenceUtil
        PreferencesService preferences = Mockito.mock(PreferencesService.class);
        Mockito.when(preferences.getFillValue()).thenReturn(0);
        Mockito.when(preferences.getFillType()).thenReturn(FillOption.NO_FILL);

        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
        Mockito.when(applicationContext.getBean(PreferencesService.class)).thenReturn(preferences);

        Field contextField = SpringContext.class.getDeclaredField("context");
        contextField.setAccessible(true);
        contextField.set(null, applicationContext);
    }

    @Test
    void numericTagsProduceSequences() {
        assertThat(runTag("IncrNum").getNewNames()).containsExactly("1", "2", "3");
        assertThat(runTag("DecrNum", 5, 2).getNewNames()).containsExactly("5", "3", "1");
        assertThat(runTag("IncrHex", 14).getNewNames()).containsExactly("E", "F", "10");
        assertThat(runTag("DecrHex", 18).getNewNames()).containsExactly("12", "11", "10");
        assertThat(runTag("IncrRom").getNewNames()).containsExactly("I", "II", "III");
        assertThat(runTag("DecrRom", 5).getNewNames()).containsExactly("V", "IV", "III");
        assertThat(runTag("IncrLet", 2).getNewNames()).containsExactly("B", "C", "D");
        assertThat(runTag("RandNum", 2).getNewNames()).hasSize(3);
    }

    @Test
    void stringTagsHandleSegmentsAndRandomness() {
        assertThat(runTag("Subs", 1, 4).getNewNames()).containsExactly("firs", "seco", "docu");
        assertThat(runTag("Word", 2).getNewNames()).containsExactly("song", "track", "pdf");
        assertThat(runTag("RandLet", 5).getNewNames())
                .allSatisfy(val -> assertThat(val).hasSize(5).matches("[A-Z]+"));
    }

    @Test
    void dateAndTimeTagsRenderCurrentValues() {
        List<String> dates = runTag("Date", "yyyy-mm-dd").getNewNames();
        assertThat(dates).hasSize(3);
        assertThat(dates.getFirst()).matches("\\d{4}-\\d{2}-\\d{2}");

        List<String> times = runTag("Time", "HH:nn").getNewNames();
        assertThat(times).hasSize(3);
        assertThat(times.getFirst()).matches("\\d{2}:\\d{2}");
    }

    @Test
    void audioTagsFallbackToFileNameWhenMetadataMissing() {
        Album album = (Album) prepareFileTag("Album");
        album.init();
        assertThat(album.getNewNames()).containsExactly("track-one", "track-two");

        Artist artist = (Artist) prepareFileTag("Artist");
        artist.init();
        assertThat(artist.getNewNames()).containsExactly("track-one", "track-two");

        Title title = (Title) prepareFileTag("Title");
        title.init();
        assertThat(title.getNewNames()).containsExactly("track-one", "track-two");
    }

    @Test
    void checksumTagsGenerateHexDigests() {
        Md5 md5 = (Md5) prepareFileTag("Md5", 8);
        md5.init();
        assertThat(md5.getNewNames()).allMatch(val -> val.length() == 8);

        Sha256 sha256 = (Sha256) prepareFileTag("Sha256", 12);
        sha256.init();
        assertThat(sha256.getNewNames()).allMatch(val -> val.length() == 12);

        Crc32 crc32 = (Crc32) prepareFileTag("Crc32");
        crc32.init();
        assertThat(crc32.getNewNames()).allSatisfy(val -> assertThat(val).isNotBlank());
    }

    @Test
    void fileSystemTagsReadMetadataSafely() throws IOException {
        // ensure modification time exists
        Files.setLastModifiedTime(tempFiles.getFirst().getSource().toPath(),
                java.nio.file.attribute.FileTime.fromMillis(System.currentTimeMillis()));

        CreationDate creation = (CreationDate) prepareFileTag("CreationDate", "yyyy-mm-dd");
        creation.init();
        assertThat(creation.getNewNames()).hasSize(2);

        ModifyDate modified = (ModifyDate) prepareFileTag("ModifyDate", "yyyy-mm-dd");
        modified.init();
        assertThat(modified.getNewNames()).hasSize(2);

        FileOwner owner = (FileOwner) prepareFileTag("FileOwner");
        owner.init();
        assertThat(owner.getNewNames()).hasSize(2);
    }

    private AbstractTag runTag(String name, Object... args) {
        AbstractTag tag = factory.create(name, args);
        tag.setOldNames(baseNames);
        tag.init();
        return tag;
    }

    private AbstractTag prepareFileTag(String name, Object... args) {
        AbstractTag tag = factory.create(name, args);
        tag.setOldNames(baseNames.subList(0, 2));
        if (tag instanceof org.ln.noor.tools.rename.service.FileAwareTag fileAware) {
            fileAware.setFilesContext(tempFiles);
        }
        return tag;
    }

    private static List<RenamableFile> createTempFiles() throws IOException {
        Path dir = Files.createTempDirectory("audio-test");
        
        Path first = dir.resolve("track-one.mp3");
        Path second = dir.resolve("track-two.wav");

        Files.writeString(first, "alpha");
        Files.writeString(second, "bravo");

        return List.of(
            new RenamableFile(first.toFile()),
            new RenamableFile(second.toFile())
        );
    }

    private static MessageSource minimalMessageSource() {
        StaticMessageSource source = new StaticMessageSource();
        source.addMessage("tag.word.description", Locale.ROOT, "word");
        source.addMessage("tag.title.description", Locale.ROOT, "title");
        source.addMessage("tag.sha256.description", Locale.ROOT, "sha256");
        source.addMessage("tag.incn.description", Locale.ROOT, "incn");
        source.addMessage("tag.decn.description", Locale.ROOT, "decn");
        source.addMessage("tag.inch.description", Locale.ROOT, "inch");
        return source;
    }
}

