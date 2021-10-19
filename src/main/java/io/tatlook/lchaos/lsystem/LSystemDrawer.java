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

package io.tatlook.lchaos.lsystem;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;

import io.tatlook.lchaos.drawer.AbstractDrawer;

/**
 * @author Administrator
 *
 */
public class LSystemDrawer extends AbstractDrawer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3386161998747360254L;

	/**
	 * 
	 */
	public LSystemDrawer() {
		super();
	}

	@Override
	public void run() {
		Graphics g;

		Vector<LSystemData.Rule> rules;
		String axiom;
		int angle;
		int len = 10;
		mainloop: while (true) {
			rules = LSystemData.getCurrent().getRules();
			axiom = LSystemData.getCurrent().getAxiom();
			angle = LSystemData.getCurrent().getAngle();
			g = image.getGraphics();
			g.setColor(penColor);
			hasChange = false;
			
			g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), imageWidth / 6));
			g.drawString("Thinking...", 0, imageHeight / 2);
			repaint();
			for (int i = 0; i < level; i++) {
				String newString = new String(axiom);
				for (int j = 0; j < axiom.length(); j++) {
					char c = axiom.charAt(j);
					for (int k = 0; k < rules.size(); k++) {
						if (c == rules.get(k).from) {
							newString += rules.get(k).to;
						}
					}
					if (hasChange) {
						continue mainloop;
					}
				}
				axiom = newString;
			}
			g.clearRect(0, 0, imageWidth, imageHeight);
			
			Stack<Point> pointStack = new Stack<>();
			Stack<Integer> angleStack  = new Stack<>();
			Point currentPoint = new Point(imageWidth / 2, imageHeight / 2);
			int currentAngle = 0;
			for (int i = 0; i < axiom.length(); i++) {
				switch (axiom.charAt(i)) {
					case 'F' :
						Point lastPoint = currentPoint;
						currentPoint = new Point(lastPoint);
						currentPoint.x += len * Math.cos(currentAngle * 180 / Math.PI);
						currentPoint.y -= len * Math.sin(currentAngle * 180 / Math.PI);
						g.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
						break;
					case '+' :
						currentAngle += angle;
						break;
					case '-' :
						currentAngle -= angle;
						break;
					case '[' :
						pointStack.push((Point) currentPoint.clone());
						angleStack.push(currentAngle);
						break;
					case ']' :
						try {
							currentPoint = pointStack.pop();
							currentAngle = angleStack.pop();
						} catch (EmptyStackException e) {
						}
						break;
				}
				//if (i % 1000 == 0) {
					repaint();
				//}
				if (hasChange) {
					continue mainloop;
				}
			}
			try {
				synchronized (this) {
					// Kun piirtäminen on valmis ja ei ole eroa, pitä odota.
					while (!hasChange) {
						wait(100);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
