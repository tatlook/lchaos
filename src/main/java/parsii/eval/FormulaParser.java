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
import java.util.ArrayList;
import java.util.List;

import parsii.tokenizer.ParseError;
import parsii.tokenizer.ParseException;
import parsii.tokenizer.Token;
import parsii.tokenizer.Token.TokenType;

public class FormulaParser extends Parser {

    protected FormulaParser(Reader input, Scope scope) {
        super(input, scope);
        tokenizer.setKeywordsCaseSensitive(true);
        scope.withStrictLookup(false);
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
    protected Expression parse() throws ParseException {
        Expression variableDefinitions = statments(":");
        tokenizer.consume();
        variableDefinitions.evaluate();
        scope.withStrictLookup(true);
        
        Statments statments = statments();

        if (tokenizer.current().isNotEnd()) {
            Token token = tokenizer.consume();
            errors.add(ParseError.error(token,
                                        String.format("Unexpected token: '%s'. Expected an expression.",
                                                      token.getSource())));
        }
        if (statments.isEmpty()) {
            Token token = tokenizer.consume();
            errors.add(ParseError.error(token, "No result."));
        }
        if (!errors.isEmpty()) {
            throw ParseException.create(errors);
        }
        // The last expression is condition.
        Expression loopCondition = statments.remove(statments.size() - 1);
        return new FormulaEvaluter(statments, loopCondition);
    }

    protected Statments statments(String... endKeyWords) {
        List<Expression> expressions = new ArrayList<>();
        while (true) {
            if (tokenizer.current().isKeyword(endKeyWords) || tokenizer.current().isSymbol(endKeyWords)) {
                break;
            }
            if (tokenizer.current().isEnd()) {
                if (endKeyWords.length == 0) {
                    break;
                } else {
                    errors.add(ParseError.error(tokenizer.current(), "No such end marks but actual EOI."));   // TODO: change message
                    break;
                }
            }
            expressions.add(expression());
        }
        return new Statments(expressions);
    }

    @Override
    protected Expression expression() {

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
                    StatmentMatches matches = statments("ELSE", "ENDIF");
                    then = matches.statments;
                    if ("ELSE".equals(matches.endKeyWord)) {
                        tokenizer.consume();
                        matches = statments("ENDIF");
                        elze = matches.statments;
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
