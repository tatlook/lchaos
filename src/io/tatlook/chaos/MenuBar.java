/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import io.tatlook.chaos.MainWindow.MainWindowListener;

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
		JMenu viewMenu = new JMenu("View");
		
		fileMenu.setMnemonic('F');
		viewMenu.setMnemonic('V');
		
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenu openRecentMenu = FileHistoryManager.get().getMenu();
        JMenuItem saveImageMenuItem = new JMenuItem("Save Image");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        newMenuItem.setMnemonic('N');
        saveMenuItem.setMnemonic('S');
        openMenuItem.setMnemonic('O');
        openRecentMenu.setMnemonic('R');
        saveImageMenuItem.setMnemonic('I');
        exitMenuItem.setMnemonic('E');
        
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(openRecentMenu);
        fileMenu.add(saveImageMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        newMenuItem.addActionListener((e) -> {
        	if (checkFileSave() == false) {
				return;
			} 
        	
        	new NullChaosFileParser().readChaos();
        	App.mainWindow.updateToolPanel();
			App.mainWindow.getDrawer().setChange();
        });
        saveMenuItem.addActionListener((e) -> {
        	ChaosFileSaver.staticSave();
        });
        openMenuItem.addActionListener((e) -> {
        	checkFileSave();
        	
        	ChaosFileChooser fileChooser = new ChaosFileChooser();
        	fileChooser.chose();
        	File file = fileChooser.getChaosFile();
        	if (file == null || !file.exists() || !file.canRead()) {
        		return;
        	}
        	try {
				openFile(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
        });
        saveImageMenuItem.addActionListener((e) -> {
        	ImageFileChooser fileChooser = new ImageFileChooser();
        	fileChooser.chose();
        	File file = fileChooser.getImageFile();
        	if (file == null) {
				return;
			}
        	String imageType = fileChooser.getImageType();
        	Drawer drawer = App.mainWindow.getDrawer();

        	BufferedImage image = new BufferedImage(Drawer.imageWidth, Drawer.imageHeight, BufferedImage.TYPE_INT_ARGB);
        	Graphics2D graphics = image.createGraphics();
        	graphics.drawImage(drawer.getImage(), 0, 0, null);
        	graphics.dispose();
        	
        	try {
				ImageIO.write(image, imageType, file);
			} catch (IOException e1) {
				ErrorMessageDialog.createExceptionDialog(e1);
			}
        });
        exitMenuItem.addActionListener((e) -> {
        	MainWindowListener mainWindowListener = MainWindow.getWindowListener();
        	mainWindowListener.windowClosing(App.mainWindow);
        	mainWindowListener.windowClosed(App.mainWindow);
        });
        
        JMenuItem cleanImageMenuItem = new JMenuItem("Clean Display");
        JMenuItem intoMiddleMenuItem = new JMenuItem("Into the Middle");
        JMenuItem chooseColorMenuItem = new JMenuItem("Select Color");
        JMenu imageSizeMenu = new JMenu("Image Size");
        JMenuItem fullScreenMenuItem = new JMenuItem("Full Screen (F11)");
        cleanImageMenuItem.setMnemonic('C');
        intoMiddleMenuItem.setMnemonic('I');
        chooseColorMenuItem.setMnemonic('O');
        imageSizeMenu.setMnemonic('S');
        fullScreenMenuItem.setMnemonic('F');
        
        ButtonGroup imageSizeButtonGroup = new ButtonGroup();
        @SuppressWarnings("serial")
		class ImageSizeRadioButtonMenuItem extends JRadioButtonMenuItem {
        	public ImageSizeRadioButtonMenuItem(int size, boolean selected) {
        		super(size + "×" + size, selected);
        		imageSizeButtonGroup.add(this);
        		imageSizeMenu.add(this);
        		addChangeListener((e) -> {
        			if (isSelected()) {
        				App.mainWindow.getDrawer().setImageSize(size);
        			}
        		});
        	}
			public ImageSizeRadioButtonMenuItem(int size) {
				this(size, false);
			}
        }
        
        new ImageSizeRadioButtonMenuItem(300);
        new ImageSizeRadioButtonMenuItem(500);
        new ImageSizeRadioButtonMenuItem(1000);
        new ImageSizeRadioButtonMenuItem(2000);
        new ImageSizeRadioButtonMenuItem(3000, true);
        new ImageSizeRadioButtonMenuItem(4000);
        new ImageSizeRadioButtonMenuItem(5000);
        
        viewMenu.add(cleanImageMenuItem);
        viewMenu.add(intoMiddleMenuItem);
        viewMenu.add(chooseColorMenuItem);
        viewMenu.add(imageSizeMenu);
        viewMenu.addSeparator();
        viewMenu.add(fullScreenMenuItem);
        
        cleanImageMenuItem.addActionListener((e) -> App.mainWindow.getDrawer().clean());
        intoMiddleMenuItem.addActionListener((e) -> App.mainWindow.getDrawer().intoMiddle());
        chooseColorMenuItem.addActionListener((e) -> {
        	Color color = JColorChooser.showDialog(
					App.mainWindow, 
					"Color Chooser", 
					App.mainWindow.getDrawer().getPenColor());
        	if (color != null) {
        		App.mainWindow.getDrawer().setPenColor(color);				
			}
        });
        fullScreenMenuItem.addActionListener((e) -> {
        	App.mainWindow.getFullScreenRunnable().run();
        });

		add(fileMenu);
		add(viewMenu);
	}
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean checkFileSave() {
		if (ChaosData.current.isChanged()) {
			int result = ErrorMessageDialog.createSaveDialog();
			if (result == JOptionPane.YES_OPTION) {
				return ChaosFileSaver.staticSave();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}
	
	public static void openFile(File file) throws FileNotFoundException {
		try {
			ChaosFileParser parser = new ChaosFileParser(file);
			parser.readChaos();
			App.mainWindow.updateToolPanel();
			App.mainWindow.getDrawer().setChange();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
		
		FileHistoryManager.get().add(file);
	}
}
