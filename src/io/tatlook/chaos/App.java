package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class App {
	static MainWindow mainWindow;
	static File defaultFile = new File("bin/sysrule.ch");
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