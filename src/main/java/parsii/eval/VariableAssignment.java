package parsii.eval;

public class VariableAssignment implements Expression {

	private VariableReference variable;
	private Expression toSet;

	public VariableAssignment(VariableReference variable, Expression toSet) {
		this.variable = variable;
		this.toSet = toSet;
	}

	@Override
	public Complex evaluate() {
		variable.getVariable().setValue(toSet.evaluate());
		return variable.evaluate();
	}
	
}
