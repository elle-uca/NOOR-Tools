package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.NumberSequenceUtil;

/**
 * Tag <DecN>
 * 
 * Generates a sequence of decreasing numbers starting from a given value.
 * 
 * - The first argument (optional): starting number (default = 1, can also be negative).
 * - The second argument (optional): step size (default = 1).
 * - The number of generated values matches the size of the input list.
 * 
 * Example:
 *   <DecN:10:2> → [10, 8, 6, 4, ...]
 * 
 * Author: Luca Noale
 */
public class DecN extends AbstractTag {

	/**
	 * @param arg
	 */
	public DecN(I18n i18n, Object...arg) {
		super(i18n, arg);
		this.tagName = "DecN";
	}

	@Override
	public String getDescription() {
		return i18n.get("tag.decn.description");
	}

	@Override
	public void init() {
		start = getIntArg(0, 1);
		step = getIntArg(1, 1);
		// Use utility to generate decreasing sequence
		setNewNames(NumberSequenceUtil.generate(
				start, step, oldSize(), false));
	}


}
