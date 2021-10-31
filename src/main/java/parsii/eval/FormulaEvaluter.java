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

public class FormulaEvaluter implements Expression {

    private Expression doLoop;
    private Expression loopCondition;

    public FormulaEvaluter(Expression doLoop, Expression loopCondition) {
        this.doLoop = doLoop;
        this.loopCondition = loopCondition;
    }

    @Override
    public Expression simplify() {
        return new FormulaEvaluter(doLoop.simplify(), loopCondition.simplify());
    }

    @Override
    public boolean isConstant() {
        return loopCondition.isConstant();
    }

    @Override
    public Complex evaluate() {
        int i = 0;
        while (loopCondition.evaluate().toBoolean()) {
            doLoop.evaluate();
            i++;
            if (i >= 255) {
                return Complex.valueOf(i);
            }
        }
        return Complex.valueOf(i);
    }

}
