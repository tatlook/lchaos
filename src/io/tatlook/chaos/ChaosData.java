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
	// 每个变换的执行概率
    double[] dist;
	
	// 矩阵值
    double[][] cx;
    double[][] cy;
    
    private Vector<ChaosDataRule> ruleVector;
    private Vector<Double> distVector;
    private Vector<Vector<Double>> cxVector;
    private Vector<Vector<Double>> cyVector;
    
    public ChaosData(double[] dist, double[][] cx, double[][] cy) {
    	if (dist.length != cx.length || dist.length != cy.length) {
    		System.err.println("d:" + dist.length + " x:" + cx.length + " y:" + cy.length);
			throw new IllegalArgumentException("dist.length != cx.length || dist.length != cy.length");
		}
    	distVector = arrayToVector1D(dist);
    	cxVector = arrayToVector2D(cx);
    	cyVector = arrayToVector2D(cy);
    	ruleVector = new Vector<>();
    	for (int i = 0; i < dist.length; i++) {
			ruleVector.add(new ChaosDataRule(dist[i], cx[i], cy[i]));
		}
    	
    	this.dist = dist;
    	this.cx = cx;
    	this.cy = cy;
    }
    
    public ChaosData() {
	}
    
    public double[] getDist() {
    	dist = vectorToArray1D(distVector);
    	return dist;
    }
    
    public double[][] getCX() {
    	cx = vectorToArray2D(cxVector);
    	return cx;
    }
    
    public double[][] getCY() {
    	cy = vectorToArray2D(cyVector);
    	return cy;
    }
    
    public void addRule(double dist, double[] cx, double[] cy) {
    	addRule(dist, arrayToVector1D(cx), arrayToVector1D(cy));
    }
    
	public void addRule(double dist, Vector<Double> cx, Vector<Double> cy) {
    	distVector.add(dist);
    	cxVector.add(cx);
    	cyVector.add(cy);
    }
    
	public void addRule() {
		addRule(0.0, new double[distVector.size()], new double[distVector.size()]);
	}
	
    public static ChaosData current;
	
	public static double[] vectorToArray1D(Vector<Double> vector) {
		double[] array = new double[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			array[i] = vector.get(i);
		}
		return array;
	}
	
	public static double[][] vectorToArray2D(Vector<Vector<Double>> vector) {
    	double[][] array = new double[vector.size()][vector.get(0).size()];
    	for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < vector.get(i).size(); j++) {
				array[i][j] = vector.get(i).get(j);
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
	
	public static Vector<Vector<Double>> arrayToVector2D(double[][] array) {
		Vector<Vector<Double>> vector = new Vector<>();
    	for (int i = 0; i < array.length; i++) {
    		Vector<Double> subVector = new Vector<>();
    		for (int j = 0; j < array[i].length; j++) {
    			subVector.add(array[i][j]);
			}
    		vector.add(subVector);
    	}
    	return vector;
	}
}
