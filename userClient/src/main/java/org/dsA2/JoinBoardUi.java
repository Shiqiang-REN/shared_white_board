package org.dsA2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson2.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ClassName: JoinBoardUi
 * Package: org.dsA2
 * Description: user join Board Ui
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 12:32
 * @Version 1.0
 */

public class JoinBoardUi {

    private static Boolean joinRequest = false;
    private static List<String[]> shapes = new ArrayList<>();

    public static void main(String[] args) {
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
//        Messaging message = new Messaging();
//        message.start();

        Socket user = new Socket();

        checkAndCreateConnection(args[0],args[1], username, userId,  user);

        synchronized (user){
            try {
                user.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //Create WhiteBoardUi
            if(joinRequest){
                WhiteBoardUi wb = new WhiteBoardUi(username, userId);
                wb.setShapes(shapes);
            }
        }
    }

    public static void checkAndCreateConnection(String hostIp, String port, String username, int userId, Socket user){
        //Create new thread for network
        int portNumber = Integer.parseInt(port);
        int timeout = 2000;
        try {

            InetAddress ipAddress = InetAddress.getByName(hostIp);
            SocketAddress address = new InetSocketAddress(ipAddress, portNumber);
            //Socket user = new Socket();
            user.connect(address, timeout);


            //List<String[]> initShapes = ((Painting)wb.getWhiteBoardPanel()).getShapes();
            // create a thread handle the user socket
            Connection sc = new Connection(user,username, userId );
            //message.getUsers().add(sc);
            sc.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static int generateId() {
        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.hashCode();
        return Math.abs(hashCode % 10000);
    }

    public static void setJoinRequest(JSONObject respond){
        String answer = (String) respond.get("join");
        if(answer.equals("Ok")){
            joinRequest = true;
            shapes = JSON.parseObject(respond.get("shapes").toString(), new TypeReference<List<String[]>>(){});
        }else if(respond.get("shapes") != null){
            shapes = JSON.parseObject(respond.get("shapes").toString(), new TypeReference<List<String[]>>(){});
        }
    }
}

