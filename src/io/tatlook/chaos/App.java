package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class App {
    public static void main(String[] args) {
    	MainWindow mainWindow = new MainWindow();
    	
    	
    	// 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));

        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(true);

        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plain text(*.txt)", "txt"));
        // 设置默认使用的文件过滤器
        fileChooser.setFileFilter(new FileNameExtensionFilter("Chaos file(*.ch)", "ch"));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(mainWindow);

        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            file = fileChooser.getSelectedFile();

            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();

            System.out.println("打开文件: " + file.getAbsolutePath() + "\n\n");
        } else if (result == JFileChooser.CANCEL_OPTION) {
			file = new File("D:/Documents/p.txt");
		}
    	try {
			new ChaosFileParser(file).readChaos();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	mainWindow.UI();
    	mainWindow.setVisible(true);
    } 
} 