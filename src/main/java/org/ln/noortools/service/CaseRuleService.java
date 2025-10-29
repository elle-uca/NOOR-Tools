package org.ln.noortools.service;

import org.ln.noortools.enums.ModeCase;
import org.ln.noortools.util.StringCaseUtil;
import org.springframework.stereotype.Service;

/**
 * Rule service <Case>
 *
 * Applies case transformations to filenames (without extension),
 * according to the selected {@link ModeCase}.
 *
 * Supported modes:
 * - UPPER: convert entire string to uppercase
 * - LOWER: convert entire string to lowercase
 * - TITLE_CASE: capitalize first letter of each word
 * - CAPITALIZE_FIRST: capitalize only the first letter of the string
 * - TOGGLE_CASE: invert the case of each letter
 *
 * Example:
 *   Input file: "myDocument.txt"
 *   Mode: UPPER
 *   Result: "MYDOCUMENT"
 *
 * Author: Luca Noale
 */
@Service("caseruleservice")
public class CaseRuleService extends AbstractRuleService {

	@Override
	protected String transformName(String base, Object... params) {
		ModeCase mode = (ModeCase) params[0];
		return StringCaseUtil.transformCase(base, mode);
	}
	


}
