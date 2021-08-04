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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

/**
 * This class is used to record the location of a file chooser.
 * The principle is to save the data to a file and read the data
 * from the file the next tim use it.
 * 
 * @author YouZhe Zhen
 * 
 * @see ChaosFileChooser
 * @see ImageFileChooser
 */
public class StartDirectoryManager {
	private final File dataFile;
	private static final File defaultDirectory = new File(".");
	private static final String DATA_PATH = "data";
	
	/**
	 * Constructs a new StartDirectoryManager with the data file name.
	 * The data will be saved to the given file and read from it the next time use it.
	 * 
	 * @param dataFileName File path from the data directory
	 */
	public StartDirectoryManager(String dataFileName) {
		dataFile = new File(DATA_PATH + File.separator + dataFileName);
	}
	
	/**
	 * Sets the start directory.
	 * The directory will be recorded in the data file.
	 * 
	 * @param directory 
	 */
	public void setStartDirectory(File directory) {
		try {
			createDataFile();
		} catch (IOException e) {
			return;
		}
		try {
			Writer out = new BufferedWriter(new FileWriter(dataFile));
			out.append(directory.getPath());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 * Returns the start directory.
	 * Read data from the data file.
	 * 
	 * @return the start directory
	 */
	public File getStartDirectory() {
		try {
			createDataFile();
		} catch (IOException e) {
			return defaultDirectory;
		}
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(dataFile);
			if (!scanner.hasNextLine()) {
				return defaultDirectory;
			}
			File directory = new File(scanner.nextLine());
			if (!directory.exists()) {
				return defaultDirectory;
			}
			return directory;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return defaultDirectory;
	}
	
	private void createDataFile() throws IOException {
		if (!dataFile.isFile()) {
			createDataDirectory();
			if (!dataFile.createNewFile()) {
				throw new IOException();
			}
		}
	}

	private static void createDataDirectory() throws IOException {
		File dataDirectory = new File(DATA_PATH);
		if (!dataDirectory.isDirectory()) {
			if (!dataDirectory.mkdir()) {
				throw new IOException();
			}
		}
	}
}
