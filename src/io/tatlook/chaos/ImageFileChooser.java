package io.tatlook.chaos;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageFileChooser {
	private String imageType;
	private File imageFile;
	public void chose() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG(*.jpg, *.jpeg, *.jpe, *.jfif)", "jpg", "jpeg", "jpe", "jfif"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF(*.gif)", "gif"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG(*.png)", "png"));

        int result = fileChooser.showSaveDialog(App.mainWindow);

        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            if (!file.exists()) {
            	try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
            imageType = switch (getFileExtension(file).toLowerCase()) {
				case "jpg", "jpeg", "jpe", "jfif" -> "jpg";
				case "png" -> "png";
				case "gif" -> "gif";
				default -> "png";
			};
            System.out.println("Save: " + file.getAbsolutePath() + "\n\n");
        }
        imageFile = file;
	}
	
	private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        	return fileName.substring(fileName.lastIndexOf(".")+1);
        else
        	return "";
    }
	
	/**
	 * @return the imageFile
	 */
	public File getImageFile() {
		return imageFile;
	}

	public String getImageType() {
		return imageType;
	}
}
