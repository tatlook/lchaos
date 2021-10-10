/**
 * 
 */
package io.tatlook.lchaos.randomwalk;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import io.tatlook.lchaos.App;
import io.tatlook.lchaos.drawer.RandomWalkDrawer;
import io.tatlook.lchaos.editor.AbstractEditor;

/**
 * @author Administrator
 *
 */
public class RandomWalkEditor extends AbstractEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5244867784897992563L;

	/**
	 * 
	 */
	public RandomWalkEditor() {
		super();
		createSpeedControl();
		createDoWhenPointOutOfBoundsOptionPanel();
		createPointDistanceOptionPanel();
	}

	private void createDoWhenPointOutOfBoundsOptionPanel() {
		
		Box box = Box.createHorizontalBox();
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JRadioButton doNothingButton = new JRadioButton("Do Nothing");
		JRadioButton backToInitialButton = new JRadioButton("Back to Initial Position");
		JRadioButton stopButton = new JRadioButton("Stop");
		stopButton.setSelected(true);
		
		doNothingButton.addChangeListener((e) -> {
			RandomWalkDrawer drawer = (RandomWalkDrawer) App.mainWindow.getDrawer();
			drawer.setDoOnPointOut(RandomWalkDrawer.DO_NOTHING_ON_POINT_OUT);
		});
		backToInitialButton.addChangeListener((e) -> {
			RandomWalkDrawer drawer = (RandomWalkDrawer) App.mainWindow.getDrawer();
			drawer.setDoOnPointOut(RandomWalkDrawer.BACK_TO_INITIAL_POSITION_ON_POINT_OUT);
		});
		stopButton.addChangeListener((e) -> {
			RandomWalkDrawer drawer = (RandomWalkDrawer) App.mainWindow.getDrawer();
			drawer.setDoOnPointOut(RandomWalkDrawer.STOP_ON_POINT_OUT);
		});
		
		buttonGroup.add(doNothingButton);
		buttonGroup.add(backToInitialButton);
		buttonGroup.add(stopButton);
		
		box.add(doNothingButton);
		box.add(backToInitialButton);
		box.add(stopButton);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Do When Point Out of Bounds"));
		panel.add(box);
		contentBox.add(panel);
	}
	
	private void createPointDistanceOptionPanel() {
		Box box = Box.createHorizontalBox();
		
		box.add(new EditTextField("1", (value) -> {
			int pointDistance = Integer.parseInt(value);
			if (pointDistance < 0) {
				throw new NumberFormatException();
			}
			RandomWalkDrawer drawer = (RandomWalkDrawer) App.mainWindow.getDrawer();
			drawer.setPointDistance(pointDistance);
		}));
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Point Distance"));
		panel.add(box, BorderLayout.CENTER);
		contentBox.add(panel);
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

}
