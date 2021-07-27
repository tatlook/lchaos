/*
 * Chaos - simple 2D iterated function system plotter and editor.
 * Copyright (C) 2021 YouZhe Zhen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.tatlook.lchaos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import io.tatlook.lchaos.parser.AbstractFileParser;
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
		assertTrue(AbstractFileParser.getCurrentFileParser() instanceof NullFileParser);
	}

	@Test
	void testFailureOption() {
		String[] args = {
				"--help", "dsakdkk"
		};
		App.main(args);
		assertTrue(AbstractFileParser.getCurrentFileParser() instanceof NullFileParser);
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
		assertTrue(AbstractFileParser.getCurrentFileParser() instanceof NullFileParser);
	}
	
	@Test
	void testChaosFile() {
		File file = new File("om.ch");
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
		assertTrue(AbstractFileParser.getCurrentFileParser() instanceof ChaosFileParser);
		while (true) {
			try {
				synchronized (this) {
					wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		assertTrue(AbstractFileParser.getCurrentFileParser() instanceof FractintFileParser);
	}

}
