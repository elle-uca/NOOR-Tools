package org.ln.noortools.enums;

/**
 * Defines the available modes for applying a renaming operation to a file.
 * A file name is typically composed of a base name and an extension (e.g., "document.pdf").
 * This enum specifies which parts of the file name should be affected by the renaming process.
 * 
 * Author: Luca Noale
 */
public enum RenameMode {
    
//    /** * Renames the entire file name: both the base name and the extension.
//     * (e.g., "old_file.txt" -> "new_document.pdf")
//     */
//    FULL,        
    
    /** * Renames only the base name of the file, keeping the existing extension unchanged.
     * (e.g., "old_file.txt" -> "new_document.txt")
     */
    NAME_ONLY,   
    
    /** * Renames only the extension of the file, keeping the base name unchanged.
     * (e.g., "document.pdf" -> "document.doc")
     */
    EXT_ONLY     
}