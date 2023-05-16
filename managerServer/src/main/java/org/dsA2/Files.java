package org.dsA2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * ClassName: Files
 * Package: org.dsA2
 * Description:
 *
 * @Author Shiqiang Ren
 * @Create 16/5/2023 09:45
 * @Version 1.0
 */
public class Files {

    //manager features
//    public void saveFile (String type){
//        fileChooser.setDialogTitle("Save File!");
//        fileChooser.setAcceptAllFileFilterUsed(false);
//
//        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
//        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("Png Files (*.csv)", "png");
//
//        fileChooser.addChoosableFileFilter(txtFilter);
//        fileChooser.addChoosableFileFilter(pngFilter);
//
//        fileChooser.setFileFilter(txtFilter);
//
//        fileChooser.setCurrentDirectory(new File("."));
//        int fileSelection = fileChooser.showSaveDialog(panelMain);
//
//
//        if (fileSelection == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            String fileType = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
//            try {
//                if(type.equals("text")){
//
//                    String jsonStr = JSON.toJSONString(shapes);
//                    String fileName = selectedFile.getAbsolutePath();
//                    fileName = fileName +"."+ fileType;
//
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//                    writer.write(jsonStr);
//                    writer.close();
//                    System.out.println("File saved successfully.");
//                } else if (type.equals("pic")) {
//                    BufferedImage image = new BufferedImage(whiteBoardPanel.getWidth(), whiteBoardPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
//                    Graphics2D g2d = image.createGraphics();
//                    whiteBoardPanel.paint(g2d);
//                    g2d.dispose();
//                    String fileName = selectedFile.getAbsolutePath();
//                    fileName = fileName +"."+ fileType;
//                    try {
//                        ImageIO.write(image, fileType, new File(fileName));
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    public void openFile(){
//        fileChooser.setDialogTitle("Open the file!");
//        fileChooser.setAcceptAllFileFilterUsed(true);
//        fileChooser.setCurrentDirectory(new File("."));
//        int fileSelection = fileChooser.showOpenDialog(panelMain);
//        if (fileSelection == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//
//            StringBuilder jsonStr = new StringBuilder();
//            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    jsonStr.append(line);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            String s = jsonStr.toString();
//            shapes = JSON.parseObject(s, new TypeReference<List<String[]>>(){});
//            ((Painting)whiteBoardPanel).setShapes(shapes);
//            updateShapesToAll();
//        }
//    }
}
