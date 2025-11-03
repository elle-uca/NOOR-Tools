package org.ln.noortools.tag;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.i18n.I18n;

/**
 * Tag <IncL>
 * Generates incremental alphabetical labels.
 * Example: A, B, C ... Z, AA, AB ...
 *
 * Author: Luca Noale
 */
public class IncL extends AbstractTag {

    public IncL(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "IncL";
    }

    @Override
    public void init() {
    	start = Math.max(1, getIntArg(0, 1));
        setNewNames(generateLabelsFrom(toLabel(start), oldSize()));
    }

    @Override
    public String getDescription() {
    	i18n.get("tag.incl.description");
        return "An incremental string label";
    }

    /**
     * Converts a label like "ABJ" into a numeric index.
     * Example: A=1, B=2, ..., Z=26, AA=27, AB=28 ...
     *
     * @param label alphabetical label
     * @return numeric index
     */
    public static int fromLabel(String label) {
        int result = 0;
        for (int i = 0; i < label.length(); i++) {
            char c = label.charAt(i);
            result = result * 26 + (c - 'A' + 1);
        }
        return result;
    }

    /**
     * Converts a numeric index into an alphabetical label.
     * Example: 1=A, 2=B, ..., 26=Z, 27=AA ...
     *
     * @param index numeric index
     * @return alphabetical label
     */
    public static String toLabel(int index) {
        StringBuilder sb = new StringBuilder();
        while (index > 0) {
            index--;
            char c = (char) ('A' + (index % 26));
            sb.insert(0, c);
            index /= 26;
        }
        return sb.toString();
    }

    /**
     * Generates a sequence of labels starting from a given label.
     *
     * @param startLabel starting label
     * @param count      number of labels to generate
     * @return list of labels
     */
    public static List<String> generateLabelsFrom(String startLabel, int count) {
        List<String> result = new ArrayList<>();
        int startIndex = fromLabel(startLabel);

        for (int i = 0; i < count; i++) {
            String label = toLabel(startIndex + i);
            result.add(label);
        }
        return result;
    }
}
