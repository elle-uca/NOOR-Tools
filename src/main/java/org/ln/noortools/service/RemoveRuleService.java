package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileUtil;
import org.springframework.stereotype.Service;

/**
 * Service that removes a given number of characters from a string,
 * starting at the specified position (1-based index).
 *
 * Example:
 *   input = ["filename"], position = 2, count = 3
 *   → ["fname"]   (removed "ile")
 *   
 *    Author: Luca Noale
 */
@Service("removeruleservice")
public class RemoveRuleService  implements RuleService {

    /**
     * Applies the remove rule to a list of names.
     *
     * @param names     list of original names (nulls preserved)
     * @param position  1-based start position (if <=0 → treated as 1)
     * @param count     number of characters to remove (if <=0 → no change)
     * @return list of transformed names
     */
//    public List<String> apply(List<String> names, int position, int count) {
//        List<String> result = new ArrayList<>();
//        if (names == null) {
//            return result;
//        }
//
//        for (String name : names) {
//            if (name == null) {
//                result.add(null);
//                continue;
//            }
//
//            // Normalize values
//            int startPos = Math.max(position, 1) - 1; // convert to 0-based
//            int charsToRemove = Math.max(count, 0);
//
//            if (startPos >= name.length() || charsToRemove == 0) {
//                result.add(name); // nothing removed
//            } else {
//                int endPos = Math.min(startPos + charsToRemove, name.length());
//                String newName = name.substring(0, startPos) + name.substring(endPos);
//                result.add(newName);
//            }
//        }
//
//        return result;
//    }

    @Override
    public List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
        int position = (int) params[0];
        int length = (int) params[1];
        return applyRule(files, position, length);
    }

    // ✅ overload tipizzato
    public List<RenamableFile> applyRule(List<RenamableFile> files, int position, int length) {
        if (files == null) return List.of();
        List<RenamableFile> updated = new ArrayList<RenamableFile>();

        for (RenamableFile file : files) {
            String base = FileUtil.getNameWithoutExtension(file.getSource());
            String newName = removeSubstring(base, position, length);
            // 🔑 Copia l'oggetto per non rompere l’osservabilità
            RenamableFile copy = new RenamableFile(file.getSource());
            copy.setDestinationName(newName);
            updated.add(copy);
        }
        return updated;
    }

    private String removeSubstring(String base, int pos, int len) {
        if (base == null || base.isEmpty() || len <= 0) return base;
        if (pos < 0) pos = 0;
        if (pos >= base.length()) return base;

        int end = Math.min(pos + len, base.length());
        return base.substring(0, pos) + base.substring(end);
    }

    
}
