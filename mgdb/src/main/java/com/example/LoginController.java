package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField EmailTextField;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginBtn;

    @FXML
    void login(ActionEvent event) {
        System.out.println("login button clicked !!!");
    }

}
