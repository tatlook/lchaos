/**
 * 
 */
package io.tatlook.lchaos.formula;

import java.io.IOException;
import java.util.NoSuchElementException;

import io.tatlook.lchaos.ChaosFileDataException;
import io.tatlook.lchaos.parser.AbstractFileParser;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetParser extends AbstractFileParser<MandelbrotSetData> {

	private MandelbrotSetData data;
	
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
			String javaCode = "";
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.indexOf('}') != -1) {
					break;
				}
				javaCode += line + '\n';
			}
			data.setFormulaCode(javaCode);
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
