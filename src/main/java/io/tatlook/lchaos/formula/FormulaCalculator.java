/**
 * 
 */
package io.tatlook.lchaos.formula;

import parsii.eval.Complex;

public interface FormulaCalculator {
	/**
	 * 
	 * @param c
	 * @param max
	 * @return
	 */
	public int calculate(Complex c, int max);
}
