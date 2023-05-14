package org.dsA2.dsA2;

import javax.swing.*;

/**
 * ClassName: LoginUi
 * Package: org.dsA2
 *
 * @Author Shiqiang Ren
 * @Create 12/5/2023 12:32
 * @Version 1.0
 */

public class LoginUi {
    public static void main(String[] args) {

        Object[] inputs = {"Please enter your name：", new JTextField()};
        int option = JOptionPane.showConfirmDialog(null, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = ((JTextField) inputs[1]).getText();
            WhiteBoardUi wb = new WhiteBoardUi(username);
        }
    }
}

