/*
 * Chaos - simple 2D iterated function system plotter and editor.
 * Copyright (C) 2021 YouZhe Zhen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import io.tatlook.lchaos.saver.AbstractFileSaver;

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
		if (AbstractFileSaver.checkFileSave() == false) {
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
