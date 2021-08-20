/**
 * 
 */
package io.tatlook.lchaos.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import io.tatlook.lchaos.App;

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
		
		contentBox.add(speedControlPanel);
	}

}
