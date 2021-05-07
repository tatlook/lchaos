/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
	
	public ToolPanel() {
		contentBox = Box.createVerticalBox();
		add(contentBox);
		createRulePanel();
	}
	
	private void createRulePanel() {
		rulePanels = new Vector<>();
		
		for (int i = 0; i < ChaosData.current.dist.length; i++) {
			createRule();
		}
		
		JButton createRuleButton = new JButton("新建规则");
		createRuleButton.addActionListener((e) -> createRule());
		contentBox.add(createRuleButton);
	}
	
	private void createRule() {
		JPanel panel = new JPanel(new BorderLayout());
		
		System.out.println("ToolPanel.createRule()");
		
		Border border = BorderFactory.createTitledBorder("规则" + (rulePanels.size() + 1));
		panel.setBorder(border);
		
		Box xBox = Box.createHorizontalBox();
		for (int i = 0; i < ChaosData.current.cx.length; i++) {
			Box cxBox = Box.createHorizontalBox();
			JLabel label = new JLabel("CX " + (i + 1));
			cxBox.add(label);
			for (int j = 0; j < ChaosData.current.cx[i].length; j++) {
				JTextField field = new JTextField("" + ChaosData.current.cx[i][j]);
				cxBox.add(field);
			}
			xBox.add(cxBox);
		}
		panel.add(xBox, BorderLayout.SOUTH);
		Box yBox = Box.createHorizontalBox();
		for (int i = 0; i < ChaosData.current.cy.length; i++) {
			Box cyBox = Box.createHorizontalBox();
			JLabel label = new JLabel("CY " + (i + 1));
			cyBox.add(label);
			for (int j = 0; j < ChaosData.current.cy[i].length; j++) {
				JTextField field = new JTextField("" + ChaosData.current.cy[i][j]);
				cyBox.add(field);
			}
			yBox.add(cyBox);
		}
		panel.add(yBox, BorderLayout.NORTH);
		panel.add(new JButton("HHHHA"));
		
		System.out.print(rulePanels.size());
		
		contentBox.add(panel);
		rulePanels.add(panel);
	}
}
