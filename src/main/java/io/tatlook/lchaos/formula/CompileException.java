package io.tatlook.lchaos.formula;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class CompileException extends Exception {
	private DiagnosticCollector<JavaFileObject> diagnostics;
	
	public CompileException(DiagnosticCollector<JavaFileObject> diagnostics) {
		super();
		this.diagnostics = diagnostics;
	}

	public DiagnosticCollector<JavaFileObject> getDiagnosticCollector() {
		return diagnostics;
	}

}
