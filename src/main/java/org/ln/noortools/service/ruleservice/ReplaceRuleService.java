package org.ln.noortools.service.ruleservice;

import org.ln.noortools.enums.ReplacementType;
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
 * * Works with three modes:
 *  - FULL      → replace in full filename (name+ext)
 *  - NAME_ONLY → replace in name only
 *  - EXT_ONLY  → replace in extension only
 *  
 *   * Examples:
 *   NAME_ONLY: "file.txt" + replace("i","X")  -> "fXle.txt"
 *   EXT_ONLY:  "file.txt" + replace("t","X")  -> "file.xXx"
 *   FULL:      "file.txt" + replace("l","X")  -> "fiXe.txX"
 * 
 *  Author: Luca Noale
 */
@Service("replaceruleservice")
public class ReplaceRuleService extends AbstractRuleService {
	
    @Override
    protected String transformName(String base, Object... params) {
        return replace(base, params);
    }

    @Override
    protected String transformExtension(String ext, Object... params) {
        return replace(ext, params);
    }

    @Override
    protected String transformFullName(String full, Object... params) {
        return replace(full, params);
    }
	
    
    private String replace(String input, Object... params) {
        if (input == null) return null;

        String target = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
        String replacement = (params.length > 1 && params[1] instanceof String) ? (String) params[1] : "";
        ReplacementType type = (params.length > 2 && params[2] instanceof ReplacementType)
                ? (ReplacementType) params[2]
                : ReplacementType.ALL;

        if (target.isEmpty()) return input;

        return switch (type) {
            case FIRST -> input.replaceFirst(target, replacement);
            case LAST -> {
                int index = input.lastIndexOf(target);
                if (index == -1) yield input;
                yield input.substring(0, index) + replacement + input.substring(index + target.length());
            }
            case ALL -> input.replace(target, replacement);
        };
    }

//	private String replace(String input, Object... params) {
//        if (input == null) return null;
//        String target = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
//        String replacement = (params.length > 1 && params[1] instanceof String) ? (String) params[1] : "";
//        ReplacementType type = (params.length > 2 && params[2] instanceof ReplacementType)
//                ? (ReplacementType) params[2] : ReplacementType.ALL;
//
//        if (target.isEmpty()) return input;
//
//        return switch (type) {
//            case FIRST -> input.replaceFirst(target, replacement);
//            case LAST -> {
//                int idx = input.lastIndexOf(target);
//                if (idx == -1) yield input;
//                yield input.substring(0, idx) + replacement + input.substring(idx + target.length());
//            }
//            case ALL -> input.replace(target, replacement);
//        };
//    }
    
    
//    protected String transformName(String base, Object... params) {
//        if (params.length < 2) return base;
//
//        String search  = (String) params[0];
//        String replace = (String) params[1];
//        ReplacementType type = (params.length > 2 && params[2] instanceof ReplacementType)
//                ? (ReplacementType) params[2]
//                : ReplacementType.ALL;
//        boolean caseSensitive = (params.length > 3 && params[3] instanceof Boolean)
//                ? (Boolean) params[3]
//                : true;
//
//        return replaceText(base, search, replace, type, caseSensitive);
//    }


//	
//    private String replaceText(String base, String search, String replace,
//                               ReplacementType type, boolean caseSensitive) {
//        if (base == null || search == null || search.isEmpty()) return base;
//
//        String target = caseSensitive ? base : base.toLowerCase();
//        String needle = caseSensitive ? search : search.toLowerCase();
//
//        switch (type) {
//            case FIRST:
//                int firstIdx = target.indexOf(needle);
//                if (firstIdx >= 0) {
//                    return base.substring(0, firstIdx) + replace + base.substring(firstIdx + search.length());
//                }
//                break;
//            case LAST:
//                int lastIdx = target.lastIndexOf(needle);
//                if (lastIdx >= 0) {
//                    return base.substring(0, lastIdx) + replace + base.substring(lastIdx + search.length());
//                }
//                break;
//            case ALL:
//            default:
//                if (!caseSensitive) {
//                    // naive approach: regex with case-insensitivity
//                    return base.replaceAll("(?i)" + java.util.regex.Pattern.quote(search),
//                            java.util.regex.Matcher.quoteReplacement(replace));
//                } else {
//                    return base.replace(search, replace);
//                }
//        }
//        return base;
//    }
	

//    private String replaceText(String input, String search, String replace,
//                               ReplacementType type, boolean caseSensitive) {
//        if (input == null || search == null || search.isEmpty()) return input;
//
//        String src = caseSensitive ? input : input.toLowerCase();
//        String token = caseSensitive ? search : search.toLowerCase();
//
//        return switch (type) {
//            case FIRST -> {
//                int idx = src.indexOf(token);
//                yield (idx >= 0)
//                        ? input.substring(0, idx) + replace + input.substring(idx + search.length())
//                        : input;
//            }
//            case LAST -> {
//                int last = src.lastIndexOf(token);
//                yield (last >= 0)
//                        ? input.substring(0, last) + replace + input.substring(last + search.length())
//                        : input;
//            }
//            case ALL -> input.replaceAll("(?i)" + java.util.regex.Pattern.quote(search), replace);
//        };
//    }
	
	

}
