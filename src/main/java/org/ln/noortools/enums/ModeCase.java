package org.ln.noortools.enums;

/**
 * Defines the available modes of case transformation for file renaming operations.
 * This enumeration provides a safe, type-checked way to specify how the case 
 * (upper/lower/title, etc.) of the file name should be modified.
 * 
 * Author: Luca Noale
 */
public enum ModeCase {
    
    /** Converts all characters in the file name to UPPERCASE (e.g., "file.txt" -> "FILE.TXT"). */
    UPPER,
    
    /** Converts all characters in the file name to lowercase (e.g., "File.Txt" -> "file.txt"). */
    LOWER,
    
    /** Converts the file name to Title Case, where the first letter of every significant word is capitalized 
     * (e.g., "my file name.txt" -> "My File Name.Txt"). 
     * The exact implementation often depends on treating spaces as word separators.
     */
    TITLE_CASE,
    
    /** Toggles the case of every character (e.g., "File.Txt" -> "fILE.tXT"). */
    TOGGLE_CASE,
    
    /** Capitalizes only the first character of the entire file name (e.g., "the file name.txt" -> "The file name.txt"). */
    CAPITALIZE_FIRST
}