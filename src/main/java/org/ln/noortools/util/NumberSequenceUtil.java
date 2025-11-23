package org.ln.noortools.util;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.preferences.PreferencesService;
import org.ln.noortools.util.ZeroPadder.FillOption;

public class NumberSequenceUtil {

    public static List<String> generate(int start, int step, int count, boolean ascending) {
        List<String> result = new ArrayList<>();
        int curr = start;

        for (int i = 0; i < count; i++) {
            result.add(
                ZeroPadder.padNumber(
                    curr,
                    Integer.parseInt(PreferencesService.getProp("FILL_VALUE", "0")),
                    FillOption.getByPref()
                )
            );
            curr = ascending ? curr + step : curr - step;
        }
        return result;
    }
}
