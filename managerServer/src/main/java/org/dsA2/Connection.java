package org.dsA2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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

    LinkedBlockingQueue<JSONObject> requests = new LinkedBlockingQueue<>();
    Socket socket;

    List<String[]> initShapes;

    public Connection(Socket user, List<String[]> initShapes) {
        this.socket = user;
        this.initShapes = initShapes;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            JSONObject json = new JSONObject();
            json.put("init", initShapes);
            String initBoard = JSON.toJSONString(json);
            writer.write(initBoard);
            writer.newLine();
            writer.flush();
            while(!isInterrupted()){
                //System.out.println(requests);
                if (!requests.isEmpty()) {
                    String jsonString = JSON.toJSONString(requests.poll());
                    System.out.println(jsonString);
                    writer.write(jsonString);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println(1);

        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }

}
