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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.data.IFSData;
import io.tatlook.lchaos.data.LSystemData;
import io.tatlook.lchaos.data.RandomWalkData;
import io.tatlook.lchaos.drawer.AbstractDrawer;
import io.tatlook.lchaos.drawer.IFSDrawer;
import io.tatlook.lchaos.drawer.LSystemDrawer;
import io.tatlook.lchaos.drawer.RandomWalkDrawer;
import io.tatlook.lchaos.editor.AbstractEditor;
import io.tatlook.lchaos.editor.IFSEditor;
import io.tatlook.lchaos.editor.LSystemEditor;
import io.tatlook.lchaos.editor.RandomWalkEditor;
import io.tatlook.lchaos.parser.AbstractFileParser;
import io.tatlook.lchaos.parser.ChaosFileParser;
import io.tatlook.lchaos.parser.FractintFileParser;
import io.tatlook.lchaos.parser.LSystemFileParser;
import io.tatlook.lchaos.parser.NullFileParser;
import io.tatlook.lchaos.parser.NullIFSFileParser;
import io.tatlook.lchaos.parser.NullLSystemFileParser;
import io.tatlook.lchaos.parser.RandomWalkParser;
import io.tatlook.lchaos.saver.AbstractFileSaver;
import io.tatlook.lchaos.saver.ChaosFileSaver;
import io.tatlook.lchaos.saver.FractintFileSaver;
import io.tatlook.lchaos.saver.LSystemFileSaver;

/**
 * @author Administrator
 *
 */
public class FractalManager {
	private static FractalManager INSTANCE = new FractalManager();

	private List<Fractal> fractals = new Vector<>();

	private FractalManager() {
		Fractal ifsFractal = new Fractal("Iterated Function System",
				IFSData.class, IFSDrawer.class, IFSEditor.class, NullIFSFileParser.class,
				new FileFormat("ch", "Chaos File(*.ch)", ChaosFileParser.class, ChaosFileSaver.class),
				new FileFormat("ifs", "Fractint IFS File(*.ifs)", FractintFileParser.class, FractintFileSaver.class));
		registerFractal(ifsFractal);
		Fractal lSystemFractal = new Fractal("L-System",
				LSystemData.class, LSystemDrawer.class, LSystemEditor.class, NullLSystemFileParser.class,
				new FileFormat("l", "LSystem File(*.l)", LSystemFileParser.class, LSystemFileSaver.class));
		registerFractal(lSystemFractal);
		Fractal randomWalkFractal = new Fractal("Random Walk",
				RandomWalkData.class, RandomWalkDrawer.class, RandomWalkEditor.class, 
				RandomWalkParser.class);
		registerFractal(randomWalkFractal);
	}

	public static class FileFormat {
		private String extension;
		private String description;
		private Class<? extends AbstractFileParser> parserClass;
		private Class<? extends AbstractFileSaver> saverClass;

		public FileFormat(String extension, String description,
				Class<? extends AbstractFileParser> parserClass,
				Class<? extends AbstractFileSaver> saverClass) {
			this.extension = extension;
			this.description = description;
			this.parserClass = parserClass;
			this.saverClass = saverClass;
		}
		
		public FileFilter toFileFilter() {
			return new FileNameExtensionFilter(description, extension);
		}

		/**
		 * @return the extension
		 */
		public String getExtension() {
			return extension;
		}

		/**
		 * @param extension the extension to set
		 */
		public void setExtension(String extension) {
			this.extension = extension;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the parserClass
		 */
		public Class<? extends AbstractFileParser> getParserClass() {
			return parserClass;
		}

		/**
		 * @param parserClass the parserClass to set
		 */
		public void setParserClass(Class<? extends AbstractFileParser> parserClass) {
			this.parserClass = parserClass;
		}

		/**
		 * @return the saverClass
		 */
		public Class<? extends AbstractFileSaver> getSaverClass() {
			return saverClass;
		}

		/**
		 * @param saverClass the saverClass to set
		 */
		public void setSaverClass(Class<? extends AbstractFileSaver> saverClass) {
			this.saverClass = saverClass;
		}
	}

	public static class Fractal {

		private List<FileFormat> formats = new Vector<>();
		private String description;
		private Class<? extends AbstractData> dataClass;
		private Class<? extends AbstractDrawer> drawerClass;
		private Class<? extends AbstractEditor> editorClass;
		private Class<? extends NullFileParser> nullParserClass;

