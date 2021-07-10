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

package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

import java.util.Vector;

/**
 * @author Administrator
 *
 */
public class ToolPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3016839462507037732L;

	private static final Border STD_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	@SuppressWarnings("unused")
	private static final Border VERCTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 0, 2, 0);
	private static final Border HORIZONTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(0, 1, 0, 2);
	private static final Border BROAD_SPACING_BORDER_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	
	private Box contentBox;
	private Vector<RulePanel> rulePanels;
	private JButton createRuleButton = new JButton("Create a Rule");
	private JPanel createRulePanel = new JPanel();
	
	public ToolPanel() {
		super(new BorderLayout());
		contentBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(contentBox,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentBox.setBorder(BROAD_SPACING_BORDER_BORDER);
		add(scrollPane, BorderLayout.CENTER);
		createSpeedControl();
		createCreateRulePanel();
	}
	
	private void createCreateRulePanel() {
		rulePanels = new Vector<>();
		
		for (int i = 0; i < ChaosData.current.getDist().length; i++) {
			createRule(false);
		}
		
		createRuleButton.addActionListener((e) -> createRule(true));
		createRulePanel.add(createRuleButton);
		createRulePanel.setBorder(BROAD_SPACING_BORDER_BORDER);
		contentBox.add(createRulePanel);
	}
	
	private void createSpeedControl() {
		JPanel speedControlPanel = new JPanel();
		speedControlPanel.setBorder(BorderFactory.createTitledBorder("Speed"));
		speedControlPanel.setMaximumSize(new Dimension(speedControlPanel.getMaximumSize().width, 90));
		
		JSlider slider = new JSlider(0, 10);
		if (App.mainWindow.getDrawer() != null) {
			int waitLevel = App.mainWindow.getDrawer().getWaitLevel();
			slider.setValue(waitLevel);			
		} else {
			slider.setValue(0);
		}
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.addChangeListener((e) -> {
			int waitLevel = slider.getValue();
			App.mainWindow.getDrawer().setWaitLevel(waitLevel);
		});
		
		speedControlPanel.add(slider);
		
		contentBox.add(speedControlPanel);
	}
	
	private JLabel createSpacing() {
		JLabel spacing = new JLabel();
		spacing.setBorder(HORIZONTAL_SPACING_BORDER);
		return spacing;
	}
	
	@SuppressWarnings("serial")
	class RulePanel extends JPanel {
		int panelIndex = rulePanels.size();
		private Border border;
		private JButton deleteButton = new JButton("✕");
		
		private Double parseFieldValue(JTextField textField, boolean negative) throws NumberFormatException {
			Double value;
			try {
				value = Double.parseDouble(textField.getText());
				if ((!negative) && value < 0) {
					throw new NumberFormatException();
				}
				textField.setBorder(new JTextField().getBorder());
			} catch (NumberFormatException e) {
				textField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 255), 3));
				throw e;
			}
			
			return value;
		}
		
		public RulePanel() {
			super(new BorderLayout());
			
			abstract class DocumentAdapter implements DocumentListener {
				@Override
				public void removeUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
			}
			
			final int fieldMinimumWidth = 100;
			final int fieldMaximumHeight = 22;
			{
				JLabel label = new JLabel("Possibility");
				JTextField textField = new JTextField("" + ChaosData.current.getDist()[panelIndex]);
				addListeners(textField);
				textField.setMaximumSize(new Dimension(textField.getMaximumSize().width, fieldMaximumHeight));
				textField.getDocument().addUndoableEditListener(new UndoManager());
				textField.getDocument().addDocumentListener(new DocumentAdapter() {
					@Override
					public void changedUpdate(DocumentEvent e) {
						Double value;
						try {
							value = parseFieldValue(textField, false);
						} catch (NumberFormatException e2) {
							return;
						}
						Vector<Double> vector = ChaosData.current.getDistVector();
						Double dists = vector.get(panelIndex);
						dists = value;
						vector.set(panelIndex, dists);
						App.mainWindow.getDrawer().setChange();
						ChaosData.current.setChanged(true);
					}
				});
				deleteButton.setMaximumSize(new Dimension(fieldMaximumHeight, fieldMaximumHeight));
				deleteButton.setSize(fieldMaximumHeight, fieldMaximumHeight);
				deleteButton.addActionListener((e) -> {
					if (rulePanels.size() <= 0) {
						throw new AssertionError();
					}
					// Poista tiedoista
					ChaosData.current.removeRule(panelIndex);
					// Tämän jälkeen paneelia pitää tiedä, että sen numero vaihtuu.
					for (int i = panelIndex + 1; i < rulePanels.size(); i++) {
						RulePanel panel = rulePanels.get(i);
						panel.panelIndex--;
						// Virkistää näyttön
						panel.updateUI();
					}	
					
					contentBox.remove(this);
					rulePanels.remove(this);
					rulePanels.get(0).updateUI();
					contentBox.updateUI();
					
					App.mainWindow.getDrawer().setChange();
					ChaosData.current.setChanged(true);
				});
				
				Box box = Box.createHorizontalBox();
				box.add(label);
				box.add(createSpacing());
				box.add(textField);
				box.add(createSpacing());
				box.add(deleteButton);
				box.add(createSpacing());
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.NORTH);
			}
			{
				Box xBox = Box.createHorizontalBox();
				JLabel label = new JLabel("CX");
				xBox.add(label);
				for (int i = 0; i < 3; i++) {
					JTextField field = new JTextField("" + ChaosData.current.getCX()[panelIndex][i]);
					addListeners(field);
					field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
					field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
					final int theI = i;
					field.getDocument().addDocumentListener(new DocumentAdapter() {
						@Override
						public void changedUpdate(DocumentEvent e) {
							Double value;
							try {
								value = parseFieldValue(field, true);
							} catch (NumberFormatException e2) {
								return;
							}
							Vector<Double[]> vector = ChaosData.current.getCXVector();
							Double[] cxs = vector.get(panelIndex);
							cxs[theI] = value;
							vector.set(panelIndex, cxs);
							App.mainWindow.getDrawer().setChange();
							ChaosData.current.setChanged(true);
						}
					});
					xBox.add(createSpacing());
					xBox.add(field);
				}
				xBox.setBorder(STD_SPACING_BORDER);
				add(xBox);
			}
			{
				Box yBox = Box.createHorizontalBox();
				JLabel label = new JLabel("CY");
				yBox.add(label);
				for (int i = 0; i < 3; i++) {
					JTextField field = new JTextField("" + ChaosData.current.getCY()[panelIndex][i]);
					addListeners(field);
					field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
					field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
					final int theI = i;
					field.getDocument().addDocumentListener(new DocumentAdapter() {
						@Override
						public void changedUpdate(DocumentEvent e) {
							Double value;
							try {
								value = parseFieldValue(field, true);
							} catch (NumberFormatException e2) {
								return;
							}
							Vector<Double[]> vector = ChaosData.current.getCYVector();
							Double[] cys = vector.get(panelIndex);
							cys[theI] = value;
							vector.set(panelIndex, cys);
							App.mainWindow.getDrawer().setChange();
							ChaosData.current.setChanged(true);
						}
					});
					yBox.add(createSpacing());
					yBox.add(field);
				}
				yBox.setBorder(STD_SPACING_BORDER);
				add(yBox, BorderLayout.SOUTH);
			}
			System.out.print(rulePanels.size());
		}
		
		private void addListeners(JTextField field) {
			UndoManager manager = new UndoManager();
			
			JMenuItem undoMenuItem = new JMenuItem("Undo    (Ctrl+Z)");
			JMenuItem redoMenuItem = new JMenuItem("Redo    (Ctrl+Y)");
			field.getDocument().addUndoableEditListener((e) -> {
				manager.undoableEditHappened(e);
				undoMenuItem.setEnabled(manager.canUndo());
				redoMenuItem.setEnabled(manager.canRedo());
			});
			
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			ActionListener cutActionListener = (e) -> {
				String selected = field.getSelectedText();
				// Jos ei ole valinnut mitään, leikkaa kaikki tekstikenttästä pois.
				if (selected == null || selected.equals("")) {
					Transferable transferable = new StringSelection(field.getText());
					clipboard.setContents(transferable, null);
					field.setText("");
				} else {
					field.cut();
				}
			};
			ActionListener copyActionListener = (e) -> {
				String selected = field.getSelectedText();
				// Jos ei ole valinnut mitään, kopioida kaikki tekstikenttästä leikepöydälle.
				if (selected == null || selected.equals("")) {
					Transferable transferable = new StringSelection(field.getText());
					clipboard.setContents(transferable, null);
				} else {
					field.copy();
				}
			};
			ActionListener undoActionListener = (e) -> {
				if (manager.canUndo()) {
					manager.undo();
				}
				undoMenuItem.setEnabled(manager.canUndo());
				redoMenuItem.setEnabled(manager.canRedo());
			};
			ActionListener redoActionListener = (e) -> {
				if (manager.canRedo()) {
					manager.redo();
				} 
				undoMenuItem.setEnabled(manager.canUndo());
				redoMenuItem.setEnabled(manager.canRedo());
			};
			
			field.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (!e.isControlDown()) {
						return;
					}
					switch (e.getKeyCode()) {
						case KeyEvent.VK_C :
							copyActionListener.actionPerformed(null);
							break;
						case KeyEvent.VK_X :
							cutActionListener.actionPerformed(null);
							break;
						case KeyEvent.VK_Z :
							undoActionListener.actionPerformed(null);
							break;
						case KeyEvent.VK_Y :
							redoActionListener.actionPerformed(null);
							break;
						default :
							break;
					}
				}
			});
			field.addMouseListener(new MouseAdapter() {
				JPopupMenu popupMenu = new JPopupMenu();
				
				JMenuItem copyMenuItem = new JMenuItem("Copy   (Ctrl+C)");
				JMenuItem pasteMenuItem = new JMenuItem("Paste (Ctrl+V)");
				JMenuItem cutMenuItem = new JMenuItem("Cut      (Ctrl+X)");
				
				{
					undoMenuItem.setEnabled(manager.canUndo());
					redoMenuItem.setEnabled(manager.canRedo());
					copyMenuItem.setMnemonic('C');
					pasteMenuItem.setMnemonic('V');
					cutMenuItem.setMnemonic('X');
					undoMenuItem.setMnemonic('Z');
					redoMenuItem.setMnemonic('Y');
					copyMenuItem.addActionListener(copyActionListener);
					pasteMenuItem.addActionListener((e) -> field.paste());
					cutMenuItem.addActionListener(cutActionListener);
					undoMenuItem.addActionListener(undoActionListener);
					redoMenuItem.addActionListener(redoActionListener);
					popupMenu.add(copyMenuItem);
					popupMenu.add(pasteMenuItem);
					popupMenu.add(cutMenuItem);
					popupMenu.addSeparator();
					popupMenu.add(undoMenuItem);
					popupMenu.add(redoMenuItem);
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						pasteMenuItem.setEnabled(clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor));
						popupMenu.show(field, e.getX(), e.getY());
					}
				}
			});
		}
		
		@Override
		public void updateUI() {
			super.updateUI();
			setMaximumSize(new Dimension(getMaximumSize().width, 110));
			border = BorderFactory.createTitledBorder("Rule " + (panelIndex + 1));
			setBorder(border);
			if (deleteButton == null) {
				deleteButton = new JButton("✕");
			}
			deleteButton.setEnabled(rulePanels.size() != 1);
		}
	}
	
	private void createRule(boolean itIsNew) {
		System.out.println("ToolPanel.createRule()");
		
		if (itIsNew) {
			ChaosData.current.addRule();
			App.mainWindow.getDrawer().setChange();
			ChaosData.current.setChanged(true);
		}
		
		RulePanel panel = new RulePanel();
		contentBox.remove(createRulePanel);
		contentBox.add(panel);
		contentBox.add(createRulePanel);
		rulePanels.add(panel);
		rulePanels.get(0).updateUI();
		panel.updateUI();
	}
}
