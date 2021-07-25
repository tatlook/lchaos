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
/******************************************************************************
 *  Compilation:  javac StdArrayIO.java
 *  Execution:    java StdArrayIO < input.txt
 *  Dependencies: StdOut.java
 *  Data files:    https://introcs.cs.princeton.edu/java/22library/tinyDouble1D.txt
 *                 https://introcs.cs.princeton.edu/java/22library/tinyDouble2D.txt
 *                 https://introcs.cs.princeton.edu/java/22library/tinyBoolean2D.txt
 *
 *  A library for reading in 1D and 2D arrays of integers, doubles,
 *  and booleans from standard input and printing them out to
 *  standard output.
 *
 *  % more tinyDouble1D.txt 
 *  4
 *    .000  .246  .222  -.032
 *
 *  % more tinyDouble2D.txt 
 *  4 3 
 *    .000  .270  .000 
 *    .246  .224 -.036 
 *    .222  .176  .0893 
 *   -.032  .739  .270 
 *
 *  % more tinyBoolean2D.txt 
 *  4 3 
 *    1 1 0
 *    0 0 0
 *    0 1 1
 *    1 1 1
 *
 *  % cat tinyDouble1D.txt tinyDouble2D.txt tinyBoolean2D.txt | java StdArrayIO
 *  4
 *    0.00000   0.24600   0.22200  -0.03200 
 *  
 *  4 3
 *    0.00000   0.27000   0.00000 
 *    0.24600   0.22400  -0.03600 
 *    0.22200   0.17600   0.08930 
 *    0.03200   0.73900   0.27000 
 *
 *  4 3
 *  1 1 0 
 *  0 0 0 
 *  0 1 1 
 *  1 1 1 
 *
 ******************************************************************************/
/******************************************************************************
 *  Copyright 2002-2019, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/

package io.tatlook.lchaos.parser;

import java.io.File;
import java.io.FileNotFoundException;

import io.tatlook.lchaos.ChaosFileDataException;
import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.data.IFSData;

/**
 * @author Administrator
 *
 */
public class ChaosFileParser extends IFSFileParser {
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
	
	public void parse() throws ChaosFileDataException {
		data = new IFSData(readDouble1D(),
				readDouble2D(),
				readDouble2D());
		if (data.getDist() == null || data.getCX() == null || data.getCY() == null) {
			throw new NullPointerException();
		}
		AbstractData.setCurrent(data);
		
		scanner.close();
	}
}
