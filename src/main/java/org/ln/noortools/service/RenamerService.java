package org.ln.noortools.service;

import org.ln.noortools.model.RenamableFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenamerService {

    private final List<RenamableFile> files = new ArrayList<>();
    private final List<RenamerServiceListener> listeners = new ArrayList<>();

    public void setFiles(List<RenamableFile> newFiles) {
        files.clear();
        if (newFiles != null) {
            files.addAll(newFiles);
        }
        notifyListeners();
    }

    public List<RenamableFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public void addListener(RenamerServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RenamerServiceListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (RenamerServiceListener l : listeners) {
            l.onFilesUpdated(getFiles());
        }
    }
}
