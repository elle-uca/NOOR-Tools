package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.i18n.I18n;
import org.ln.noor.util.NumberSequenceUtil;


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
public class IncrNum extends AbstractTag {
	
    public IncrNum(I18n i18n, Object...arg) {
        super(i18n, arg);
        this.tagName = "IncrNum";
        this.type = TagType.NUMERIC;
    }

    @Override
    public void init() {
        start = getIntArg(0, 1);
        step = getIntArg(1, 1);
        setNewNames(NumberSequenceUtil.generate(start, step, oldSize(), true));
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.incn.description");
    }


}
