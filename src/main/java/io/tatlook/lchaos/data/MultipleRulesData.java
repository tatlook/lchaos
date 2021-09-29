/**
 * 
 */
package io.tatlook.lchaos.data;

/**
 * Data with multiple rules.
 * 
 * @author YouZhe Zhen
 */
public abstract class MultipleRulesData extends AbstractData {

	protected MultipleRulesData(AbstractData origin) {
		super(origin);
	}

	/**
	 * Add the default rule to data.
	 * Subclasses can add additional parameters.
	 */
	public abstract void addRule();
	
	/**
	 * Removes the rule at the specified position in this ChaosData.
	 * 
	 * @param index the index of the rule to be removed
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *         ({@code index < 0 || index >= The number of rules})
	 */
	public abstract void removeRule(int index);

	public static MultipleRulesData getCurrent() {
		if (!(current instanceof MultipleRulesData)) {
			throw new IllegalStateException();
		}
		return (MultipleRulesData) current;
	}

}
