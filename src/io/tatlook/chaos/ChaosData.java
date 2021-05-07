/**
 * 
 */
package io.tatlook.chaos;

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
    
    public static ChaosData current;
}
