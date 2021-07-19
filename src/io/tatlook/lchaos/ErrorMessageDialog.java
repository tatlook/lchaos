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

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * @author Administrator
 *
 */
public class ErrorMessageDialog {
	/** Don't let anyone instantiate this class */
	private ErrorMessageDialog() {
	}
	
	public static void createExceptionDialog(Exception e) {
		String title = "Error";
		String message = e.getMessage();
		if (e instanceof IOException) {
			title = "Input/Output Error";
			if (e instanceof FileNotFoundException) {
				title = "File Does Not Exist";
			}
		}
		if (e instanceof ChaosFileDataException) {
			title = "File Format Error";
			message = "There is an error in file \"" + 
					((ChaosFileDataException) e).getFile().getPath() + "\"";
		}
		JOptionPane.showMessageDialog(
				App.mainWindow,
				message,
				title,
				JOptionPane.ERROR_MESSAGE
		);
	}
	
	public static int createSaveDialog() {
		int result = JOptionPane.showConfirmDialog(
				App.mainWindow,
				"If you don't save, your changes will be lost.",
				"Save the changes?",
				JOptionPane.YES_NO_CANCEL_OPTION
		);
		return result;
	}
}
