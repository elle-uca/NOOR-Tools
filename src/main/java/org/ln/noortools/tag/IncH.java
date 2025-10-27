package org.ln.noortools.tag;

/**
 * Tag <IncH>
 * 
 * Generates a sequence of hexadecimal numbers starting from a given decimal value.
 * 
 * Example:
 *   <IncH:5> → [5, 6, 7, ...] but in HEX → [5, 6, 7, 8, 9, A, B, ...]
 * 
 * Arguments:
 *   - First argument (optional): starting decimal value (default = 1).
 *   - The number of values generated depends on the size of the input list.
 * 
 * Output:
 *   - A list of uppercase hexadecimal strings (e.g., "A", "1F", "2A").
 *   
 *    Author: Luca Noale
 */
public class IncH extends AbstractTag {

    public IncH(Object... arg) {
        super(arg);
        this.tagName = "IncH";
    }

    @Override
    public void init() {
    	start = getIntArg(0, 1) > 0 ? getIntArg(0, 1) : 1;
        newClear();              // reset result list

        for (int i = 0; i < oldSize(); i++) {
            int currentNumber = start + i;
            String hex = Integer.toHexString(currentNumber).toUpperCase();
            newAdd(hex);
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.inch.description");
    }
}

