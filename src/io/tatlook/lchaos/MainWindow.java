/*
 * Chaos - simple 2D iterated function system plotter and editor.
 * Copyright (C) 2021 YouZhe Zhen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.tatlook.lchaos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import io.tatlook.lchaos.FractalManager.Fractal;
import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.drawer.AbstractDrawer;
import io.tatlook.lchaos.parser.AbstractFileParser;
import io.tatlook.lchaos.saver.AbstractFileSaver;
import static io.tatlook.lchaos.App.s;

/**
 * @author Administrator
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8480434536614023106L;
	
	private static final String NAME = "LChaos";
	

	public MainWindow() {
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setMinimumSize(new Dimension(900, 600));
		super.addWindowListener(windowListener);
		super.setContentPane(mainPanel);
		super.setTitle(s("window.welcome") + " - " + NAME);
		menuBar = new MenuBar();
		super.setJMenuBar(menuBar);
	}
	
	static class MainWindowListener extends WindowAdapter {
		private int result = -100;
		@Override
		public void windowClosing(WindowEvent e) {
			windowClosing((JFrame) e.getWindow());
		}
		
		public void windowClosing(JFrame frame) {
			if (!AbstractData.getCurrent().isChanged()) {
				result = JOptionPane.NO_OPTION;
				return;
			}
			result = ErrorMessageDialog.createSaveDialog();
			if (result == JOptionPane.CANCEL_OPTION) {
				frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			} else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
				System.exit(0);
			} else if (result == JOptionPane.YES_OPTION) {
				if (AbstractFileSaver.staticSave() == true) {
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
	
	private JPanel welcomePanel = new WelcomePanel();
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private AbstractDrawer drawer;
	
	public AbstractDrawer getDrawer() {
		return drawer;
	}

	private JPanel editor;
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JMenuBar menuBar;
	
	private void update(boolean drawerstart) {
		mainPanel.removeAll();
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		Fractal[] fractals = FractalManager.get().getFractals();
		for (Fractal fractal : fractals) {
			if (fractal.getDataClass().equals(AbstractData.getCurrent().getClass())) {
				try {
					editor = fractal.getEditorClass().getConstructor().newInstance();
					if (!fractal.getDrawerClass().isInstance(drawer)) {
						if (drawer != null) {
							drawer.stop();
						}
						drawer = fractal.getDrawerClass().getConstructor().newInstance();
						splitPane.setRightComponent(drawer);
						if (drawerstart) {
							drawer.start();
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		splitPane.setLeftComponent(editor);
		updateTitle();
		mainPanel.updateUI();
	}
	
	public void update() {
		update(true);
	}
	
	public void updateTitle() {
		File file = App.getCurrentFile();
		String fileName = file != null ? file.getName() : s("window.untitled");
		if (AbstractData.getCurrent().isChanged()) {
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
				mainPanel.add(welcomePanel, BorderLayout.CENTER);
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
				if (drawer != null) {
					setContentPane(drawer);
				} else {
					setContentPane(welcomePanel);
				}
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
		mainPanel.add(welcomePanel, BorderLayout.CENTER);
		if (AbstractFileParser.getCurrentFileParser() != null) {
			update(false);
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
			}, "WaitDrawerStart").start();
		}
		
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
