/**
 * 
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
				int r = Math.abs(random.nextInt() % 4);
				
				int x0 = x;
				int y0 = y;
				
				switch (r) {
					case 0 :
						x++;
						break;
					case 1 :
						y++;
						break;
					case 2 :
						x--;
						break;
					case 3 :
						y--;
						break;
					default :
						throw new AssertionError(r);
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

}
