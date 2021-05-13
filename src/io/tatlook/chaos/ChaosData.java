/**
 * 
 */
package io.tatlook.chaos;

import java.util.Iterator;
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
    
    private Vector<Double> distVector;
    private Vector<Vector<Double>> cxVector;
    private Vector<Vector<Double>> cyVector;
    
    public ChaosData(double[] dist, double[][] cx, double[][] cy) {
    	distVector = new Vector<>();
    	for (int i = 0; i < dist.length; i++) {
    		distVector.add(dist[i]);
    	}
    	cxVector = new Vector<>();
    	for (int i = 0; i < cx.length; i++) {
    		Vector<Double> cxSubVector = new Vector<>();
    		for (int j = 0; j < cx[i].length; j++) {
    			cxSubVector.add(cx[i][j]);
			}
    		cxVector.add(cxSubVector);
    	}
    	cyVector = new Vector<>();
    	for (int i = 0; i < cy.length; i++) {
    		Vector<Double> cySubVector = new Vector<>();
    		for (int j = 0; j < cy[i].length; j++) {
    			cySubVector.add(cy[i][j]);
			}
    		cyVector.add(cySubVector);
    	}
    	
    	this.dist = dist;
    	this.cx = cx;
    	this.cy = cy;
    }
    
    public ChaosData() {
	}

	private void updateDist() {
    	dist = new double[distVector.size()];
    	for (int i = 0; i < dist.length; i++) {
    		dist[i] = distVector.get(i);
    	}    	
    }
    
    private void updateCX() {
    	cx = new double[cxVector.get(0).size()][cxVector.size()];
    	for (int i = 0; i < cx.length; i++) {
			for (int j = 0; j < cxVector.get(i).size(); j++) {
				cx[j][i] = cxVector.get(i).get(j);
			}
		}
    }
    
    private void updateCY() {
    	cy = new double[cyVector.get(0).size()][cyVector.size()];
    	for (int i = 0; i < cy.length; i++) {
			for (int j = 0; j < cyVector.get(i).size(); j++) {
				cy[j][i] = cyVector.get(i).get(j);
			}
		}
    }
    
    public double[] getDist() {
    	updateDist();
    	return dist;
    }
    
    public double[][] getCX() {
    	updateCX();
    	return cx;
    }
    
    public double[][] getCY() {
    	updateCY();
    	return cy;
    }
    
	public void addRule(double dist, double[] cx, double[] cy) {
    	
    }
    
    public static ChaosData current;

	public void addRule() {
		addRule(0.0, new double[distVector.size()], new double[distVector.size()]);
	}
}
