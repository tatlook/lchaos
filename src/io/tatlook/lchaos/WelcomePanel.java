/**
 * 
 */
package io.tatlook.lchaos;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import io.tatlook.lchaos.parser.NullFileParser;
import io.tatlook.lchaos.parser.NullIFSFileParser;
import io.tatlook.lchaos.parser.NullLSystemFileParser;
import io.tatlook.lchaos.saver.ChaosFileSaver;

/**
 * @author Administrator
 *
 */
public class WelcomePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6026304110689149742L;

	/**
	 * 
	 */
	public WelcomePanel() {
		super(new BorderLayout());
		
		createNewFractalPanel();
	}
	
	private void createNewFractalPanel() {
		Box box = Box.createVerticalBox();
		
		box.setBorder(BorderFactory.createTitledBorder("New Fractal"));
		
		@SuppressWarnings("serial")
		class FractalCreateButton extends JButton {
			public FractalCreateButton(String name, Class<? extends NullFileParser> parser) {
				super(name);
				addActionListener((e) -> createFractal(parser));
				setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		}
		
		box.add(new FractalCreateButton("Iterated Function System", NullIFSFileParser.class));
		box.add(new FractalCreateButton("L-System", NullLSystemFileParser.class));
		
		add(box, BorderLayout.WEST);
	}
	
	public static void createFractal(Class<? extends NullFileParser> parser) {
		if (ChaosFileSaver.checkFileSave() == false) {
			return;
		}
		
		try {
			parser.getConstructor().newInstance().parse();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		App.mainWindow.update();
		App.mainWindow.getDrawer().setChange();
	}
}
