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

package io.tatlook.lchaos.drawer;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Administrator
 *
 */
public class MandelbrotSetDrawer extends AbstractDrawer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1649373241162160919L;

	/**
	 * 
	 */
	public MandelbrotSetDrawer() {
		super();
		level = 255;
	}

	class Complex {
		double real;
		double image;
	
		public Complex(double real, double image) {
			this.real = real;
			this.image = image;
		}
	
		public double abs() {
			return Math.hypot(real, image);
		}
	
		public Complex mul(Complex other) {
			double real = this.real * other.real - this.image * other.image;
			double image = this.real * other.image + this.image * other.real;
			return new Complex(real, image);
		}
	
		public Complex plus(Complex other) {
			return new Complex(real + other.real, image + other.image);
		}
	
	}

	@Override
	public void run() {
		image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		
		mainloop: while (true) {
			hasChange = false;
			
			double xc = 0;
			double yc = 0;
			double size = 4;
			
			int n = imageHeight; // create n-by-n image
			int max = level; // maximum number of iterations
			
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					double x0 = xc - size / 2 + size * i / n;
					double y0 = yc - size / 2 + size * j / n;
					Complex z0 = new Complex(x0, y0);
					int gray = max - mand(z0, max);
					Color color = new Color(gray, gray, gray);
					((BufferedImage) image).setRGB(i, j, getColor(i - j, gray));
				}
				if (i % 20 == 0) {
					if (hasChange) {
						continue mainloop;
					}
					repaint();
				}
			}
			repaint();
			
			try {
				synchronized (this) {
					// Wait() funktio ei saa olla nolla.
					while (!hasChange) {
						wait(100);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private int mand(Complex z0, int max) {
		Complex z = z0;
		for (int t = 0; t < max; t++) {
			if (z.abs() > 2.0)
				return t;
			z = z.mul(z).plus(z0);
		}
		return max;
	}

	public int getColor(int i, int maxIterations) {
		// A color scheme
		int a = (int) (255 * ((double) i) / (maxIterations / 4));
		return
		// Red & black with fade, a classic!
		// ( (2*a<<16) );
		// Other options of varying qualities...
		// Hot pink bar & black
		// ( (255 * (i/15)) << 16 | (255 * (i/15)) );
		// Red bars & black
		// ((255 * (i/20)) << 16 | 0 | 0 );
		// The cow level! Black & white bars
		// ((255 * (i/10)) << 16 | (255 * (i/10)) << 8 | (255 * (i/10)) );
		// Blue, blue-green fade, and black
		// (65536 + i*256 + i/2+128);
		// Black & purple/pink fade
		((0) | (2 * a << 16) | (a << 8) | ((a * 2) << 0));
	}

}
