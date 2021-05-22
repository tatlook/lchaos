/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

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
	
	public static final int imageWidth = 3000;
	public static final int imageHeight = 3000;
	
	private int zoom = imageHeight;
	private int imageX = 0;
	private int imageY = 0;
	
	public Drawer() {
		// Kuva suurennee/pienennee, kun hiiren rullaa selaa.
		addMouseWheelListener((e) -> {
			zoom -= e.getWheelRotation() * 50;
			if (zoom < 600) {
				zoom = 600;
				return;
			} else if (zoom > 2 * imageHeight) {
				zoom = 2 * imageHeight;
				return;
			}
			int moveX = 100 * e.getX() / imageWidth;
			int moveY = 100 * e.getY() / imageHeight;
			if (e.getWheelRotation() < 0) {
				imageX += moveX;
				imageY += moveY;
			} else {
				imageX -= moveX;
				imageY -= moveY;
			}
//			imageX += getWidth() / 50;
//			imageY += getHeight() / 50;
//			imageX *= Math.abs(e.getWheelRotation());
//			imageY *= Math.abs(e.getWheelRotation());
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			boolean first = true;
			int ex, ey;
			@Override
            public void mouseDragged(MouseEvent e) {
				if (first) {
					ex = e.getX();
					ey = e.getY();
					first = false;
					return;
				}
				int lx = ex - e.getX();
				int ly = ey - e.getY();
				if (Math.abs(lx) > 50 || Math.abs(ly) > 50) {
					ex = e.getX();
					ey = e.getY();
					return;
				}
				
                imageX += lx;
                imageY += ly;
                
                ex = e.getX();
				ey = e.getY();
            }
        }) ;
	}
	
	public void paint(Graphics g) {
		g.drawImage(image, -imageX, -imageY, zoom, zoom, this);
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
		image = createImage(imageWidth, imageHeight);
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
//
//            if (r == 0) {
//            	g.setColor(Color.red);
//			} else if (r== 1) {
//				g.setColor(Color.BLUE);
//			} else if (r== 2) {
//				g.setColor(Color.YELLOW);
//			}
            
            // Laske seurava pisten koordinaati
            double x0 = cx[r][0] * x + cx[r][1] * y + cx[r][2]; 
            double y0 = cy[r][0] * x + cy[r][1] * y + cy[r][2]; 
            x = x0; 
            y = y0; 

            x0 *= imageWidth;
            y0 *= imageHeight;
            // Pirtään kuvassa
            g.drawLine((int)x0, (int)y0, (int)x0, (int)y0);
            
            // Kun on jo tuhat pistettä kuvassa
            if (t % 1000 == 0) {
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
