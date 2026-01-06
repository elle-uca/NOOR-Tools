package org.ln.noor.tools.rename.util;

import org.ln.noor.core.enums.FillOption;
/**
 * Formats numeric values with configurable zero padding for rename preview output.
 * The utility only operates on provided integers and never interacts with the filesystem.
 *
 * @author Luca Noale
 */

public class ZeroPadder {

        /**
         * Formats an integer by adding leading zeros or returns it
         * as a plain string, based on the specified mode.
         * This method runs entirely in memory and does not touch the filesystem.
         *
         * @param number The number to format.
         * @param value The total final length or the number of zeros to add.
         * This parameter is ignored if the type is NO_FILL.
         * @param type The type of formatting to apply.
         * @return A string of the number with the requested formatting.
         */
        public static String padNumber(int number, int value, FillOption type) {

                // Guard clause keeps the no-fill path fast.
                if (type == FillOption.NO_FILL) {
                        return String.valueOf(number);
                }

                // Validate padding to avoid silent truncation or negative widths.
                if (value < 0) {
                        throw new IllegalArgumentException("The padding value cannot be negative.");
                }

                // Switch determines the intended total length without branching elsewhere.
                int totalLength = switch (type) {
                // 'value' represents the total final length directly.
                case FILL_TO_NUMBER -> value;

                // 'value' represents the number of zeros to add.
                // We calculate the total length by adding the zeros to the number's length.
                case FILL_TO_ZERO -> String.valueOf(number).length() + value;

                // Handles any other undefined FillOption enum constants
                default -> throw new IllegalStateException("Unsupported padding type: " + type);
                };

                // String.format() correctly handles cases where the number is already
                // longer than the totalLength (it won't truncate).
                String formatSpecifier = "%0" + totalLength + "d";
                return String.format(formatSpecifier, number);
        }

}
