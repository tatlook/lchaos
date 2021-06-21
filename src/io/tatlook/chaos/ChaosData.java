/**
 * 
 */
package io.tatlook.chaos;

import java.util.Vector;

/**
 * @author Administrator
 *
 */
public class ChaosData {
	public static ChaosData current;
    
    private Vector<Double> distVector;
    private Vector<Double[]> cxVector;
    private Vector<Double[]> cyVector;

	private boolean changed;
    
    public ChaosData(double[] dist, double[][] cx, double[][] cy) {
    	if (dist.length != cx.length || dist.length != cy.length) {
    		System.err.println("d:" + dist.length + " x:" + cx.length + " y:" + cy.length);
			throw new AssertionError();
		}
    	distVector = arrayToVector1D(dist);
    	cxVector = arrayToVector2D(cx);
    	cyVector = arrayToVector2D(cy);
    }
    
    public ChaosData() {
    	this(new double[1], new double[1][3], new double[1][3]);
    }
    
    public double[] getDist() {
    	return vectorToArray1D(distVector);
    }
    
    public double[][] getCX() {
    	return vectorToArray2D(cxVector);
    }
    
    public double[][] getCY() {
    	return vectorToArray2D(cyVector);
    }
    
    public Vector<Double> getDistVector() {
    	return distVector;
    }
    
    public Vector<Double[]> getCXVector() {
    	return cxVector;
    }
    
    public Vector<Double[]> getCYVector() {
    	return cyVector;
    }
    
    public void setChanged(boolean changed) {
		this.changed = changed; 
	}
    
    public boolean isChanged() {
    	return changed;
    }
    
	public void addRule(double dist, Double[] cx, Double[] cy) {
    	distVector.add(dist);
    	cxVector.add(cx);
    	cyVector.add(cy);
    }
    
	public void addRule() {
		Double[] dx = new Double[3];
		Double[] dy = new Double[3];
		for (int i = 0; i < dy.length; i++) {
			dx[i] = 0.0;
			dy[i] = 0.0;
		}
		addRule(0.0, dx, dy);
	}
	
	public void removeRule(int index) {
		distVector.remove(index);
    	cxVector.remove(index);
    	cyVector.remove(index);
	}
	
	public static double[] vectorToArray1D(Vector<Double> vector) {
		double[] array = new double[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			array[i] = vector.get(i);
		}
		return array;
	}
	
	public static double[][] vectorToArray2D(Vector<Double[]> vector) {
    	double[][] array = new double[vector.size()][3];
    	for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < 3; j++) {
				array[i][j] = vector.get(i)[j];
			}
		}
    	return array;
    }
	
	public static Vector<Double> arrayToVector1D(double[] array) {
		Vector<Double> vector = new Vector<>();
    	for (int i = 0; i < array.length; i++) {
    		vector.add(array[i]);
    	}
		return vector;
	}
	
	public static Vector<Double[]> arrayToVector2D(double[][] array) {
		Vector<Double[]> vector = new Vector<>();
    	for (int i = 0; i < array.length; i++) {
    		double[] d1 = array[i];
    		Double[] d2 = new Double[d1.length];
    		for (int j = 0; j < d1.length; j++) {
				d2[j] = d1[j];
			}
    		vector.add(d2);
    	}
    	return vector;
	}
}
