/**
 * 
 */
package io.tatlook.lchaos.parser;

import io.tatlook.lchaos.data.MandelbrotSetData;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetParser extends AbstractFileParser implements NullFileParser {

	@Override
	public MandelbrotSetData parse() {
		return new MandelbrotSetData();
	}

}
