/**
 * 
 */
package io.tatlook.chaos;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
		
		JLabel messageLabel = new JLabel(e.getMessage());
		String stackTraceString = "";
		{
			StackTraceElement[] elements = e.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				stackTraceString += elements[i].toString();
				stackTraceString += "\n";
			}			
		}
		JTextArea stackTraceTextArea= new JTextArea(stackTraceString);
		stackTraceTextArea.setEditable(false);
		
		panel.add(messageLabel);
		panel.add(stackTraceTextArea);
		dialog.setContentPane(panel);
		dialog.setSize(500, 300);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
		
		System.exit(1);
	}
}
