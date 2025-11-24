package org.ln.noortools.util;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.SpringContext;
import org.ln.noortools.preferences.PreferencesService;
import org.ln.noortools.util.ZeroPadder.FillOption;

public class NumberSequenceUtil {

    public static List<String> generate(int start, int step, int count, boolean ascending) {
        List<String> result = new ArrayList<>();
        int curr = start;
    	PreferencesService prefs = SpringContext.getBean(PreferencesService.class);

        for (int i = 0; i < count; i++) {
            result.add(
                ZeroPadder.padNumber(
                    curr,
                    prefs.getFillValue(),
                    FillOption.getByPref()
                )
            );
            curr = ascending ? curr + step : curr - step;
        }
        return result;
    }
}
