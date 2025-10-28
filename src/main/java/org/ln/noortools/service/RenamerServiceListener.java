package org.ln.noortools.service;

import java.util.List;

import org.ln.noortools.model.RenamableFile;

public interface RenamerServiceListener {
    void onFilesUpdated(List<RenamableFile> updatedFiles);
}
