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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import parsii.tokenizer.ParseException;

public class TestFormulaParser {

    @Test
    public void testSigleExpression() throws ParseException {
        String code = "(1+1)*2";
        Expression expr = FormulaParser.parse(code);
        assertEquals(new Complex(4, 0), expr.evaluate());
    }

    @Test
    public void testMultiExpression() throws ParseException {
        String code = "(123-15i)*2 3+21 (1+1)*2i";
        Expression expr = FormulaParser.parse(code);
        assertEquals(new Complex(0, 4), expr.evaluate());
    }

    @Test
    public void testIfElse() throws ParseException {
        String code = "IF (1.0+0.000 = 1)"
                + "1- 0"
                + " ELSE "
                + "1 + 4"
                + " ENDIF";
        Expression expr = FormulaParser.parse(code);
        assertEquals(new Complex(1, 0), expr.evaluate());
    }

    @Test
    public void testNestedIfElse() throws ParseException {
        String code = "IF (IF (1+1=2) 34 ELSE 1=0 ENDIF )"
                + "1- 0"
                + " ELSE IF(0=0)"
                + "1 + 4"
                + " ENDIF"
                + " ENDIF"
                + "1+1";
        Expression expr = FormulaParser.parse(code);
        assertEquals(new Complex(1, 0), expr.evaluate());
    }

}
