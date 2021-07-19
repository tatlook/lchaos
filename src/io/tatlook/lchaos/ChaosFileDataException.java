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
