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

import java.io.Reader;
import java.io.StringReader;

import parsii.tokenizer.ParseException;

public class FormulaParser extends Parser {

    protected FormulaParser(Reader input, Scope scope) {
        super(input, scope);
        tokenizer.setKeywordsCaseSensitive(true);
        String[] keywords = {
            "IF", "ELSE", "ELSEIF", "ENDIF"
        };
        for (String keyword : keywords) {
            tokenizer.addKeyword(keyword);
        }
    }
    
    /**
     * Parses the given input into an expression.
     *
     * @param input the expression to be parsed
     * @return the resulting AST as expression
     * @throws ParseException if the expression contains one or more errors
     */
    public static Expression parse(String input) throws ParseException {
        return new FormulaParser(new StringReader(input), new Scope()).parse();
    }

    /**
     * Parses the given input into an expression.
     *
     * @param input the expression to be parsed
     * @return the resulting AST as expression
     * @throws ParseException if the expression contains one or more errors
     */
    public static Expression parse(Reader input) throws ParseException {
        return new FormulaParser(input, new Scope()).parse();
    }

    /**
     * Parses the given input into an expression.
     * <p>
     * Referenced variables will be resolved using the given Scope
     *
     * @param input the expression to be parsed
     * @param scope the scope used to resolve variables
     * @return the resulting AST as expression
     * @throws ParseException if the expression contains one or more errors
     */
    public static Expression parse(String input, Scope scope) throws ParseException {
        return new FormulaParser(new StringReader(input), scope).parse();
    }

    /**
     * Parses the given input into an expression.
     * <p>
     * Referenced variables will be resolved using the given Scope
     *
     * @param input the expression to be parsed
     * @param scope the scope used to resolve variables
     * @return the resulting AST as expression
     * @throws ParseException if the expression contains one or more errors
     */
    public static Expression parse(Reader input, Scope scope) throws ParseException {
        return new FormulaParser(input, scope).parse();
    }

    @Override
    public Expression expression() {

        return parseIfElse();
    }

    protected Expression parseIfElse() {
        if (tokenizer.current().isKeyword("IF")) {
            tokenizer.consume();
            if (tokenizer.current().isSymbol("(")) {
                tokenizer.consume();
                Expression condition = expression();
                Expression then;
                Expression elze = null;
                if (tokenizer.current().isSymbol(")")) {
                    tokenizer.consume();
                    then = expression();
                    if (tokenizer.current().isKeyword("ELSE")) {
                        tokenizer.consume();
                        elze = expression();
                    }
                    // TODO: else if
                    if (tokenizer.current().isKeyword("ENDIF")) {
                        tokenizer.consume();
                        return new IfElseCondition(condition, then, elze);
                    } else {
                        expect(tokenizer.current().getType(), "ENDIF");
                        return Constant.EMPTY;
                    }
                } else {
                    expect(tokenizer.current().getType(), ")");
                    return Constant.EMPTY;
                }
            } else {
                expect(tokenizer.current().getType(), "(");
                return Constant.EMPTY;
            }
        }
        return relationalExpression();
    }

    // protected Expression parseVariableAssignment() {
    //     VariableReference left;
    //     Expression leftExpression = expression();
    //     if (leftExpression instanceof VariableReference) {
    //         left = (VariableReference) leftExpression;
    //     } else {
    //         errors.add(ParseError.error(tokenizer.current(), ""));
    //         return Constant.EMPTY;
    //     }
    //     if (tokenizer.current().isSymbol("=")) {
    //         tokenizer.consume();
    //         Expression right = expression();
    //         return new VariableAssignment(left, right);
    //     }
    //     return left;
    // }

}
