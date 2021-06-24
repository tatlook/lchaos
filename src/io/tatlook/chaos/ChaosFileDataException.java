/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;

/**
 * @author Administrator
 *
 */
public class ChaosFileDataException extends Exception {
	private static final long serialVersionUID = 7983856589835980238L;

	private File file;
	
	public ChaosFileDataException(File file) {
		this.file = file;
	}
	
	public void openDialog() {
		ErrorMessageDialog.createExceptionDialog(this);
	}

	public File getFile() {
		return file;
	}
}
