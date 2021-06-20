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
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

/**
 * @author Administrator
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8480434536614023106L;
	
	private static final String NAME = "Iterated function system";
	

	public MainWindow() {
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setMinimumSize(new Dimension(900, 600));
		super.addWindowListener(windowListener);
	}
	
	static class MainWindowListener extends WindowAdapter {
		int result = JOptionPane.NO_OPTION;
		@Override
		public void windowClosing(WindowEvent e) {
			windowClosing((JFrame) e.getWindow());
		}
		
		public void windowClosing(JFrame frame) {
			if (!ChaosData.current.isChanged()) {
				return;
			}
			result = ErrorMessageDialog.createSaveDialog();
			if (result == JOptionPane.CANCEL_OPTION) {
				frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			} else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
				System.exit(0);
			} else if (result == JOptionPane.YES_OPTION) {
				if (ChaosFileSaver.staticSave() == true) {
					System.exit(0);					
				} else {
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
			}
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			windowClosed((JFrame) e.getWindow());
		}
		
		public void windowClosed(JFrame frame) {
			if (result == JOptionPane.CANCEL_OPTION) {
				frame.setVisible(true);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
		}
	}
	
	private static MainWindowListener windowListener = new MainWindowListener();
	
	public static MainWindowListener getWindowListener() {
		return windowListener;
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
		File file = ChaosFileParser.getCurrentFileParser().getFile();
		if (file == null) {
			super.setTitle("untitled - " + NAME);
		} else {
			super.setTitle(file.getName() + " - " + NAME);			
		}
	}
	
	public void UI() {
		super.setContentPane(mainPanel);
		
		JMenuBar menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		updateToolPanel();
		drawer = new Drawer();
		
		splitPane.setRightComponent(drawer);
		
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
	
	public void waitDrawerStart() {
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
