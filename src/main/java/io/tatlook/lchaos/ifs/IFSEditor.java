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

package io.tatlook.lchaos.ifs;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.editor.AbstractEditor;
import io.tatlook.lchaos.editor.MultipleRulesEditor;
import io.tatlook.lchaos.editor.AbstractEditor.EditTextField;
import io.tatlook.lchaos.editor.MultipleRulesEditor.AbstractRulePanel;

/**
 * @author Administrator
 *
 */
public class IFSEditor extends MultipleRulesEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3016839462507037732L;

	public IFSEditor() {
		super();
	}
	
	@Override
	protected void createRulePanels() {
		createSpeedControl();
		for (int i = 0; i < IFSData.getCurrent().getDist().length; i++) {
			AbstractRulePanel panel = createRulePanel();
			contentBox.add(panel);
			rulePanels.add(panel);
		}
	}
	
	private void createSpeedControl() {
		JPanel speedControlPanel = new JPanel();
		speedControlPanel.setBorder(BorderFactory.createTitledBorder("Speed"));
		speedControlPanel.setMaximumSize(new Dimension(speedControlPanel.getMaximumSize().width, 90));
		
		JSlider slider = new JSlider(0, 10);
		if (App.mainWindow.getDrawer() != null) {
			int waitLevel = App.mainWindow.getDrawer().getLevel();
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
			App.mainWindow.getDrawer().setLevel(waitLevel);
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
				box.add(new JLabel("Possibility"));
				box.add(createSpacing());
				box.add(new EditTextField("" + IFSData.getCurrent().getDist()[getIndex()], (value) -> {
					Double value2 = Double.valueOf(value);
					if (value2 < 0) {
						throw new NumberFormatException();
					}
					IFSData.getCurrent().getDistVector().set(getIndex(), value2);
				}));
				box.add(createSpacing());
				box.add(deleteButton);
				box.add(createSpacing());
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.NORTH);
			}
			{
				Box box = Box.createHorizontalBox();
				box.add(new JLabel("CX"));
				for (int i = 0; i < 3; i++) {
					final int theI = i;
					box.add(createSpacing());
					box.add(new EditTextField("" + IFSData.getCurrent().getCX()[getIndex()][i], (value) -> {
						IFSData.getCurrent().getCXVector().get(getIndex())[theI] = Double.valueOf(value);
					}));
				}
				box.setBorder(STD_SPACING_BORDER);
				add(box);
			}
			{
				Box box = Box.createHorizontalBox();
				box.add(new JLabel("CY"));
				for (int i = 0; i < 3; i++) {
					final int theI = i;
					box.add(createSpacing());
					box.add(new EditTextField("" + IFSData.getCurrent().getCY()[getIndex()][i], (value) -> {
						IFSData.getCurrent().getCYVector().get(getIndex())[theI] = Double.valueOf(value);
					}));
				}
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.SOUTH);
			}
			System.out.print(rulePanels.size());
		}
	}
	
	@Override
	protected RulePanel createRulePanel() {
		return new RulePanel();
	}
}
