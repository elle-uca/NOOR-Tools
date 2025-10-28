package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ln.noortools.model.RenamableFile;
import org.springframework.stereotype.Service;

@Service
public class RenamerServiceOld {

    private final List<RenamableFile> files = new ArrayList<>();
    private final List<RenamerServiceListener> listeners = new ArrayList<>();

    public List<RenamableFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public void setFiles(List<RenamableFile> newFiles) {
        files.clear();
        if (newFiles != null) {
            files.addAll(newFiles);
        }
        notifyListeners();
    }

    public void addFile(RenamableFile file) {
        if (file != null) {
            files.add(file);
            notifyListeners();
        }
    }

    public void addFiles(List<RenamableFile> newFiles) {
        if (newFiles != null) {
            files.addAll(newFiles);
            notifyListeners();
        }
    }

    public void clearFiles() {
        files.clear();
        notifyListeners();
    }

    public void addListener(RenamerServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RenamerServiceListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (RenamerServiceListener listener : listeners) {
            listener.onFilesUpdated(getFiles());
        }
    }
}
