/**
 * 
 */
package io.tatlook.chaos;

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
		JMenu fileMenu = new JMenu("文件");
		fileMenu.setMnemonic('F');
		
		JMenuItem newMenuItem = new JMenuItem("新建");
        JMenuItem openMenuItem = new JMenuItem("打开");
        JMenuItem exitMenuItem = new JMenuItem("退出");
        // 子菜单添加到一级菜单
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();       // 添加一条分割线
        fileMenu.add(exitMenuItem);
        
        exitMenuItem.addActionListener((e) -> {
        	System.exit(0);
        });
		
		add(fileMenu);
	}
}
