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

package io.tatlook.lchaos.data;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.drawer.AbstractDrawer;
import io.tatlook.lchaos.editor.AbstractEditor;
import io.tatlook.lchaos.parser.AbstractFileParser;

/**
 * Data of fractal drawer.
 * The instance of this class should be created by {@link AbstractFileParser parser},
 * edited by {@link AbstractEditor editor},
 * received by {@link AbstractDrawer drawer}.
 * 
 * @author YouZhe Zhen
 */
public abstract class AbstractData implements Cloneable {
	protected static AbstractData current;

	protected boolean changed;

	/**
	 * Data before editing.
	 */
	protected AbstractData origin;
	
	/**
	 * Constructs a new AbstractData with the initial data.
	 * 
	 * @param origin data before editing
	 */
	protected AbstractData(AbstractData origin) {
		this.origin = origin;
	}
	
	/**
	 * Set this instance copy to {@link #origin}.
	 */
	public abstract void setCurrentToOrigin();

	public void checkChanged() {
		boolean tmpchanged = changed;
		changed = !equals(origin);
		if (tmpchanged != changed) {
			App.mainWindow.updateTitle();
		}
	}
	
	/**
	 * Determines whether the file has been edited since it was opened.
	 * If the data is edited back to its {@link #origin original state},
	 * would be considered unedited.
	 * 
	 * @return {@code true} if data has been edited, {@code false} otherwise
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @return the current
	 */
	public static AbstractData getCurrent() {
		if (current == null) {
			current = new NullData();
		}
		return current;
	}

	/**
	 * @param current the current to set
	 */
	public static void setCurrent(AbstractData current) {
		AbstractData.current = current;
	}
}
