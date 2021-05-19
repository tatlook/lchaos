/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

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
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        newMenuItem.setMnemonic('N');
        openMenuItem.setMnemonic('O');
        exitMenuItem.setMnemonic('E');
        
        // 子菜单添加到一级菜单
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();       // 添加一条分割线
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
		
        Dimension dimension = new Dimension(70, 10);
        newMenuItem.setSize(dimension);
        openMenuItem.setSize(dimension);
        exitMenuItem.setSize(dimension);
        
		add(fileMenu);
	}
}
