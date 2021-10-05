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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

import io.tatlook.lchaos.data.AbstractData;

public class App {
	public static MainWindow mainWindow;
	public static void main(String[] args) {
		mainWindow = new MainWindow();
		
		try {
			if (args.length > 0) {
				File file = new File(args[0]);
				if (file.exists()) {
					try {
						AbstractData.setCurrent(ChaosFileChooser.chooseAvailableParser(file).parse());
						setCurrentFile(file);
						FileHistoryManager.get().add(file);
					} catch (FileFormatNotFoundException e) {
						e.openDialog();
					} catch (ChaosFileDataException e) {
						e.openDialog();
					}
				} else {
					if (args[0].charAt(0) == '-') {
						ErrorMessageDialog.createFailureOptionDialog();
					} else {
						throw new FileNotFoundException();
					}
				}
			}
		} catch (FileNotFoundException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
		
		mainWindow.UI();
		mainWindow.setVisible(true);
	}

	private static ResourceBundle bundle = ResourceBundle.getBundle("locale");

	public static String s(String key) {
		return bundle.getString(key);
	}

	public static String s(String key, Object... formats) {
		return String.format(bundle.getString(key), formats);
	}

	private static File currentFile;

	public static File getCurrentFile() {
		return currentFile;
	}

	public static void setCurrentFile(File file) {
		currentFile = file;
	}

}
