package org.ln.noortools.util;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberConverter {

    private static final Logger logger = LoggerFactory.getLogger(NumberConverter.class);

    /**
     * Esegue la conversione di un numero tra due basi, mostrando i passaggi.
     * @param number Il numero da convertire (come String).
     * @param baseIn La base di partenza (es. 2, 10, 16).
     * @param baseOut La base di arrivo (es. 2, 10, 16).
     * @return Il numero convertito (come String).
     */
    public String convert(String number, int baseIn, int baseOut) {
        if (baseIn == baseOut) {
            return number;
        }

        // --- PASSO 1: Conversione dalla Base di Partenza alla Base 10 ---
        logger.info("\n--- PASSO 1: Da Base {} a Base 10 ---", baseIn);
        long decimalValue = toDecimal(number, baseIn);
        logger.info("\nRisultato in Base 10: {}", decimalValue);

        if (baseOut == 10) {
            return String.valueOf(decimalValue);
        }

        // --- PASSO 2: Conversione dalla Base 10 alla Base di Arrivo ---
        logger.info("\n--- PASSO 2: Da Base 10 a Base {} ---", baseOut);
        String result = fromDecimal(decimalValue, baseOut);
        logger.info("\nRisultato Finale in Base {}: {}", baseOut, result);

        return result;
    }

    /**
     * Converte un numero da una base N alla base 10 (Decimale).
     */
    private long toDecimal(String number, int base) {
        long decimalValue = 0;
        String digits = "0123456789ABCDEF"; // Simboli usati per le basi > 10
        number = number.toUpperCase();

        logger.debug("  Calcolo: Somma dei (Simbolo * Base ^ Posizione)");

        for (int i = 0; i < number.length(); i++) {
            char digitChar = number.charAt(i);
            int digitValue = digits.indexOf(digitChar); // Valore numerico del simbolo (es. 'A' è 10)
            int power = number.length() - 1 - i; // Posizione (partendo da 0 a destra)

            // Il contributo di questa cifra al valore decimale
            long contribution = digitValue * (long) Math.pow(base, power);
            decimalValue += contribution;

            logger.debug("  Posizione {}: {} ({}) * {}^{} = {}",
                              power, digitChar, digitValue, base, power, contribution);
        }
        return decimalValue;
    }

    /**
     * Converte un numero dalla base 10 (Decimale) alla base N.
     */
    private String fromDecimal(long decimalValue, int base) {
        if (decimalValue == 0) return "0";

        String result = "";
        String digits = "0123456789ABCDEF";
        long currentNumber = decimalValue;

        logger.debug("  Calcolo: Divisioni successive per la Base {}", base);
        
        while (currentNumber > 0) {
            // Calcola il resto, che è la prossima cifra nella nuova base
            long remainder = currentNumber % base;
            
            // Calcola il quoziente, che è il nuovo numero da dividere
            long quotient = currentNumber / base;

            // Il simbolo corrispondente al resto (es. 10 -> 'A')
            char digitChar = digits.charAt((int) remainder);
            
            // Aggiunge la cifra all'inizio del risultato
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