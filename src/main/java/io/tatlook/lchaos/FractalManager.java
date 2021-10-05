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

import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.drawer.AbstractDrawer;
import io.tatlook.lchaos.drawer.RandomWalkDrawer;
import io.tatlook.lchaos.editor.AbstractEditor;
import io.tatlook.lchaos.formula.MandelbrotSetData;
import io.tatlook.lchaos.formula.MandelbrotSetDrawer;
import io.tatlook.lchaos.formula.MandelbrotSetEditor;
import io.tatlook.lchaos.formula.MandelbrotSetParser;
import io.tatlook.lchaos.formula.MandelbrotSetSaver;
import io.tatlook.lchaos.ifs.ChaosFileParser;
import io.tatlook.lchaos.ifs.ChaosFileSaver;
import io.tatlook.lchaos.ifs.FractintFileParser;
import io.tatlook.lchaos.ifs.FractintFileSaver;
import io.tatlook.lchaos.ifs.IFSData;
import io.tatlook.lchaos.ifs.IFSDrawer;
import io.tatlook.lchaos.ifs.IFSEditor;
import io.tatlook.lchaos.lsystem.LSystemData;
import io.tatlook.lchaos.lsystem.LSystemDrawer;
import io.tatlook.lchaos.lsystem.LSystemEditor;
import io.tatlook.lchaos.lsystem.LSystemFileParser;
import io.tatlook.lchaos.lsystem.LSystemFileSaver;
import io.tatlook.lchaos.parser.AbstractFileParser;
import io.tatlook.lchaos.randomwalk.RandomWalkData;
import io.tatlook.lchaos.randomwalk.RandomWalkEditor;
import io.tatlook.lchaos.saver.AbstractFileSaver;

/**
 * @author Administrator
 *
 */
public class FractalManager {
	private static FractalManager INSTANCE = new FractalManager();

	private List<Fractal> fractals = new Vector<>();

	private FractalManager() {
		Fractal ifsFractal = new Fractal("Iterated Function System",
				IFSData.class, IFSDrawer.class, IFSEditor.class,
				new FileFormat("ch", "Chaos File(*.ch)", ChaosFileParser.class, ChaosFileSaver.class),
				new FileFormat("ifs", "Fractint IFS File(*.ifs)", FractintFileParser.class, FractintFileSaver.class));
		registerFractal(ifsFractal);
		Fractal lSystemFractal = new Fractal("L-System",
				LSystemData.class, LSystemDrawer.class, LSystemEditor.class,
				new FileFormat("l", "LSystem File(*.l)", LSystemFileParser.class, LSystemFileSaver.class));
		registerFractal(lSystemFractal);
		Fractal randomWalkFractal = new Fractal("Random Walk",
				RandomWalkData.class, RandomWalkDrawer.class, RandomWalkEditor.class);
		registerFractal(randomWalkFractal);
		Fractal mandelbrotSetFractal = new Fractal("Formula",
				MandelbrotSetData.class, MandelbrotSetDrawer.class, MandelbrotSetEditor.class,
				new FileFormat("frm", "Formula File(*.frm)", MandelbrotSetParser.class, MandelbrotSetSaver.class));
		registerFractal(mandelbrotSetFractal);
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

		public Fractal(String description,
				Class<? extends AbstractData> dataClass,
				Class<? extends AbstractDrawer> drawerClass,
				Class<? extends AbstractEditor> editorClass,
				FileFormat... formats) {
			setDescription(description);
			setDataClass(dataClass);
			setDrawerClass(drawerClass);
			setEditorClass(editorClass);
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

	private JMenuItem saveMenuItem = new JMenuItem("Save");

	/**
	 * Returns the saveMenuItem.
	 * 
	 * @return the saveMenuItem
	 */
	public JMenuItem getSaveMenuItem() {
		return saveMenuItem;
	}

	/** XXX */
	void checkSaveMenuItemEnabled() {
		for (Fractal fractal : fractals) {
			if (fractal.getDataClass().equals(AbstractData.getCurrent().getClass())) {
				saveMenuItem.setEnabled(!fractal.formats.isEmpty());
				return;
			}
		}
		saveMenuItem.setEnabled(false);
	}

	/**
	 * @return the fractals
	 */
	public Fractal[] getFractals() {
		checkSaveMenuItemEnabled();
		return fractals.toArray(new Fractal[fractals.size()]);
	}

	public static void createFractal(Fractal fractal) {
		if (AbstractFileSaver.checkFileSave() == false) {
			return;
		}
		
		try {
			AbstractData.setCurrent(fractal.getDataClass().getConstructor().newInstance());
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
		
		App.setCurrentFile(null);
		App.mainWindow.update();
		App.mainWindow.getDrawer().setChange();
	}

	public static FractalManager get() {
		return INSTANCE;
	}

}
