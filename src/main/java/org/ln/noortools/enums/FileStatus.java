package org.ln.noortools.enums;

/**
 * FileStatus is an enumeration used to define the possible states of a file, 
 * typically used to signal the success or failure of a proposed file renaming operation.
 * It stores a user-friendly string representation (title) for each status.
 * 
 * Author: Luca Noale
 */
public enum FileStatus {
    
    /** Indicates the proposed name is valid and unique ("Ok"). */
    OK ("Ok"),  
    
    /** Indicates the proposed name already exists in the target folder (a specific conflict). */
    KO1 ("Esiste nella cartella"),
    
    /** Indicates the proposed name is duplicated among the currently processed files (a general conflict). */
    KO ("Nome duplicato");

    // --- Enum Fields and Constructor ---
    
    /** The user-friendly display string for the status. */
	private String title;
    
    /**
     * Private constructor used to initialize the title for each enum constant.
     * @param string The display string for the status.
     */
	FileStatus(String string) {
		this.title = string;
	}

    // --- Overrides ---

	/**
     * Overrides the default toString() method to return the user-friendly title
     * instead of the enum constant name (e.g., returns "Ok" instead of "OK").
     */
	@Override
	public String toString() {
		return title;
	}

}