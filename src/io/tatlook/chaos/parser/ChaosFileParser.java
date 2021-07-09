/**
 * 
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
