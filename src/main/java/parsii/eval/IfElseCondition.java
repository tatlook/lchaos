/*
 * LChaos - simple 2D fractal plotter and editor.
 * Copyright (C) 2021 YouZhe Zhen
 *
 * LChaos is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LChaos is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LChaos.  If not, see <https://www.gnu.org/licenses/>.
 */

package parsii.eval;

public class IfElseCondition implements Expression {

	private Expression condition;
	private Expression then;
	private Expression elze;

	public IfElseCondition(Expression condition, Expression then, Expression elze) {
		this.condition = condition;
		this.then = then;
		this.elze = elze;
	}

	@Override
	public Complex evaluate() {
		return condition.evaluate().toBoolean()
				? then.evaluate()
				: elze.evaluate();
	}

}
