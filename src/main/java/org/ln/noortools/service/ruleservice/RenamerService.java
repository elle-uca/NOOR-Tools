package org.ln.noortools.service.ruleservice;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ln.noortools.enums.FileStatus;
import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerServiceListener;
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

        // 🔥 1) Applica la regola solo ai file selezionati
        List<RenamableFile> selectedFiles = new ArrayList<>();
        for (RenamableFile f : files) {
            if (f.isSelected()) {
                selectedFiles.add(f);
            }
        }

        if (selectedFiles.isEmpty()) {
            setFiles(new ArrayList<>(files));
            return;
        }

        List<RenamableFile> updatedFiles = service.applyRule(selectedFiles, mode, params);

        Map<Path, RenamableFile> updatedByPath = updatedFiles.stream()
                .collect(Collectors.toMap(
                        f -> f.getSource().toPath(),
                        Function.identity(),
                        (existing, replacement) -> replacement));

        // 3) Ricombina preservando l'ordine originale
        List<RenamableFile> merged = new ArrayList<>(files.size());
        for (RenamableFile current : files) {
            RenamableFile replacement = updatedByPath.get(current.getSource().toPath());
            merged.add(replacement != null ? replacement : current);
        }

        // 4) Aggiorna modello
        setFiles(merged);
    }

    
    public void setFiles(List<RenamableFile> newFiles) {
    	
    	
    	// sostituisci solo gli oggetti, non resettiamo la selection
    	Map<Path, Boolean> prevSelection = files.stream()
    	        .collect(Collectors.toMap(f -> f.getSource().toPath(), RenamableFile::isSelected));

    	files.clear();
    	files.addAll(newFiles);

    	// ripristina selection precedente
    	for (RenamableFile f : files) {
    	    f.setSelected(prevSelection.getOrDefault(f.getSource().toPath(), true));
    	}
    	
//         // mappa selezioni correnti per path
//        Map<Path, Boolean> selectedByPath = new HashMap<>();
//        for (RenamableFile f : this.files) {
//            selectedByPath.put(f.getSource().toPath(), f.isSelected());
//        }
//
//        files.clear();
//        if (newFiles != null) {
//            files.addAll(newFiles);
//        }
//
//        // re-applica la selezione pre-esistente
//        for (RenamableFile f : files) {
//            Boolean sel = selectedByPath.get(f.getSource().toPath());
//            if (sel != null) f.setSelected(sel);
//        }

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
        	
            if (!f.isSelected()) {
                f.setFileStatus(FileStatus.OK); // o NONE, se aggiungi un enum
                continue;
            }
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
    
    public void updateDestinationNames(List<RenamableFile> updated) {
        for (int i = 0; i < files.size(); i++) {
            files.get(i).setDestinationName(updated.get(i).getDestinationName());
        }
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
