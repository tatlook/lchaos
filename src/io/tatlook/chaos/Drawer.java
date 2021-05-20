/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

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

	/**
	 * "Thread", jossa pirtäminen toimii.
	 */
	private Thread drawThread = new Thread(this);
	
	/**
	 * Kuinka paljon pistettä pitä piirtä
	 */
	private final int trials = Integer.MAX_VALUE - 1000;
	
	private int waitTime = 1000;
	
	/**
	 * Kuva, johon pirtään pistettä
	 */
	private Image image;
	
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);
		System.out.println("Drawer.paint()");
	}
	
	public void start() {
		drawThread.start();
	}
	
	public Image getImage() {
		return image;
	}
	
	/**
	 * @param waitTime the waitTime to set
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public void run() {
		image = createImage(getWidth(), getHeight());
		Graphics g = image.getGraphics();
		g.setColor(Color.red);
		
		ChaosFileParser parser = ChaosFileParser.getCurrentFileParser();
		try {
			parser.readChaos();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
	
        double[] dist = ChaosData.current.getDist();
        double[][] cx = ChaosData.current.getCX();
        double[][] cy = ChaosData.current.getCY();

        // Ensimäisen pisteen koordinaati
        double x = 0.0, y = 0.0;

        for (int t = 0; t < trials; t++) { 
            // Säännöstä valitaan yksi, r on sen numero
            int r = StdRandom.discrete(dist); 

            // Laske seurava pisten koordinaati
            double x0 = cx[r][0] * x + cx[r][1] * y + cx[r][2]; 
            double y0 = cy[r][0] * x + cy[r][1] * y + cy[r][2]; 
            x = x0; 
            y = y0; 

            x0 *= 1000;
            y0 *= 1000;
            // Pirtään kuvassa
            g.drawLine((int)x0, (int)y0, (int)x0, (int)y0);
            
            // Kun on jo viisikymenttä pistettä kuvassa
            if (t % 50 == 0) {
            	// Wait() funktio ei saa olla nolla.
            	if (waitTime != 0) {
            		try {
            			synchronized (this) {
            				wait(waitTime);
            			}
            		} catch (InterruptedException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}	
				}
            	// Lisää kuva komponenttiin
				repaint();
			}
        } 
	}
}
