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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Statments extends ArrayList<Expression> implements Expression {

	public Statments(Collection<? extends Expression> expressions) {
		super(expressions);
	}

	public Statments() {
	}

	@Override
	public Expression simplify() {
		List<Expression> simplifiedExpressions = new ArrayList<>();
		for (Expression expression : this) {
			simplifiedExpressions.add(expression.simplify());
		}
		return new Statments(simplifiedExpressions);
	}

	@Override
	public Complex evaluate() {
		int i;
		for (i = 0; i < size() - 1; i++) {
			get(i).evaluate();
		}
		return get(i).evaluate();
	}

}
