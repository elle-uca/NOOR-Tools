package org.ln.noortools.service;

import java.util.List;

import org.ln.noortools.model.RenamableFile;

/**
 * RenamerServiceListener is an interface that defines a contract for components
 * interested in receiving notifications about changes in the list of files managed
 * by a renaming service (e.g., RenamerService).
 * * This is the 'Listener' part of the Observer pattern.
 * 
 * Author: Luca Noale
 */
public interface RenamerServiceListener {
    
    /**
     * Called by the Renamer Service whenever its internal list of files has been
     * updated (e.g., after loading new files, applying renaming rules, or removal).
     * * Implementers of this method (the 'Observers') should update their UI 
     * or internal state based on the new list of files.
     * * @param updatedFiles The complete, current list of RenamableFile objects, 
     * containing both original and new proposed names.
     */
    void onFilesUpdated(List<RenamableFile> updatedFiles);
}