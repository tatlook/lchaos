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

import static io.tatlook.lchaos.App.s;

/**
 * The {@code ErrorMessageDialog} class contains several useful class fields
 * and methods. It cannot be instantiated. 
 * 
 * Among the facilities provided by the ErrorMessageDialog class are 
 * display to the user with a dialog box.
 * 
 * @author YouZhe Zhen
 */
public class ErrorMessageDialog {
	/** Don't let anyone instantiate this class */
	private ErrorMessageDialog() {
	}
	
	/**
	 * Create a dialog to describe some exception
	 * 
	 * @param e the exception to be displayed
	 */
	public static void createExceptionDialog(Exception e) {
		String title = s("errdialog.error");
		String message = e.getMessage();
		if (e instanceof IOException) {
			title = s("errdialog.ioerror");
			if (e instanceof FileNotFoundException) {
				title = s("errdialog.enofile");
			}
		}
		if (e instanceof ChaosFileDataException) {
			title = s("errdialog.filefmterror");
			message = s("errdialog.parseerror", ((ChaosFileDataException) e).getFile().getPath());
		}
		if (e instanceof FileFormatNotFoundException) {
			title = s("errdialog.unknownfmterror");
		}
		JOptionPane.showMessageDialog(
				App.mainWindow,
				message,
				title,
				JOptionPane.ERROR_MESSAGE
		);
	}
	
	/**
	 * Create a dialog and ask the user to save the file on the dialog.
	 */
	public static int createSaveDialog() {
		int result = JOptionPane.showConfirmDialog(
				App.mainWindow,
				s("errdialog.save.message"),
				s("errdialog.save.title"),
				JOptionPane.YES_NO_CANCEL_OPTION
		);
		return result;
	}

	/**
	 * Create a dialog to tell the user that this program has no options
	 */
	public static void createFailureOptionDialog() {
		JOptionPane.showMessageDialog(
				App.mainWindow,
				s("errdialog.no_command_line")
		);
	}

}
