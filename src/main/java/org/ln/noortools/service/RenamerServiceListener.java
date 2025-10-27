package org.ln.noortools.service;

import org.ln.noortools.model.RenamableFile;

import java.util.List;

public interface RenamerServiceListener {
    void onFilesUpdated(List<RenamableFile> updatedFiles);
}
