/*
 * 
 */
package io.tatlook.lchaos.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.Diagnostic.Kind;

class FormulaCompiler implements Callable<Boolean> {

	private String formulaCode;
	private final DiagnosticCollector<FormulaFileObject> diagnosticCollector;
	private FormulaFileObject fileObject;
	private String javaCode = "";

	public FormulaCompiler(String formulaCode, 
			DiagnosticCollector<FormulaFileObject> diagnosticCollector) {
		this.formulaCode = formulaCode;
		this.diagnosticCollector = diagnosticCollector;
		fileObject = new FormulaFileObject();
	}

	static class FormulaDiagnostic implements Diagnostic<FormulaFileObject> {
		
		Kind kind;
		FormulaFileObject source;
		long position;
		long startPosition;
		long endPosition;
		long lineNumber;
		long columnNumber;
		String code;
		String message;
		
		public FormulaDiagnostic(Kind kind, FormulaFileObject source, long position, long startPosition,
				long endPosition, long lineNumber, long columnNumber, String code, String message) {
			this.kind = kind;
			this.source = source;
			this.position = position;
			this.startPosition = startPosition;
			this.endPosition = endPosition;
			this.lineNumber = lineNumber;
			this.columnNumber = columnNumber;
			this.code = code;
			this.message = message;
		}

		@Override
		public Kind getKind() {
			return kind;
		}

		@Override
		public FormulaFileObject getSource() {
			return source;
		}

		@Override
		public long getPosition() {
			return position;
		}

		@Override
		public long getStartPosition() {
			return startPosition;
		}

		@Override
		public long getEndPosition() {
			return endPosition;
		}

		@Override
		public long getLineNumber() {
			return lineNumber;
		}

		@Override
		public long getColumnNumber() {
			return columnNumber;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getMessage(Locale locale) {
			return message;
		}

	}

	static class Token {
		enum Kind {
			NUMBER("[0-9]+"),
			IM_NUMBER("[0-9]+i"),
			EXPER(""),
			IDENTIFIER("[a-zA-Z]+|[a-zA-Z][a-zA-Z0-9]+");
			Pattern pattern;
			Kind(String pattern) {
				this.pattern = Pattern.compile(pattern);
			}
		}
		String value;
		Kind kind;
		double number;
		Token(Kind kind, String value) {
			this.value = value;
			this.kind = kind;
		}
		Token(Kind kind, double value) {
			this.number = value;
			this.kind = kind;
		}
	}

	void reportWhithIndexOnly(int index) {
		diagnosticCollector.report(getDiagnosticWhithIndexOnly(fileObject, formulaCode, index));
	}

	static FormulaDiagnostic getDiagnosticWhithIndexOnly(FormulaFileObject fileObject,
			String formulaCode, int index) {
		int lineNumber = 0;
		int columnNumber = 0;
		int i;
		for (i = 0; i < index; i++, columnNumber++) {
			char c = formulaCode.charAt(i);
			if (c == '\n') {
				lineNumber++;
				columnNumber = 0;
			}
		}
		return new FormulaDiagnostic(
				Kind.ERROR,
				fileObject,
				i, i, i,
				lineNumber,
				columnNumber,
				"{}",
				"error token");
	}

	@Override
	public Boolean call() {
		formulaCode = takeCommentsDown(formulaCode);
		int index = formulaCode.indexOf('}');
		if (index != -1) {
			reportWhithIndexOnly(index);
			return false;
		}
		index = formulaCode.indexOf(':');
		if (index == -1) {
			reportWhithIndexOnly(index);
			return false;
		}
		javaCode += formulaVaribleDeclarationToJavaCode(formulaCode.substring(0, index));
		formulaCode = formulaCode.substring(index);
		
		return true;
	}

	List<Token> getTokens(String formulaCode) {
		List<Token> tokens = new ArrayList<>();

		Scanner scanner = new Scanner(formulaCode);
		mainloop: while (scanner.hasNext()) {
			if (scanner.hasNext(Token.Kind.IDENTIFIER.pattern)) {
				String identifier = scanner.next(Token.Kind.IDENTIFIER.pattern);
				Token token = new Token(Token.Kind.IDENTIFIER, identifier);
				tokens.add(token);
			} else if (scanner.hasNext(Token.Kind.IM_NUMBER.pattern)) {
				double imNumber = Double.valueOf(scanner.next(Token.Kind.IM_NUMBER.pattern));
				Token token = new Token(Token.Kind.IM_NUMBER, imNumber);
				tokens.add(token);
			} else if (scanner.hasNextDouble()) {
				double number = scanner.nextDouble();
				Token token = new Token(Token.Kind.NUMBER, number);
				tokens.add(token);
			} else {
				String[] expers = { "\\*", ",", "/", "\\(", "\\)", "\\+", "-" };
				for (String exper : expers) {
					if (scanner.hasNext(exper)) {
						scanner.skip(exper);
						Token token = new Token(Token.Kind.EXPER, exper);
						tokens.add(token);
						continue mainloop;
					}
				}
				reportWhithIndexOnly(scanner.skip("\\G").match().end());
				break;
			}
		}
		scanner.close();

		return tokens;
	}

	String formulaVaribleDeclarationToJavaCode(String formulaCode) {
		String result = "";
		return result;
	}

	static String takeCommentsDown(String formulaCode) {
		String result = "";
		String[] lines = formulaCode.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			int semicolonIndex = line.indexOf(';');
			if (semicolonIndex != -1) {
				line = line.substring(0, semicolonIndex);
			}
			result += line;
			// all lines has a '\n', unless the last line
			if (i != lines.length) {
				result += '\n';
			}
		}
		// String line;
		// int position;
		// int lastPosition = 0;
		// boolean firstLine = true;
		// while (true) {
		// 	position = formulaCode.indexOf('\n', lastPosition);
		// 	if (formulaCode.indexOf('\n', lastPosition) == -1) {
		// 		if (firstLine) {
		// 			position = formulaCode.length();
		// 		} else {
		// 			break;
		// 		}
		// 	}
		// 	firstLine = false;

		// 	line = formulaCode.substring(lastPosition, position);
		// 	int semicolonIndex = line.indexOf(';');
		// 	if (semicolonIndex != -1) {
		// 		line = line.substring(0, semicolonIndex - 1);
		// 	}
		// 	result += line;
		// 	lastPosition = position;
		// }
		return result;
	}

	public String getResultJavaCode() {
		return javaCode;
	}

}
