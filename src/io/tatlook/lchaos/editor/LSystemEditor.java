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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.data.LSystemData;
import io.tatlook.lchaos.data.MultipleRulesData;

/**
 * @author Administrator
 *
 */
public class LSystemEditor extends MultipleRulesEditor {

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
		createOrderControl();
		createAxiomAngleControl();
		for (int i = 0; i < LSystemData.getCurrent().getRules().size(); i++) {
			createRule(false);
		}
	}
	
	private void createAxiomAngleControl() {
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.setBorder(BorderFactory.createTitledBorder("Other Parameters"));
		controlPanel.setMaximumSize(new Dimension(controlPanel.getMaximumSize().width, 90));
		
		{
			Box box = Box.createHorizontalBox();
			box.add(new JLabel("Axiom"));
			box.add(createSpacing());
			box.add(new EditTextField(LSystemData.getCurrent().getAxiom(), (value) -> {
				LSystemData.getCurrent().setAxiom(value);
			}));
			box.setBorder(STD_SPACING_BORDER);
			controlPanel.add(box, BorderLayout.SOUTH);
		}
		{
			Box box = Box.createHorizontalBox();
			box.add(new JLabel("Angle"));
			box.add(createSpacing());
			box.add(new EditTextField("" + LSystemData.getCurrent().getAngle(), (value) -> {
				LSystemData.getCurrent().setAngle(Integer.parseInt(value));
			}));
			box.setBorder(STD_SPACING_BORDER);
			controlPanel.add(box, BorderLayout.NORTH);
		}
		
		contentBox.add(controlPanel);
	}
	
	private void createOrderControl() {
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
		public RulePanel() {
			super();
			
			{
				Box box = Box.createHorizontalBox();
				box.add(new JLabel("From"));
				box.add(createSpacing());
				box.add(new EditTextField("" + LSystemData.getCurrent().getRules().get(getIndex()).from, (value) -> {
					try {
						LSystemData.getCurrent().getRules().get(getIndex()).from = value.charAt(0);
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
				box.add(new EditTextField("" + LSystemData.getCurrent().getRules().get(getIndex()).to, (value) -> {
					LSystemData.getCurrent().getRules().get(getIndex()).to = value;
				}));
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.SOUTH);
			}
			System.out.print(rulePanels.size());
		}
	}
	
	@Override
	protected void createRule(boolean itIsNew) {
		System.out.println("LSystemEditor.createRule()");
		
		if (itIsNew) {
			MultipleRulesData.getCurrent().addRule();
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
