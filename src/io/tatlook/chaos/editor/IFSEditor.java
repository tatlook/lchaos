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
import javax.swing.Box;
import javax.swing.JLabel;

import io.tatlook.chaos.App;
import io.tatlook.chaos.data.ChaosData;

import java.util.Vector;

/**
 * @author Administrator
 *
 */
public class IFSEditor extends AbstractEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3016839462507037732L;

	public IFSEditor() {
		super();
	}
	
	private JLabel createSpacing() {
		JLabel spacing = new JLabel();
		spacing.setBorder(HORIZONTAL_SPACING_BORDER);
		return spacing;
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
					ChaosData.current.removeRule(panelIndex);
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
					ChaosData.current.setChanged(true);
				});
				
				Box box = Box.createHorizontalBox();
				box.add(new JLabel("Possibility"));
				box.add(createSpacing());
				box.add(new EditTextField("" + ChaosData.current.getDist()[panelIndex], (value) -> {
					Double value2 = Double.valueOf(value);
					if (value2 < 0) {
						throw new NumberFormatException();
					}
					Vector<Double> vector = ChaosData.current.getDistVector();
					Double dists = vector.get(panelIndex);
					dists = value2;
					vector.set(panelIndex, dists);
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
					box.add(new EditTextField("" + ChaosData.current.getCX()[panelIndex][i], (value) -> {
						Double value2 = Double.valueOf(value);
						Vector<Double[]> vector = ChaosData.current.getCXVector();
						Double[] cxs = vector.get(panelIndex);
						cxs[theI] = value2;
						vector.set(panelIndex, cxs);
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
					box.add(new EditTextField("" + ChaosData.current.getCY()[panelIndex][i], (value) -> {
						Double value2 = Double.valueOf(value);
						Vector<Double[]> vector = ChaosData.current.getCYVector();
						Double[] cxs = vector.get(panelIndex);
						cxs[theI] = value2;
						vector.set(panelIndex, cxs);
					}));
				}
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.SOUTH);
			}
			System.out.print(rulePanels.size());
		}
	}
	
	@Override
	protected void createRule(boolean itIsNew) {
		System.out.println("IFSEditor.createRule()");
		
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
