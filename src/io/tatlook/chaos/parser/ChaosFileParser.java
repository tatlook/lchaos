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
import java.io.FileNotFoundException;

import io.tatlook.chaos.ChaosData;
import io.tatlook.chaos.ChaosFileDataException;

/**
 * @author Administrator
 *
 */
public class ChaosFileParser extends AbstractFileParser {
	public ChaosFileParser(File file) throws FileNotFoundException {
		super(file);
	}
	
	private double[] readDouble1D() throws ChaosFileDataException {
		if (!scanner.hasNextInt()) {
			throw new ChaosFileDataException(chaosFile);
		}
		int n = scanner.nextInt();
		if (n <= 0) {
			throw new ChaosFileDataException(chaosFile);
		}
		double[] a = new double[n];
		for (int i = 0; i < n; i++) {
			if (!scanner.hasNextDouble()) {
				throw new ChaosFileDataException(chaosFile);
			}
			a[i] = scanner.nextDouble();
		}
		return a;
	}
	
	private double[][] readDouble2D() throws ChaosFileDataException {
		if (!scanner.hasNextInt()) {
			throw new ChaosFileDataException(chaosFile);
		}
		int m = scanner.nextInt();
		if (!scanner.hasNextInt()) {
			throw new ChaosFileDataException(chaosFile);
		}
		int n = scanner.nextInt();
		if (n != 3 || m <= 0) {
			throw new ChaosFileDataException(chaosFile);
		}
		System.out.println("m=" + m + ";n=" + n);
		double[][] a = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (!scanner.hasNextDouble()) {
					throw new ChaosFileDataException(chaosFile);
				}
				a[i][j] = scanner.nextDouble();
				System.out.println("a[" + i + "][" + j + "]=" + a[i][j]);
			}
		}
		return a;
	}
	
	public void readChaos() throws ChaosFileDataException {
		data = new ChaosData(readDouble1D(),
				readDouble2D(),
				readDouble2D());
		if (data.getDist() == null || data.getCX() == null || data.getCY() == null) {
			throw new NullPointerException();
		}
		ChaosData.current = data;
		
		scanner.close();
	}
}
