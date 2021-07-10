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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

/**
 * @author Administrator
 *
 */
public class StartDirectoryManager {
	private final File dataFile;
	private static final File defaultDirectory = new File(".");
	private static final String DATA_PATH = "data";
	
	public StartDirectoryManager(String dataFileName) {
		dataFile = new File(DATA_PATH + File.separator + dataFileName);
	}
	
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
