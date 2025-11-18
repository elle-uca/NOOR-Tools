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
 * This class:
 *  - Applies renames to actual files on disk
 *  - Stores operations to allow UNDO
 *  - Notifies UI listeners when undo availability changes
 *
 * It does NOT decide *what* the new name should be — 
 * that logic belongs to RenamerService.
 * 
 *  Author: Luca Noale
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
    	
        // 🔥 1) PRIMA DI QUALSIASI RENAME → esegui azioni con conferma
       // actionManager.executeAllIfConfirmed();
        
        performManager.showConfirm();
        StringBuilder sb = new StringBuilder();
        sb.append("The following actions will be executed:\n\n");
//        for (RenamableFile rf : files) {
//        	 sb.append("+ file ")
//        	 .append(rf.getSource().getName())
//        	 .append(" renamed to  ")
//        	 .append(rf.getDestinationName())
//        	 .append("\n");
//        }
//        
//        ActionConfirmationDialog.show(sb.toString());
        List<RenameOperation> operations = new ArrayList<>();

//        try {
//            for (RenamableFile rf : files) {
//            	
//               // if (!rf.isSelected()) continue; // ❗ skip non selected
//            	
//                Path oldPath = rf.getSource().toPath();
//                Path newPath = oldPath.resolveSibling(rf.getDestinationName());
//
//                // If name did not change → skip
//                if (oldPath.equals(newPath)) continue;
//
//                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
//                rf.setFileStatus(FileStatus.OK);
//                operations.add(new RenameOperation(oldPath, newPath));
//            }
//
//            performManager.reset();
//            // Save batch for undo
//            history.push(operations);
//            notifyUndoStateChanged();
//
//        } catch (IOException e) {
//            rollback(operations);
//            throw e;
//        }
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
