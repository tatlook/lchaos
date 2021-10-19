/**
 * 
 */
package io.tatlook.lchaos.formula;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import io.tatlook.lchaos.ChaosFileDataException;
import io.tatlook.lchaos.parser.AbstractFileParser;
import parsii.tokenizer.ParseException;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetParser extends AbstractFileParser<MandelbrotSetData> {

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
			// if (scanner.hasNextLine()) {
			// 	String line = scanner.nextLine();
			// 	Scanner lineScanner = new Scanner(line);
			// 	if (line.indexOf('}') == -1) {
			// 		do {
			// 			try {
			// 				lineScanner.skip(",|[\\s]+,");
			// 			} catch (NoSuchElementException e) {
			// 			}
			// 			String varname = lineScanner.next("[a-zA-Z]+|[a-zA-Z][a-zA-Z0-9]+");
			// 			lineScanner.skip("=|[\\s]+=");
			// 			 lineScanner.next("[^,]");
			// 			System.out.println("yse");
			// 		//	data.addVariable(varname, exp);
			// 		} while (lineScanner.hasNext(",|[\\s]+,"));
			// 	}
			// 	lineScanner.close();
			// }
			// while (scanner.hasNextLine()) {
			// 	String line = scanner.nextLine();
			// 	if (line.indexOf('}') != -1) {
			// 		break;
			// 	}
			// 	scanLine(line);
			// }
			String javaCode = "";
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.indexOf('}') != -1) {
					break;
				}
				javaCode += line + '\n';
			}
			data.setJavaCode(javaCode);
			data.setCurrentToOrigin();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			throw new ChaosFileDataException(file);
		} finally {
			scanner.close();
		}
		
		return data;
	}

}
