package org.dsA2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
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
    String connectionType;

    Boolean connectionStatus = true;

    public Connection(Socket socket, WhiteBoardUi wb, String connectionType) {
        this.socket = socket;
        this.wb = wb;
        this.connectionType = connectionType;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            if(connectionType.equals("request")){
                while(connectionStatus){
                    if (!requests.isEmpty()) {
                        String jsonString = JSON.toJSONString(requests.poll());
                        writer.write(jsonString);
                        writer.newLine();
                        writer.flush();
                    }
                }
                socket.close();
            }else if(connectionType.equals("respond")){
                String jsonString;
                while(connectionStatus && (jsonString = reader.readLine()) != null){
                    JSONObject receivingInfo = JSON.parseObject(jsonString);
                    if(receivingInfo != null){
                        String respondType = (String) receivingInfo.get("requestType");
                        if (respondType.equals("join")) {
                            wb.requestJoin(receivingInfo, socket);
                        } else if (respondType.equals("chatting")) {
                            wb.addChattingInfo(receivingInfo);
                        } else if (respondType.equals("shapes")) {
                            wb.updateShapesToAll2(receivingInfo);
                        } else if (respondType.equals("close")) {
                            return;
                        } else {
                            System.out.println("Request type can not be identified!");
                        }
                    }
                }
                socket.close();
            }
        } catch (IOException e) {
            if(connectionStatus){
                wb.userOffline(socket);
            }
        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }


    public void close (){
        connectionStatus = false;
    }

}
