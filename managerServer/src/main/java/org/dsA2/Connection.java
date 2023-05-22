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

    private LinkedBlockingQueue<JSONObject> requests = new LinkedBlockingQueue<>();
    private Socket socket;
    private WhiteBoardUi wb;
    private String connectionType;

    private Boolean connectionStatus = true;

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
                        } else if (respondType.equals("newUser")) {
                            wb.updateUsersToAll();
                        } else if (respondType.equals("close")) {
                            wb.userOffline(socket);
                        } else {
                            System.out.println("Request type can not be identified!");
                        }
                    }
                }
            }

        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println(e.getMessage());
            }
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            //client force quit
            if(connectionStatus && connectionType.equals("respond")){
                wb.userForceQuit(socket);
            }else{
                wb.removeSocket(socket);
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
