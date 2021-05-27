/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
	private JSplitPane splitPane;
	
	public void updateToolPanel() {
		toolPanel = new ToolPanel();
		splitPane.setLeftComponent(toolPanel);
	}
	
	public void UI() {
		super.setContentPane(mainPanel);
		
		JMenuBar menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		toolPanel = new ToolPanel();
		drawer = new Drawer();
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(drawer);
		splitPane.setLeftComponent(toolPanel);
		
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
			int stal = 0;
			boolean full = false;
			@Override
			public boolean postProcessKeyEvent(KeyEvent e) {
				stal++;
				if (stal != 1) {
					stal = 0;
					return true;
				}
				if (e.getKeyCode() != KeyEvent.VK_F11) {
					return true;
				}
				System.out.println(
						"MainWindow.UI().new KeyEventPostProcessor() {...}.postProcessKeyEvent()");
				if (full == true) {
					setJMenuBar(menuBar);
					setContentPane(mainPanel);
					splitPane.setRightComponent(drawer);
					update(getGraphics());
					full = false;
				} else {
					setJMenuBar(null);
					setContentPane(drawer);
					update(getGraphics());
					setExtendedState(JFrame.NORMAL);
					setExtendedState(JFrame.MAXIMIZED_BOTH);
					full = true;
				}
				return true; 
			}
		});
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
