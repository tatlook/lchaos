/*
 * 
 */
package io.tatlook.lchaos.formula;

import java.util.Locale;
import java.util.concurrent.Callable;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.Diagnostic.Kind;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.tokenizer.ParseException;

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
		try {
			Expression expression = Parser.parse(formulaCode);
			javaCode = expression.toString();
		} catch (ParseException e) {
			return false;
		}
		return true;
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
