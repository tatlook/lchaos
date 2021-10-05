/**
 * 
 */
package io.tatlook.lchaos.formula;

import java.io.IOException;
import java.util.Scanner;

import io.tatlook.lchaos.ChaosFileDataException;
import io.tatlook.lchaos.parser.AbstractFileParser;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetParser extends AbstractFileParser {

	public MandelbrotSetParser() {
	}

	private MandelbrotSetData data;

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
			lineScanner.close();
			return;
		}
		
		String exp = lineScanner.next();
		int i = exp.indexOf('=');
		if (i == -1) {
			throw new ChaosFileDataException(file);
		}
		
	
		lineScanner.close();
	}
	
	@Override
	public MandelbrotSetData parse() throws ChaosFileDataException {
		data = new MandelbrotSetData();
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
			data.setCurrentToOrigin();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		return data;
	}

}
