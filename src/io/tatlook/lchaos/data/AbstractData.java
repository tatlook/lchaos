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
import io.tatlook.lchaos.parser.ChaosFileParser;

/**
 * @author Administrator
 *
 */
public abstract class AbstractData {
	protected static AbstractData current;

	protected boolean changed;
	/**
	 * 
	 */
	public AbstractData() {
		// TODO Auto-generated constructor stub
	}

	public void setChanged(boolean changed) {
		boolean thischanged = this.changed;
		this.changed = changed;
		if (thischanged != changed) {
			App.mainWindow.setTitle(ChaosFileParser.getCurrentFileParser().getFile());    		
		}
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	public abstract void addRule();
	public abstract void removeRule(int index);

	/**
	 * @return the current
	 */
	public static AbstractData getCurrent() {
		return current;
	}

	/**
	 * @param current the current to set
	 */
	public static void setCurrent(AbstractData current) {
		AbstractData.current = current;
	}
}