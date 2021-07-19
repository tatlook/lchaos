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

import java.util.Vector;

/**
 * @author Administrator
 *
 */
public class LSystemData extends AbstractData {
	private Vector<Rule> rules;
	private String axiom;
	private int angle;

	/**
	 * 
	 */
	public LSystemData() {
		rules = new Vector<>();
	}

	public Vector<Rule> getRules() {
		return rules;
	}

	public void addRule(char from, String to) {
		rules.add(new Rule(from, to));
	}

	public void addRule() {
		addRule('F', "");
	}

	public void setAxiom(String axiom) {
		this.axiom = axiom;
	}

	/**
	 * @return the axiom
	 */
	public String getAxiom() {
		return axiom;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	/**
	 * @return the angle
	 */
	public int getAngle() {
		return angle;
	}

	@Override
	public void removeRule(int index) {
		rules.remove(index);
	}
	
	public class Rule {
		public char from;
		public String to;
		public Rule(char from, String to) {
			this.from = from;
			this.to = to;
		}
	}
	
	public static LSystemData getCurrent() {
		if (!(current instanceof LSystemData)) {
			throw new IllegalStateException();
		}
		return (LSystemData) current;
	}
}
