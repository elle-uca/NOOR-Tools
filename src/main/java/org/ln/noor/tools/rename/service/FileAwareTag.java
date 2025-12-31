package org.ln.noor.tools.rename.service;

import java.util.List;

import org.ln.noor.tools.rename.model.RenamableFile;
/**
 * FileAwareTag.
 *
 * @author Luca Noale
 */

public interface FileAwareTag {
    
	void setFilesContext(List<RenamableFile> files);
}
