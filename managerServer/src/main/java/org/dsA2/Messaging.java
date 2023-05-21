package org.dsA2;

import com.alibaba.fastjson2.JSONObject;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
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
     Map<Connection,Socket> userConnections = new ConcurrentHashMap<>();

     Map<String,Socket> clients = new ConcurrentHashMap<>();

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
        for(Connection user : users) {
            user.addRequest(json);
        }
    }
    public List<Connection> getUsers() {
        return users;
    }

    public Map<Connection, Socket> getUserConnections() {
        return userConnections;
    }

    public Map<String, Socket> getClients() {
        return clients;
    }

    //according to socket, close connection
    public void closeConnection (Socket socket){
        for (Connection key : userConnections.keySet()) {
            Socket value = userConnections.get(key);
            if(value == socket){
                key.close();
                removeSocket(socket);
            }
        }
    }

    //according to userId, find the socket and then close
    public void closeConnectionByID (String id){
        Socket socket = null;
        for (String key : clients.keySet()) {
            if(Objects.equals(key, id)){
                socket = clients.get(key);
                closeConnection(socket);
            }
        }
    }

    public String getUserIDBySocket (Socket socket){
        String userId = null;
        for (String key : clients.keySet()) {
            Socket value = clients.get(key);
            if(value == socket){
                userId = key;
            }
        }
        return userId;
    }


    public String removeSocket(Socket socket){
        String userId = null;
        for (String key : clients.keySet()) {
            Socket value = clients.get(key);
            if(value == socket){
                clients.remove(key);
                userId = key;
            }
        }
        for (Connection key : userConnections.keySet()) {
            Socket value = userConnections.get(key);
            if(value == socket){
                users.removeIf(element -> element == key);
                userConnections.remove(key);
            }
        }
        return userId;
    }
}
