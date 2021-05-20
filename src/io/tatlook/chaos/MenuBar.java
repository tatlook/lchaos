/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Administrator
 *
 */
public class MenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7347092499428362250L;

	public MenuBar() {
		JMenu fileMenu = new JMenu("File");
		
		fileMenu.setMnemonic('F');
		
		JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveImageMenuItem = new JMenuItem("Save image");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        newMenuItem.setMnemonic('N');
        openMenuItem.setMnemonic('O');
        saveImageMenuItem.setMnemonic('I');
        exitMenuItem.setMnemonic('E');
        
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveImageMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        exitMenuItem.addActionListener((e) -> {
        	System.exit(0);
        });
        openMenuItem.addActionListener((e) -> {
        	ChaosFileChooser fileChooser = new ChaosFileChooser();
        	fileChooser.chose();
        	File file = fileChooser.getChaosFile();
        	if (file == null) {
        		return;
        	}
        	try {
				ChaosFileParser parser = new ChaosFileParser(file);
				parser.readChaos();
				App.mainWindow.updateToolPanel();
        	} catch (ChaosFileDataException e1) {
        		e1.openDialog();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
        });
        
        saveImageMenuItem.addActionListener((e) -> {
        	ImageFileChooser fileChooser = new ImageFileChooser();
        	fileChooser.chose();
        	File file = fileChooser.getImageFile();
        	String imageType = fileChooser.getImageType();
        	Drawer drawer = App.mainWindow.getDrawer();

            Dimension imageSize = drawer.getSize();

        	BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        	Graphics2D graphics = image.createGraphics();
        	drawer.paint(graphics);
        	graphics.dispose();
        	
        	try {
				ImageIO.write(image, imageType, file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        });
		
        Dimension dimension = new Dimension(70, 10);
        newMenuItem.setSize(dimension);
        openMenuItem.setSize(dimension);
        exitMenuItem.setSize(dimension);
        
		add(fileMenu);
	}
}
