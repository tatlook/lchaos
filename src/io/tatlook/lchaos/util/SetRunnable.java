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

package io.tatlook.lchaos.util;

/**
 * A functional interface that assigns a value to an object.
 * 
 * @author YouZhe Zhen
 */
@FunctionalInterface
public interface SetRunnable {
	
	/**
	 * A function that assigns a value to an object.
	 * 
	 * @param value to assign a value
	 * @throws NumberFormatException 
	 *         Don't be fooled by this type of exception, 
	 *         because not only is the number illegal,
	 *         it can be thrown if the assignment fails.
	 */
	public void set(String value) throws NumberFormatException;
}
