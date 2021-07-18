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

package io.tatlook.chaos;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.tatlook.chaos.saver.AbstractFileSaver;

public class ImageFileChooser {
	private static final StartDirectoryManager manager = new StartDirectoryManager("imagechoosedefault");
	private String imageType;
	private File imageFile;
	public void choose() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setCurrentDirectory(manager.getStartDirectory());
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG (*.jpg, *.jpeg, *.jpe, *.jfif)", "jpg", "jpeg", "jpe", "jfif"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TIFF (*.tiff, *.tif)", "tiff", "tif"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap (*.bmp, *.dib)", "bmp", "dib"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF (*.gif)", "gif"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));
		
		int result = fileChooser.showSaveDialog(App.mainWindow);
		
		File file = null;
		if (result == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			switch (AbstractFileSaver.getFileExtension(file)) {
				case "jpg":
				case "jpeg":
				case "jpe":
				case "jfif": 
					imageType = "jpg";
					break;
				case "tif":
				case "tiff":
					imageType = "tif";
					break;
				case "bmp":
				case "dib":
					imageType = "bmp";
					break;
				case "gif":
					imageType = "gif";
					break;
				case "png" :
				default: 
					imageType = "png";
					break;
			};
			System.out.println("Save: " + file.getAbsolutePath() + "\n\n");
			manager.setStartDirectory(fileChooser.getCurrentDirectory());
		}
		imageFile = file;
	}
	
	/**
	 * @return the imageFile
	 */
	public File getImageFile() {
		return imageFile;
	}
	
	public String getImageType() {
		return imageType;
	}
}
