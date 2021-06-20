/**
 * 
 */
package io.tatlook.chaos;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * @author Administrator
 *
 */
public class ErrorMessageDialog {
	private ErrorMessageDialog() {
	}
	
	public static void createExceptionDialog(Exception e) {
		JDialog dialog = new JDialog(App.mainWindow, true);
		JPanel panel = new JPanel();
		
		
		dialog.setTitle(e.getClass().getName());
		
		JLabel messageLabel = new JLabel(e.getClass().getName());
		if (e.getMessage() != null) {
			messageLabel.setText(e.getClass().getName() + ": " + e.getMessage());
		}
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
	
	public static int createSaveDialog() {
		int result = JOptionPane.showConfirmDialog(
                App.mainWindow,
                "If you don't save, your changes will be lost.",
                "Save the changes?",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
		return result;
	}
}