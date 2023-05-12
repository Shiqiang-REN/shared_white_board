package org.dsA2;

import javax.swing.*;
import java.awt.*;

/**
 * ClassName: MyWhiteBoard
 * Package: org.dsA2
 * Description:
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 21:13
 * @Version 1.0
 */
public class MyWhiteBoard extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(10, 10, 100, 100);
    }
}
