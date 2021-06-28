/**
 * 
 */
package io.tatlook.chaos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
/**
 * @author Administrator
 *
 */
public class FileHistoryManager {
	private Vector<File> historyFiles = new Vector<>();
	private JMenu openRecentMenu = new JMenu("Open Recent");
	private Vector<RecentMenuItem> openRecentMenuItems;
	private File fileHistoryRecordFile = new File("data/filehistory");
	
	private static final FileHistoryManager MANAGER = new FileHistoryManager();
	private static final int RECENT_MENU_ITEM_INDEX = 3;
	
	private FileHistoryManager() {
		JMenuItem clearAllFilesMenuItem = new JMenuItem("Clear All Files");
		JMenuItem clearMissingFilesMenuItem = new JMenuItem("Clear Missing Files");
		clearAllFilesMenuItem.setMnemonic('C');
		clearMissingFilesMenuItem.setMnemonic('M');
		
		openRecentMenu.add(clearAllFilesMenuItem);
		openRecentMenu.add(clearMissingFilesMenuItem);
		openRecentMenu.addSeparator();
		
		clearAllFilesMenuItem.addActionListener((e) -> removeAll());
		clearMissingFilesMenuItem.addActionListener((e) -> removeMissings());
		
		findRecentFiles();
		for (RecentMenuItem recentMenuItem : openRecentMenuItems) {
			openRecentMenu.add(recentMenuItem, RECENT_MENU_ITEM_INDEX);
		}
	}

	@SuppressWarnings("serial")
	class RecentMenuItem extends JMenuItem {
		File file;
		public RecentMenuItem(File file) {
			super(file.getPath());
			this.file = file;
			addActionListener((e) -> {
				MenuBar.checkFileSave();
				try {
					MenuBar.openFile(file);					
				} catch (FileNotFoundException e1) {
					ErrorMessageDialog.createExceptionDialog(e1);
				}
			});
		}
	}
	
	/**
	 * Poistaa jotain sama kuin historyFiles[i]
	 * 
	 * @param i
	 */
	private void clearPepeat(int i) {
		for (int j = i + 1; j < historyFiles.size(); j++) {
			if (historyFiles.get(i).equals(historyFiles.get(j))) {
				remove(i);
			}
		}
	}
	
	/**
	 * Poistaa samat asiat että vain yksi jäljellä
	 */
	private void clearPepeat() {
		for (int i = 0; i < historyFiles.size(); i++) {
			clearPepeat(i);
		}
	}
	
	/**
	 * Lue historia tiedostosta
	 */
	private void findRecentFiles() {
		openRecentMenuItems = new Vector<>();
		
		// Jos ei ole, tee sen.
		if (!fileHistoryRecordFile.exists()) {
			File directory = new File("data");
			if (!directory.isDirectory()) {
				directory.mkdir();
			}
			try {
				fileHistoryRecordFile.createNewFile();
			} catch (IOException e) {
			}
			return;
		}
		
		try {
			Scanner scanner = new Scanner(fileHistoryRecordFile);
			while (scanner.hasNextLine()) {
				File file = new File(scanner.nextLine());
				historyFiles.add(file);
				openRecentMenuItems.add(new RecentMenuItem(file));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		clearPepeat();
		
		// Järjestää tiedoston tietoja
		try {
			Writer output = new BufferedWriter(new FileWriter(fileHistoryRecordFile));
			for (File file : historyFiles) {
				output.append(file.getPath() + "\n");
			}
			output.close();
		} catch (IOException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
	}
	
	public void removeAll() {
		historyFiles.removeAllElements();
		openRecentMenuItems.removeAllElements();
		openRecentMenu.removeAll();
		openRecentMenu.setEnabled(false);
	}
	
	private void remove(int i) {
		historyFiles.remove(i);
		openRecentMenu.remove(openRecentMenuItems.remove(i));
	}
	
	private void removeMissings() {
		for (int i = 0; i < historyFiles.size(); i++) {
			if (!historyFiles.get(i).exists()) {
				remove(i);
			}
		}
	}	

	public void add(File file) {
		historyFiles.add(file);
		RecentMenuItem recentMenuItem = new RecentMenuItem(file);
		openRecentMenuItems.add(recentMenuItem);
		openRecentMenu.add(recentMenuItem, RECENT_MENU_ITEM_INDEX);
		clearPepeat(historyFiles.indexOf(file));
		
		try {
			Writer output = new BufferedWriter(new FileWriter(fileHistoryRecordFile, true));
			output.append("\n" + file.getPath());
			output.close();
		} catch (IOException e) {
		}
	}
	
	/**
	 * @return the openRecentMenu
	 */
	public JMenu getMenu() {
		return openRecentMenu;
	}

	/**
	 * @return the manager
	 */
	public static FileHistoryManager get() {
		return MANAGER;
	}
}
