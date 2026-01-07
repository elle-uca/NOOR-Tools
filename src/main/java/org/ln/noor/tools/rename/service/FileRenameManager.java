package org.ln.noor.tools.rename.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import org.ln.noor.core.enums.FileStatus;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.ui.dialog.ActionConfirmationDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Applies rename batches to the filesystem and tracks undo history for the
 * rename tool. This component confirms pending changes, applies the
 * filesystem rename step, and notifies interested listeners about undo state.
 * Naming rules and rename preview generation remain the responsibility of
 * {@link RenamerService} and its collaborators.
 *
 * @author Luca Noale
 */
@Component
public class FileRenameManager {

	@Autowired
	private RenamerService renamerService;



	/**
	 * Represents a single rename action: (old path → new path).
	 * Stored so that it can be undone later.
	 *
	 * @author Luca Noale
	 */
	public static class RenameOperation {
		public final Path oldPath;
		public final Path newPath;

		public RenameOperation(Path oldPath, Path newPath) {
			this.oldPath = oldPath;
			this.newPath = newPath;
		}
	}

        /** A stack of rename batches. Last batch can be undone (LIFO). */
    private final Deque<List<RenameOperation>> history = new ArrayDeque<>();

    /** UI listeners for undo availability state (toolbar button enable/disable). */
    private final List<UndoStateListener> undoListeners = new ArrayList<>();

    /**
     * Hook used to ask the user confirmation before renaming files.
     * Default implementation shows the Swing dialog, but tests can
     * inject a deterministic supplier to avoid UI popups.
     */
    private Function<String, Boolean> confirmationSupplier =
            ActionConfirmationDialog::show;


        // --------------------------------------------------------------------
        // Undo Listener Management
        // --------------------------------------------------------------------

        /**
         * Subscribes a listener interested in undo availability changes. This
         * method updates only in-memory listener registration and does not touch
         * the filesystem.
         *
         * @param l listener invoked when undo state changes
         */
        public void addUndoStateListener(UndoStateListener l) {
                undoListeners.add(l);
        }

        /**
         * Unsubscribes a listener from undo availability changes. This method
         * does not touch the filesystem.
         *
         * @param l listener to remove
         */
        public void removeUndoStateListener(UndoStateListener l) {
                undoListeners.remove(l);
        }

    /**
     * Allows injecting a custom confirmation handler (primarily for tests)
     * so the rename flow can run headless.
     */
    void setConfirmationSupplier(Function<String, Boolean> supplier) {
        this.confirmationSupplier = supplier == null ? msg -> true : supplier;
    }

	/** Notifies UI whether undo is currently available. */
	private void notifyUndoStateChanged() {
		boolean available = !history.isEmpty();
		for (UndoStateListener l : undoListeners) {
			l.onUndoStateChanged(available);
		}
	}


        // --------------------------------------------------------------------
        // Rename Apply
        // --------------------------------------------------------------------

        /**
         * Applies the rename preview to the filesystem. When any rename fails,
         * prior operations in the same batch are rolled back to avoid partial
         * conflicts.
         *
         * @param files the file list with destination names ready to apply
         * @throws IOException when a filesystem rename cannot be completed
         */
        public void commitRename(List<RenamableFile> files) throws IOException {
                StringBuilder confirmMsg = new StringBuilder();
                confirmMsg.append("The following actions will be applied:\n\n");

		for (RenamableFile rf : files) {

			Path oldPath = rf.getSource().toPath();
			Path newPath = oldPath.resolveSibling(rf.getDestinationName());
			
			confirmMsg.append("+ rename file ")
			.append(oldPath.getFileName())
			.append(" → ")
			.append(newPath.getFileName())
			.append("\n");
		}

        boolean ok = confirmationSupplier.apply(confirmMsg.toString());
        if (!ok) {
            return; // user cancelled
        }

		List<RenameOperation> operations = new ArrayList<>();

		try {
			for (RenamableFile rf : files) {

				Path oldPath = rf.getSource().toPath();
				Path newPath = oldPath.resolveSibling(rf.getDestinationName());
				
				String oldName = oldPath.getFileName().toString();
				String newName = newPath.getFileName().toString();

                                // Windows requires a two-step rename when only casing changes to avoid conflicts
                                if (System.getProperty("os.name").toLowerCase().contains("win") &&
                                                (oldName.toLowerCase().equals(newName.toLowerCase())  &&
                                                !oldName.equals(newName))) {

                                        Path tempPath = oldPath.resolveSibling(rf.getDestinationName() + ".tmp_rename");

                                    Files.move(oldPath, tempPath, StandardCopyOption.REPLACE_EXISTING);

                                    Files.move(tempPath, newPath, StandardCopyOption.REPLACE_EXISTING);

                                }
                                else {
                                        Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                                }
				
				rf.setFileStatus(FileStatus.OK);
				rf.setSource(newPath.toFile());
				rf.setDestinationName(newPath.getFileName().toString());
				operations.add(new RenameOperation(oldPath, newPath));
				renamerService.notifyListeners();
			}

		// Save batch for undo.
		history.push(operations);
		notifyUndoStateChanged();

		} catch (IOException e) {
			rollback(operations);
			throw e;
		}
	}


        // --------------------------------------------------------------------
        // Undo Last Rename
        // --------------------------------------------------------------------

        /**
         * Restores all files from the most recent rename batch. This method
         * operates on the filesystem and reloads the current folder afterward to
         * refresh the rename preview.
         */
        public void undoLast() throws IOException {
                if (history.isEmpty()) return;

		List<RenameOperation> ops = history.pop();
		rollback(ops);
		notifyUndoStateChanged();

		// Refresh UI by reloading folder content
		Path parentDir = ops.getFirst().oldPath.getParent();
		renamerService.reloadDirectory(parentDir);
	}


        // --------------------------------------------------------------------
        // Internal Rollback Helper
        // --------------------------------------------------------------------

        /**
         * Reverses a list of rename operations, in reverse order to minimize
         * conflicts when restoring the previous state.
         */
        private void rollback(List<RenameOperation> operations) throws IOException {
                for (int i = operations.size() - 1; i >= 0; i--) {
                        RenameOperation op = operations.get(i);
			if (Files.exists(op.newPath)) {
				Files.move(op.newPath, op.oldPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
}
