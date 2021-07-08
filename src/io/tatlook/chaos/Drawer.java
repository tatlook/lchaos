/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
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
	 * Kuinka pitkä aika pitä odota (millisekunti)
	 */
	private int waitTime = 1000;
	private int waitLevel = 0;
	
	/**
	 * Kuva, johon pirtään pistettä
	 */
	private Image image;
	private Color penColor = Color.red;
	
	private int imageWidth = 3000;
	private int imageHeight = 3000;
	
	private int zoom = imageHeight;
	private int imageX = imageWidth / 2;
	private int imageY = imageHeight / 2;
	
	private boolean hasChange = true;
	
	public Drawer() {
		// Kuva suurennee/pienennee, kun paina Ctrl++/Ctrl+-
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventPostProcessor((e) -> {
			if (!e.isControlDown()) {
				return true;
			}
			if (e.getKeyCode() == KeyEvent.VK_PLUS) {
				zoom(-1, getWidth() / 2, getHeight() / 2);
			} else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
				zoom(1, getWidth() / 2, getHeight() / 2);
			}
			return true;
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
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(image, -imageX, -imageY, zoom, zoom, this);
	}
	
	public void start() {
		drawThread.start();
	}
	
	public Image getImage() {
		return image;
	}
	
	/**
	 * @return the waitLevel
	 */
	public int getWaitLevel() {
		return waitLevel;
	}

	/**
	 * @param waitLevel the waitLevel to set
	 */
	public void setWaitLevel(int waitLevel) {
		this.waitLevel = waitLevel;
		this.waitTime = ((11 - waitLevel) * 500 + 1) / imageHeight;
	}

	public void setChange() {
		hasChange = true;
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
			// Wait() funktio ei saa olla nolla.
			if (waitTime != 0) {
				try {
					synchronized (this) {
						wait(waitTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
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
		double r = StdRandom.uniform(0.0, sumb);
			for (int i = 0; i < probabilities.length; i++) {
				sum = sum + probabilities[i];
				if (sum > r) return i;
			}
		}
	}

	public void clean() {
		image = createImage(imageWidth, imageHeight);
		
		setChange();
	}

	public void intoMiddle() {
		imageX = (zoom - getWidth()) / 2;
		imageY = (zoom - getHeight()) / 2;
	}

	public void setImageSize(int size) {
		imageWidth = imageHeight = zoom = size;
		intoMiddle();
		clean();
	}
	
	public int getImageSize() {
		return imageWidth;
	}

	public void setPenColor(Color color) {
		penColor = color;
		setChange();
	}

	public Color getPenColor() {
		return penColor;
	}
}
