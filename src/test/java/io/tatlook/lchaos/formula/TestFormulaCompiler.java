package io.tatlook.lchaos.formula;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.tatlook.lchaos.formula.FormulaCompiler.Token;
import parsii.tokenizer.Token.TokenType;

import javax.tools.DiagnosticCollector;

import org.junit.Test;

public class TestFormulaCompiler {
	@Test
	public void testTakeComments() {
		String expected = "lalalalalalalaiiii\n"
				+ "hh,hhhhhhfdfa hhrihirqh q  \t\tkkdjk jk \t\n"
				+ "\n\n\n\n\n\n"
				+ "........    ";
		String code = "lalalalalalalaiiii; ppffaa ;;;;sgf  \n"
				+ "hh,hhhhhhfdfa hhrihirqh q  \t\tkkdjk jk \t;   \t \tdasdfdfdsf\n"
				+ "\n\n\n\n\n\n"
				+ "........    ; hmm \t\t\t";
		assertEquals(expected, 
				FormulaCompiler.takeCommentsDown(code));
	}

	@Test
	public void testParseTokens() {
		List<Token> expected = Arrays.asList(new Token[] {
				new Token(Token.Kind.EXPER, "*"),
				new Token(Token.Kind.EXPER, "+"),
				new Token(Token.Kind.IDENTIFIER, "i34"),
				new Token(Token.Kind.EXPER, ","),
				new Token(Token.Kind.IDENTIFIER, "file"),
				new Token(Token.Kind.EXPER, "("),
				new Token(Token.Kind.IDENTIFIER, "line"),
				new Token(Token.Kind.EXPER, "("),
				new Token(Token.Kind.IDENTIFIER, "x"),
				new Token(Token.Kind.EXPER, ","),
				new Token(Token.Kind.NUMBER, 2),
				new Token(Token.Kind.EXPER, "+"),
				new Token(Token.Kind.IM_NUMBER, 1),
				new Token(Token.Kind.EXPER, ")"),
			});
		String code = "* + i34, file(line(x,2+1i)";
		assertEquals(expected,
				new FormulaCompiler(code, new DiagnosticCollector<>()).getTokens(code)); 
	}

}
