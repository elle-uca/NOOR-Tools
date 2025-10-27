package org.ln.noortools.tag;

import java.security.SecureRandom;

/**
 * Tag <RandN>
 *
 * Generates random numbers with a fixed number of digits.
 *
 * - The first argument (optional): number of digits (default = 1).
 * - The number of generated values matches the size of the input list.
 *
 * Example:
 *   <RandN:3> → ["123", "045", "678"]
 *
 * Author: Luca Noale
 */
public class RandN extends AbstractTag {

    private static final SecureRandom RANDOM = new SecureRandom();

    public RandN(Object... arg) {
        super(arg);
        this.tagName = "RandN";
    }

    @Override
    public void init() {
        int digits = Math.max(getIntArg(0, 1), 1); // default = 1, must be >= 1
        newClear();

        for (int i = 0; i < oldSize(); i++) {
            newAdd(String.valueOf(generateRandomNumber(digits)));
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.randn.description") ;
    }

    /**
     * Generates a random number with a fixed number of digits.
     *
     * @param length number of digits (1–9)
     * @return random integer with the required number of digits
     * @throws IllegalArgumentException if length < 1 or > 9
     */
    public static int generateRandomNumber(int length) {
        if (length <= 0 || length > 9) {
            throw new IllegalArgumentException("Length must be between 1 and 9.");
        }

        int lowerBound = (int) Math.pow(10, length - 1);
        int upperBound = (int) Math.pow(10, length) - 1;

        // Special case for 1-digit numbers (0–9)
        if (length == 1) {
            return RANDOM.nextInt(10);
        }

        return RANDOM.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }
}
