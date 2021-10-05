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

package io.tatlook.lchaos.ifs;

import java.io.IOException;
import java.util.Scanner;

import io.tatlook.lchaos.ChaosFileDataException;

/**
 * @author Administrator
 *
 */
public class FractintFileParser extends IFSFileParser {
	
	private double readNumber(Scanner scanner) throws ChaosFileDataException {
		if (scanner.hasNextInt()) {
			return (double) scanner.nextInt();
		} else if (scanner.hasNextDouble()) {
			return scanner.nextDouble();
		} else {
			throw new ChaosFileDataException(file);
		}
	}
	
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
			lineScanner.close();
			return;
		}
		
		Double[] cx = new Double[3];
		cx[0] = readNumber(lineScanner);
		cx[1] = readNumber(lineScanner);
		
		Double[] cy = new Double[3];
		cy[0] = readNumber(lineScanner);
		cy[1] = readNumber(lineScanner);
		
		cx[2] = readNumber(lineScanner);
		cy[2] = readNumber(lineScanner);
		
		double dist;
		if (!lineScanner.hasNextDouble()) {
			dist = 0.5;
		} else {
			dist = lineScanner.nextDouble();
		}
		
		data.addRule(dist, cx, cy);
	
		lineScanner.close();
	}
	
	@Override
	public IFSData parse() throws ChaosFileDataException {
		data = new IFSData(new double[0], new double[0][0], new double[0][0]);
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
			if (data.getDist().length == 0) {
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
