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

import java.io.File;

/**
 * @author Administrator
 *
 */
public class FractintFileSaver extends IFSFileSaver {

	public FractintFileSaver(File file, IFSData data) {
		super(file, data);
	}

	@Override
	public void save() {
		double[] dist = data.getDist();
		double[][] cx = data.getCX();
		double[][] cy = data.getCY();
		
		out.println(getFileNameNoEx(file.getName()) + " {");
		for (int i = 0; i < dist.length; i++) {
			out.print(cx[i][0] + " " + cx[i][1] + " ");
			out.print(cy[i][0] + " " + cy[i][1] + " ");
			out.print(cx[i][2] + " " + cy[i][2] + " ");
			out.println(dist[i]);
		}
		out.println("}");
	}
	
	public static String getFileNameNoEx(String filename) { 
		if ((filename != null) && (filename.length() > 0)) { 
			int dot = filename.lastIndexOf('.'); 
			if ((dot >-1) && (dot < (filename.length()))) { 
				return filename.substring(0, dot); 
			} 
		} 
		return filename; 
	}
}
