/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

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

	private Box contentBox;
	private Vector<JPanel> rulePanels;
	private JButton createRuleButton = new JButton("Create a rule");
	
	public ToolPanel() {
		try {
			ChaosFileParser.getCurrentFileParser().readChaos();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
		contentBox = Box.createVerticalBox();
		add(contentBox);
		createRandomControl();
		createRulePanel();
	}
	
	private void createRulePanel() {
		rulePanels = new Vector<>();
		
		for (int i = 0; i < ChaosData.current.dist.length; i++) {
			createRule(false);
		}
		
		createRuleButton.addActionListener((e) -> createRule(true));
		contentBox.add(createRuleButton);
	}
	
	private void createRandomControl() {
		Box box = Box.createHorizontalBox();
		
		Border border = BorderFactory.createTitledBorder("Possibility of rule");
		box.setBorder(border);
		
		for (int i = 0; i < ChaosData.current.dist.length; i++) {
			JLabel label = new JLabel("Rule" + (i + 1));
			JTextField textField = new JTextField("" + ChaosData.current.dist[i]);
			box.add(label);
			box.add(textField);
		}
		
		contentBox.add(box);
	}
	
	private void createRule(boolean itIsNew) {
		JPanel panel = new JPanel(new BorderLayout());
		
		if (itIsNew) {
			ChaosData.current.addRule();
		}
		
		System.out.println("ToolPanel.createRule()");
		int panelIndex = rulePanels.size();
		
		Border border = BorderFactory.createTitledBorder("Rule" + (panelIndex + 1));
		panel.setBorder(border);
		{
			Box xBox = Box.createHorizontalBox();
			JLabel label = new JLabel("CX");
			xBox.add(label);
			for (int i = 0; i < ChaosData.current.getCX()[0].length; i++) {
				JTextField field = new JTextField("" + ChaosData.current.getCX()[panelIndex][i]);
				xBox.add(field);
			}
			panel.add(xBox);
		}
		{
			Box yBox = Box.createHorizontalBox();
			JLabel label = new JLabel("CY");
			yBox.add(label);
			for (int i = 0; i < ChaosData.current.getCY()[0].length; i++) {
				JTextField field = new JTextField("" + ChaosData.current.getCY()[panelIndex][i]);
				yBox.add(field);
			}
			panel.add(yBox, BorderLayout.SOUTH);
		}
		System.out.print(rulePanels.size());
		
		contentBox.remove(createRuleButton);
		contentBox.add(panel);
		contentBox.add(createRuleButton);
		panel.updateUI();
		rulePanels.add(panel);
	}
}
