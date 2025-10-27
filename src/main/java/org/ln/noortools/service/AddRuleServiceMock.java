package org.ln.noortools.service;

import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock semplice per test GUI dell'AddPanel.
 * Applica una regola di aggiunta testo senza cancellare mai i file dalla lista.
 */
public class AddRuleServiceMock extends AddRuleService {
	
	@Override
	public List<RenamableFile> applyRule(List<RenamableFile> names, Object... params) {
		 String text = (String) params[0];
	        int position = (int) params[1];
	        return applyRule(names, text, position);
	}

    
    public List<RenamableFile> applyRule(List<RenamableFile> files, String textToAdd, int position) {
        if (files == null || files.isEmpty()) return List.of();
        if (textToAdd == null) textToAdd = "";

        List<RenamableFile> updated = new ArrayList<>();

        for (RenamableFile f : files) {
            if (f == null) continue;

            String baseName = FileUtil.getNameWithoutExtension(f.getSource());
            String ext = f.getExtension();

            String newName;
            if (position == Integer.MAX_VALUE) { // fine
                newName = baseName + textToAdd;
            } else if (position <= 1) { // inizio
                newName = textToAdd + baseName;
            } else { // posizione
                int pos = Math.min(position - 1, baseName.length());
                newName = baseName.substring(0, pos) + textToAdd + baseName.substring(pos);
            }

            // Copia file aggiornato
            RenamableFile copy = new RenamableFile(f.getSource());
            copy.setDestinationName(newName + (ext.isEmpty() ? "" : "." + ext));
            updated.add(copy);
        }
        return updated;
    }


}
