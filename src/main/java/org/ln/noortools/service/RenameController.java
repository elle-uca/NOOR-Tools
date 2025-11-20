package org.ln.noortools.service;

import java.io.IOException;
import java.util.List;

import org.ln.noortools.model.RenamableFile;
import org.springframework.stereotype.Service;

/**
 * Mediates rename/undo operations between the UI and {@link FileRenameManager}.
 * Having this intermediate layer allows the Swing views to stay focused on UI
 * concerns while delegating filesystem operations to a dedicated service.
 */
@Service
public class RenameController {

    private final FileRenameManager fileRenameManager;

    public RenameController(FileRenameManager fileRenameManager) {
        this.fileRenameManager = fileRenameManager;
    }

    public void renameFiles(List<RenamableFile> files) throws IOException {
        fileRenameManager.commitRename(files);
    }

    public void undoLastRename() throws IOException {
        fileRenameManager.undoLast();
    }

    public void addUndoStateListener(UndoStateListener listener) {
        fileRenameManager.addUndoStateListener(listener);
    }
}
