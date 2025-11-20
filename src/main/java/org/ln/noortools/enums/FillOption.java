package org.ln.noortools.enums;

/**
 * An enumeration to define the type of padding to apply.
 *
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
	 * Questo metodo è case-insensitive (non fa distinzione tra maiuscole e minuscole)
	 * e restituisce un valore di default se la stringa non corrisponde a nessuna costante.
	 *
	 * @param text Il nome della costante da cercare (es. "TOTAL_DIGITS").
	 * @param defaultType Il valore da restituire se 'text' non è valido o è nullo.
	 * @return Il PaddingType corrispondente o il valore di default.
	 */
	public static FillOption fromString(String text) {
		if (text == null) {
			return NO_FILL;
		}

		try {
			// valueOf() cerca una corrispondenza esatta (case-sensitive)
			// quindi convertiamo il testo in maiuscolo per renderlo flessibile.
			return FillOption.valueOf(text.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			// Se la stringa non corrisponde a nessuna costante dell'enum...
			return NO_FILL;
		}
	} 

}
