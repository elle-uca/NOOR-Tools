package org.ln.noor.tools.rename.service;

import java.io.IOException;
import java.util.List;

import org.ln.noor.tools.rename.model.RenamableFile;
import org.springframework.stereotype.Service;

/**
 * Mediates rename/undo operations between the UI and {@link FileRenameManager}.
 * Having this intermediate layer allows the Swing views to stay focused on UI
 * concerns while delegating filesystem operations to a dedicated service.
 *
 * @author Luca Noale
 */
@Service
public class RenameController {

    private final FileRenameManager fileRenameManager;

    public RenameController(FileRenameManager fileRenameManager) {
        this.fileRenameManager = fileRenameManager;
    }

    /**
     * Applies the rename preview to the filesystem by delegating to the
     * {@link FileRenameManager}. Triggers rename rules but does not alter the
     * list content itself.
     *
     * @param files file list containing destination names to apply
     * @throws IOException when filesystem renaming fails
     */
    public void renameFiles(List<RenamableFile> files) throws IOException {
        fileRenameManager.commitRename(files);
    }

    /**
     * Restores the last applied rename batch from the undo history. This method
     * reverts filesystem changes; it does not recompute the rename preview.
     *
     * @throws IOException when reverting filesystem changes fails
     */
    public void undoLastRename() throws IOException {
        fileRenameManager.undoLast();
    }

    /**
     * Registers a listener that reacts to undo availability updates. Listener
     * callbacks report UI state only and do not touch the filesystem.
     *
     * @param listener component interested in undo state notifications
     */
    public void addUndoStateListener(UndoStateListener listener) {
        fileRenameManager.addUndoStateListener(listener);
    }
}
