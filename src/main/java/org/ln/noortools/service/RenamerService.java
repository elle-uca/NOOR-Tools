package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    
    // 🔑 Metodo per applicare una regola dal registry
    public void applyRule(String ruleName, Object... params) {
        RuleService service = ruleRegistry.get(ruleName.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Unknown rule: " + ruleName);
        }
        List<RenamableFile> updated = service.applyRule(files, params);
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
