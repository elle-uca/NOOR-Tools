package org.ln.noor.service;

import java.util.List;

import org.ln.noor.core.model.RenamableFile;

public interface FileAwareTag {
    
	void setFilesContext(List<RenamableFile> files);
}