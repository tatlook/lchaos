/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import edu.princeton.cs.algs4.InplaceMSD;
import edu.princeton.cs.algs4.StdArrayIO;
import edu.princeton.cs.algs4.StdIn;

/**
 * @author Administrator
 *
 */
public class ChaosFileParser {
	private static ChaosFileParser currentFileParser;
	private Scanner scanner;
	private FileInputStream inputStream;
	public ChaosFileParser(File file) throws FileNotFoundException {
		inputStream = new FileInputStream(file);
		scanner = new Scanner(inputStream);
		currentFileParser = this;
	}
	
	ChaosData data = new ChaosData();
	// 每个变换的执行概率
    double[] dist;
	
	// 矩阵值
    double[][] cx;
    double[][] cy;
	
    double[] readDouble1D() {
        int n = scanner.nextInt();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextDouble();
        }
        return a;
    }
    
    double[][] readDouble2D() {
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        System.out.println("m=" + m + ";n=" + n);
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = scanner.nextDouble();
                System.out.println("a[i][j]=" + a[i][j]);
            }
        }
        return a;
    }
    
	public void readChaos() {
		// 每个变换的执行概率
        data.dist = dist = readDouble1D();
        data.cx = cx = readDouble2D();
        data.cy = cy = readDouble2D();
		if (dist == null || cx == null || cy == null) {
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
