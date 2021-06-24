package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

public class App {
	static MainWindow mainWindow;
    public static void main(String[] args) {
    	mainWindow = new MainWindow();
		
		try {
			if (args.length > 0) {
				File file = new File(args[0]);
				if (file.exists()) {
					new ChaosFileParser(file);
				} else {
					if (args[0].charAt(0) == '-') {
						JOptionPane.showMessageDialog(
								mainWindow,
								"This program doesn't have command-line options"
						);
					} else {
						// = Throw new FileNotFoundException.
						new ChaosFileParser(file);
					}
				}
			}
			new NullChaosFileParser();
		} catch (FileNotFoundException e) {
			ErrorMessageDialog.createExceptionDialog(e);
			new NullChaosFileParser();
		}
    	
    	mainWindow.UI();
    	mainWindow.setVisible(true);
    	mainWindow.waitDrawerStart();
    } 
}
