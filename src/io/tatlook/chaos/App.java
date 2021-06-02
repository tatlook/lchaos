package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

public class App {
	static MainWindow mainWindow;
	static File defaultFile = new File("rules/bas.ch");
    public static void main(String[] args) {
    	mainWindow = new MainWindow();
		
		try {
			if (args.length > 0) {
				new ChaosFileParser(new File(args[0]));
			} else {
				new ChaosFileParser(defaultFile);
			}
		} catch (FileNotFoundException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
    	
    	mainWindow.UI();
    	mainWindow.setVisible(true);
    	mainWindow.waitDrawerStart();
    } 
}
