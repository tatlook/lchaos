/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import java.util.Iterator;
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

	private static final Border STD_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private static final Border VERCTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 0, 2, 0);
	private static final Border HORIZONTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(0, 1, 0, 2);
	private static final Border BROAD_SPACING_BORDER_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	
	private Box contentBox;
	private Vector<RulePanel> rulePanels;
	private JButton createRuleButton = new JButton("Create a rule");
	private JPanel createRulePanel = new JPanel();
	
	public ToolPanel() {
		super(new BorderLayout());
		try {
			ChaosFileParser.getCurrentFileParser().readChaos();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
		contentBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(contentBox,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentBox.setBorder(BROAD_SPACING_BORDER_BORDER);
		add(scrollPane, BorderLayout.CENTER);
		createSpeedControl();
		createCreateRulePanel();
	}
	
	private void createCreateRulePanel() {
		rulePanels = new Vector<>();
		
		for (int i = 0; i < ChaosData.current.getDist().length; i++) {
			createRule(false);
		}

		createRuleButton.addActionListener((e) -> createRule(true));
		createRulePanel.add(createRuleButton);
		createRulePanel.setBorder(BROAD_SPACING_BORDER_BORDER);
		contentBox.add(createRulePanel);
	}
	
	private void createSpeedControl() {
		JPanel speedControlPanel = new JPanel();
		speedControlPanel.setBorder(BorderFactory.createTitledBorder("Speed"));
		speedControlPanel.setMaximumSize(new Dimension(speedControlPanel.getMaximumSize().width, 90));
		
		JSlider slider = new JSlider(0, 10);
		slider.setValue(0);
		slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.addChangeListener((e) -> {
			int sliderValue = slider.getValue();
			int waitTime = (Byte.MAX_VALUE) / (sliderValue + 1);
			if (sliderValue == 10) {
				waitTime = 0;
			}
			App.mainWindow.getDrawer().setWaitTime(waitTime);
		});
		
		speedControlPanel.add(slider);
		
		contentBox.add(speedControlPanel);
	}
	
	private JLabel createSpacing() {
		JLabel spacing = new JLabel();
		spacing.setBorder(HORIZONTAL_SPACING_BORDER);
		return spacing;
	}
	
	@SuppressWarnings("serial")
	class RulePanel extends JPanel {
		int panelIndex = rulePanels.size();
		
		public RulePanel() {
			super(new BorderLayout());
			
			setMaximumSize(new Dimension(getMaximumSize().width, 110));
			System.out.println("ToolPanel.createRule()");
			
			final int fieldMinimumWidth = 100;
			final int fieldMaximumHeight = 22;
			Border border = BorderFactory.createTitledBorder("Rule" + (panelIndex + 1));
			setBorder(border);
			{
				JLabel label = new JLabel("Possibility");
				JTextField textField = new JTextField("" + ChaosData.current.getDist()[panelIndex]);
				textField.setMaximumSize(new Dimension(textField.getMaximumSize().width, fieldMaximumHeight));
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.isAltDown() || e.isActionKey()) {
							return;
						}
						Double value;
						try {
							value = Double.parseDouble(textField.getText());
							if (value < 0) {
								return;
							}
						} catch (NumberFormatException e2) {
							return;
						}
						Vector<Double> vector = ChaosData.current.getDistVector();
						Double dists = vector.get(panelIndex);
						dists = value;
						vector.set(panelIndex, dists);
						App.mainWindow.getDrawer().setChange();
						ChaosData.current.setChange();
					}
				});
				JButton deleteButton = new JButton("✕");
				deleteButton.setMaximumSize(new Dimension(fieldMaximumHeight, fieldMaximumHeight));
				deleteButton.setSize(fieldMaximumHeight, fieldMaximumHeight);
				deleteButton.addActionListener((e) -> {
					if (rulePanels.size() <= 0) {
						return;
					}
					ChaosData.current.removeRule(panelIndex);
					for (int i = panelIndex + 1; i < rulePanels.size(); i++) {
						RulePanel panel = rulePanels.get(i);
						panel.panelIndex--;
						panel.updateUI();
					}
					contentBox.remove(this);
					rulePanels.remove(this);
					contentBox.updateUI();
				});
				
				Box box = Box.createHorizontalBox();
				box.add(label);
				box.add(createSpacing());
				box.add(textField);
				box.add(createSpacing());
				box.add(deleteButton);
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.NORTH);
			}
			{
				Box xBox = Box.createHorizontalBox();
				JLabel label = new JLabel("CX");
				xBox.add(label);
				for (int i = 0; i < ChaosData.current.getCX()[0].length; i++) {
					JTextField field = new JTextField("" + ChaosData.current.getCX()[panelIndex][i]);
					field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
					field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
					final int theI = i;
					field.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent e) {
							if (e.getKeyCode() != KeyEvent.VK_ENTER) {
								return;
							}
							Double value;
							try {
								value = Double.parseDouble(field.getText());
								if (value < 0) {
									return;
								}
							} catch (NumberFormatException e2) {
								return;
							}
							Vector<Double[]> vector = ChaosData.current.getCXVector();
							Double[] cxs = vector.get(panelIndex);
							cxs[theI] = value;
							vector.set(panelIndex, cxs);
							App.mainWindow.getDrawer().setChange();
							ChaosData.current.setChange();
						}
					});
					xBox.add(createSpacing());
					xBox.add(field);
				}
				xBox.setBorder(STD_SPACING_BORDER);
				add(xBox);
			}
			{
				Box yBox = Box.createHorizontalBox();
				JLabel label = new JLabel("CY");
				yBox.add(label);
				for (int i = 0; i < ChaosData.current.getCY()[0].length; i++) {
					JTextField field = new JTextField("" + ChaosData.current.getCY()[panelIndex][i]);
					field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
					field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
					final int theI = i;
					field.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent e) {
							if (e.getKeyCode() != KeyEvent.VK_ENTER) {
								return;
							}
							Double value;
							try {
								value = Double.parseDouble(field.getText());
							} catch (NumberFormatException e2) {
								return;
							}
							Vector<Double[]> vector = ChaosData.current.getCYVector();
							Double[] cys = vector.get(panelIndex);
							cys[theI] = value;
							vector.set(panelIndex, cys);
							App.mainWindow.getDrawer().setChange();
							ChaosData.current.setChange();
						}
					});
					yBox.add(createSpacing());
					yBox.add(field);
				}
				yBox.setBorder(STD_SPACING_BORDER);
				add(yBox, BorderLayout.SOUTH);
			}
			System.out.print(rulePanels.size());
			
			contentBox.remove(createRulePanel);
			contentBox.add(this);
			contentBox.add(createRulePanel);
			updateUI();
			rulePanels.add(this);
		}
	}
	
	private void createRule(boolean itIsNew) {
		RulePanel panel = new RulePanel();
		
		if (itIsNew) {
			ChaosData.current.addRule();
			App.mainWindow.getDrawer().setChange();
			ChaosData.current.setChange();
		}
	}
}
