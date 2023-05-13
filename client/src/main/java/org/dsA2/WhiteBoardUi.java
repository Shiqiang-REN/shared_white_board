package org.dsA2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ClassName: WhiteBoardUi
 * Package: org.dsA2
 * Description:
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 12:33
 * @Version 1.0
 */
public class WhiteBoardUi {
    private JPanel panelMain;
    private JPanel toolsPanel;
    private JButton lineButton;
    private JButton circleButton;
    private JButton ovalButton;
    private JButton rectangleButton;
    private JButton textButton;
    private JPanel whiteBoardPanel;
    private JButton removeUser;
    private JList<String> usersList;
    private JPanel usersPanel;
    private JPanel chatPanel;
    private JPanel rightPanel;
    private JTextField inputMessageTextField;
    private JButton send;
    private JButton ColorButton;
    private JScrollPane chatPane;
    private JLabel userLabel;
    private JComboBox filesComboBox;
    private JTextArea textArea1;
    private JScrollPane userPane;
    private JFrame frame;
    private Color selectedColor = Color.BLACK;
    private String toolType = "draw";
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public WhiteBoardUi(String username, String role) {
        initBoard();
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                toolType = "line";
            }
        });

        ColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = JColorChooser.showDialog(panelMain, "Choose Color", Color.black);
                System.out.println(selectedColor);
            }
        });

        whiteBoardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX= e.getX();
                startY= e.getY();
                //System.out.println(e.getX()+"---"+e.getY());
                //System.out.println(selectedColor);
                //createUIComponents("Line");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                endX = e.getX();
                endY = e.getY();
                ((Painting)whiteBoardPanel).addPaintingShape(new String[]{
                        toolType,
                        String.valueOf(selectedColor),
                        String.valueOf(startX),
                        String.valueOf(startY),
                        String.valueOf(endX),
                        String.valueOf(endY)});
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }
        });
    }

    public void initBoard() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setContentPane(panelMain);
        frame.setVisible(true);
        frame.pack();
        frame.setBounds(500, 300, 800, 500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        removeUser.setVisible(false);
        filesComboBox.setVisible(false);


        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("元素1");
        model.addElement("元素2");
        model.addElement("元素3");

        DefaultListModel newModel = (DefaultListModel) usersList.getModel();
        newModel.addElement("new item1");
        newModel.addElement("new item2");
        newModel.addElement("new item3");
        newModel.addElement("new item4");
        usersList.setModel(newModel);
        //String selectedValue = (String) list1.getSelectedValue();

        DefaultListModel newModel2 = (DefaultListModel) usersList.getModel();
        int index = newModel2.indexOf("new item2");
        newModel2.remove(index);
        usersList.setModel(newModel2);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        whiteBoardPanel = new Painting();
    }


}
