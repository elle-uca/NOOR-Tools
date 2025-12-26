package org.ln.noor.tools.rename.service;

import java.util.List;

import org.ln.noor.tools.rename.model.RenamableFile;

public interface FileAwareTag {
    
	void setFilesContext(List<RenamableFile> files);
}