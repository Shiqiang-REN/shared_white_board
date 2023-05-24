package org.dsA2;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
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
        int port = 0;
        try{ 
            if (args.length>1){
                JOptionPane.showMessageDialog(null,"\"The command parameters is incorrect!\"Please check the command again please!" );
                System.exit(0);
            }
            port = Integer.parseInt(args[0]);
        }catch(ArrayIndexOutOfBoundsException | NumberFormatException e  ){
            JOptionPane.showMessageDialog(null,"\"The command of parameters is incorrect!\"Please check the command again please!" );
            System.exit(0);
        }
        //login window
        int userId = generateId();
        Object[] inputs = {"Please enter your name：", new JTextField(),"Your User Id is :",new JLabel(String.valueOf(userId))};
        int option = JOptionPane.showConfirmDialog(null, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);

        String username = ((JTextField) inputs[1]).getText();
        if(option == JOptionPane.CANCEL_OPTION){
            System.exit(0);
        }
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
        checkAndCreateConnection(port, message, wb);

    }

    public static void checkAndCreateConnection(int port, Messaging message, WhiteBoardUi wb){
        //Create new thread for network
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket user = serverSocket.accept();
                // create a thread handle the user socket
                Connection sc1 = new Connection(user,  wb, "request");
                Connection sc2 = new Connection(user,  wb, "respond");
                message.getUsers().add(sc1);
                //UserConnections();
                message.getUserConnections().put(sc1,user);
                message.getUserConnections().put(sc2,user);
                sc1.start();
                sc2.start();
            }
        } catch (BindException err) {
            JOptionPane.showMessageDialog(null,"Port number already be used!" );
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static int generateId() {
        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.hashCode();
        return Math.abs(hashCode % 10000);
    }

}

