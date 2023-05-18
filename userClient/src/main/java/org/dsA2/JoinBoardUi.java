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
    static String[] userArrayList;
    static WhiteBoardUi  wb;
    static Connection sc1;
    static Connection sc2;

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

        // create connection threads(request listener thread and respond listener)
        checkAndCreateConnection(args[0],args[1], username, userId);

        // waiting manager approved
        while(true){
            try {
                Thread.sleep(1000);
                if(joinRequest){
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //create white board UI
        wb = new WhiteBoardUi(username, userId, sc1);
        //sc1.setWhiteBoardUi(wb);
        sc2.setWhiteBoardUi(wb);
        wb.setInitShapes(shapes);
        wb.users.setUserList(userArrayList);
        JSONObject json = new JSONObject();
        json.put("requestType", "newUser");
        sc1.addRequest(json);
    }

    public static void checkAndCreateConnection(String hostIp, String port, String username, int userId){

        int portNumber = Integer.parseInt(port);
        int timeout = 2000;
        try {

            InetAddress ipAddress = InetAddress.getByName(hostIp);
            SocketAddress address = new InetSocketAddress(ipAddress, portNumber);
            Socket user = new Socket();
            user.connect(address, timeout);
            // create threads handle the user socket
            sc1 = new Connection(user, username, userId, "request");
            sc2 = new Connection(user, username, userId, "respond");
            sc1.start();
            sc2.start();
            //send join request
            JSONObject json = new JSONObject();
            json.put("requestType", "join");
            json.put("requestJoinName", username);
            json.put("requestJoinId", userId);
            sc1.addRequest(json);
        }catch (ConnectException e) {
            JOptionPane.showMessageDialog(null,"The whiteboard is not created!\nor\nCheck the port number your inputted!");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static int generateId() {
        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.hashCode();
        return Math.abs(hashCode % 10000);
    }

    public static void setInitJoin(JSONObject respond){
        if(respond.get("status") !=null){
            shapes = JSON.parseObject(respond.get("shapes").toString(), new TypeReference<List<String[]>>(){});
            userArrayList = JSON.parseObject(respond.get("userList").toString(), new TypeReference<String[]>(){});
            joinRequest = true;
        }
    }
}

