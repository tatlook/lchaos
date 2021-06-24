/**
 * 
 */
package io.tatlook.chaos;

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
