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

package io.tatlook.lchaos.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

import io.tatlook.lchaos.ChaosFileDataException;
import io.tatlook.lchaos.data.AbstractData;

/**
 * The parent class of all file parsers.
 * 
 * @author YouZhe Zhen
 */
public abstract class AbstractFileParser {
	private static AbstractFileParser currentFileParser;
	
	protected File file;
	protected Scanner scanner;
	protected FileInputStream inputStream;
	
	/**
	 * Constructs a new file parser with the target file.
	 * 
	 * @param file the target file
	 * @throws     FileNotFoundException  if the file does not exist,
	 *             is a directory rather than a regular file,
	 *             or for some other reason cannot be opened for
	 *             reading.
	 */
	public AbstractFileParser(File file) throws FileNotFoundException {
		this.file = file;
		inputStream = new FileInputStream(file);
		scanner = new Scanner(inputStream);
		scanner.useLocale(Locale.US);
		currentFileParser = this;
	}
	
	/**
	 * A way for {@link NullFileParser}
	 */
	protected AbstractFileParser() {
		currentFileParser = this;
	}
	
	/**
	 * Key file parsing steps.
	 * 
	 * @return the result of parsing the file
	 *
	 * @throws ChaosFileDataException the file format is incorrect
	 */
	public abstract AbstractData parse() throws ChaosFileDataException;
	
	/**
	 * Returns the target file of this file parser.
	 * 
	 * @return the target file of this {@code AbstractFileParser} instance
	 *          (which may be {@code null}).
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * @return the currentFileParser
	 */
	public static AbstractFileParser getCurrentFileParser() {
		return currentFileParser;
	}
}
