package org.dsA2;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.dsA2.JoinBoardUi.setJoinRequest;

/**
 * ClassName: ServerConnection
 * Package: org.dsA2
 * Description: handling Server network connection
 *
 * @Author Shiqiang Ren
 * @Create 15/5/2023 11:00
 * @Version 1.0
 */
public class Connection extends Thread{

    private String username;
    private int userId;
    LinkedBlockingQueue<JSONObject> requests = new LinkedBlockingQueue<>();
    final Socket socket;


    public Connection(Socket user, String username, int userId) {
        this.socket = user;
        this.username = username;
        this.userId = userId;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // send join request data
            JSONObject json = new JSONObject();
            json.put("requestType", "join");
            json.put("requestJoinName", username);
            json.put("requestJoinId", userId);
            String initBoard = JSON.toJSONString(json);
            writer.write(initBoard);
            writer.newLine();
            writer.flush();
            //join request reply
            String respondString = reader.readLine();
            JSONObject respond = JSON.parseObject(respondString);
            synchronized (socket){
                setJoinRequest(respond);
                socket.notify();
            }
            while(true){
                String respondString1 = reader.readLine();
                System.out.println(1111111);
                System.out.println(respondString1);
                if (!requests.isEmpty()) {
                    String jsonString = JSON.toJSONString(requests.poll());
                    System.out.println(jsonString);
                    writer.write(jsonString);
                    writer.newLine();
                    writer.flush();
                }
//                //recieve data

//                JSONObject respond2 = JSON.parseObject(respondString1);
//                setJoinRequest(respond2);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println(1234);

        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }

}
