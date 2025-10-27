package org.ln.noortools.util;

import org.ln.noortools.prefs.Prefs;

public class ZeroPadder {

//    /**
//     * Aggiunge un numero specifico di zeri iniziali a un numero intero.
//     *
//     * @param number Il numero da formattare.
//     * @param zeroCount Il numero di zeri da anteporre.
//     * @return Una stringa rappresentante il numero con gli zeri iniziali.
//     */
//    public static String padNumberWithZeros(int number, int zeroCount) {
//        // Converte il numero in stringa per calcolarne la lunghezza attuale
//        String numberStr = String.valueOf(number);
//        
//        // Calcola la lunghezza finale che la stringa dovrà avere
//        int totalLength = numberStr.length() + zeroCount;
//        
//        // Crea la stringa di formato, es. "%04d" per una lunghezza totale di 4
//        String formatSpecifier = "%0" + totalLength + "d";
//        
//        // Applica la formattazione e restituisce il risultato
//        return String.format(formatSpecifier, number);
//    }
//
//    /**
//     * Formatta un numero aggiungendo zeri iniziali fino a raggiungere
//     * una lunghezza totale specificata.
//     *
//     * @param number Il numero da formattare.
//     * @param totalDigits La lunghezza totale desiderata per la stringa finale.
//     * @return Una stringa del numero con riempimento di zeri.
//     */
//    public static String padToTotalDigits(int number, int totalDigits) {
//        // Costruisce la stringa di formato, es. "%05d"
//        String formatSpecifier = "%0" + totalDigits + "d";
//        
//        // Applica la formattazione e restituisce il risultato
//        return String.format(formatSpecifier, number);
//    }
    
    



//    /**
//     * Formatta un numero intero aggiungendo zeri iniziali o lo restituisce
//     * come stringa semplice, in base alla modalità specificata.
//     *
//     * @param number Il numero da formattare.
//     * @param value La lunghezza totale o il numero di zeri da aggiungere.
//     * Questo parametro viene ignorato se il tipo è NO_PAD.
//     * @param type Il tipo di formattazione da applicare.
//     * @return Una stringa del numero con la formattazione richiesta.
//     */
//    public static String padNumber(int number, int value, FillOption type) {
//
//    	if (type == FillOption.NO_FILL) {
//    		return String.valueOf(number);
//    	}
//    	if (value < 0) {
//    		throw new IllegalArgumentException("Il valore per il padding non può essere negativo-The padding value cannot be negative.");
//    	}
//
//    	int totalLength;
//
//    	switch (type) {
//    	case FILL_TO_NUMBER:
//    		// La 'value' rappresenta direttamente la lunghezza totale finale.
//    		totalLength = value;
//    		break;
//
//    	case FILL_TO_ZERO:
//    		// La 'value' rappresenta il numero di zeri da aggiungere.
//    		// Calcoliamo la lunghezza totale sommando gli zeri alla lunghezza del numero.
//    		String numberStr = String.valueOf(number);
//    		totalLength = numberStr.length() + value;
//    		break;
//
//    	default:
//    		throw new IllegalStateException("Tipo di padding non supportato-Unsupported padding type: " + type);
//    	}
//
//    	// La logica di formattazione finale è ora comune a entrambi i casi.
//    	String formatSpecifier = "%0" + totalLength + "d";
//    	return String.format(formatSpecifier, number);
//    }


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

		public  static FillOption getByPref() {
			return fromString(Prefs.getProp("FILL_TYPE", "NO_FILL"));

		}
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
	
	/**
	 * Formats an integer by adding leading zeros or returns it
	 * as a plain string, based on the specified mode.
	 *
	 * @param number The number to format.
	 * @param value The total final length or the number of zeros to add.
	 * This parameter is ignored if the type is NO_FILL.
	 * @param type The type of formatting to apply.
	 * @return A string of the number with the requested formatting.
	 */
	public static String padNumber(int number, int value, FillOption type) {

	    // 1. Handle the simple case first (Guard Clause)
	    if (type == FillOption.NO_FILL) {
	        return String.valueOf(number);
	    }

	    // 2. Validate the padding value
	    if (value < 0) {
	        throw new IllegalArgumentException("The padding value cannot be negative.");
	    }

	    // 3. Use a Switch Expression (Java 14+) to determine the total length
	    int totalLength = switch (type) {
	        // 'value' represents the total final length directly.
	        case FILL_TO_NUMBER -> value;

	        // 'value' represents the number of zeros to add.
	        // We calculate the total length by adding the zeros to the number's length.
	        case FILL_TO_ZERO -> String.valueOf(number).length() + value;

	        // Handles any other undefined FillOption enum constants
	        default -> throw new IllegalStateException("Unsupported padding type: " + type);
	    };

	    // 4. The final formatting logic is now common to both cases.
	    // String.format() correctly handles cases where the number is already
	    // longer than the totalLength (it won't truncate).
	    String formatSpecifier = "%0" + totalLength + "d";
	    return String.format(formatSpecifier, number);
	}

}