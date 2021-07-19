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

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.tatlook.lchaos.parser.AbstractFileParser;
import io.tatlook.lchaos.parser.ChaosFileParser;
import io.tatlook.lchaos.parser.FractintFileParser;
import io.tatlook.lchaos.parser.LSystemFileParser;
import io.tatlook.lchaos.saver.AbstractFileSaver;

/**
 * @author Administrator
 *
 */
public class ChaosFileChooser {
	private static final StartDirectoryManager manager = new StartDirectoryManager("chaoschoosedefault");
	private File chaosFile;
	private int dialogMode = JFileChooser.OPEN_DIALOG;
	public ChaosFileChooser(int dialogMode) {
		this.dialogMode = dialogMode;
	}

	public ChaosFileChooser() {
	}

	public void choose() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setCurrentDirectory(manager.getStartDirectory());
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plain Text(*.txt)", "txt"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("LSystem File(*.l)", "l"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Fractint IFS File(*.ifs)", "ifs"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Chaos File(*.ch)", "ch"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("All Supported Files", "ch", "ifs", "l", "txt"));
		
		int result;
		if (dialogMode == JFileChooser.OPEN_DIALOG) {
			result = fileChooser.showOpenDialog(App.mainWindow);
		} else {
			result = fileChooser.showSaveDialog(App.mainWindow);
		}
		File file = null;
		if (result == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			
			System.out.println("Open file: " + file.getAbsolutePath() + "\n\n");
			
			manager.setStartDirectory(fileChooser.getCurrentDirectory());
		}
		chaosFile = file;
	}
	
	/**
	 * @return the chaosFile
	 */
	public File getChaosFile() {
		return chaosFile;
	}
	
	public static void staticOpen(File file) throws FileNotFoundException {
		try {
			AbstractFileParser parser;
			switch (AbstractFileSaver.getFileExtension(file)) {
				case "l":
				case "lsys":
					parser = new LSystemFileParser(file);
					break;
				case "ifs":
					parser = new FractintFileParser(file);
					break;
				case "ch":
				default: 
					parser = new ChaosFileParser(file);
					break;
			};
			parser.readChaos();
			App.mainWindow.updateToolPanel();
			App.mainWindow.getDrawer().setChange();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
		
		FileHistoryManager.get().add(file);
	}
}
