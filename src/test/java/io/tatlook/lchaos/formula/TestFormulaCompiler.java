package io.tatlook.lchaos.formula;

import static org.junit.Assert.assertEquals;

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

}
