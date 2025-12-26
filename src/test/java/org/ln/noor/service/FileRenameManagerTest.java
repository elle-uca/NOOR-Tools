package org.ln.noor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noor.core.model.RenamableFile;
import org.ln.noor.service.FileRenameManager;
import org.ln.noor.service.ruleservice.RenamerService;

class FileRenameManagerTest {

    @Test
    void commitAndUndoRenameRoundTrip() throws IOException {
        RenamerService renamerService = mock(RenamerService.class);
        FileRenameManager manager = new FileRenameManager();

        manager.setConfirmationSupplier(msg -> true);

        Path tempDir = Files.createTempDirectory("rename-test");
        Path original = Files.createTempFile(tempDir, "original", ".txt");
        Files.writeString(original, "sample");

        RenamableFile renamable = new RenamableFile(original.toFile());
        renamable.setDestinationName("renamed.txt");

        manager.commitRename(List.of(renamable));

        assertThat(original).doesNotExist();
        Path renamedPath = tempDir.resolve("renamed.txt");
        assertThat(renamedPath).exists();

        // inject mock after commit so rollback triggers reload
        java.lang.reflect.Field serviceField = null;
		try {
			serviceField = FileRenameManager.class.getDeclaredField("renamerService");
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        serviceField.setAccessible(true);
        try {
			serviceField.set(manager, renamerService);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        manager.undoLast();

        assertThat(original).exists();
        assertThat(renamedPath).doesNotExist();
        verify(renamerService).reloadDirectory(tempDir);
    }
}

