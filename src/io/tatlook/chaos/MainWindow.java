/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
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
		super.setTitle("Iterated function system");
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setMinimumSize(new Dimension(900, 600));
	}
	
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private Drawer drawer;
	
	public Drawer getDrawer() {
		return drawer;
	}

	private JPanel toolPanel;
	
	public void updateToolPanel() {
		mainPanel.remove(toolPanel);
		toolPanel = new ToolPanel();
		mainPanel.add(toolPanel);
	}
	
	public void UI() {
		super.setContentPane(mainPanel);
		
		JMenuBar menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		toolPanel = new ToolPanel();
		drawer = new Drawer();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(drawer);
		splitPane.setLeftComponent(toolPanel);
		
		mainPanel.add(splitPane, BorderLayout.CENTER);
	}
	
	@Override
	public void setVisible(boolean rootPaneCheckingEnabled) {
		super.setVisible(rootPaneCheckingEnabled);
		// Ennen kuin "thread" lähtee, pitä odotta ikkunan valmis
		new Thread(() -> {
			try {
				// Ei saa tapahtu mitää, ennen kuin odotaminen loppu
				synchronized (this) {
					wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Nyt ikkuna on suurin pirtein valmis.
			drawer.start();
		}).start();
	}
}
