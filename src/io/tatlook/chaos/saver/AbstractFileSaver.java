/**
 * 
 */
package io.tatlook.chaos.saver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import io.tatlook.chaos.App;
import io.tatlook.chaos.ChaosData;
import io.tatlook.chaos.ChaosFileChooser;
import io.tatlook.chaos.ErrorMessageDialog;
import io.tatlook.chaos.FileHistoryManager;

/**
 * @author Administrator
 *
 */
public abstract class AbstractFileSaver {
	protected PrintStream out;
	protected File file;
	
	public AbstractFileSaver(File file) {
		this.file = file;
		try {
			if (file == null) {
				throw new AssertionError();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new PrintStream(file);
		} catch (IOException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
	}
	
	public abstract void save();
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean staticSave() {
		ChaosFileChooser fileChooser = new ChaosFileChooser(JFileChooser.SAVE_DIALOG);
		fileChooser.chose();
		File file = fileChooser.getChaosFile();
		if (file == null) {
			return false;
		}
		AbstractFileSaver saver;
		switch (AbstractFileSaver.getFileExtension(file).toLowerCase()) {
			case "ifs":
				saver = new FractintFileSaver(file);
				break;
			case "ch":
			default: 
				saver = new ChaosFileSaver(file);
				break;
		};
		saver.save();
		ChaosData.current.setChanged(false);
		
		FileHistoryManager.get().add(file);
		App.mainWindow.setTitle(file);
		
		return true;
	}
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean checkFileSave() {
		if (ChaosData.current.isChanged()) {
			int result = ErrorMessageDialog.createSaveDialog();
			if (result == JOptionPane.YES_OPTION) {
				return staticSave();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}
	
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex != -1 && lastIndex != 0) {
			return fileName.substring(lastIndex + 1);
		} else {
			return "";
		}
	}
}
