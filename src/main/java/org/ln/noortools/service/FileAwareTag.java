package org.ln.noortools.service;

import java.util.List;

import org.ln.noortools.model.RenamableFile;

public interface FileAwareTag {
    
	void setFilesContext(List<RenamableFile> files);
}