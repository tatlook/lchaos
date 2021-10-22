package io.tatlook.lchaos.formula;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;

public class CompileException extends Exception {
	private DiagnosticCollector<? extends FileObject> diagnostics;
	
	public CompileException(DiagnosticCollector<? extends FileObject> diagnostics) {
		super();
		this.diagnostics = diagnostics;
	}

	public DiagnosticCollector<? extends FileObject> getDiagnosticCollector() {
		return diagnostics;
	}

}
