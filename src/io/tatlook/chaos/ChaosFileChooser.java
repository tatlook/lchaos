/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Administrator
 *
 */
public class ChaosFileChooser {
	private File chaosFile;
	private int dialogMode = JFileChooser.OPEN_DIALOG;
	public ChaosFileChooser(int dialogMode) {
		this.dialogMode = dialogMode;
	}

	public ChaosFileChooser() {
	}

	public void chose() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plain text(*.txt)", "txt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Chaos file(*.ch)", "ch"));

        
        int result;
        if (dialogMode == JFileChooser.OPEN_DIALOG) {
            result = fileChooser.showOpenDialog(App.mainWindow);
        } else {
        	result = fileChooser.showSaveDialog(App.mainWindow);
        }
        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();

            System.out.println("Open file: " + file.getAbsolutePath() + "\n\n");
        }
        chaosFile = file;
	}
	
	/**
	 * @return the chaosFile
	 */
	public File getChaosFile() {
		return chaosFile;
	}
}
