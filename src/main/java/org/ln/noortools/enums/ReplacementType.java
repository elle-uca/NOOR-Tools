package org.ln.noortools.enums;

/**
 * Enum to define the scope or extent of a text replacement operation within a string (e.g., a file name).
 * It determines whether only the first occurrence, the last occurrence, or all occurrences of 
 * a substring should be replaced.
 * 
 * Author: Luca Noale
 */
public enum ReplacementType {
    
    /** * Specifies that only the first occurrence of the target substring 
     * should be replaced.
     */
    FIRST,
    
    /** * Specifies that only the last occurrence of the target substring 
     * should be replaced.
     */
    LAST,
    
    /** * Specifies that all occurrences of the target substring 
     * should be replaced.
     */
    ALL
}