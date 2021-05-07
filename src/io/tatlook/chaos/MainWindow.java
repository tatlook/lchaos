/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * @author Administrator
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8480434536614023106L;

	public MainWindow() {
		super.setTitle("IFS函数迭代系统");
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public void UI() {
		JMenuBar menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		Drawer drawer = new Drawer();
		//drawer.start();
		mainPanel.add(drawer, BorderLayout.CENTER);
		super.setContentPane(mainPanel);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel toolPanel = new ToolPanel();
		mainPanel.add(toolPanel, BorderLayout.WEST);
	}
}
