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
    WhiteBoardUi wb;

    List<String[]> initShapes;

    public Connection(Socket user, List<String[]> initShapes, WhiteBoardUi wb) {
        this.socket = user;
        this.wb = wb;
        this.initShapes = initShapes;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            JSONObject json = new JSONObject();
//            json.put("init", initShapes);
//            String initBoard = JSON.toJSONString(json);
//            writer.write(initBoard);
//            writer.newLine();
//            writer.flush();
            while(true){
                Thread.sleep(1000);
                System.out.println(requests);
                if (!requests.isEmpty()) {
                    System.out.println("1233333333");

                    String jsonString = JSON.toJSONString(requests.poll());
                    System.out.println(jsonString);
                    writer.write(jsonString);
                    writer.newLine();
                    writer.flush();
                }

                if(reader.readLine() != null){
                    String jsonString = reader.readLine();
                    JSONObject request = JSON.parseObject(jsonString);
                    if(request != null){
                        JSONObject respond = null;
                        String requestType = (String) request.get("requestType");
                        if (requestType.equals("join")) {
                            respond = wb.requestJoin(request);
                        } else if (requestType.equals("chat")) {
                            respond = wb.requestJoin(request);
                        } else if (requestType.equals("shapes")) {
                            respond = wb.requestJoin(request);
                        } else if (requestType.equals("CLOSE")) {
                            System.out.println("User disconnected from IP ---" + socket.getInetAddress().getHostAddress());
                            return;
                        } else {
                            System.out.println("Request type can not be identified!");
                        }
                        String message = JSON.toJSONString(respond);
                        System.out.println(message);
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                    }
                }


            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(1);
        }
    }

    public void addRequest(JSONObject json){
        requests.add(json);
    }

}
