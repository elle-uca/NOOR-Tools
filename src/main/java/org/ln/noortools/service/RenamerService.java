package org.ln.noortools.service;

import java.io.File;
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

   // private final PanelFactory panelFactory;

    
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
    

    public void renameFiles() {
    	
    	
        System.out.println("=== Rename simulation ===");
        for (RenamableFile file : files) {
            System.out.printf(
                "Would rename: %s → %s%n",
                file.getSource().getName(),
                file.getDestinationName()
            );
        }
    }
    

	/**
	 * @param directory
	 * @param newNames
	 * @return
	 */
	public boolean checkConflicts2() {
		Map<RenamableFile, String> newNames = new HashMap<>();
		List<RenamableFile> list = getFiles();
		File directory = list.getFirst().getSource().getParentFile();
		for (RenamableFile rnFile : list) {
			newNames.put(rnFile, rnFile.getDestinationName());
		}
		
		
		
		File[] files = directory.listFiles();
		// Nomi già esistenti
		Set<String> existingNames = new HashSet<>();
		for (File f : files) {
			existingNames.add(f.getName());
		}

		// Controllo conflitti
		Set<String> usedNewNames = new HashSet<>();
		boolean conflictFound = false;

		for (Map.Entry<RenamableFile, String> entry : newNames.entrySet()) {
			File oldFile = entry.getKey().getSource();
			String newName = entry.getValue();

			// 1. conflitto con file esistenti (ma non se è lo stesso file)
			if (existingNames.contains(newName) && !oldFile.getName().equals(newName)) {
				entry.getKey().setFileStatus(FileStatus.KO);
				 System.out.println("❌ Conflitto: " + newName + " esiste già nella cartella.");
				conflictFound = true;
			}

			// 2. duplicati nella lista dei nuovi nomi
			if (!usedNewNames.add(newName)) {
				entry.getKey().setFileStatus(FileStatus.KO);
				 System.out.println("❌ Conflitto: il nuovo nome " + newName + " è duplicato.");
				conflictFound = true;
			}else {
				entry.getKey().setFileStatus(FileStatus.OK);
			}
		}
		return conflictFound;
	}
    
    public void setFiles(List<RenamableFile> newFiles) {
        files.clear();
        if (newFiles != null) {
            files.addAll(newFiles);
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
