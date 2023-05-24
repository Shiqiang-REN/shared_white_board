package org.dsA2;

import javax.swing.*;

/**
 * ClassName: Users
 * Package: org.dsA2
 *
 * @Author Shiqiang Ren
 * @Create 16/5/2023 09:46
 * @Version 1.0
 */
public class Users {

    private JList<String>  usersList;
    private DefaultListModel<String> model;

    public Users(JList<String>  usersList){
        this.usersList= usersList;
        model = (DefaultListModel<String>) usersList.getModel();
    }

    public String[] getUserArrayList (){
        String[] elements = new String[model.getSize()];
        for (int i = 0; i < model.getSize(); i++) {
            String element = model.getElementAt(i);
            elements[i] = element;
        }
        return elements;
    }


    public String getUserByIDAndRemove (String id){
        String element = null;
        for (int i = 0; i < model.getSize(); i++) {
            element = model.getElementAt(i);
            if (element.contains(id)) {
                model.remove(i);
                return element;
            }
        }
        return element;
    }

    public String removeSelectedUser (String managerId){
        int selectedIndex = usersList.getSelectedIndex();
        if (selectedIndex != -1) {
            String value = model.getElementAt(selectedIndex);
            String selectedUserId = getUserID(value);

            if(selectedUserId.equals(managerId)){
                return "manager";
            }else{
                model.remove(selectedIndex);
                return selectedUserId;
            }
        }else{
            return "error";
        }
    }

    public void addUser (String username, String userId){
        model.addElement("User - "+username+" (id:"+userId+")");
    }

    public String getUserID (String s){
        int startIndex = s.indexOf("id:") + 3;
        int endIndex = s.indexOf(")", startIndex);
        if (startIndex != -1 && endIndex != -1) {
            return s.substring(startIndex, endIndex);
        } else {
            return "error";
        }
    }
}
