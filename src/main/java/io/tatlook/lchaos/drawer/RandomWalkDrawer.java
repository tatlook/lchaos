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

import java.awt.Graphics;
import java.util.Random;

/**
 * @author Administrator
 *
 */
public class RandomWalkDrawer extends AbstractDrawer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1471595944572520282L;

	public static final int DO_NOTHING_ON_POINT_OUT = 0;
	public static final int BACK_TO_INITIAL_POSITION_ON_POINT_OUT = 1;
	public static final int STOP_ON_POINT_OUT = 2;

	private int doOnPointOut = STOP_ON_POINT_OUT;
	private int pointDistance = 1;

	/**
	 * 
	 */
	public RandomWalkDrawer() {
		super();
	}

	@Override
	public void run() {
		image = createImage(imageWidth, imageHeight);
		Graphics g = image.getGraphics();
		
		// Ensimäisen pisteen koordinaati
		int x = imageWidth / 2, y = imageHeight / 2;
		
		Random random = new Random();
		
		while (true) {
			g.setColor(penColor);
			// Piirtään pistettä kuvaan.
			for (int t = 0; t < imageHeight / 100; t++) { 
				// Suunnasta valitaan yksi, r on sen numero
				int r = Math.abs(random.nextInt() % 5);
				
				int x0 = x;
				int y0 = y;
				
				switch (r) {
					case 0 :
						x += pointDistance;
						break;
					case 1 :
						y += pointDistance;
						break;
					case 2 :
						x -= pointDistance;
						break;
					case 3 :
						y -= pointDistance;
						break;
					case 4:
						break;
					default :
						throw new AssertionError(r);
				}
				
				if (doOnPointOut == DO_NOTHING_ON_POINT_OUT) {
				} else if (doOnPointOut == BACK_TO_INITIAL_POSITION_ON_POINT_OUT) {
					if (x > imageWidth || x < 0 || y > imageHeight || y < 0) {
						x = imageWidth / 2;
						y = imageHeight / 2;
					}
				} else if (doOnPointOut == STOP_ON_POINT_OUT) {
					if (x > imageWidth) {
						x = imageWidth;
					} else if (x < 0) {
						x = 0;
					}
					if (y > imageHeight) {
						y = imageHeight;
					} else if (y < 0) {
						y = 0;
					}
				}
				
				// Pirtään kuvassa
				g.drawLine(x0, y0, x, y);
			}
			
			// Kun on jo riitävä pistettä lisääntyy kuvassa
			
			// Jos jotain parametri muutui, päivitää sen.
			if (hasChange) {
				g = image.getGraphics();
				x = imageWidth / 2;
				y = imageHeight / 2;
				hasChange = false;
			}
			try {
				synchronized (this) {
					// Kun piirtäminen pysähtyy, pitää odota ikuisesti.
					while (level == 0) {
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

	/**
	 * @param doOnPointOut the doOnPointOut to set
	 */
	public void setDoOnPointOut(int doOnPointOut) {
		this.doOnPointOut = doOnPointOut;
	}

	/**
	 * @return the doOnPointOut
	 */
	public int getDoOnPointOut() {
		return doOnPointOut;
	}

	/**
	 * Returns the pointDistance.
	 * 
	 * @return the pointDistance
	 */
	public int getPointDistance() {
		return pointDistance;
	}

	/**
	 * @param pointDistance the pointDistance to set
	 */
	public void setPointDistance(int pointDistance) {
		this.pointDistance = pointDistance;
	}

}
