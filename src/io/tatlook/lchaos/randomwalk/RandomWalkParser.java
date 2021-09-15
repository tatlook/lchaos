/**
 * 
 */
package io.tatlook.lchaos.randomwalk;

import io.tatlook.lchaos.parser.AbstractFileParser;
import io.tatlook.lchaos.parser.NullFileParser;

/**
 * @author Administrator
 *
 */
public class RandomWalkParser extends AbstractFileParser implements NullFileParser {

	@Override
	public RandomWalkData parse() {
		return new RandomWalkData();
	}

}
