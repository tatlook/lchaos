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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

/**
 * @author Administrator
 *
 */
public abstract class AbstractDrawer extends JComponent implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354400998749112028L;

	/**
	 * "Thread", jossa pirtäminen toimii.
	 */
	protected Thread drawThread = new Thread(this);
	
	/**
	 * Kuinka pitkä aika pitä odota (millisekunti)
	 */
	protected int waitTime = 1000;
	protected int level = 0;
	
	/**
	 * Kuva, johon pirtään pistettä
	 */
	protected Image image;
	protected Color penColor = Color.red;
	
	protected int imageWidth = 3000;
	protected int imageHeight = 3000;
	
	protected int zoom = imageHeight;
	protected int imageX = imageWidth / 2;
	protected int imageY = imageHeight / 2;
	
	protected boolean hasChange = true;
	
	protected double paintingZoom = 1.0;
	protected double xOffset = 0.0;
	protected double yOffset = 0.0;
	
	/**
	 * 
	 */
	public AbstractDrawer() {
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
	public void paint(Graphics g) {
		g.drawImage(image, -imageX, -imageY, zoom, zoom, this);
	}
	
	public void start() {
		drawThread.start();
	}
	
	public Image getImage() {
		return image;
	}

	public void setPenColor(Color color) {
		penColor = color;
		setChange();
	}

	public void setChange() {
		hasChange = true;
	}

	public Color getPenColor() {
		return penColor;
	}
	
	public int getImageSize() {
		return imageWidth;
	}
	
	public void intoMiddle() {
		imageX = (zoom - getWidth()) / 2;
		imageY = (zoom - getHeight()) / 2;
		repaint();
	}

	public void setImageSize(int size) {
		imageWidth = imageHeight = zoom = size;
		Image originalImage = image;
		image = createImage(imageWidth, imageHeight);
		image.getGraphics().drawImage(originalImage, 0, 0, imageWidth, imageHeight, this);
		setChange();
		intoMiddle();
	}
	

	public void clean() {
		image = createImage(imageWidth, imageHeight);
		repaint();
		setChange();
	}
	
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
		this.waitTime = ((11 - level) * 500 + 1) / imageHeight;
	}


	public void setPaintingZoom(double paintingZoom) {
		this.paintingZoom = paintingZoom;
	}

	/**
	 * @return the paintingZoom
	 */
	public double getPaintingZoom() {
		return paintingZoom;
	}

	public void setXOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	public void setYOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	/**
	 * @return the xOffset
	 */
	public double getXOffset() {
		return xOffset;
	}

	/**
	 * @return the yOffset
	 */
	public double getYOffset() {
		return yOffset;
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		drawThread.stop();
	}
}
