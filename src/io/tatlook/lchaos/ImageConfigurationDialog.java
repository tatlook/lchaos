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
import java.awt.Color;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.tatlook.lchaos.drawer.AbstractDrawer;
import io.tatlook.lchaos.util.SetRunnable;

import static io.tatlook.lchaos.App.s;

/**
 * @author Administrator
 *
 */
public class ImageConfigurationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6577595387861415962L;
	
	private JPanel panel = new JPanel(new BorderLayout());
	
	class ImageInfo {
		int size;
		double paintingZoom;
		double xOffset;
		double yOffset;
		public ImageInfo() {
			AbstractDrawer drawer = App.mainWindow.getDrawer();
			size = drawer.getImageSize();
			paintingZoom = drawer.getPaintingZoom();
			xOffset = drawer.getXOffset();
			yOffset = drawer.getYOffset();
		}
	}
	private ImageInfo info = new ImageInfo();
	
	public ImageConfigurationDialog() {
		super(App.mainWindow, s("ic.title"), true);
		setResizable(false);
		setSize(400, 250);
		setLocationRelativeTo(null);
		setContentPane(panel);
		createConfigurationPanel();
		createConfirmButton();
	}
	
	@SuppressWarnings("serial")
	class ParameterPanel extends JPanel {
		private JComboBox<? extends Number> comboBox;
		private SetRunnable setRunnable;
		public <T extends Number> ParameterPanel(String name, T[] values, T defaultValue, SetRunnable setRunnable) {
			super(new BorderLayout());
			
			comboBox = new JComboBox<T>(values);
			this.setRunnable = setRunnable;
			
			comboBox.setEditable(true);
			comboBox.setSelectedItem(defaultValue);
			comboBox.addItemListener((e) -> {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				chengeValue();
			});
			
			JLabel nameLabel = new JLabel(name);
			nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
			add(nameLabel, BorderLayout.WEST);
			add(comboBox, BorderLayout.CENTER);
			
			setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		}
		
		private void chengeValue() {
			try {
				setRunnable.set(comboBox.getSelectedItem().toString());
				comboBox.setBorder(new JComboBox<>().getBorder());
			} catch (NumberFormatException e) {
				comboBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 255), 3));
			}
		}
	}
	
	private void createConfigurationPanel() {
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		
		box.add(new ParameterPanel(s("ic.image_size"), new Integer[] {
				300, 500, 1000, 2000, 3000, 4000, 5000
		}, info.size, (value) -> {
			int v = Integer.parseInt(value);
			if (v < 0) {
				throw new NumberFormatException();
			}
			info.size = v;
		}));
		box.add(new ParameterPanel(s("ic.painting_zoom"), new Double[] {
				0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75
		}, info.paintingZoom, (value) -> {
			info.paintingZoom = Double.parseDouble(value);
		}));
		box.add(new ParameterPanel(s("ic.x_offset"), new Double[] {
				-0.5, -0.25, 0.0, 0.25, 0.5
		}, info.xOffset, (value) -> {
			info.xOffset = Double.parseDouble(value);
		}));
		box.add(new ParameterPanel(s("ic.y_offset"), new Double[] {
				-0.5, -0.25, 0.0, 0.25, 0.5
		}, info.yOffset, (value) -> {
			info.yOffset = Double.parseDouble(value);
		}));
		
		this.panel.add(box , BorderLayout.CENTER);
	}

	private void createConfirmButton() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 200, 5, 0));
		JButton confirmButton = new JButton(s("ic.ok"));
		JButton cancelButton = new JButton(s("ic.cancel"));
		confirmButton.addActionListener((e) -> {
			AbstractDrawer drawer = App.mainWindow.getDrawer();
			if (drawer.getImageSize() != info.size) {
				drawer.setImageSize(info.size);
			}
			drawer.setPaintingZoom(info.paintingZoom);
			drawer.setXOffset(info.xOffset);
			drawer.setYOffset(info.yOffset);
			super.dispose();
		});
		cancelButton.addActionListener((e) -> {
			super.dispose();
		});
		panel.add(confirmButton);
		panel.add(cancelButton);
		this.panel.add(panel, BorderLayout.SOUTH);
	}
}
