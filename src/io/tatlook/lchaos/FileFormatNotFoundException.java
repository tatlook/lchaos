/**
 * 
 */
package io.tatlook.lchaos;

import java.io.File;

/**
 * An exception thrown when a suitable file type cannot be found.
 * 
 * @author YouZhe Zhen
 */
public class FileFormatNotFoundException extends Exception {
	private static final long serialVersionUID = -8101625206720935229L;

	private File file;
	
	/**
	 * Constructs a new FileFormatNotFoundException with the cause file.
	 * 
	 * @param   file   The cause file. The cause file is saved for
	 *          later retrieval by the {@link #getFile()} method.
	 */
	public FileFormatNotFoundException(File file) {
		super("An error while opening file " + file + ": unknown file format");
		this.file = file;
	}

	/**
	 * Displays a dialog describing the exception
	 * 
	 * @see ErrorMessageDialog#createExceptionDialog(Exception)
	 */
	public void openDialog() {
		ErrorMessageDialog.createExceptionDialog(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated Should use {@link #openDialog()}, because this exception is user-facing.
	 */
	@Deprecated
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}

	/**
	 * Returns the cause file of this exception.
	 * 
	 * @return  the cause file of this {@code FileFormatNotFoundException} instance
	 *          (which may be {@code null}).
	 */
	public File getFile() {
		return file;
	}
}
