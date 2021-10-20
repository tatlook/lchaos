/**
 * 
 */
package io.tatlook.lchaos.formula;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.editor.AbstractEditor;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetEditor extends AbstractEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5291441209585725924L;

	/**
	 * 
	 */
	public MandelbrotSetEditor() {
		createSpeedControl();
	}

	private void createSpeedControl() {
		JPanel speedControlPanel = new JPanel(new BorderLayout());
		speedControlPanel.setBorder(BorderFactory.createTitledBorder("Maximum Iterations"));
		speedControlPanel.setMaximumSize(new Dimension(speedControlPanel.getMaximumSize().width, 90));
		EditTextField field = new EditTextField(App.mainWindow.getDrawer() == null
				? "255" : String.valueOf(App.mainWindow.getDrawer().getLevel()),
				(value) -> {
			int level = Integer.parseInt(value);
			if (level > 255) {
				throw new NumberFormatException();
			}
			App.mainWindow.getDrawer().setLevel(level);
			App.mainWindow.getDrawer().setChange();
		});
		field.setColumns(20);
		
		speedControlPanel.add(field, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea(MandelbrotSetData.getCurrent().getJavaCode());
		textArea.setTabSize(4);
		JTextArea errorArea = new JTextArea();
		errorArea.setTabSize(4);
		errorArea.setVisible(false);
		errorArea.setEditable(false);
		errorArea.setLineWrap(true);
		errorArea.setColumns(10);
		errorArea.setBorder(BorderFactory.createTitledBorder("Errors"));

		JButton compileButton = new JButton("Compile");
		compileButton.addActionListener((e0) -> {
			errorArea.setVisible(false);

			MandelbrotSetData data = MandelbrotSetData.getCurrent();
			data.setJavaCode(textArea.getText());
			try {
				data.compile();
			} catch (CompileException e) {
				String fullMessage = "";
				for (Diagnostic<? extends JavaFileObject> diagnostic :
						e.getDiagnosticCollector().getDiagnostics()) {
						fullMessage += String.format("Error on line %d in %s: %s%n",
								diagnostic.getLineNumber(),
								diagnostic.getSource().toUri(),
								diagnostic.getMessage(Locale.getDefault()));
				}
				errorArea.setVisible(true);
				errorArea.setText(fullMessage);
				errorArea.updateUI();
			}
		});

		JScrollPane textAreaScrollPane = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textAreaScrollPane.setBorder(BorderFactory.createTitledBorder("Source"));
		
		contentBox.add(speedControlPanel);
		contentBox.add(textAreaScrollPane);
		contentBox.add(errorArea);
		contentBox.add(compileButton);
	}

}
