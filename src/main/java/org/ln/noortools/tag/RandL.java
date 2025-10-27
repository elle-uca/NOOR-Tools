package org.ln.noortools.tag;

import java.security.SecureRandom;

/**
 * Tag <RandL>
 *
 * Generates random strings consisting of uppercase letters (A–Z).
 *
 * - The first argument (optional): length of the random string (default = 5).
 * - The number of generated strings matches the size of the input list.
 *
 * Example:
 *   <RandL:3> → ["ABC", "ZQP", "LMN"]
 *
 * Author: Luca Noale
 */
public class RandL extends AbstractTag {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    public RandL(Object... arg) {
        super(arg);
        this.tagName = "RandL";
    }

    @Override
    public void init() {
        int length = getIntArg(0, 5); // default length = 5
        newClear();

        for (int i = 0; i < oldSize(); i++) {
            newAdd(generateRandomLetters(length));
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.randl.description") ;
    }

    /**
     * Generates a random uppercase string of the given length.
     *
     * @param length desired length (>0)
     * @return random string
     * @throws IllegalArgumentException if length <= 0
     */
    public static String generateRandomLetters(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
