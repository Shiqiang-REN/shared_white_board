package org.dsA2;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ClassName: Messaging
 * Package: org.dsA2
 * Description: handling chatting and userLists
 *
 * @Author Shiqiang Ren
 * @Create 15/5/2023 11:00
 * @Version 1.0
 */
public class Messaging extends Thread{
    //userlist
    private final List<Connection> users = new CopyOnWriteArrayList<>();

    private final LinkedBlockingQueue<JSONObject> requests = new LinkedBlockingQueue<>();

    //chat history

    @Override
    public void run(){

        while(!isInterrupted()){
            //System.out.println(requests);
            if (!requests.isEmpty()) {
                broadcastMessage(requests.poll());
            }
        }

    }

    public  void broadcastMessage (JSONObject json){
        System.out.println(json);
        for(Connection user : users) {
            user.addRequest(json);
        }
    }

    public LinkedBlockingQueue<JSONObject> getRequests() {
        return requests;
    }

    public List<Connection> getUsers() {
        return users;
    }
}
