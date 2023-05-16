package org.dsA2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
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
    WhiteBoardUi wb;
    //List<String[]> initShapes;
    String connectionType;

    public Connection(Socket user, WhiteBoardUi wb, String connectionType) {
        this.socket = user;
        this.wb = wb;
        //this.initShapes = initShapes;
        this.connectionType = connectionType;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            if(connectionType.equals("request")){
                while(true){
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
                            String respondType = (String) receivingInfo.get("requestType");
                            if (respondType.equals("join")) {
                                wb.requestJoin(receivingInfo);
                            } else if (respondType.equals("chatting")) {
                                wb.addChattingInfo(receivingInfo);
                            } else if (respondType.equals("shapes")) {
                                wb.updateShapesToAll2(receivingInfo);
                            } else if (respondType.equals("CLOSE")) {
                                System.out.println("User disconnected from IP ---" + socket.getInetAddress().getHostAddress());
                                return;
                            } else {
                                System.out.println("Request type can not be identified!");
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
            System.out.println(1);
        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }

}
