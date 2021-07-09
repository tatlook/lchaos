/**
 * 
 */
package io.tatlook.chaos.saver;

import java.io.File;

import io.tatlook.chaos.ChaosData;

/**
 * @author Administrator
 *
 */
public class FractintFileSaver extends AbstractFileSaver {

	public FractintFileSaver(File file) {
		super(file);
	}

	@Override
	public void save() {
		double[] dist = ChaosData.current.getDist();
		double[][] cx = ChaosData.current.getCX();
		double[][] cy = ChaosData.current.getCY();
		
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
