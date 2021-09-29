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

package io.tatlook.lchaos.parser;

import io.tatlook.lchaos.data.AbstractData;

/**
 * Null object of AbstractFileParser.
 * This doesn't actually parse any files, 
 * but {@link #parse} still returns a valid data.
 * 
 * @author YouZhe Zhen
 */
public interface NullFileParser {
	
	/**
	 * Parse files that do not exist.
	 * That is return a default data.
	 * 
	 * @return a default data, must not be null
	 * 
	 * @see AbstractFileParser#parse()
	 */
	public AbstractData parse();
}
