package org.ln.noortools.service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ln.noortools.enums.FileStatus;
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
       // this.panelFactory = panelFactory;
    }
   
    
    /**
     * Apply a rule to the current file list.
     *
     * @param ruleName rule identifier (e.g. "add", "remove", "case")
     * @param mode     rename mode (FULL, NAME_ONLY, EXT_ONLY)
     * @param params   rule-specific parameters
     */
//    public void applyRule(String ruleName, RenameMode mode, Object... params) {
//        RuleService service = ruleRegistry.get(ruleName.toLowerCase());
//        if (service == null) {
//            throw new IllegalArgumentException("Unknown rule: " + ruleName);
//        }
//
//   
//         List<RenamableFile> activeFiles = new ArrayList<>();
//        List<RenamableFile> inactiveFiles = new ArrayList<>();
//
//        // Separate active/inactive
//        for (RenamableFile f : files) {
//            if (f.isSelected()) activeFiles.add(f);
//            else inactiveFiles.add(f);
//        }
//
//        // Apply rule only to selected files
//        List<RenamableFile> updatedActive = service.applyRule(activeFiles, mode, params);
//
//        // Merge back (inactive unchanged)
//        List<RenamableFile> result = new ArrayList<>();
//        result.addAll(updatedActive);
//        result.addAll(inactiveFiles);
//
//        setFiles(result);
//    }
   
    public void applyRule(String ruleName, RenameMode mode, Object... params) {
        RuleService service = ruleRegistry.get(ruleName.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Unknown rule: " + ruleName);
        }

        // 🔥 1) Salva lo stato dei selected
        Map<String, Boolean> selectionMap = new HashMap<>();
        for (RenamableFile f : files) {
            selectionMap.put(f.getSource().getAbsolutePath(), f.isSelected());
        }

        // 2) Applica la regola SOLO ai file selezionati
        List<RenamableFile> active = new ArrayList<>();
        List<RenamableFile> inactive = new ArrayList<>();

        for (RenamableFile f : files) {
            if (f.isSelected()) active.add(f);
            else inactive.add(f);
        }

        List<RenamableFile> updatedActive = service.applyRule(active, mode, params);

        // 3) Ricombina attivi + inattivi
        List<RenamableFile> result = new ArrayList<>();
        result.addAll(updatedActive);
        result.addAll(inactive);

        // 🔥 4) Ripristina il flag selected su ogni file
        for (RenamableFile f : result) {
            Boolean sel = selectionMap.get(f.getSource().getAbsolutePath());
            if (sel != null) {
                f.setSelected(sel);
            }
        }

        // 5) Aggiorna modello
        setFiles(result);
    }

    
    public void setFiles(List<RenamableFile> newFiles) {
        // mappa selezioni correnti per path
        Map<Path, Boolean> selectedByPath = new HashMap<>();
        for (RenamableFile f : this.files) {
            selectedByPath.put(f.getSource().toPath(), f.isSelected());
        }

        files.clear();
        if (newFiles != null) {
            files.addAll(newFiles);
        }

        // re-applica la selezione pre-esistente
        for (RenamableFile f : files) {
            Boolean sel = selectedByPath.get(f.getSource().toPath());
            if (sel != null) f.setSelected(sel);
        }

        checkConflicts();
        notifyListeners();
    }
    
    public boolean checkConflicts() {
        if (files.isEmpty()) return false;

        File directory = files.getFirst().getSource().getParentFile();
        Set<String> existingNames = new HashSet<>();
        for (File f : directory.listFiles()) {
            existingNames.add(f.getName());
        }

        Set<String> used = new HashSet<>();
        boolean conflict = false;

        for (RenamableFile f : files) {
            String oldName = f.getSource().getName();
            String newName = f.getDestinationName();

            // ✅ SE NUOVO NOME NON ANCORA CALCOLATO → USA IL VECCHIO
            if (newName == null || newName.isBlank()) {
                newName = oldName;
                f.setDestinationName(newName); // opzionale ma utile
            }
            // Reset predefinito
            f.setFileStatus(FileStatus.OK);

            // 1) Conflitto con file esistenti (se non è lo stesso file)
            if (existingNames.contains(newName) && !newName.equals(oldName)) {
                f.setFileStatus(FileStatus.KO);
                conflict = true;
                continue;
            }

            // 2) Conflitto tra destinazioni duplicate
            if (!used.add(newName)) {
                f.setFileStatus(FileStatus.KO);
                conflict = true;
            }
        }
        return conflict;
    }

    public void reloadDirectory(Path directory) {
        if (directory == null) return;
        File dirFile = directory.toFile();
        if (!dirFile.isDirectory()) return;

        List<RenamableFile> reloaded = new ArrayList<>();

        File[] list = dirFile.listFiles();
        if (list != null) {
            for (File f : list) {
                // Ricostruisci RenamableFile usando lo stato attuale come base
                RenamableFile newFile = new RenamableFile(f);

                // Mantieni la destinazione se già calcolata in precedenza
                // altrimenti copia il nome sorgente
                newFile.setDestinationName(f.getName());

                reloaded.add(newFile);
            }
        }

        // 🔄 Sostituisci l’elenco attuale
        files.clear();
        files.addAll(reloaded);

        // ✅ Ricalcola conflitti + aggiorna UI
        checkConflicts();
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
