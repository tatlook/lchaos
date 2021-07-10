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

package io.tatlook.chaos.parser;

import java.io.File;

import io.tatlook.chaos.ChaosData;

/**
 * @author Administrator
 *
 */
public class NullFileParser extends AbstractFileParser {
	public NullFileParser() {
		currentFileParser = this;
	}
	
	@Override
	public void readChaos() {
		data = new ChaosData();
		ChaosData.current = data;
	}
	
	@Override
	public File getFile() {
		return null;
	}
}
