package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

public class App {
	static MainWindow mainWindow;
	static File defaultFile = new File("rules/swirl.ch");
    public static void main(String[] args) {
    	mainWindow = new MainWindow();
		
    	try {
			new ChaosFileParser(defaultFile).readChaos();
    	} catch (ChaosFileDataException e) {
    		e.openDialog();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	mainWindow.UI();
    	mainWindow.setVisible(true);
    } 
}
