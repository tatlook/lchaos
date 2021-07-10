/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import io.tatlook.chaos.parser.ChaosFileParser;
import io.tatlook.chaos.saver.ChaosFileSaver;

/**
 * @author Administrator
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8480434536614023106L;
	
	private static final String NAME = "Iterated Function System";
	

	public MainWindow() {
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setMinimumSize(new Dimension(900, 600));
		super.addWindowListener(windowListener);
	}
	
	static class MainWindowListener extends WindowAdapter {
		private int result = -100;
		@Override
		public void windowClosing(WindowEvent e) {
			windowClosing((JFrame) e.getWindow());
		}
		
		public void windowClosing(JFrame frame) {
			if (!ChaosData.current.isChanged()) {
				result = JOptionPane.NO_OPTION;
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
			} else if (result == -100) {
			} else {
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				frame.setVisible(false);
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
	private JMenuBar menuBar;
	
	public void updateToolPanel() {
		toolPanel = new ToolPanel();
		splitPane.setLeftComponent(toolPanel);
		setTitle(ChaosFileParser.getCurrentFileParser().getFile());
	}
	
	public void setTitle(File file) {
		String fileName = file != null ? file.getName() : "untitled";
		if (ChaosData.current.isChanged()) {
			super.setTitle("*" + fileName + " - " + NAME);
		} else {
			super.setTitle(fileName + " - " + NAME);
		}
	}
	
	private Runnable fullScreenRunnable = new Runnable() {
		/**
		 * Tallennetut ikkunatiedot 
		 */
		int state;
		Dimension size;
		Point location;
		@Override
		public void run() {
			if (isUndecorated()) {
				setJMenuBar(menuBar);
				setContentPane(mainPanel);
				splitPane.setRightComponent(drawer);
				// On pakko käytä tämä funktio, kun aikeissa käyttää setUndecorated()
				dispose();
				// Käytä tallennettuja tietoja.
				setSize(size);
				setExtendedState(state);
				setLocation(location);
				// Tee raja ikkunalle.
				setUndecorated(false);
				// On pakko käytä tämä funktio, kun äsken käyttää dispose()
				setVisible(true);
			} else {
				setJMenuBar(null);
				setContentPane(drawer);
				// Laita tiedot talteen.
				size = getSize();
				state = getExtendedState();
				location = getLocationOnScreen();
				// On pakko käytä tämä funktio, kun aikeissa käyttää setUndecorated()
				dispose();
				// Maksimoida ikkunaa.
				setExtendedState(JFrame.MAXIMIZED_BOTH);
				setUndecorated(true);	// Otta raja ikkunasta pois.
				// On pakko käytä tämä funktio, kun äsken käyttää dispose()
				setVisible(true);
			}
		}
	};
	
	public void fullScreen() {
		fullScreenRunnable.run();
	}

	public void UI() {
		super.setContentPane(mainPanel);
		
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		updateToolPanel();
		drawer = new Drawer();
		
		splitPane.setRightComponent(drawer);
		
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
			int stal = 0;
			@Override
			public boolean postProcessKeyEvent(KeyEvent e) {
				stal++;
				if (stal != 1) {
					stal = 0;
					return false;
				}
				if (isUndecorated()) {
					if (e.getKeyCode() != KeyEvent.VK_F11 && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
						return false;
					}
					fullScreen();
				} else {
					if (e.getKeyCode() != KeyEvent.VK_F11) {
						return false;
					}
					fullScreen();
				}
				return false;
			}
		});
	}
}
