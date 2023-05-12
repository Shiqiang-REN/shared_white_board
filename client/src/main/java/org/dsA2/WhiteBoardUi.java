package org.dsA2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private Color selectedColor;

    public WhiteBoardUi(String username, String role) {
        initBoard();
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

            }
        });
        whiteBoardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });

        ColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = JColorChooser.showDialog(panelMain, "Choose Color", Color.black);
                System.out.println(selectedColor);
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
        whiteBoardPanel = new MyWhiteBoard();
    }
}
