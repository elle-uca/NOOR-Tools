package org.ln.noortools.service;

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
    
    
    // ✅ overload tipizzato
    public List<RenamableFile> applyRule(List<RenamableFile> files, String text, int position) {
        if (files == null) return List.of();

        for (RenamableFile file : files) {
            String base = FileUtil.getNameWithoutExtension(file.getSource());
            String newName = insertAt(base, text, position);
            file.setDestinationName(newName);
            System.out.println("applyRule su " + files.size() + " file con text=" + text);
        }
        return files;
    }

    private String insertAt(String base, String text, int pos) {
        if (base == null) return text;
        if (pos <= 0) return text + base;
        if (pos >= base.length()) return base + text;
        return base.substring(0, pos) + text + base.substring(pos);
    }


	


}