		public Fractal(String description,
				Class<? extends AbstractData> dataClass,
				Class<? extends AbstractDrawer> drawerClass,
				Class<? extends AbstractEditor> editorClass,
				Class<? extends NullFileParser> nullParserClass,
				FileFormat... formats) {
			setDescription(description);
			setDataClass(dataClass);
			setDrawerClass(drawerClass);
			setEditorClass(editorClass);
			setNullParserClass(nullParserClass);
			for (FileFormat fileFormat : formats) {
				this.formats.add(fileFormat);
			}
		}

		public void addFormat(FileFormat format) {
			formats.add(format);
		}

		/**
		 * @return the formats
		 */
		public FileFormat[] getFormats() {
			return formats.toArray(new FileFormat[formats.size()]);
		}

		public Class<? extends AbstractFileParser> getAvailableParserClass(String extension) {
			for (FileFormat fileFormat : formats) {
				if (fileFormat.extension.equals(extension)) {
					return fileFormat.parserClass;
				}
			}
			return null;
		}

		public Class<? extends AbstractFileSaver> getAvailableSaverClass(String extension) {
			for (FileFormat fileFormat : formats) {
				if (fileFormat.extension.equals(extension)) {
					return fileFormat.saverClass;
				}
			}
			return null;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the dataClass
		 */
		public Class<? extends AbstractData> getDataClass() {
			return dataClass;
		}

		/**
		 * @param dataClass
		 *            the dataClass to set
		 */
		public void setDataClass(Class<? extends AbstractData> dataClass) {
			this.dataClass = dataClass;
		}

		/**
		 * @return the editorClass
		 */
		public Class<? extends AbstractEditor> getEditorClass() {
			return editorClass;
		}

		/**
		 * @param editorClass
		 *            the editorClass to set
		 */
		public void setEditorClass(
				Class<? extends AbstractEditor> editorClass) {
			this.editorClass = editorClass;
		}

		/**
		 * @return the drawerClass
		 */
		public Class<? extends AbstractDrawer> getDrawerClass() {
			return drawerClass;
		}

		/**
		 * @param drawerClass
		 *            the drawerClass to set
		 */
		public void setDrawerClass(
				Class<? extends AbstractDrawer> drawerClass) {
			this.drawerClass = drawerClass;
		}

		/**
		 * @return the nullParserClass
		 */
		public Class<? extends NullFileParser> getNullParserClass() {
			return nullParserClass;
		}

		/**
		 * @param nullParserClass the nullParserClass to set
		 */
		public void setNullParserClass(Class<? extends NullFileParser> nullParserClass) {
			this.nullParserClass = nullParserClass;
		}
	}

	public void registerFractal(Fractal fractal) {
		fractals.add(fractal);
	}

//	public Class<? extends AbstractFileParser> getAvailableParser(String ext) {
//		return getAvailableParser(new ExtensionFilter("", "nttt"));
//	}
//
//	public Class<? extends AbstractFileParser> getAvailableParser(
//			FileFilter filter) {
//		for (Fractal fractal : fractals) {
//			if (fractal.getAvailableParser(filter) != null) {
//				return fractal.getAvailableParser(filter);
//			}
//		}
//		throw new AssertionError(filter);
//	}

	public FileFormat[] getAvailableFileFormats(Class<? extends AbstractData> dataClass) {
		for (Fractal fractal : fractals) {
			if (fractal.dataClass.equals(dataClass)) {
				return fractal.getFormats();
			}
		}
		throw new AssertionError();
	}

	public FileFormat[] getAllFileFormats() {
		List<FileFormat> formats = new ArrayList<>();
		for (Fractal fractal : fractals) {
			formats.addAll(fractal.formats);
		}
		return formats.toArray(new FileFormat[formats.size()]);
	}

	/**
	 * @return the fractals
	 */
	public Fractal[] getFractals() {
		return fractals.toArray(new Fractal[fractals.size()]);
	}

	public static void createFractal(Class<? extends NullFileParser> parser) {
		if (AbstractFileSaver.checkFileSave() == false) {
			return;
		}
		
		try {
			AbstractData.setCurrent(parser.getConstructor().newInstance().parse());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		App.mainWindow.update();
		App.mainWindow.getDrawer().setChange();
	}

	public static FractalManager get() {
		return INSTANCE;
	}

}
