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
	/**
	 * 
	 */
	public RandomWalkParser() {
		super();
		currentFileParser = this;
	}

	@Override
	public void parse() {
		AbstractData.setCurrent(new RandomWalkData());
	}

}
