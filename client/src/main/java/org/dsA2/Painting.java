package org.dsA2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    List<String[]> shapes = new ArrayList<>();
    private Color backgroundColor;
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (String[] shape : shapes) {
            g.drawLine(Integer.parseInt(shape[2]),
                    Integer.parseInt(shape[3]),
                    Integer.parseInt(shape[4]),
                    Integer.parseInt(shape[5])
            );
//            for (String hobby : hobbyArray) {
//                System.out.print(hobby + " ");
//            }
            //System.out.println(hobbyArray[0]);
            //String jsonStr = JSON.toJSONString(hobbies);
            //List<String[]> hobbies = JSON.parseObject(jsonStr, new TypeReference<List<String[]>>(){});
        }


        System.out.println("shape");
        g.setColor(Color.BLUE);
        g.drawLine(10,15,20,30);
        //g.fillOval(10, 10, 100, 100);
    }

    public void addPaintingShape(String[] s) {
        shapes.add(s);
        System.out.println(Arrays.toString(s));
        repaint();
    }
}
