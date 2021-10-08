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

package io.tatlook.lchaos.ifs;

/**
 * @author Administrator
 *
 */
public class ChaosFileSaver extends IFSFileSaver {

	@Override
	public void save() {
		double[] dist = data.getDist();
		double[][] cx = data.getCX();
		double[][] cy = data.getCY();
		
		out.println(dist.length);
		out.print("    ");
		for (int i = 0; i < dist.length; i++) {
			out.print(dist[i]);
			out.print(' ');
		}
		out.println();
		
		out.println(cx.length + " " + 3);
		for (int i = 0; i < cx.length; i++) {
			out.print("   ");
			for (int j = 0; j < 3; j++) {
				out.print(' ');
				out.print(cx[i][j]);
			}
			out.println();
		}
		
		out.println(cy.length + " " + 3);
		for (int i = 0; i < cy.length; i++) {
			out.print("   ");
			for (int j = 0; j < 3; j++) {
				out.print(' ');
				out.print(cy[i][j]);
			}
			out.println();
		}
		
		out.close();
	}
}
