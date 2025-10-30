package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.springframework.stereotype.Service;

@Service
public class RenamerService {

    
    private final List<RenamableFile> files = new ArrayList<>();
    private final List<RenamerServiceListener> listeners = new ArrayList<>();
    private final Map<String, RuleService> ruleRegistry = new HashMap<>();

    // ✅ Costruttore con injection di tutti i RuleService registrati come @Service
    public RenamerService(List<RuleService> ruleServices) {
        for (RuleService service : ruleServices) {
            String key = service.getClass().getSimpleName()
                    .replace("RuleService", "")
                    .toLowerCase();
            ruleRegistry.put(key, service);
            System.out.println("Registered rule: " + key + " -> " + service.getClass().getName());
        }
    }
   
    
    /**
     * Apply a rule to the current file list.
     *
     * @param ruleName rule identifier (e.g. "add", "remove", "case")
     * @param mode     rename mode (FULL, NAME_ONLY, EXT_ONLY)
     * @param params   rule-specific parameters
     */
    public void applyRule(String ruleName, RenameMode mode, Object... params) {
        RuleService service = ruleRegistry.get(ruleName.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Unknown rule: " + ruleName);
        }
        
//        // 🔑 Filtra i file selezionati
//        List<RenamableFile> activeFiles = new ArrayList<>();
//        List<RenamableFile> inactiveFiles = new ArrayList<>();
//
//        for (RenamableFile f : files) {
//            if (f.isSelected()) {
//                activeFiles.add(f);
//            } else {
//                inactiveFiles.add(f);
//            }
//        }
//          // 🔄 Applica la regola solo ai file attivi
//        List<RenamableFile> updated = service.applyRule(activeFiles, mode, params);
//       // ➕ Aggiungi quelli inattivi invariati
//        updated.addAll(inactiveFiles);
//        setFiles(updated);
        
        List<RenamableFile> updated = service.applyRule(files, mode, params);
        setFiles(updated);
    }
    


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
