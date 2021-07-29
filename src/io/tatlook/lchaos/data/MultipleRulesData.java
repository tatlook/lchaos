/**
 * 
 */
package io.tatlook.lchaos.data;

/**
 * @author Administrator
 *
 */
public abstract class MultipleRulesData extends AbstractData {

	protected MultipleRulesData(AbstractData origin) {
		super(origin);
	}

	public abstract void addRule();
	public abstract void removeRule(int index);

	public static MultipleRulesData getCurrent() {
		if (!(current instanceof MultipleRulesData)) {
			throw new IllegalStateException();
		}
		return (MultipleRulesData) current;
	}

}
