package org.ln.noortools.service;

import java.util.List;

import org.ln.noortools.enums.ReplacementType;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileUtil;
import org.springframework.stereotype.Service;

/**
 * Service for applying "Replace" rules on file names.
 *
 * Supports replacing substrings with options:
 *  - FIRST: only the first occurrence
 *  - LAST: only the last occurrence
 *  - ALL: all occurrences
 *
 * Optionally case-sensitive or case-insensitive.
 * 
 *  Author: Luca Noale
 */
@Service("replaceruleservice")
public class ReplaceRuleService implements RuleService {
	
    @Override
    public List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
        String search = (String) params[0];
        String replace = (String) params[1];
        ReplacementType type = (ReplacementType) params[2];
        boolean caseSensitive = (boolean) params[3];
        return applyRule(files, search, replace, type, caseSensitive);
    }

    // ✅ overload tipizzato
    public List<RenamableFile> applyRule(List<RenamableFile> files,
                                         String search,
                                         String replace,
                                         ReplacementType type,
                                         boolean caseSensitive) {
        if (files == null) return List.of();

        for (RenamableFile file : files) {
            String base = FileUtil.getNameWithoutExtension(file.getSource());
            String newName = replaceText(base, search, replace, type, caseSensitive);
            file.setDestinationName(newName);
        }
        return files;
    }

    private String replaceText(String input, String search, String replace,
                               ReplacementType type, boolean caseSensitive) {
        if (input == null || search == null || search.isEmpty()) return input;

        String src = caseSensitive ? input : input.toLowerCase();
        String token = caseSensitive ? search : search.toLowerCase();

        return switch (type) {
            case FIRST -> {
                int idx = src.indexOf(token);
                yield (idx >= 0)
                        ? input.substring(0, idx) + replace + input.substring(idx + search.length())
                        : input;
            }
            case LAST -> {
                int last = src.lastIndexOf(token);
                yield (last >= 0)
                        ? input.substring(0, last) + replace + input.substring(last + search.length())
                        : input;
            }
            case ALL -> input.replaceAll("(?i)" + java.util.regex.Pattern.quote(search), replace);
        };
    }
	
	

}
