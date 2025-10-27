package org.ln.noortools.view;


import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * A specialization of JSpinner that works with integer values.
 * Provides a convenient getIntValue() method to avoid explicit casts.
 */
@SuppressWarnings("serial")
public class JIntegerSpinner extends JSpinner {



    /**
     * Costruttore di default.
     * Crea uno JIntegerSpinner con valori di default (1, 0, max int, 1).
     */
    public JIntegerSpinner() {
    	this(1, 0, Integer.MAX_VALUE, 1);
    }
    
    /**
     * Costruttore di default.
     * Crea uno JIntegerSpinner con valori di default (1, 0, max int, 1).
     */
    public JIntegerSpinner(int value) {
        this(value, 0, Integer.MAX_VALUE, 1);
    }

    /**
     * Constructs a JIntegerSpinner with an initial value, a minimum, a maximum,
     * and a step (increment/decrement value).
     *
     * @param value the initial and current value of the spinner
     * @param min   the minimum allowed value
     * @param max   the maximum allowed value
     * @param step  the amount to increase or decrease at each step
     */
    public JIntegerSpinner(int value, int min, int max, int step) {
        super(new SpinnerNumberModel(value, min, max, step));
    }
    
    /**
     * Restituisce il valore corrente dello spinner come un primitivo int.
     *
     * @return il valore intero dello spinner.
     */
    public int getIntValue() {
        // Poiché abbiamo garantito che il modello è un SpinnerNumberModel,
        // possiamo tranquillamente ottenere il valore come Number e poi
        // convertirlo in int. Usare .intValue() è più sicuro che
        // castare direttamente a (Integer).
        return ((Number) super.getValue()).intValue();
    }
}