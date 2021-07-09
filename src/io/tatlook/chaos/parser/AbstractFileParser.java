/**
 * 
 */
package io.tatlook.chaos.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import io.tatlook.chaos.ChaosData;
import io.tatlook.chaos.ChaosFileDataException;

/**
 * @author Administrator
 *
 */
public abstract class AbstractFileParser {
	protected static AbstractFileParser currentFileParser;
	
	protected File chaosFile;
	protected Scanner scanner;
	protected FileInputStream inputStream;
	protected ChaosData data;
	
	public AbstractFileParser(File file) throws FileNotFoundException {
		chaosFile = file;
		inputStream = new FileInputStream(file);
		scanner = new Scanner(inputStream);
		currentFileParser = this;
	}
	
	protected AbstractFileParser() {
	}
	
	public abstract void readChaos() throws ChaosFileDataException;
	
	public File getFile() {
		return chaosFile;
	}
	
	/**
	 * @return the currentFileParser
	 */
	public static AbstractFileParser getCurrentFileParser() {
		return currentFileParser;
	}
}
