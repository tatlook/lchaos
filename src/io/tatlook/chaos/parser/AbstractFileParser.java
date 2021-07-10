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

package io.tatlook.chaos.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import io.tatlook.chaos.ChaosData;
import io.tatlook.chaos.ChaosFileDataException;

/**
 * @author Administrator
 *
 */
public abstract class AbstractFileParser {
	protected static AbstractFileParser currentFileParser;
	
	protected File chaosFile;
	protected Scanner scanner;
	protected FileInputStream inputStream;
	protected ChaosData data;
	
	public AbstractFileParser(File file) throws FileNotFoundException {
		chaosFile = file;
		inputStream = new FileInputStream(file);
		scanner = new Scanner(inputStream);
		currentFileParser = this;
	}
	
	protected AbstractFileParser() {
	}
	
	public abstract void readChaos() throws ChaosFileDataException;
	
	public File getFile() {
		return chaosFile;
	}
	
	/**
	 * @return the currentFileParser
	 */
	public static AbstractFileParser getCurrentFileParser() {
		return currentFileParser;
	}
}
