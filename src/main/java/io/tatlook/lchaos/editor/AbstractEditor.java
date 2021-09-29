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

package io.tatlook.lchaos.editor;

import static io.tatlook.lchaos.App.s;

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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.util.SetRunnable;

/**
 * @author Administrator
 *
 */
public abstract class AbstractEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1243215275097932380L;

	protected static final Border STD_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	protected static final Border VERCTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 0, 2, 0);
	protected static final Border HORIZONTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(0, 1, 0, 2);
	protected static final Border BROAD_SPACING_BORDER_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	
	protected Box contentBox;

	
	/**
	 * 
	 */
	public AbstractEditor() {
		super(new BorderLayout());
		contentBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(contentBox,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentBox.setBorder(BROAD_SPACING_BORDER_BORDER);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	protected JLabel createSpacing() {
		JLabel spacing = new JLabel();
		spacing.setBorder(HORIZONTAL_SPACING_BORDER);
		return spacing;
	}
	
	@SuppressWarnings("serial")
    public class EditTextField extends JTextField {
		final int fieldMinimumWidth = 100;
		final int fieldMaximumHeight = 22;
		
		public EditTextField(String value, SetRunnable doSet) {
			super(value);
			
			setMinimumSize(new Dimension(fieldMinimumWidth, 0));
			setMaximumSize(new Dimension(getMaximumSize().width, fieldMaximumHeight));
			
			addListeners(this);
			getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					try {
						doSet.set(getText());
						setBorder(new JTextField().getBorder());
					} catch (NumberFormatException e2) {
						setBorder(BorderFactory.createLineBorder(new Color(180, 180, 255), 3));
						return;
					}
					App.mainWindow.getDrawer().setChange();
					AbstractData.getCurrent().checkChanged();
				}
				
			});
		}
		
		protected void addListeners(JTextField field) {
			UndoManager manager = new UndoManager();
			
			JMenuItem undoMenuItem = new JMenuItem(s("editf.undo"));
			JMenuItem redoMenuItem = new JMenuItem(s("editf.redo"));
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
				
				JMenuItem copyMenuItem = new JMenuItem(s("editf.copy"));
				JMenuItem pasteMenuItem = new JMenuItem(s("editf.paste"));
				JMenuItem cutMenuItem = new JMenuItem(s("editf.cut"));
				
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
	};
}
