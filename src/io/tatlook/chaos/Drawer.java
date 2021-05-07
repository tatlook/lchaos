/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import edu.princeton.cs.algs4.StdRandom;

/**
 * @author Administrator
 *
 */
public class Drawer extends JComponent implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839460539960225035L;

	private Thread drawTheard = new Thread(this);
	
	private final int trials = 100000;	// 迭代次数*（需输入在命令行中，或者直接赋值）*
	
	private Graphics graphics;
	
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		graphics = g.create();
		if (graphics == null) {
			System.err.println("EEEE");
		}
	}
	
	public void start() {
		drawTheard.start();
	}
	
	@Override
	public void run() {
		ChaosFileParser parser = ChaosFileParser.getCurrentFileParser();
		parser.readChaos();
		// 每个变换的执行概率
        double[] dist = parser.dist;
        // 矩阵值
        double[][] cx = parser.cx;
        double[][] cy = parser.cy;

        // 初始值 (x, y)
        double x = 0.0, y = 0.0;

        for (int t = 0; t < trials; t++) { 

            // 根据概率分布随机选择变换
            int r = StdRandom.discrete(dist); 

            // 迭代
            double x0 = cx[r][0] * x + cx[r][1] * y + cx[r][2]; 
            double y0 = cy[r][0] * x + cy[r][1] * y + cy[r][2]; 
            x = x0; 
            y = y0; 

            // 绘制结果
            graphics.drawLine((int)x, (int)y, (int)x, (int)y); 

            // 每迭代100次显示1次
            if (t % 100 == 0) {
                
            }
        } 
	
	}
}
