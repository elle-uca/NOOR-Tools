package org.ln.noor.tools.rename.util;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

import org.ln.noor.core.app.SpringContext;
import org.ln.noor.core.preferences.PreferencesService;

public class NumberSequenceUtil {

    public static List<String> generate(int start, int step, int count, boolean ascending) {
        PreferencesService prefs = SpringContext.getBean(PreferencesService.class);
        return generate(start, step, count, ascending, prefs);
    }

    /**
     * Generates a padded numeric sequence using the provided preferences.
     * Extracted for testability so unit tests can supply a lightweight
     * {@link PreferencesService} implementation without the Spring context.
     */
    public static List<String> generate(
            int start,
            int step,
            int count,
            boolean ascending,
            PreferencesService preferences
    ) {
        Objects.requireNonNull(preferences, "preferences");

        List<String> result = new ArrayList<>();
        int curr = start;

        for (int i = 0; i < count; i++) {
            result.add(
                    ZeroPadder.padNumber(
                            curr,
                            preferences.getFillValue(),
                            preferences.getFillType()
                    )
            );
            curr = ascending ? curr + step : curr - step;
        }
        return result;
    }
}
