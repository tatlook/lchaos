/**
 * 
 */
package io.tatlook.chaos;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Administrator
 *
 */
public class ErrorMessageDialog {
	private ErrorMessageDialog() {
	}
	
	public static void createChaosFileDataExceptionDialog(ChaosFileDataException e) {
		JDialog dialog = new JDialog(App.mainWindow, true);
		JPanel panel = new JPanel();
		
		
		dialog.setTitle("File format error");
		
		JLabel messageLabel = new JLabel("chhhhhiuciodu" + e.getMessage());
		
		panel.add(messageLabel);
		dialog.setContentPane(panel);
		dialog.setSize(300, 200);
		dialog.setResizable(false);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}
}
