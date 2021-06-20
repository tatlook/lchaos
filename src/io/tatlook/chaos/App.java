package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

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
						System.out.println("This program doesn't have command-line options");
						System.exit(0);
					}
					// = Throw new FileNotFoundException.
					new ChaosFileParser(file);
				}
			} else {
				new NullChaosFileParser();
			}
		} catch (FileNotFoundException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
    	
    	mainWindow.UI();
    	mainWindow.setVisible(true);
    	mainWindow.waitDrawerStart();
    } 
}
