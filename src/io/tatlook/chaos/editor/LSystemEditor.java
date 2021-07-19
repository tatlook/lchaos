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

package io.tatlook.chaos.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import io.tatlook.chaos.App;
import io.tatlook.chaos.data.AbstractData;
import io.tatlook.chaos.data.LSystemData;

/**
 * @author Administrator
 *
 */
public class LSystemEditor extends AbstractEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8539324619525093384L;

	/**
	 * 
	 */
	public LSystemEditor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void createRulePanels() {
		createSpeedControl();
		for (int i = 0; i < LSystemData.getCurrent().getRules().size(); i++) {
			createRule(false);
		}
	}
	
	private void createSpeedControl() {
		JPanel speedControlPanel = new JPanel();
		speedControlPanel.setBorder(BorderFactory.createTitledBorder("Order"));
		speedControlPanel.setMaximumSize(new Dimension(speedControlPanel.getMaximumSize().width, 90));
		
		JSlider slider = new JSlider(0, 10);
		if (App.mainWindow.getDrawer() != null) {
			int order = App.mainWindow.getDrawer().getLevel();
			slider.setValue(order);
		} else {
			slider.setValue(0);
		}
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.addChangeListener((e) -> {
			int order = slider.getValue();
			App.mainWindow.getDrawer().setLevel(order);
			App.mainWindow.getDrawer().setChange();
		});
		
		speedControlPanel.add(slider);
		
		contentBox.add(speedControlPanel);
	}

	@SuppressWarnings("serial")
	class RulePanel extends AbstractRulePanel {
		int panelIndex = rulePanels.size();
		
		public RulePanel() {
			super();
			
			{
				final int buttonMaximumHeight = 22;
				deleteButton.setMaximumSize(new Dimension(buttonMaximumHeight, buttonMaximumHeight));
				deleteButton.setSize(buttonMaximumHeight, buttonMaximumHeight);
				deleteButton.addActionListener((e) -> {
					if (rulePanels.size() <= 0) {
						throw new AssertionError();
					}
					// Poista tiedoista
					LSystemData.getCurrent().removeRule(panelIndex);
					// Tämän jälkeen paneelia pitää tiedä, että sen numero vaihtuu.
					for (int i = panelIndex + 1; i < rulePanels.size(); i++) {
						AbstractRulePanel panel = rulePanels.get(i);
						panel.panelIndex--;
						// Virkistää näyttön
						panel.updateUI();
					}	
					
					contentBox.remove(this);
					rulePanels.remove(this);
					rulePanels.get(0).updateUI();
					contentBox.updateUI();
					
					App.mainWindow.getDrawer().setChange();
					LSystemData.getCurrent().setChanged(true);
				});
				
				Box box = Box.createHorizontalBox();
				box.add(new JLabel("From"));
				box.add(createSpacing());
				box.add(new EditTextField("" + LSystemData.getCurrent().getRules().get(panelIndex).from, (value) -> {
					try {
						LSystemData.getCurrent().getRules().get(panelIndex).from = value.charAt(0);
					} catch (IndexOutOfBoundsException e) {
						throw new NumberFormatException();
					}
				}));
				box.add(createSpacing());
				box.add(deleteButton);
				box.add(createSpacing());
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.NORTH);
			}
			{
				Box box = Box.createHorizontalBox();
				box.add(new JLabel("To"));
				box.add(createSpacing());
				box.add(new EditTextField("" + LSystemData.getCurrent().getRules().get(panelIndex).to, (value) -> {
					LSystemData.getCurrent().getRules().get(panelIndex).to = value;
				}));
				box.setBorder(STD_SPACING_BORDER);
				add(box);
			}
			System.out.print(rulePanels.size());
		}
	}
	
	@Override
	protected void createRule(boolean itIsNew) {
		System.out.println("LSystemEditor.createRule()");
		
		if (itIsNew) {
			AbstractData.getCurrent().addRule();
			App.mainWindow.getDrawer().setChange();
			AbstractData.getCurrent().setChanged(true);
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
