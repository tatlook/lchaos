/**
 * 
 */
package io.tatlook.chaos.parser;

import java.io.File;

import io.tatlook.chaos.ChaosData;

/**
 * @author Administrator
 *
 */
public class NullFileParser extends AbstractFileParser {
	public NullFileParser() {
		currentFileParser = this;
	}
	
	@Override
	public void readChaos() {
		data = new ChaosData();
		ChaosData.current = data;
	}
	
	@Override
	public File getFile() {
		return null;
	}
}
