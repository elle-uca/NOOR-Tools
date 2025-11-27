package org.ln.noortools.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.ln.noortools.enums.FileStatus;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ruleservice.RenamerService;
import org.ln.noortools.view.dialog.ActionConfirmationDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Handles the final filesystem rename operations.
 *
 * Responsibilities:
 *  - apply renames to disk
 *  - execute registered ActionTag (metadata writes…)
 *  - manage UNDO history
 *  - show global confirm/log dialogs
 *
 * It does NOT decide *what* the new name should be — 
 * that logic belongs to RenamerService/StringParser.
 * 
 * Author: Luca Noale
 */
@Component
public class FileRenameManager {

	@Autowired
	private RenamerService renamerService;

	@Autowired
	private ActionManager actionManager;


	@Autowired
	private PerformManager performManager;


	/**
	 * Represents a single rename action: (old path → new path).
	 * Stored so that it can be undone later.
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


	// --------------------------------------------------------------------
	// Undo Listener Management
	// --------------------------------------------------------------------

	public void addUndoStateListener(UndoStateListener l) {
		undoListeners.add(l);
	}

	public void removeUndoStateListener(UndoStateListener l) {
		undoListeners.remove(l);
	}

	/** Notifies UI whether undo is currently available. */
	private void notifyUndoStateChanged() {
		boolean available = !history.isEmpty();
		for (UndoStateListener l : undoListeners) {
			l.onUndoStateChanged(available);
		}
	}


	// --------------------------------------------------------------------
	// Rename Execution
	// --------------------------------------------------------------------

	/**
	 * Performs the actual file renaming on disk.
	 * If one rename fails, previous renames are rolled back.
	 *
	 * @param files The list of files with their computed destination names
	 */
	public void commitRename(List<RenamableFile> files) throws IOException {

		System.out.println("commitRename  "+files.size());
		
		// 🔥 1) PRIMA DI QUALSIASI RENAME → esegui azioni con conferma
		StringBuilder confirmMsg = new StringBuilder();
		confirmMsg.append("The following actions will be executed:\n\n");

		for (RenamableFile rf : files) {

			Path oldPath = rf.getSource().toPath();
			Path newPath = oldPath.resolveSibling(rf.getDestinationName());
			
			System.out.println("old  "+oldPath+"  new  "+newPath);
			//if (oldPath.equals(newPath)) continue;
			
			// renameCount++;
			confirmMsg.append("+ rename file ")
			.append(oldPath.getFileName())
			.append(" → ")
			.append(newPath.getFileName())
			.append("\n");
		}

		List<ActionTag> actions = actionManager.getActionTags();
		if (!actions.isEmpty()) {
			confirmMsg.append("\nAdditional actions:\n");
			for (ActionTag at : actions) {
				confirmMsg.append("• ")
				.append(at.getActionDescription())
				.append("\n");
			}
		}

		boolean ok = ActionConfirmationDialog.show(confirmMsg.toString());
		if (!ok) {
			return; // user cancelled
		}  

		//        ActionConfirmationDialog.show(sb.toString());
		List<RenameOperation> operations = new ArrayList<>();

		try {
			for (RenamableFile rf : files) {


				Path oldPath = rf.getSource().toPath();
				Path newPath = oldPath.resolveSibling(rf.getDestinationName());
				
				System.out.println("equals path  "+oldPath.normalize().toString().equals(newPath.normalize().toString()));
				
				String oldName = oldPath.getFileName().toString();
				String newName = newPath.getFileName().toString();

				boolean b = oldName.toLowerCase().equals(newName.toLowerCase())  && !oldName.equals(newName);
				
				System.out.println("equals name  "+b);
				// Su Windows serve rename intermedio
				Path tempPath = oldPath.resolveSibling(rf.getDestinationName() + ".tmp_rename");
				
			    // 1) rename → temporaneo
			    Files.move(oldPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
			    
			    // 2) rename → finale (case-sensitive)
			    Files.move(tempPath, newPath, StandardCopyOption.REPLACE_EXISTING);

				//Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
				rf.setFileStatus(FileStatus.OK);
				operations.add(new RenameOperation(oldPath, newPath));
			}

			performManager.reset();
			// Save batch for undo
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
	 * Restores all files from the most recent rename batch.
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
	 * Reverses a list of rename operations, in reverse order.
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
