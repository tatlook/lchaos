/**
 * 
 */
package io.tatlook.lchaos.formula;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import io.tatlook.lchaos.ErrorMessageDialog;
import io.tatlook.lchaos.data.AbstractData;
import parsii.eval.Expression;
import parsii.eval.Scope;
import parsii.eval.Variable;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetData extends AbstractData {
	private final Scope scope = new Scope();
	private List<Variable> variables = new ArrayList<>();
	private List<Expression> variablesInit = new ArrayList<>();
	private Class<? extends FormulaCalculator> compiledFormulaCalculatorClass;

	private FormulaCalculator compiledFormulaCalculatorInstanse;
	private String javaCode;

	public MandelbrotSetData(MandelbrotSetData origin) {
		super(origin);
	}

	public MandelbrotSetData() {
		this(new MandelbrotSetData(null));
	}

	public void addVariable(String variable, Expression init) {
		Variable variable2 = scope.create(variable);
		variable2.setValue(init.evaluate());
		variables.add(variable2);
		variablesInit.add(init);
	}

	public List<Variable> getVariables() {
		return variables;
	}

	@Override
	public void setCurrentToOrigin() {
		MandelbrotSetData origin = new MandelbrotSetData(null);
		origin.javaCode = javaCode;
		this.origin = origin;
	}

	public List<Expression> getVariablesInit() {
		return null;
	}

	public void setJavaCode(String javaCode) {
		this.javaCode = javaCode;
	}

	@SuppressWarnings("unchecked")
	public void compile() throws CompileException {
		String absoluteCode = "package io.tatlook.formula;\n\n"
				+ "import parsii.eval.Complex;\n\n"
				+ "public class FormulaCalculatorImpl\n"
				+ "        implements io.tatlook.lchaos.formula.FormulaCalculator {\n"
				+ "    @Override\n"
				+ "    public int calculate(Complex c, int max) {\n"
				+          javaCode
				+ "    }\n"
				+ "}\n";
		
		File sourceFile = new File("data/io/tatlook/formula/FormulaCalculatorImpl.java");
		if (sourceFile.getParentFile().exists() || sourceFile.getParentFile().mkdirs()) {
		}

		try {
			try (Writer writer = new FileWriter(sourceFile)) {
				writer.write(absoluteCode);
				writer.flush();
			}

			/** Compilation Requirements **/
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

			// This sets up the class path that the compiler will use.
			// I've added the .jar file that contains the DoStuff interface within in it...
			List<String> optionList = new ArrayList<>();

			Iterable<? extends JavaFileObject> compilationUnit
					= fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
			JavaCompiler.CompilationTask task = compiler.getTask(
					null,
					fileManager,
					diagnostics,
					optionList,
					null,
					compilationUnit);
			if (task.call()) {
				/** Load and execute */
				// Create a new custom class loader, pointing to the directory that contains the compiled
				// classes, this should point to the top of the package structure!
				URLClassLoader classLoader = new URLClassLoader(new URL[] { 
						new File("./data").toURI().toURL() 
				});
				// Load the class from the classloader by name....
				compiledFormulaCalculatorClass = (Class<? extends FormulaCalculator>) 
						classLoader.loadClass("io.tatlook.formula.FormulaCalculatorImpl");
				classLoader.close();
				// Create a new instance...
				compiledFormulaCalculatorInstanse = compiledFormulaCalculatorClass.getConstructor().newInstance();
			} else {
				throw new CompileException(diagnostics);
			}
			fileManager.close();
		} catch (IOException | ClassNotFoundException | 
				InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
	}

	public static MandelbrotSetData getCurrent() {
		if (!(current instanceof  MandelbrotSetData)) {
			throw new IllegalStateException();
		}
		return (MandelbrotSetData) current;
	}

	public Class<? extends FormulaCalculator> getCompiledFormulaCalculatorClass() {
		return compiledFormulaCalculatorClass;
	}

	public void setCompiledFormulaCalculatorClass(
			Class<? extends FormulaCalculator> compiledFormulaCalculatorClass) {
		this.compiledFormulaCalculatorClass = compiledFormulaCalculatorClass;
	}

	public FormulaCalculator getCompiledFormulaCalculatorInstanse() {
		return compiledFormulaCalculatorInstanse;
	}

	public void setCompiledFormulaCalculatorInstanse(
			FormulaCalculator compiledFormulaCalculatorInstanse) {
		this.compiledFormulaCalculatorInstanse = compiledFormulaCalculatorInstanse;
	}

	public String getJavaCode() {
		return javaCode;
	}

}
