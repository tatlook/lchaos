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
public class ChaosFileSaver extends AbstractFileSaver {
	public ChaosFileSaver(File file) {
		super(file);
	}
	
	@Override
	public void save() {
		double[] dist = ChaosData.current.getDist();
		double[][] cx = ChaosData.current.getCX();
		double[][] cy = ChaosData.current.getCY();
		
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
