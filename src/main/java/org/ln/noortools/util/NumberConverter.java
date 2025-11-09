package org.ln.noortools.util;
import java.util.Scanner;

public class NumberConverter {

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
        System.out.println("\n--- PASSO 1: Da Base " + baseIn + " a Base 10 ---");
        long decimalValue = toDecimal(number, baseIn);
        System.out.println("\nRisultato in Base 10: " + decimalValue);

        if (baseOut == 10) {
            return String.valueOf(decimalValue);
        }

        // --- PASSO 2: Conversione dalla Base 10 alla Base di Arrivo ---
        System.out.println("\n--- PASSO 2: Da Base 10 a Base " + baseOut + " ---");
        String result = fromDecimal(decimalValue, baseOut);
        System.out.println("\nRisultato Finale in Base " + baseOut + ": " + result);

        return result;
    }

    /**
     * Converte un numero da una base N alla base 10 (Decimale).
     */
    private long toDecimal(String number, int base) {
        long decimalValue = 0;
        String digits = "0123456789ABCDEF"; // Simboli usati per le basi > 10
        number = number.toUpperCase();

        System.out.println("  Calcolo: Somma dei (Simbolo * Base ^ Posizione)");

        for (int i = 0; i < number.length(); i++) {
            char digitChar = number.charAt(i);
            int digitValue = digits.indexOf(digitChar); // Valore numerico del simbolo (es. 'A' è 10)
            int power = number.length() - 1 - i; // Posizione (partendo da 0 a destra)

            // Il contributo di questa cifra al valore decimale
            long contribution = digitValue * (long) Math.pow(base, power);
            decimalValue += contribution;

            System.out.printf("  Posizione %d: %s (%d) * %d^%d = %d\n", 
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

        System.out.println("  Calcolo: Divisioni successive per la Base " + base);
        
        while (currentNumber > 0) {
            // Calcola il resto, che è la prossima cifra nella nuova base
            long remainder = currentNumber % base;
            
            // Calcola il quoziente, che è il nuovo numero da dividere
            long quotient = currentNumber / base;

            // Il simbolo corrispondente al resto (es. 10 -> 'A')
            char digitChar = digits.charAt((int) remainder);
            
            // Aggiunge la cifra all'inizio del risultato
            result = digitChar + result;

            System.out.printf("  %d / %d = Quoziente %d, Resto %d (%s)\n", 
                              currentNumber, base, quotient, remainder, digitChar);
            
            currentNumber = quotient;
        }
        return result;
    }
    
    public static void main(String[] args) {
        NumberConverter converter = new NumberConverter();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("--- Convertitore di Base Numerica ---");

        System.out.print("Inserisci il numero da convertire: ");
        String number = scanner.nextLine();

        System.out.print("Inserisci la base di partenza (es. 16, 2): ");
        int baseIn = scanner.nextInt();

        System.out.print("Inserisci la base di arrivo (es. 10, 8): ");
        int baseOut = scanner.nextInt();
        
        scanner.close();

        // Esegue la conversione e mostra i passaggi
        converter.convert(number, baseIn, baseOut);
    }
}