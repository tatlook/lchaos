/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
			int waitTime = ((11 - sliderValue) * 500 + 1) / Drawer.imageHeight;
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
		private Border border;
		
		private Double parseFieldValue(JTextField textField, boolean negative) throws NumberFormatException {
			Double value;
			try {
				value = Double.parseDouble(textField.getText());
				if ((!negative) && value < 0) {
					throw new NumberFormatException();
				}
				textField.setBorder(new JTextField().getBorder());
			} catch (NumberFormatException e) {
				textField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 255), 3));
				throw e;
			}
			
			return value;
		}
		
		public RulePanel() {
			super(new BorderLayout());
			
			abstract class DocumentAdapter implements DocumentListener {
				@Override
				public void removeUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
			}
			
			final int fieldMinimumWidth = 100;
			final int fieldMaximumHeight = 22;
			{
				JLabel label = new JLabel("Possibility");
				JTextField textField = new JTextField("" + ChaosData.current.getDist()[panelIndex]);
				textField.setMaximumSize(new Dimension(textField.getMaximumSize().width, fieldMaximumHeight));
				textField.getDocument().addDocumentListener(new DocumentAdapter() {
					@Override
					public void changedUpdate(DocumentEvent e) {
						Double value;
						try {
							value = parseFieldValue(textField, false);
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
						throw new AssertionError();
					}
					// Poista tiedoista
					ChaosData.current.removeRule(panelIndex);
					// Tämän jälkeen paneelia pitää tiedä, että sen numero vaihtuu.
					for (int i = panelIndex + 1; i < rulePanels.size(); i++) {
						RulePanel panel = rulePanels.get(i);
						panel.panelIndex--;
						// Virkistää näyttön
						panel.updateUI();
					}
					contentBox.remove(this);
					rulePanels.remove(this);
					contentBox.updateUI();
					
					App.mainWindow.getDrawer().setChange();
					ChaosData.current.setChange();
				});
				
				Box box = Box.createHorizontalBox();
				box.add(label);
				box.add(createSpacing());
				box.add(textField);
				box.add(createSpacing());
				box.add(deleteButton);
				box.add(createSpacing());
				box.setBorder(STD_SPACING_BORDER);
				add(box, BorderLayout.NORTH);
			}
			{
				Box xBox = Box.createHorizontalBox();
				JLabel label = new JLabel("CX");
				xBox.add(label);
				for (int i = 0; i < 3; i++) {
					JTextField field = new JTextField("" + ChaosData.current.getCX()[panelIndex][i]);
					field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
					field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
					final int theI = i;
					field.getDocument().addDocumentListener(new DocumentAdapter() {
						@Override
						public void changedUpdate(DocumentEvent e) {
							Double value;
							try {
								value = parseFieldValue(field, true);
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
				for (int i = 0; i < 3; i++) {
					JTextField field = new JTextField("" + ChaosData.current.getCY()[panelIndex][i]);
					field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
					field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
					final int theI = i;
					field.getDocument().addDocumentListener(new DocumentAdapter() {
						@Override
						public void changedUpdate(DocumentEvent e) {
							Double value;
							try {
								value = parseFieldValue(field, true);
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
		}
		
		@Override
		public void updateUI() {
			super.updateUI();
			setMaximumSize(new Dimension(getMaximumSize().width, 110));
			border = BorderFactory.createTitledBorder("Rule" + (panelIndex + 1));
			setBorder(border);
		}
	}
	
	private void createRule(boolean itIsNew) {
		System.out.println("ToolPanel.createRule()");
		
		if (itIsNew) {
			ChaosData.current.addRule();
			App.mainWindow.getDrawer().setChange();
			ChaosData.current.setChange();
		}
		
		RulePanel panel = new RulePanel();
		contentBox.remove(createRulePanel);
		contentBox.add(panel);
		contentBox.add(createRulePanel);
		panel.updateUI();
		rulePanels.add(panel);
	}
}
