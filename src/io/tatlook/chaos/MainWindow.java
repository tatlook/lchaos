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
import javax.swing.JSplitPane;

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
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void UI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		super.setContentPane(mainPanel);
		
		JMenuBar menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		Drawer drawer = new Drawer();
		//drawer.start();
		JPanel toolPanel = new ToolPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(drawer);
		splitPane.setLeftComponent(toolPanel);
		mainPanel.add(splitPane, BorderLayout.CENTER);
	}
}
