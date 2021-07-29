/**
 * 
 */
package io.tatlook.lchaos.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.data.AbstractData;
import io.tatlook.lchaos.data.MultipleRulesData;

/**
 * @author Administrator
 *
 */
public abstract class MultipleRulesEditor extends AbstractEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7042634267654246265L;

	protected Vector<AbstractRulePanel> rulePanels = new Vector<>();
	protected JButton createRuleButton = new JButton("Create a Rule");
	protected JPanel createRulePanel = new JPanel();
	
	/**
	 * 
	 */
	public MultipleRulesEditor() {
		super();
		
		createRulePanels();
		createCreateRulePanel();
	}

	private void createCreateRulePanel() {
		createRuleButton.addActionListener((e) -> createRule(true));
		createRulePanel.add(createRuleButton);
		createRulePanel.setBorder(BROAD_SPACING_BORDER_BORDER);
		contentBox.add(createRulePanel);
	}
	
	protected abstract void createRulePanels();
	
	@SuppressWarnings("serial")
	protected abstract class AbstractRulePanel extends JPanel {
		protected int panelIndex = rulePanels.size();
		protected JButton deleteButton = new JButton("✕");
		
		
		public AbstractRulePanel() {
			super(new BorderLayout());
			
			final int buttonMaximumHeight = 22;
			deleteButton.setMaximumSize(new Dimension(buttonMaximumHeight, buttonMaximumHeight));
			deleteButton.setSize(buttonMaximumHeight, buttonMaximumHeight);
			deleteButton.addActionListener((e) -> {
				if (rulePanels.size() <= 0) {
					throw new AssertionError();
				}
				// Poista tiedoista
				MultipleRulesData.getCurrent().removeRule(panelIndex);
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
				AbstractData.getCurrent().setChanged(true);
			});
		}
		
		@Override
		public void updateUI() {
			super.updateUI();
			setMaximumSize(new Dimension(getMaximumSize().width, 110));
			setBorder(BorderFactory.createTitledBorder("Rule " + (panelIndex + 1)));
			if (deleteButton == null) {
				deleteButton = new JButton("✕");
			}
			deleteButton.setEnabled(rulePanels.size() != 1);
		}
	}

	protected abstract void createRule(boolean itIsNew);

}
