package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.RomanNumberUtil;

/**
 * Tag <DecR>
 *
 * Generates a sequence of Roman numerals decreasing from a given starting value,
 * with a specified step.
 *
 * - The first argument (optional): starting number (default = 10, must be > 0).
 * - The second argument (optional): decrement step (default = 1, must be > 0).
 * - The number of generated values matches the size of the input list.
 *
 * Example:
 *   <DecR:10:2> → [X, VIII, VI, IV, II...]
 *
 * Author: Luca Noale
 */
public class DecR extends AbstractTag {

    public DecR(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "DecR";
    }

    @Override
    
    public void init() {
        start = Math.max(getIntArg(0, 10), 1);
        step  = Math.max(getIntArg(1, 1), 1);

        newClear();
        int current = start;

        for (int i = 0; i < oldSize(); i++) {
            if (current <= 0) {
                newAdd("N/A"); // Romans had no zero or negatives
            } else {
                newAdd(RomanNumberUtil.intToRoman(current));
            }
            current -= step;
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.decr.description") ;
    }

}
