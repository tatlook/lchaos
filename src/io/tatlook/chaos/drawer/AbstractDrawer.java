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

package io.tatlook.chaos.drawer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

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
	protected int waitLevel = 0;
	
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
		// TODO Auto-generated constructor stub
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
}
