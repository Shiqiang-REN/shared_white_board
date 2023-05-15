package org.dsA2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
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
    String connectionType;

    public Connection(Socket user, String username, int userId, String connectionType) {
        this.socket = user;
        this.username = username;
        this.userId = userId;
        this.connectionType = connectionType;
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
            System.out.println(initBoard);
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

            if(connectionType.equals("request")){

                while(true){
//                    Thread.sleep(1000);
//                    System.out.println(requests);
                    if (!requests.isEmpty()) {
                        String jsonString = JSON.toJSONString(requests.poll());
                        System.out.println(jsonString);
                        writer.write(jsonString);
                        writer.newLine();
                        writer.flush();
                    }
                }
            }else if(connectionType.equals("respond")){
                while(true){
                    try {
                        String jsonString = reader.readLine();
                        JSONObject receivingInfo = JSON.parseObject(jsonString);
                        if(receivingInfo != null){
                            System.out.println(receivingInfo);
                            String respondType = (String) receivingInfo.get("requestType");
                            if(respondType != null){
                                if (respondType.equals("shapes")) {
                                    System.out.println(receivingInfo);
                                    setJoinRequest(receivingInfo);
                                }else if (respondType.equals("init")){

                                }
                            }

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("finally");
        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }

}
