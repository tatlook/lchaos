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
	private static final int RECENT_MENU_ITEM_INDEX = 0;
	
	private FileHistoryManager() {
		JMenuItem clearAllFilesMenuItem = new JMenuItem("Clear All Files");
		JMenuItem clearMissingFilesMenuItem = new JMenuItem("Clear Missing Files");
		clearAllFilesMenuItem.setMnemonic('C');
		clearMissingFilesMenuItem.setMnemonic('M');
		
		openRecentMenu.addSeparator();
		openRecentMenu.add(clearAllFilesMenuItem);
		openRecentMenu.add(clearMissingFilesMenuItem);
		
		clearAllFilesMenuItem.addActionListener((e) -> removeAll());
		clearMissingFilesMenuItem.addActionListener((e) -> removeMissings());
		
		findRecentFiles();
	}

	@SuppressWarnings("serial")
	class RecentMenuItem extends JMenuItem {
		File file;
		public RecentMenuItem(File file) {
			super(file.getPath());
			this.file = file;
			addActionListener((e) -> {
				ChaosFileSaver.checkFileSave();
				try {
					ChaosFileChooser.staticOpen(file);					
				} catch (FileNotFoundException e1) {
					ErrorMessageDialog.createExceptionDialog(e1);
				}
			});
		}
	}
	
	/**
	 * Poistaa samat asiat kuin historyFiles[i] että vain yksi jäljellä
	 * 
	 * @param i Käytetään löytää historyFiles[i]
	 */
	private void clearPepeat(int i) {
		for (int j = i + 1; j < historyFiles.size(); j++) {
			if (historyFiles.get(i).equals(historyFiles.get(j))) {
				remove(i);
			}
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
			// Lue tiedosto rivi riviltä
			while (scanner.hasNextLine()) {
				String filePath = scanner.nextLine();
				// Ei huolta tyhjää riviä
				if (filePath.equals("")) {
					continue;
				}
				add0(new File(filePath));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		checkMenuEnabled();
		flushFileHistoryRecordFile();
	}
	
	public void removeAll() {
		historyFiles.removeAllElements();
		openRecentMenuItems.removeAllElements();
		openRecentMenu.removeAll();
		checkMenuEnabled();
		flushFileHistoryRecordFile();
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
		checkMenuEnabled();
		flushFileHistoryRecordFile();
	}	

	/**
	 * Järjestää tiedoston tietoja
	 */
	private void flushFileHistoryRecordFile() {
		try {
			Writer out = new BufferedWriter(new FileWriter(fileHistoryRecordFile));
			for (File file : historyFiles) {
				out.append(file.getPath() + "\n");
			}
			out.close();
		} catch (IOException e) {
		}
	}
	
	private void add0(File file) {
		historyFiles.add(file);
		RecentMenuItem recentMenuItem = new RecentMenuItem(file);
		openRecentMenuItems.add(recentMenuItem);
		// Jos isEnabled() == false ja käytää add(), virhe on noin esiintyvän
		openRecentMenu.setEnabled(true);
		openRecentMenu.add(recentMenuItem, RECENT_MENU_ITEM_INDEX);
		clearPepeat(historyFiles.indexOf(file));
	}
	
	public void add(File file) {
		add0(file);
		flushFileHistoryRecordFile();
	}
	
	/**
	 * Sopivissa olosuhteissa poista openRecentMenu käytöstä:
	 * Jos ei ole historiaa, openRecentMenu ei enää tarvita.
	 */
	private void checkMenuEnabled() {
		openRecentMenu.setEnabled(!historyFiles.isEmpty());
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
