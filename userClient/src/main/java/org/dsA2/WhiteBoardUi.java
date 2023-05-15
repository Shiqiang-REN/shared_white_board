package org.dsA2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson2.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * ClassName: WhiteBoardUi
 * Package: org.dsA2
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
    private JList<String> usersList;
    private JPanel usersPanel;
    private JPanel chatPanel;
    private JPanel rightPanel;
    private JTextField inputMessageTextField;
    private JButton send;
    private JButton ColorButton;
    private JScrollPane chatPane;
    private JLabel userLabel;
    private JTextArea chatTextArea;
    private JButton penButton;
    private JFrame frame;
    private String selectedColor;
    private String toolType = "pen";
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private List<String[]> shapes = new CopyOnWriteArrayList<>();

    private String username;
    private int userId;

    Connection sc1;


    public WhiteBoardUi(String username, int userId, Connection sc1) {
        this.username = username;
        this.userId = userId;
        this.sc1 = sc1;

        initBoard();
        penButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                toolType = "pen";
            }
        });
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                toolType = "line";
            }
        });
        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                toolType = "circle";
            }
        });
        ovalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                toolType = "oval";
            }
        });
        rectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                toolType = "rectangle";
            }
        });
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                toolType = "text";
            }
        });
        ColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(panelMain, "Choose Color", Color.black);
                selectedColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
            }
        });

        whiteBoardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX= e.getX();
                startY= e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                endX = e.getX();
                endY = e.getY();
                switch (toolType) {
                    case "line":
                        shapes.add(new String[]{
                                toolType,
                                selectedColor,
                                String.valueOf(startX),
                                String.valueOf(startY),
                                String.valueOf(endX),
                                String.valueOf(endY)});
                        ((Painting)whiteBoardPanel).setShapes(shapes);
                        updateShapesToAll();
                        break;
                    case "circle":
                        double diameter = Math.sqrt((endX - startX)*(endX - startX)+(endY - startY)*(endY - startY));
                        shapes.add(new String[]{
                                toolType,
                                selectedColor,
                                String.valueOf(startX-(int)diameter),
                                String.valueOf(startY-(int)diameter),
                                String.valueOf(2*(int)diameter),
                                String.valueOf(2*(int)diameter)});
                        ((Painting)whiteBoardPanel).setShapes(shapes);
                        updateShapesToAll();
                        break;
                    case "oval":
                        shapes.add(new String[]{
                                toolType,
                                selectedColor,
                                String.valueOf(startX),
                                String.valueOf(startY),
                                String.valueOf(Math.abs(endX-startX)),
                                String.valueOf(Math.abs(endY- startY))});
                        ((Painting)whiteBoardPanel).setShapes(shapes);
                        updateShapesToAll();
                        //Math.abs((startY- startX)/2)
                        break;
                    case "rectangle":
                        shapes.add(new String[]{
                                toolType,
                                selectedColor,
                                String.valueOf(startX),
                                String.valueOf(startY),
                                String.valueOf(Math.abs(endX-startX)),
                                String.valueOf(Math.abs(endY- startY))});
                        ((Painting)whiteBoardPanel).setShapes(shapes);
                        updateShapesToAll();
                        break;
                    case "text":
                        String text = JOptionPane.showInputDialog("Please input the text!");
                        shapes.add(new String[]{
                                toolType,
                                selectedColor,
                                String.valueOf(startX),
                                String.valueOf(startY), text});
                        ((Painting)whiteBoardPanel).setShapes(shapes);
                        updateShapesToAll();
                        break;
                    default:
                        break;
                }
            }
        });

        whiteBoardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(toolType .equals("pen")){
                    int prevX = startX;
                    int prevY = startY;
                    startX = e.getX();
                    startY = e.getY();
                    shapes.add(new String[]{
                            toolType,
                            selectedColor,
                            String.valueOf(prevX),
                            String.valueOf(prevY),
                            String.valueOf(startX),
                            String.valueOf(startY)});
                    ((Painting)whiteBoardPanel).setShapes(shapes);
                    updateShapesToAll();
                }

            }
        });
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String info = inputMessageTextField.getText();
                chatTextArea.append(info);
                JSONObject json = new JSONObject();
                json.put("chat", info);
                //message.getRequests().add(json);
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

        //manager features
        //removeUser.setVisible(false);
        //filesComboBox.setVisible(false);

        selectedColor = "#000000";
        chatTextArea.setEditable(false);

        //only can select single
        usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //tianjia1yige1
        DefaultListModel<String> model = (DefaultListModel<String>) usersList.getModel();
        model.addElement("User - "+username+" (id:"+userId+")");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        whiteBoardPanel = new Painting();
    }

    public JPanel getWhiteBoardPanel() {
        return whiteBoardPanel;
    }

    public JList<String> getUsersList() {
        return usersList;
    }

    public void updateShapesToAll (){
        JSONObject json = new JSONObject();
        json.put("data", shapes);
        json.put("requestType", "shapes");
        sc1.addRequest(json);
        //message.broadcastMessage(json);
    }

    public void setShapes(List<String[]> shapes) {
        this.shapes = shapes;
        ((Painting)whiteBoardPanel).setShapes(shapes);
    }
}
