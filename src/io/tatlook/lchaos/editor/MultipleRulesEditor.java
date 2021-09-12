/**
 * 
 */
package io.tatlook.lchaos.editor;

import static io.tatlook.lchaos.App.s;

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
	protected JButton createRuleButton = new JButton(s("multieditor.create_rule"));
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
		createRuleButton.addActionListener((e) -> addRule());
		createRulePanel.add(createRuleButton);
		createRulePanel.setBorder(BROAD_SPACING_BORDER_BORDER);
		contentBox.add(createRulePanel);
	}
	
	protected abstract void createRulePanels();
	
	@SuppressWarnings("serial")
	protected abstract class AbstractRulePanel extends JPanel {
		private int index = rulePanels.size();
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
				MultipleRulesData.getCurrent().removeRule(index);
				// Tämän jälkeen paneelia pitää tiedä, että sen numero vaihtuu.
				for (int i = index + 1; i < rulePanels.size(); i++) {
					AbstractRulePanel panel = rulePanels.get(i);
					panel.index--;
					// Virkistää näyttön
					panel.updateUI();
				}	
				
				contentBox.remove(this);
				rulePanels.remove(this);
				rulePanels.get(0).updateUI();
				contentBox.updateUI();
				
				App.mainWindow.getDrawer().setChange();
				AbstractData.getCurrent().checkChanged();
			});
		}
		
		/**
		 * Returns the index of the rule.
		 * 
		 * @return the index of the rule
		 */
		public int getIndex() {
			return index;
		}

		@Override
		public void updateUI() {
			super.updateUI();
			setMaximumSize(new Dimension(getMaximumSize().width, 110));
			setBorder(BorderFactory.createTitledBorder(s("multieditor.rule", (getIndex() + 1))));
			if (deleteButton == null) {
				deleteButton = new JButton("✕");
			}
			deleteButton.setEnabled(rulePanels.size() != 1);
		}
	}

	public void addRule() {
		MultipleRulesData.getCurrent().addRule();
		App.mainWindow.getDrawer().setChange();
		AbstractData.getCurrent().checkChanged();
		
		AbstractRulePanel panel = createRulePanel();
		contentBox.remove(createRulePanel);
		contentBox.add(panel);
		contentBox.add(createRulePanel);
		rulePanels.add(panel);
		rulePanels.get(0).updateUI();
		panel.updateUI();
	}

	protected abstract AbstractRulePanel createRulePanel();

}
