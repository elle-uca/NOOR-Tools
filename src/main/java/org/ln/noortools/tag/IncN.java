package org.ln.noortools.tag;

import org.ln.noortools.util.NumberSequenceUtil;


/**
 * Tag <IncN>
 * Generates an incremental number sequence.
 * - First parameter = start number (can be negative)
 * - Second parameter (optional) = step (default = 1)
 * 
 * Example: <IncN:1:2> → 1, 3, 5, 7...
 * 
 * Author: Luca Noale
 */
public class IncN extends AbstractTag {
	
	/**
	 * @param arg
	 */
	public IncN(Object...arg) {
		super(arg);
		this.tagName = "IncN";
	}


	@Override
	public void init() {
		start = getIntArg(0, 1);
		step = getIntArg(1, 1);
		setNewNames(NumberSequenceUtil.generate(
				start, step, oldSize(), true));
	}

	@Override
	public String getDescription() {
		return i18n.get("tag.incn.description");
	}


}
