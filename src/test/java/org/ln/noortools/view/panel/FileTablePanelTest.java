package org.ln.noortools.view.panel;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ruleservice.RenamerService;

class FileTablePanelTest {

    private Path tempDir;

    @BeforeAll
    static void configureHeadless() {
        System.setProperty("java.awt.headless", "true");
    }

    @AfterEach
    void cleanup() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    @Test
    void shouldRefreshLabelsWhenFilesChange() throws Exception {
        RenamerService renamerService = new RenamerService(Collections.emptyList());
        AtomicReference<FileTablePanel> panelRef = new AtomicReference<>();

//        SwingUtilities.invokeAndWait(() -> panelRef.set(new FileTablePanel(
//                renamerService,
//                () -> {},
//                () -> {},
//                () -> {})));

        FileTablePanel panel = panelRef.get();

        tempDir = Files.createTempDirectory("noortools-test");
        Path sampleFile = Files.createTempFile(tempDir, "sample", ".txt");

        List<RenamableFile> files = List.of(new RenamableFile(sampleFile.toFile()));
        renamerService.setFiles(files);

        assertThat(panel.getInfoLabel().getText()).contains("1 files loaded");

        SwingUtilities.invokeAndWait(() -> panel.getTable().setRowSelectionInterval(0, 0));
        assertThat(panel.getFileInfoLabel().getText()).contains("Selected:");
    }
}
