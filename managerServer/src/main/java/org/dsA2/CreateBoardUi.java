package org.dsA2;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

/**
 * ClassName: CreateBoardUi
 * Package: org.dsA2
 * Description: manager Create Board Ui
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 12:32
 * @Version 1.0
 */

public class CreateBoardUi {

    public static void main(String[] args) {
        //login window
        int userId = generateId();
        Object[] inputs = {"Please enter your name：", new JTextField(),"Your User Id is :",new JLabel(String.valueOf(userId))};
        int option = JOptionPane.showConfirmDialog(null, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);

        String username = ((JTextField) inputs[1]).getText();
        while (username.isEmpty() && option == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(null,"Can not be empty!");
            option = JOptionPane.showConfirmDialog(null, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);
            username = ((JTextField) inputs[1]).getText();
        }


        //create a thread for chatting and user list
        Messaging message = new Messaging();
        message.start();

        //Create WhiteBoardUi
        WhiteBoardUi wb = new WhiteBoardUi(username, userId, message);

        // create connection threads(request listener thread and respond listener)
        checkAndCreateConnection(args[0], message, wb);

    }

    public static void checkAndCreateConnection(String port, Messaging message, WhiteBoardUi wb){
        //Create new thread for network
        int portNumber = Integer.parseInt(port);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            while (true) {
                Socket user = serverSocket.accept();
                //List<String[]> initShapes = ((Painting)wb.getWhiteBoardPanel()).getShapes();
                // create a thread handle the user socket
                Connection sc1 = new Connection(user,  wb, "request");
                Connection sc2 = new Connection(user,  wb, "respond");
                message.getUsers().add(sc1);
                sc1.start();
                sc2.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static int generateId() {
        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.hashCode();
        return Math.abs(hashCode % 10000);
    }

}

