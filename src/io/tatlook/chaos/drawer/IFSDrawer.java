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
/******************************************************************************
 *  Compilation:  javac StdRandom.java
 *  Execution:    java StdRandom
 *  Dependencies: StdOut.java
 *
 *  A library of static methods to generate pseudo-random numbers from
 *  different distributions (bernoulli, uniform, gaussian, discrete,
 *  and exponential). Also includes a method for shuffling an array.
 *
 *
 *  %  java StdRandom 5
 *  seed = 1316600602069
 *  59 16.81826  true 8.83954  0 
 *  32 91.32098  true 9.11026  0 
 *  35 10.11874  true 8.95396  3 
 *  92 32.88401  true 8.87089  0 
 *  72 92.55791  true 9.46241  0 
 *
 *  % java StdRandom 5
 *  seed = 1316600616575
 *  96 60.17070  true 8.72821  0 
 *  79 32.01607  true 8.58159  0 
 *  81 59.49065  true 9.10423  1 
 *  96 51.65818  true 9.02102  0 
 *  99 17.55771  true 8.99762  0 
 *
 *  % java StdRandom 5 1316600616575
 *  seed = 1316600616575
 *  96 60.17070  true 8.72821  0 
 *  79 32.01607  true 8.58159  0 
 *  81 59.49065  true 9.10423  1 
 *  96 51.65818  true 9.02102  0 
 *  99 17.55771  true 8.99762  0 
 *
 *
 *  Remark
 *  ------
 *    - Relies on randomness of nextDouble() method in java.util.Random
 *      to generate pseudo-random numbers in [0, 1).
 *
 *    - This library allows you to set and get the pseudo-random number seed.
 *
 *    - See http://www.honeylocust.com/RngPack/ for an industrial
 *      strength random number generator in Java.
 *
 ******************************************************************************/
/******************************************************************************
 *  Copyright 2002-2019, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/

package io.tatlook.chaos.drawer;

import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import io.tatlook.chaos.data.ChaosData;

/**
 * @author Administrator
 *
 */
public class IFSDrawer extends AbstractDrawer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839460539960225035L;
	
	public IFSDrawer() {
		// Kuva suurennee/pienennee, kun paina Ctrl++/Ctrl+-
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventPostProcessor((e) -> {
			if (!e.isControlDown()) {
				return false;
			}
			if (e.getKeyCode() == KeyEvent.VK_PLUS) {
				zoom(-1, getWidth() / 2, getHeight() / 2);
			} else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
				zoom(1, getWidth() / 2, getHeight() / 2);
			}
			return false;
		});
		// Kuva suurennee/pienennee, kun hiiren rullaa selaa.
		addMouseWheelListener((e) -> {
			zoom(e.getWheelRotation(), e.getX(), e.getY());
		});
		// Kuva muuta, kun hiiri vetää.
		addMouseMotionListener(new MouseMotionAdapter() {
			boolean first = true;
			int lastX, lastY;
			@Override
			public void mouseDragged(MouseEvent e) {
				if (first) {
					lastX = e.getX();
					lastY = e.getY();
					first = false;
					return;
				}
				int moveX = lastX - e.getX();
				int moveY = lastY - e.getY();
				if (Math.abs(moveX) > 50 || Math.abs(moveY) > 50) {
					lastX = e.getX();
					lastY = e.getY();
					return;
				}
				
				imageX += moveX;
				imageY += moveY;
				if (imageX > zoom * 2) {
					imageX = zoom * 2;
				}
				if (imageX < -zoom * 2) {
					imageX = -zoom * 2;
				}
				if (imageY > zoom * 2) {
					imageY = zoom * 2;
				}
				if (imageY < -zoom * 2) {
					imageY = -zoom * 2;
				}
				
				lastX = e.getX();
				lastY = e.getY();
				
				repaint();
			}
		});
	}
	
	public void zoom(int rotation, int x, int y) {
		zoom -= rotation * imageHeight / 30;
		int minSize = Math.min(getWidth(), getHeight());
		int maxSize = Math.max(getWidth(), getHeight());
		if (zoom < minSize / 4 * 3) {
			zoom = minSize / 4 * 3;
			return;
		}
		if (zoom > imageHeight * 3) {
			zoom = imageHeight * 3;
			return;
		}
		int moveX = imageWidth / 15 * x / maxSize;
		int moveY = imageHeight / 15 * y / maxSize;
		if (rotation < 0) {
			imageX += moveX;
			imageY += moveY;
		} else {
			imageX -= moveX;
			imageY -= moveY;
		}
		
		repaint();
	}
	
	@Override
	public void run() {
		image = createImage(imageWidth, imageHeight);
		Graphics g = image.getGraphics();
		
		double[] dist = ChaosData.current.getDist();
		double[][] cx = ChaosData.current.getCX();
		double[][] cy = ChaosData.current.getCY();
		
		// Ensimäisen pisteen koordinaati
		double x = 0.0, y = 0.0;
		while (true) {
			g.setColor(penColor);
			// Piirtään pistettä kuvaan.
			for (int t = 0; t < imageHeight / 100; t++) { 
				// Säännöstä valitaan yksi, r on sen numero
				int r = discrete(dist); 
				
				// Laske seurava pisten koordinaati
				double x0 = cx[r][0] * x + cx[r][1] * y + cx[r][2]; 
				double y0 = cy[r][0] * x + cy[r][1] * y + cy[r][2]; 
				x = x0; 
				y = y0; 
				
				x0 *= paintingZoom;
				y0 *= paintingZoom;
				x0 += xOffset;
				y0 -= yOffset;
				x0 *= imageWidth;
				y0 *= imageHeight;
				// Pirtään kuvassa
				g.drawLine((int)x0, imageHeight - (int)y0, (int)x0, imageHeight - (int)y0);
			}
			
			// Kun on jo riitävä pistettä lisääntyy kuvassa
			
			// Jos jotain parametri muutui, päivitää sen.
			if (hasChange) {
				g = image.getGraphics();
				dist = ChaosData.current.getDist();
				cx = ChaosData.current.getCX();
				cy = ChaosData.current.getCY();
				hasChange = false;
			}
			try {
				synchronized (this) {
					// Kun piirtäminen pysähtyy, pitää odota ikuisesti.
					while (waitLevel == 0) {
						wait(100);
					}
					// Wait() funktio ei saa olla nolla.
					if (waitTime != 0) {
						wait(waitTime);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Lisää kuva komponenttiin
			repaint();
		}
	}
	
	private static int discrete(double[] probabilities) {
		double sum = 0.0;
		for (int i = 0; i < probabilities.length; i++) {
			if (probabilities[i] < 0.0)
				throw new IllegalArgumentException("array entry " + i + " must be nonnegative: " + probabilities[i]);
			sum += probabilities[i];
		}
		final double sumb = sum;
		while (true) {
			sum = 0.0;
			if (sumb == 0.0) {
				return 0;
			}
			double r = Math.random() * sumb;
			for (int i = 0; i < probabilities.length; i++) {
				sum = sum + probabilities[i];
				if (sum > r) return i;
			}
		}
	}
}
