/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Administrator
 *
 */
public class ChaosFileParser {
	private static ChaosFileParser currentFileParser;
	
	private File chaosFile;
	private Scanner scanner;
	private FileInputStream inputStream;
	public ChaosFileParser(File file) throws FileNotFoundException {
		chaosFile = file;
		inputStream = new FileInputStream(file);
		scanner = new Scanner(inputStream);
		currentFileParser = this;
	}
	
	ChaosData data = new ChaosData();
	
    private double[] readDouble1D() throws ChaosFileDataException {
    	if (!scanner.hasNextInt()) {
			throw new ChaosFileDataException(ChaosFileDataException.DIST_ERROR);
		}
        int n = scanner.nextInt();
        if (n <= 0) {
			throw new ChaosFileDataException(ChaosFileDataException.DIST_ERROR);
		}
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
        	if (!scanner.hasNextDouble()) {
    			throw new ChaosFileDataException(ChaosFileDataException.DIST_ERROR);
    		}
            a[i] = scanner.nextDouble();
        }
        return a;
    }
    
    private double[][] readDouble2D() throws ChaosFileDataException {
    	if (!scanner.hasNextInt()) {
			throw new ChaosFileDataException(ChaosFileDataException.CX_CY_ERROR);
		}
        int m = scanner.nextInt();
        if (!scanner.hasNextInt()) {
			throw new ChaosFileDataException(ChaosFileDataException.CX_CY_ERROR);
		}
        int n = scanner.nextInt();
        if (n != 3 || m <= 0) {
        	throw new ChaosFileDataException(ChaosFileDataException.CX_CY_ERROR);
        }
        System.out.println("m=" + m + ";n=" + n);
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
            	if (!scanner.hasNextDouble()) {
        			throw new ChaosFileDataException();
        		}
                a[i][j] = scanner.nextDouble();
                System.out.println("a[" + i + "][" + j + "]=" + a[i][j]);
            }
        }
        return a;
    }
    
	public void readChaos() throws ChaosFileDataException {
		try {
			// Lue alusta
			inputStream.close();
			inputStream = new FileInputStream(chaosFile);
			scanner = new Scanner(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = new ChaosData(readDouble1D(),
				readDouble2D(),
				readDouble2D());
		if (data.getDist() == null || data.getCX() == null || data.getCY() == null) {
			throw new NullPointerException();
		}
		ChaosData.current = data;
	}

	/**
	 * @return the currentFileParser
	 */
	public static ChaosFileParser getCurrentFileParser() {
		return currentFileParser;
	}
}
