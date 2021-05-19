/**
 * 
 */
package io.tatlook.chaos;

/**
 * @author Administrator
 *
 */
public class ChaosDataRule {
	private double dist;
	private double[] x;
	private double[] y;
	private double x0;
	private double x1;
	private double x2;
	private double y0;
	private double y1;
	private double y2;
	
	public ChaosDataRule(double dist, double[] x, double[] y) {
		if (x.length != 3) {
			throw new IllegalArgumentException("x.length != 3");
		} else if (y.length != 3) {
			throw new IllegalArgumentException("y.length != 3");
		}
		x0 = x[0];
		x1 = x[1];
		x2 = x[2];
		y0 = y[0];
		y1 = y[1];
		y2 = y[2];
		
		this.x = x;
		this.y = y;
	}
	
	public ChaosDataRule() {
		
	}
	
	/**
	 * @return the y2
	 */
	public double getY2() {
		return y2;
	}
	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(double y2) {
		this.y2 = y2;
	}
	/**
	 * @return the y1
	 */
	public double getY1() {
		return y1;
	}
	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(double y1) {
		this.y1 = y1;
	}
	/**
	 * @return the y0
	 */
	public double getY0() {
		return y0;
	}
	/**
	 * @param y0 the y0 to set
	 */
	public void setY0(double y0) {
		this.y0 = y0;
	}
	/**
	 * @return the x2
	 */
	public double getX2() {
		return x2;
	}
	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(double x2) {
		this.x2 = x2;
	}
	/**
	 * @return the x1
	 */
	public double getX1() {
		return x1;
	}
	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(double x1) {
		this.x1 = x1;
	}
	/**
	 * @return the x0
	 */
	public double getX0() {
		return x0;
	}
	/**
	 * @param x0 the x0 to set
	 */
	public void setX0(double x0) {
		this.x0 = x0;
	}

	/**
	 * @return the x
	 */
	public double[] getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double[] x) {
		if (x.length != 3) {
			throw new IllegalArgumentException("x.length != 3");
		}
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double[] getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double[] y) {
		if (y.length != 3) {
			throw new IllegalArgumentException("y.length != 3");
		}
		this.y = y;
	}

	/**
	 * @return the dist
	 */
	public double getDist() {
		return dist;
	}

	/**
	 * @param dist the dist to set
	 */
	public void setDist(double dist) {
		this.dist = dist;
	}
}
