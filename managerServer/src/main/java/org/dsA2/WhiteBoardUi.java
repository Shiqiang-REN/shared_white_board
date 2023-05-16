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
import java.net.Socket;
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
    private final Messaging message;
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
    private JComboBox filesComboBox;
    private JTextArea chatTextArea;
    private JScrollPane userPane;
    private JButton penButton;
    private JButton removeUser;
    private JFrame frame;
    private String selectedColor;
    private String toolType = "pen";
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private JFileChooser fileChooser = new JFileChooser();
    private List<String[]> shapes = new CopyOnWriteArrayList<>();
    private List<String[]> chatting = new CopyOnWriteArrayList<>();
    private String username;
    private int userId;

    private Users users;



    public WhiteBoardUi(String username, int userId,Messaging message) {
        this.username = username;
        this.userId = userId;
        this.message = message;
        initBoard();
        users = new Users(usersList);
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

        //manager features
        filesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) filesComboBox.getSelectedItem();
                switch (selectedOption) {
                    case "New" ->{
                        shapes = new ArrayList<>();
                        ((Painting)whiteBoardPanel).setShapes(shapes);
                        updateShapesToAll();
                    }
                    case "Open" -> openFile();
                    case "Save" -> saveFile("text");
                    case "Save As" -> saveFile("pic");
                    case "Close" -> {
                        if (JOptionPane.showConfirmDialog(frame,
                                "Are you sure you want to close?", "Close Window?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                }
                filesComboBox.setSelectedItem("FILE");
            }
        });
        removeUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String removedUser = users.removeSelectedUser();
                updateUsersToAll();
                message.closeConnectionByID(removedUser);
            }
        });
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String info = inputMessageTextField.getText();
                chatTextArea.append("\n"+info+"\n");
                JScrollBar verticalScrollBar = chatPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                updateChattingToAll(info+"\n");
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
        model.addElement("Manager - "+username+" (id:"+userId+")");
        model.addElement("Manager");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        whiteBoardPanel = new Painting();
    }

    //manager features
    public void saveFile (String type){
        fileChooser.setDialogTitle("Save File!");
        fileChooser.setAcceptAllFileFilterUsed(false);

        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("Png Files (*.csv)", "png");

        fileChooser.addChoosableFileFilter(txtFilter);
        fileChooser.addChoosableFileFilter(pngFilter);

        fileChooser.setFileFilter(txtFilter);

        fileChooser.setCurrentDirectory(new File("."));
        int fileSelection = fileChooser.showSaveDialog(panelMain);


        if (fileSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileType = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            try {
                if(type.equals("text")){

                    String jsonStr = JSON.toJSONString(shapes);
                    String fileName = selectedFile.getAbsolutePath();
                    fileName = fileName +"."+ fileType;

                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    writer.write(jsonStr);
                    writer.close();
                    System.out.println("File saved successfully.");
                } else if (type.equals("pic")) {
                    BufferedImage image = new BufferedImage(whiteBoardPanel.getWidth(), whiteBoardPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();
                    whiteBoardPanel.paint(g2d);
                    g2d.dispose();
                    String fileName = selectedFile.getAbsolutePath();
                    fileName = fileName +"."+ fileType;
                    try {
                        ImageIO.write(image, fileType, new File(fileName));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openFile(){
        fileChooser.setDialogTitle("Open the file!");
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setCurrentDirectory(new File("."));
        int fileSelection = fileChooser.showOpenDialog(panelMain);
        if (fileSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            StringBuilder jsonStr = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonStr.append(line);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String s = jsonStr.toString();
            shapes = JSON.parseObject(s, new TypeReference<List<String[]>>(){});
            ((Painting)whiteBoardPanel).setShapes(shapes);
            updateShapesToAll();
        }
    }

    public void updateShapesToAll (){
        JSONObject json = new JSONObject();
        json.put("requestType", "shapes");
        json.put("data", shapes);
        message.broadcastMessage(json);
    }

    public void updateShapesToAll2 (JSONObject respond){
        shapes = JSON.parseObject(respond.get("data").toString(), new TypeReference<List<String[]>>(){});
        ((Painting)whiteBoardPanel).setShapes(shapes);
        message.broadcastMessage(respond);
    }

    public void updateUsersToAll (){
        JSONObject json = new JSONObject();
        String[] s = users.getUserArrayList();
        json.put("requestType", "userList");
        json.put("data", s);
        message.broadcastMessage(json);
    }


    public void updateChattingToAll (String info){
        JSONObject json = new JSONObject();
        json.put("requestType", "chatting");
        json.put("data", info);
        message.broadcastMessage(json);
    }

    public void addChattingInfo (JSONObject respond){
        String s  = respond.get("data").toString();
        chatTextArea.append(s+"\n");
        JScrollBar verticalScrollBar = chatPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        message.broadcastMessage(respond);
    }

    public void requestJoin(JSONObject request, Socket socket) {
        JSONObject jsonUserList = new JSONObject();
        JSONObject jsonJoin = new JSONObject();
        String username = (String) request.get("requestJoinName");
        int id = (int) request.get("requestJoinId");
        String userId = String.valueOf(id);
        if (JOptionPane.showConfirmDialog(frame,
                username+"(ID: "+userId+")"+"  want to join?", "Join Request?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            jsonJoin.put("requestType", "join");
            jsonJoin.put("status", "Ok");
            jsonJoin.put("shapes", shapes);
            users.addUser(username, userId);
            String[] usersArray = users.getUserArrayList();
            jsonUserList.put("requestType", "userList");
            jsonUserList.put("data", usersArray);
            jsonJoin.put("userList", usersArray);
            message.broadcastMessage(jsonJoin);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            message.broadcastMessage(jsonUserList);
            message.getClients().put(userId, socket);
        }else{
            System.out.println(socket);
            message.closeConnection(socket);
        }
    }

    public void userOffline( Socket socket) {
        String userID = message.removeSocket(socket);
        String user = users.getUserByIDAndRemove(userID);
        message.closeConnection(socket);
        if (JOptionPane.showConfirmDialog(frame,
                user+" has leave the chat", "Close Window?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        }
    }
}
