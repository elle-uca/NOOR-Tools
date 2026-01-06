package org.ln.noor.tools.rename.util;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Converts numbers between bases while logging each intermediate calculation.
 * The utility is intended for rename preview workflows and does not touch the filesystem.
 *
 * @author Luca Noale
 */

public class NumberConverter {

    private static final Logger logger = LoggerFactory.getLogger(NumberConverter.class);

    /**
     * Converts a number from one base to another while emitting detailed logs for the rename preview flow.
     * This method only performs in-memory calculations and does not interact with the filesystem.
     *
     * @param number the number to convert.
     * @param baseIn the source base (for example 2, 10, or 16).
     * @param baseOut the destination base (for example 2, 10, or 16).
     * @return the converted number represented as a string.
     */
    public String convert(String number, int baseIn, int baseOut) {
        if (baseIn == baseOut) {
            return number;
        }

        // Log the decimal conversion first to keep the preview trace easy to read.
        logger.info("\n--- PASSO 1: Da Base {} a Base 10 ---", baseIn);
        long decimalValue = toDecimal(number, baseIn);
        logger.info("\nRisultato in Base 10: {}", decimalValue);

        if (baseOut == 10) {
            return String.valueOf(decimalValue);
        }

        // Conversion back to the target base keeps the trace aligned with the UI output.
        logger.info("\n--- PASSO 2: Da Base 10 a Base {} ---", baseOut);
        String result = fromDecimal(decimalValue, baseOut);
        logger.info("\nRisultato Finale in Base {}: {}", baseOut, result);

        return result;
    }

    /**
     * Converts a number from any supported base to decimal without touching the filesystem.
     */
    private long toDecimal(String number, int base) {
        long decimalValue = 0;
        String digits = "0123456789ABCDEF"; // Digits beyond 9 support hexadecimal-style inputs.
        number = number.toUpperCase();

        logger.debug("  Calcolo: Somma dei (Simbolo * Base ^ Posizione)");

        for (int i = 0; i < number.length(); i++) {
            char digitChar = number.charAt(i);
            int digitValue = digits.indexOf(digitChar); // Translate symbol to numeric value (e.g. 'A' is 10).
            int power = number.length() - 1 - i; // Preserve positional weight from left to right.

            // Track each contribution to keep the logged preview readable.
            long contribution = digitValue * (long) Math.pow(base, power);
            decimalValue += contribution;

            logger.debug("  Posizione {}: {} ({}) * {}^{} = {}",
                              power, digitChar, digitValue, base, power, contribution);
        }
        return decimalValue;
    }

    /**
     * Converts a decimal number into the requested base, producing an in-memory preview.
     */
    private String fromDecimal(long decimalValue, int base) {
        if (decimalValue == 0) return "0";

        String result = "";
        String digits = "0123456789ABCDEF";
        long currentNumber = decimalValue;

        logger.debug("  Calcolo: Divisioni successive per la Base {}", base);

        while (currentNumber > 0) {
            // Track the remainder to capture the next symbol for the preview trace.
            long remainder = currentNumber % base;

            // Update the quotient so the loop progresses toward zero.
            long quotient = currentNumber / base;

            // Map the remainder to the symbol expected by the rename preview.
            char digitChar = digits.charAt((int) remainder);

            // Prepend to build the representation in the correct order.
            result = digitChar + result;

            logger.debug("  {} / {} = Quoziente {}, Resto {} ({})",
                              currentNumber, base, quotient, remainder, digitChar);
            
            currentNumber = quotient;
        }
        return result;
    }
    
    public static void main(String[] args) {
        NumberConverter converter = new NumberConverter();
        Scanner scanner = new Scanner(System.in);
        
        logger.info("--- Convertitore di Base Numerica ---");

        logger.info("Inserisci il numero da convertire: ");
        String number = scanner.nextLine();

        logger.info("Inserisci la base di partenza (es. 16, 2): ");
        int baseIn = scanner.nextInt();

        logger.info("Inserisci la base di arrivo (es. 10, 8): ");
        int baseOut = scanner.nextInt();
        
        scanner.close();

        // Esegue la conversione e mostra i passaggi
        converter.convert(number, baseIn, baseOut);
    }
}
