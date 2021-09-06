/**
 * 
 */
package io.tatlook.lchaos.parser;

import io.tatlook.lchaos.data.MandelbrotSetData;

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
