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

	private LSystemData(String axiom, int angle, Rule[] rules, LSystemData origin) {
		super(origin);
		this.axiom = axiom;
		this.angle = angle;
		this.rules = new Vector<>();
		for (Rule rule : rules) {
			this.rules.add(new Rule(rule));
		}
	}
	
	public LSystemData(String axiom, int angle, Rule[] rules) {
		this(axiom, angle, rules, new LSystemData(axiom, angle, rules, null));
	}
	
	/**
	 * 
	 */
	public LSystemData() {
		this("", 0, new Rule[0]);
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
		
		public Rule(Rule copy) {
			this(copy.from, copy.to);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Rule)) {
				return false;
			}
			Rule other = (Rule) obj;
			if (from != other.from) {
				return false;
			}
			if (!to.equals(other.to)) {
				return false;
			}
			return true;
		}
	}
	
	@Override
	protected boolean equalsToOrigin() {
		LSystemData origin = (LSystemData) this.origin;
		if (angle != origin.angle) {
			return false;
		}
		if (!axiom.equals(origin.axiom)) {
			return false;
		}
		if (!rules.equals(origin.rules)) {
			return false;
		}
		return true;
	}

	@Override
	public void setCurrentToOrigin() {
		origin = new LSystemData(new String(axiom), angle, rules.toArray(new Rule[rules.size()]), null);
	}

	public static LSystemData getCurrent() {
		if (!(current instanceof LSystemData)) {
			throw new IllegalStateException();
		}
		return (LSystemData) current;
	}
}
