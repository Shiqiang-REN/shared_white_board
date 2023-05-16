package org.dsA2;

import javax.swing.*;

/**
 * ClassName: Users
 * Package: org.dsA2
 * Description:
 *
 * @Author Shiqiang Ren
 * @Create 16/5/2023 09:46
 * @Version 1.0
 */
public class Users {

    JList<String>  usersList;
    DefaultListModel<String> model;

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


    public void setUserList (String[] userArrayList){
        usersList.setListData(userArrayList);
    }


    public void removeSelectedUser (){
        int selectedIndex = usersList.getSelectedIndex();
        if (selectedIndex != -1) {
            model.remove(selectedIndex);
        }
    }


    public void addUser (String username, String userId){
        model.addElement("User - "+username+" (id:"+userId+")");
    }
}
