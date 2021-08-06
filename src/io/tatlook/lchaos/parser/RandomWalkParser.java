/**
 * 
 */
package io.tatlook.lchaos.parser;

import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.data.RandomWalkData;

/**
 * @author Administrator
 *
 */
public class RandomWalkParser extends AbstractFileParser implements NullFileParser {

	@Override
	public void parse() {
		AbstractData.setCurrent(new RandomWalkData());
	}

}
