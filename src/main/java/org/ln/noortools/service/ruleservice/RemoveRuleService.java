package org.ln.noortools.service.ruleservice;

import org.springframework.stereotype.Service;

/**
 * Service that removes a given number of characters from a string,
 * starting at the specified position (1-based index).
 *
  * Examples:
 *   NAME_ONLY:
 *     "filename", position=2, count=3 → "fname" (removed "ile")
 *   EXT_ONLY:
 *     "file.txt", position=2, count=1 → "file.tt" (removed "x" in extension)
 *   FULL:
 *     "file.txt", position=5, count=3 → "filet"   (removed ".tx")
 *   
 *    Author: Luca Noale
 */
@Service("removeruleservice")
public class RemoveRuleService extends AbstractRuleService {

	   @Override
	    protected String transformName(String base, Object... params) {
	        return remove(base, params);
	    }

	    @Override
	    protected String transformExtension(String currentExt, Object... params) {
	        return remove(currentExt, params);
	    }

	    @Override
	    protected String transformFullName(String fullName, Object... params) {
	        return remove(fullName, params);
	    }

	    private String remove(String input, Object... params) {
	        int position = (params.length > 0 && params[0] instanceof Integer) ? (int) params[0] : 1;
	        int length   = (params.length > 1 && params[1] instanceof Integer) ? (int) params[1] : 1;

	        if (input == null || input.isEmpty()) return input;
	        if (length <= 0) return input;

	        int index = Math.max(0, position - 1);
	        if (index >= input.length()) return input; // nothing to remove

	        int endIndex = Math.min(input.length(), index + length);
	        return input.substring(0, index) + input.substring(endIndex);
	    }




}
