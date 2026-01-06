package org.ln.noor.core.enums;

/**
 * Enumerates the padding strategies available to the application lifecycle.
 * The value determines how numeric fields are rendered without touching the filesystem.
 *
 * @author Luca Noale
 */
public enum FillOption {
	NO_FILL("Nessun riempimento"),
	FILL_TO_ZERO("Riempi con zeri"),
	FILL_TO_NUMBER("Riempi fino a");

	private final String displayName;


	private FillOption(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName; 
	}

//	public  static FillOption getByPref() {
//		return fromString(RnPrefs.getInstance().getGlobalProperty("FILL_TYPE"));
//
//	}
        /**
         * Resolves a {@link FillOption} from a user-facing string in a case-insensitive way,
         * falling back to {@link #NO_FILL} when the input does not match any constant.
         * This method does not interact with the filesystem.
         *
         * @param text the name of the constant to resolve (for example "FILL_TO_ZERO").
         * @param defaultType the value returned when {@code text} is null or invalid.
         * @return the resolved option or {@link #NO_FILL} when resolution fails.
         */
        public static FillOption fromString(String text) {
                if (text == null) {
                        return NO_FILL;
                }

                try {
                        // valueOf() is case-sensitive, so uppercase the text to accept mixed input.
                        return FillOption.valueOf(text.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                        // Default to NO_FILL when the input does not map to a known constant.
                        return NO_FILL;
                }
        }

}
