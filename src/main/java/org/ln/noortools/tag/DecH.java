package org.ln.noortools.tag;

/**
 * Tag <DecH>
 * 
 * Generates a sequence of hexadecimal numbers decreasing from a given decimal value.
 *
 * Example:
 *   <DecH:20> → [14, 13, 12, 11]  (decimal: 20 → hex: 14)
 *
 * Arguments:
 *   - First argument (optional): starting decimal value (default = 1).
 *   - Must be >= 1 (if lower, defaults to 1).
 *   - The number of values generated depends on the size of the input list.
 *
 * Output:
 *   - A list of uppercase hexadecimal strings (e.g., "A", "1F", "2A").
 *
 * Author: Luca Noale
 */
public class DecH extends AbstractTag {

    public DecH(Object... arg) {
        super(arg);
        this.tagName = "DecH";
    }

    @Override
    public void init() {
        // Ensure start >= 1
    	//Math.max(getIntArg(0, 1), 1);
        start = Math.max(getIntArg(0, 1), 1);
//        if (start < 1) {
//            start = 1;
//        }

        newClear();

        int current = start;
        for (int i = 0; i < oldSize(); i++) {
            String hex = Integer.toHexString(current).toUpperCase();
            newAdd(hex);
            current--; // decrement
            if (current < 1) break; // stop at 1 to avoid negative hex
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.dech.description");
    }
}
