package org.ln.noortools.tag;

import org.ln.noortools.util.RomanNumberUtil;

/**
 * Tag <IncR>
 *
 * Generates a sequence of Roman numerals starting from a given value, 
 * incrementing by a specified step.
 *
 * - The first argument (optional): starting number (default = 1, must be > 0).
 * - The second argument (optional): increment step (default = 1, must be > 0).
 * - The number of generated values matches the size of the input list.
 *
 * Example:
 *   <IncR:1:2> → [I, III, V, VII, IX...]
 *
 * Author: Luca Noale
 */
public class IncR extends AbstractTag {

    public IncR(Object... arg) {
        super(arg);
        this.tagName = "IncR";
    }

    @Override
    public void init() {
        start = Math.max(getIntArg(0, 1), 1);
        step  = Math.max(getIntArg(1, 1), 1);

        newClear();
        int incr = start;

        for (int i = 0; i < oldSize(); i++) {
            newAdd(RomanNumberUtil.intToRoman(incr));
            incr += step;
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.incr.description");
    }

}
