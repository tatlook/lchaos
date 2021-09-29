/**
 * 
 */
package io.tatlook.lchaos.formula;

import io.tatlook.lchaos.parser.NullFileParser;

/**
 * @author YouZhe Zhen
 *
 */
public class NullMandelbrotSetParser extends MandelbrotSetParser
		implements
			NullFileParser {

	@Override
	public MandelbrotSetData parse() {
		return new MandelbrotSetData();
	}

}
