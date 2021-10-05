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

package io.tatlook.lchaos.lsystem;

import java.io.IOException;
import java.util.Scanner;

import io.tatlook.lchaos.ChaosFileDataException;
import io.tatlook.lchaos.parser.AbstractFileParser;

/**
 * @author Administrator
 *
 */
public class LSystemFileParser extends AbstractFileParser {
	private LSystemData data;
	
	@SuppressWarnings("resource")
	private void scanLine(String line) throws ChaosFileDataException {
		{
			// Poista kommentti
			int index = line.indexOf(';');
			if (index != -1) {
				line = line.substring(0, index);
			}
		}
		Scanner lineScanner = new Scanner(line);
		if (!lineScanner.hasNext()) {
			return;
		}
		
		String firstString = lineScanner.next();
		if (firstString.equalsIgnoreCase("axiom")) {
			if (!lineScanner.hasNext()) {
				throw new ChaosFileDataException(file);
			}
			data.setAxiom(lineScanner.next());
			return;
		} else if (firstString.equalsIgnoreCase("angle")) {
			if (!lineScanner.hasNextInt()) {
				throw new ChaosFileDataException(file);
			}
			data.setAngle(lineScanner.nextInt());
			return;
		} else {
			lineScanner.close();
			lineScanner = new Scanner(line);
		}
		
		if (!lineScanner.hasNext()) {
			throw new ChaosFileDataException(file);
		}
		
		String equation = lineScanner.next();
		if (equation.length() < 3) {
			throw new ChaosFileDataException(file);
		}
		char from = equation.charAt(0);
		String to = equation.substring(2);
		
		data.addRule(from, to);
		
		lineScanner.close();
	}

	@Override
	public LSystemData parse() throws ChaosFileDataException {
		data = new LSystemData();
		try {
			while (true) {
				int c = inputStream.read();
				if (c == '{') {
					break;
				} else if (c == -1) {
					throw new ChaosFileDataException(file);
				}
			}
			// hypää nykyinen rivi ohi
			if (scanner.hasNextLine()) {
				scanner.nextLine();
			} else {
				throw new ChaosFileDataException(file);
			}
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.indexOf('}') != -1) {
					break;
				}
				scanLine(line);
			}
			if (data.getRules().isEmpty()) {
				data.addRule();
			}
			data.setCurrentToOrigin();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		return data;
	}

}
