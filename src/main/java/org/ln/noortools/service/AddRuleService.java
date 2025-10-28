package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileUtil;
import org.springframework.stereotype.Service;


/**
 * Rule service <Add>
 *
 * Adds a substring at the beginning, end, or after a given position
 * in each filename (without extension).
 *
 * Examples:
 *   <Add:Hello:START>  -> HelloFile
 *   <Add:World:END>    -> FileWorld
 *   <Add:123:3>        -> Fil123eName   (after 3rd character)
 *
 * Author: Luca Noale
 */
@Service("addruleservice")
public class AddRuleService implements RuleService {

	@Override
    public List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
        String text = (String) params[0];
        int position = (int) params[1];
        return applyRule(files, text, position);
    }

    public List<RenamableFile> applyRule(List<RenamableFile> files, String text, int position) {
        if (files == null || files.isEmpty()) return List.of();
        if (text == null) text = "";

        List<RenamableFile> updated = new ArrayList<RenamableFile>();

        for (RenamableFile file : files) {
            if (file == null) continue;

            String base = FileUtil.getNameWithoutExtension(file.getSource());
            String ext  = file.getExtension();
            String newName = insertAt(base, text, position);

            // 🔑 Copia l'oggetto per non rompere l’osservabilità
            RenamableFile copy = new RenamableFile(file.getSource());
            copy.setDestinationName(newName + (ext.isEmpty() ? "" : "." + ext));
            updated.add(copy);
        }

        return updated;
    }

    private String insertAt(String base, String text, int pos) {
        if (base == null) return text;
        if (text == null) text = "";

        // normalizza: l'utente parte da 1, Java da 0
        int index = pos - 1;

        if (index <= 0) return text + base;                  // inserisci all'inizio
        if (pos == Integer.MAX_VALUE) return base + text;    // convenzione per fine
        if (index >= base.length()) return base + text;      // oltre la lunghezza → append
        return base.substring(0, index) + text + base.substring(index);
    }
    
//    private String insertAt(String base, String text, int pos) {
//        if (base == null) return text;
//        if (pos <= 0) return text + base;
//        if (pos == Integer.MAX_VALUE) return base + text;
//        if (pos >= base.length()) return base + text;
//        return base.substring(0, pos) + text + base.substring(pos);
//    }
}
