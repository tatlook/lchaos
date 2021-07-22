/**
 * 
 */
package io.tatlook.lchaos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import io.tatlook.lchaos.parser.ChaosFileParser;
import io.tatlook.lchaos.parser.FractintFileParser;
import io.tatlook.lchaos.parser.NullFileParser;

/**
 * @author Administrator
 *
 */
class TestParameters {

	@Test
	void testNoFile() {
		String[] args = {
				"momo...';;;\"{}!323\n[]^<>^^**??\\/.", "dsakdkk"
		};
		App.main(args);
		assertTrue(ChaosFileParser.getCurrentFileParser() instanceof NullFileParser);
	}

	@Test
	void testFailureOption() {
		String[] args = {
				"--help", "dsakdkk"
		};
		App.main(args);
		assertTrue(ChaosFileParser.getCurrentFileParser() instanceof NullFileParser);
	}

	@Test
	void testErrorFile() {
		File file = new File("omomo");
		try {
			file.createNewFile();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		String[] args = {
				file.getName(), "dsakdkk"
		};
		App.main(args);
		file.delete();
		assertTrue(ChaosFileParser.getCurrentFileParser() instanceof NullFileParser);
	}
	
	@Test
	void testChaosFile() {
		File file = new File("omomo.ch");
		try {
			file.createNewFile();
			PrintStream out = new PrintStream(file);
			out.println("1");
			out.println("    0.0");
			out.println("1 3");
			out.println("    0.0 0.0 0.0");
			out.println("1 3");
			out.println("    0.0 0.0 0.0");
			out.println("Comments!!!");
			
			out.close();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		String[] args = {
				file.getName(), "dsakdkk"
		};
		App.main(args);
		file.delete();
		assertTrue(ChaosFileParser.getCurrentFileParser() instanceof ChaosFileParser);
	}
	
	@Test
	void testFractintIFSFile() {
		File file = new File("omomo.ifs");
		try {
			file.createNewFile();
			PrintStream out = new PrintStream(file);
			out.println("demo } +++++ haha haha hahah");
			out.println("demo {");
			out.println();
			out.println("; ignore !!!");
			out.println("    0.0 0.0 0.0 0.0 0.0 0.0 0.0 ; yes");
			out.println("    0.0 0.0 0.0 0.0 0.0 0.0 0.0 haha no !! no!! ");
			out.println("} kljlkj");
			
			out.close();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		String[] args = {
				file.getName(), "dsakdkk"
		};
		App.main(args);
		file.delete();
		assertTrue(ChaosFileParser.getCurrentFileParser() instanceof FractintFileParser);
	}

}
