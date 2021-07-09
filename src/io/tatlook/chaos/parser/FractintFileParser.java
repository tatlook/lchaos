/**
 * 
 */
package io.tatlook.chaos.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import io.tatlook.chaos.ChaosData;
import io.tatlook.chaos.ChaosFileDataException;

/**
 * @author Administrator
 *
 */
public class FractintFileParser extends AbstractFileParser {
	
	public FractintFileParser(File file) throws FileNotFoundException  {
		super(file);
	}
	
	private double readNumber(Scanner scanner) throws ChaosFileDataException {
		if (scanner.hasNextInt()) {
			return (double) scanner.nextInt();
		} else if (scanner.hasNextDouble()) {
			return scanner.nextDouble();
		} else {
			throw new ChaosFileDataException(chaosFile);
		}
	}
	
	private void scanLine(String line) throws ChaosFileDataException {
		Scanner lineScanner = new Scanner(line);
		
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
	public void readChaos() throws ChaosFileDataException {
		data = new ChaosData(new double[0], new double[0][0], new double[0][0]);
		try {
			while (true) {
				int c = inputStream.read();
				if (c == '{') {
					break;
				} else if (c == -1) {
					throw new ChaosFileDataException(chaosFile);
				}
			}
			// hypää nykyinen rivi ohi
			if (scanner.hasNextLine()) {
				scanner.nextLine();
			} else {
				throw new ChaosFileDataException(chaosFile);
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		ChaosData.current = data;
	}
}
