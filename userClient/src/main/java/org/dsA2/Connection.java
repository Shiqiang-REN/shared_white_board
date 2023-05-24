package org.dsA2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import static org.dsA2.JoinBoardUi.setInitJoin;

/**
 * ClassName: ServerConnection
 * Package: org.dsA2
 * Description: handling client network connection
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
    private String connectionType;
    private WhiteBoardUi wb;

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
            if(connectionType.equals("request")){
                while(true){
                    if (!requests.isEmpty()) {
                        String jsonString = JSON.toJSONString(requests.poll());
                        writer.write(jsonString);
                        writer.newLine();
                        writer.flush();
                    }
                }
            }else if(connectionType.equals("respond")) {
                String jsonString;
                while ((jsonString = reader.readLine()) != null) {
                    JSONObject receivingInfo = JSON.parseObject(jsonString);
                    if (receivingInfo != null) {
                        String respondType = (String) receivingInfo.get("requestType");
                        if (respondType != null) {
                            if (respondType.equals("join")) {
                                setInitJoin(receivingInfo);
                            } else if (respondType.equals("shapes")) {
                                //update board
                                wb.setUpdatedShapes(receivingInfo);
                            } else if (respondType.equals("userList")) {
                                wb.setUpdatedUsers(receivingInfo);
                            }else if (respondType.equals("chatting")) {
                                wb.setUpdatedChatting(receivingInfo);
                            } else if (respondType.equals("serverClosed")) {
                                wb.serverClosed();
                            } else if (respondType.equals("kickOut")) {
                                String kickOutUserId = (String) receivingInfo.get("data");
                                if(kickOutUserId.equals( String.valueOf(userId))){
                                    wb.kickOutPanel();
                                }
                            } else if (respondType.equals("refused")) {
                                String kickOutUserId = (String) receivingInfo.get("data");
                                if(kickOutUserId.equals( String.valueOf(userId))){
                                    setInitJoin(new JSONObject());
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(wb!=null){
                    wb.serverClosed();
                }
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }


    public void setWhiteBoardUi(WhiteBoardUi wb) {
        this.wb = wb;
    }
}
