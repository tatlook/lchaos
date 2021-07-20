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

import java.io.File;
import java.io.FileNotFoundException;

import io.tatlook.lchaos.data.IFSData;

/**
 * @author Administrator
 *
 */
public abstract class IFSFileParser extends AbstractFileParser {
	protected IFSData data;
	
	public IFSFileParser(File file) throws FileNotFoundException {
		super(file);
	}
}
