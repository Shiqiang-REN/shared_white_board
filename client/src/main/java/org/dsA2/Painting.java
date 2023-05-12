package org.dsA2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * ClassName: MyWhiteBoard
 * Package: org.dsA2
 * Description:
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 21:13
 * @Version 1.0
 */
public class Painting extends JPanel {

    //private List<Shape> shapes = new ArrayList<>();
    private Color backgroundColor;
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        System.out.println("shape");
        g.setColor(Color.BLUE);
        g.drawLine(10,15,20,30);
        //g.fillOval(10, 10, 100, 100);
    }

    public void setPainting(int i) {
        System.out.println(i);
        repaint();
    }
}
