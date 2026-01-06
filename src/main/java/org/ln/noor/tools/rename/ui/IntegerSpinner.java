package org.ln.noor.tools.rename.ui;


import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * A specialization of JSpinner that works with integer values.
 * Provides a convenient getIntValue() method to avoid explicit casts.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class IntegerSpinner extends JSpinner {



    /**
     * Builds an integer spinner with the default value range (1, 0, max int, 1).
     * This constructor has no filesystem side effects.
     */
    public IntegerSpinner() {
        this(1, 0, Integer.MAX_VALUE, 1);
    }

    /**
     * Builds an integer spinner with a custom starting value and default bounds.
     * This constructor has no filesystem side effects.
     */
    public IntegerSpinner(int value) {
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
    public IntegerSpinner(int value, int min, int max, int step) {
        super(new SpinnerNumberModel(value, min, max, step));
    }
    
    /**
     * Retrieves the current spinner value as a primitive int without touching the filesystem.
     *
     * @return the numeric value of the spinner.
     */
    public int getIntValue() {
        // The model is constrained to SpinnerNumberModel, so Number ensures safe conversion.
        return ((Number) super.getValue()).intValue();
    }
}
