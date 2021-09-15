/**
 * 
 */
package io.tatlook.lchaos.formula;

import io.tatlook.lchaos.data.MultipleRulesData;
import parsii.eval.Expression;
import parsii.eval.Scope;
import parsii.eval.Variable;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetData extends MultipleRulesData {
	private Scope scope = new Scope();

	public MandelbrotSetData(MandelbrotSetData origin) {
		super(origin);
		
		scope.create("pixel");
		scope.create("z");
		
	}

	public MandelbrotSetData() {
		this(new MandelbrotSetData(null));
	}

	/**
	 * Returns the scope.
	 * 
	 * @return the scope
	 */
	public Scope getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public void addRule() {
		addRule("z", "");
	}

	class Equation {
		Variable variable;
		Expression expression;
	}

	/**
	 * @param string
	 * @param string2
	 */
	public void addRule(String variable, String expression) {
		
	}

	@Override
	public void removeRule(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentToOrigin() {
		MandelbrotSetData origin = new MandelbrotSetData(null);
		origin.scope = new Scope().withParent(scope);
		this.origin = origin;
	}

}
