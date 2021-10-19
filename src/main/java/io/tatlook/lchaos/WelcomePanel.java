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
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import io.tatlook.lchaos.FractalManager.Fractal;
import static io.tatlook.lchaos.App.s;

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
		createOpenRecentPanel();
	}
	
	private void createNewFractalPanel() {
		Box box = Box.createVerticalBox();
		
		box.setBorder(BorderFactory.createTitledBorder(s("welcome.new_fractal")));
		
		Fractal[] fractals = FractalManager.get().getFractals();
		for (Fractal fractal : fractals) {
			JButton button = new JButton(fractal.getDescription());
			button.addActionListener((e) -> FractalManager.createFractal(fractal));
			button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			box.add(button);
		}
		
		add(box, BorderLayout.WEST);
	}
	
	private void createOpenRecentPanel() {
		Box box = Box.createVerticalBox();
		
		box.setBorder(BorderFactory.createTitledBorder(s("welcome.open_recent")));
		
		File[] files = FileHistoryManager.get().getHistoryFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[files.length - 1 - i];
			@SuppressWarnings("serial")
			class OpenRecentButton extends JButton {
				public OpenRecentButton() {
					super(file.getAbsolutePath());
					addActionListener((e) -> {
						try {
							ChaosFileChooser.staticOpen(file);
						} catch (FileNotFoundException e1) {
							ErrorMessageDialog.createExceptionDialog(e1);
						}
					});
					setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
				}
			}
			box.add(new OpenRecentButton());
		}
		
		add(box, BorderLayout.EAST);
	}
}
