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

package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import io.tatlook.chaos.parser.ChaosFileParser;
import io.tatlook.chaos.parser.NullFileParser;

public class App {
	public static MainWindow mainWindow;
	public static void main(String[] args) {
		mainWindow = new MainWindow();
		
		try {
			if (args.length > 0) {
				File file = new File(args[0]);
				if (file.exists()) {
					new ChaosFileParser(file);
				} else {
					if (args[0].charAt(0) == '-') {
						JOptionPane.showMessageDialog(
								mainWindow,
								"This program doesn't have command-line options"
						);
					} else {
						// = Throw new FileNotFoundException.
						new ChaosFileParser(file);
					}
				}
			}
			new NullFileParser();
		} catch (FileNotFoundException e) {
			ErrorMessageDialog.createExceptionDialog(e);
			new NullFileParser();
		}
		
		try {
			ChaosFileParser.getCurrentFileParser().readChaos();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
		
		mainWindow.UI();
		mainWindow.setVisible(true);
		mainWindow.getDrawer().start();
	} 
}
