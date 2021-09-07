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

package io.tatlook.lchaos.saver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.ChaosFileChooser;
import io.tatlook.lchaos.ErrorMessageDialog;
import io.tatlook.lchaos.FileHistoryManager;
import io.tatlook.lchaos.FractalManager;
import io.tatlook.lchaos.FractalManager.Fractal;
import io.tatlook.lchaos.FileFormatNotFoundException;
import io.tatlook.lchaos.data.AbstractData;

/**
 * The parent class of all file savers.
 * 
 * @author YouZhe Zhen
 */
public abstract class AbstractFileSaver {
	protected PrintStream out;
	protected File file;
	
	/**
	 * Constructs a new file saver with the target file.
	 * 
	 * @param file
	 */
	public AbstractFileSaver(File file) {
		this.file = file;
		try {
			if (file == null) {
				throw new AssertionError();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new PrintStream(file);
		} catch (IOException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
	}
	
	/**
	 * Key file saving steps.
	 * The data is provided by given data.
	 */
	public abstract void save();
	
	public static AbstractFileSaver chooseAvailableSaver(File file) throws FileFormatNotFoundException {
		String extension = AbstractFileSaver.getFileExtension(file);
		Fractal[] fractals = FractalManager.get().getFractals();
		for (Fractal fractal : fractals) {
			Class<? extends AbstractFileSaver> saverClass = fractal.getAvailableSaverClass(extension);
			Class<? extends AbstractData> dataClass = fractal.getDataClass();
			if (saverClass != null) {
				try {
					return saverClass.getConstructor(File.class, dataClass)
							.newInstance(file, AbstractData.getCurrent());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		throw new FileFormatNotFoundException(file);
	}

	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean staticSave() {
		ChaosFileChooser fileChooser = new ChaosFileChooser(JFileChooser.SAVE_DIALOG);
		fileChooser.choose();
		File file = fileChooser.getFile();
		if (file == null) {
			return false;
		}
		try {
			chooseAvailableSaver(file).save();
		} catch (FileFormatNotFoundException e) {
			e.openDialog();
			return false;
		}
		AbstractData.getCurrent().setCurrentToOrigin();
		AbstractData.getCurrent().setChanged(false);
		
		FileHistoryManager.get().add(file);
		App.setCurrentFile(file);
		App.mainWindow.updateTitle();
		
		return true;
	}
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean checkFileSave() {
		if (AbstractData.getCurrent().isChanged()) {
			int result = ErrorMessageDialog.createSaveDialog();
			if (result == JOptionPane.YES_OPTION) {
				return staticSave();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}
	
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex != -1 && lastIndex != 0) {
			return fileName.substring(lastIndex + 1).toLowerCase();
		} else {
			return "";
		}
	}
}
