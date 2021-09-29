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

/**
 * An internal file format error.
 * This exception should be raised by the file, not by an error of the parser itself.
 * 
 * @author YouZhe Zhen
 */
public class ChaosFileDataException extends Exception {
	private static final long serialVersionUID = 7983856589835980238L;

	private File file;
	
	/**
	 * Constructs a new ChaosFileDataException with the cause file.
	 * 
	 * @param   file   The cause file. The cause file is saved for
	 *          later retrieval by the {@link #getFile()} method.
	 */
	public ChaosFileDataException(File file) {
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
	 * @return  the cause file of this {@code ChaosFileDataException} instance
	 *          (which may be {@code null}).
	 */
	public File getFile() {
		return file;
	}
}
