/**
 * 
 */
package io.tatlook.lchaos.parser;

import io.tatlook.lchaos.data.RandomWalkData;

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
