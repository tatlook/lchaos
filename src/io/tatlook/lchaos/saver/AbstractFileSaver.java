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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.ChaosFileChooser;
import io.tatlook.lchaos.ErrorMessageDialog;
import io.tatlook.lchaos.FileHistoryManager;
import io.tatlook.lchaos.data.AbstractData;

/**
 * @author Administrator
 *
 */
public abstract class AbstractFileSaver {
	protected PrintStream out;
	protected File file;
	
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
	
	public abstract void save();
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean staticSave() {
		ChaosFileChooser fileChooser = new ChaosFileChooser(JFileChooser.SAVE_DIALOG);
		fileChooser.choose();
		File file = fileChooser.getChaosFile();
		if (file == null) {
			return false;
		}
		AbstractFileSaver saver;
		switch (getFileExtension(file)) {
			case "l":
			case "lsys":
				saver = new LSystemFileSaver(file);
				break;
			case "ifs":
				saver = new FractintFileSaver(file);
				break;
			case "ch":
			default: 
				saver = new ChaosFileSaver(file);
				break;
		};
		saver.save();
		AbstractData.getCurrent().setChanged(false);
		
		FileHistoryManager.get().add(file);
		App.mainWindow.setTitle(file);
		
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
