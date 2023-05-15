package org.dsA2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: Painting
 * Package: org.dsA2
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 21:13
 * @Version 1.0
 */
public class Painting extends JPanel {

    List<String[]> shapes = new ArrayList<>();
    private Graphics graphics;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics = g;

        for (String[] shape : shapes) {
            switch (shape[0]) {
                case "line" -> drawLine(shape[1], shape[2], shape[3], shape[4], shape[5]);
                case "circle" -> drawCircle(shape[1], shape[2], shape[3], shape[4], shape[5]);
                case "oval" -> drawOval(shape[1], shape[2], shape[3], shape[4], shape[5]);
                case "rectangle" -> drawRectangle(shape[1], shape[2], shape[3], shape[4], shape[5]);
                case "text" -> drawString(shape[1], shape[2], shape[3], shape[4]);
                case "pen" -> drawLine(shape[1], shape[2], shape[3], shape[4], shape[5]);
            }

            //System.out.println(hobbyArray[0]);
            //String jsonStr = JSON.toJSONString(hobbies);
            //List<String[]> hobbies = JSON.parseObject(jsonStr, new TypeReference<List<String[]>>(){});
        }
    }

    public void addPaintingShape(String[] s) {
        shapes.add(s);
        //System.out.println(Arrays.toString(s));
        repaint();
    }


    public void drawLine(String color, String startX, String startY, String endX, String endY){
        Color newColor = Color.decode(color);
        graphics.setColor(newColor);
        graphics.drawLine(Integer.parseInt(startX),
                Integer.parseInt(startY),
                Integer.parseInt(endX),
                Integer.parseInt(endY)
        );
    }

    public void drawCircle(String color, String startX, String startY, String endX, String endY){
        Color newColor = Color.decode(color);
        graphics.setColor(newColor);
        graphics.drawOval(Integer.parseInt(startX),
                Integer.parseInt(startY),
                Integer.parseInt(endX),
                Integer.parseInt(endY)
        );
    }

    public void drawOval(String color, String startX, String startY, String endX, String endY){
        Color newColor = Color.decode(color);
        graphics.setColor(newColor);
        graphics.drawOval(Integer.parseInt(startX),
                Integer.parseInt(startY),
                Integer.parseInt(endX),
                Integer.parseInt(endY)
        );
    }

    public void drawRectangle(String color, String startX, String startY, String endX, String endY){
        Color newColor = Color.decode(color);
        graphics.setColor(newColor);
        graphics.drawRect(Integer.parseInt(startX),
                Integer.parseInt(startY),
                Integer.parseInt(endX),
                Integer.parseInt(endY)
        );
    }
    public void drawString(String color, String startX, String startY, String text){
        Color newColor = Color.decode(color);
        Font font = new Font("Arial", Font.PLAIN, 20);
        graphics.setFont(font);
        graphics.setColor(newColor);
        graphics.drawString(text,
                Integer.parseInt(startX),
                Integer.parseInt(startY));
    }

    public void setShapes(List<String[]> shapes) {
        this.shapes = shapes;
        repaint();
    }
}
