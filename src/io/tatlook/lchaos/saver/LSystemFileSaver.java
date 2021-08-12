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

package io.tatlook.lchaos.saver;

import java.io.File;
import java.util.Vector;

import io.tatlook.lchaos.data.LSystemData;

public class LSystemFileSaver extends AbstractFileSaver {

	private LSystemData data;

	public LSystemFileSaver(File file, LSystemData data) {
		super(file);
		this.data = data;
	}

	@Override
	public void save() {
		String axiom  = data.getAxiom();
		int angle = data.getAngle();
		Vector<LSystemData.Rule> rules = data.getRules();
		
		out.println(FractintFileSaver.getFileNameNoEx(file.getName()) + " {");
		out.println("Angle " + angle);
		out.println("Axiom " + axiom);
		for (int i = 0; i < rules.size(); i++) {
			LSystemData.Rule rule = rules.get(i);
			out.println(rule.from + "=" + rule.to);
		}
		out.println("}");
	}

}
