package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class EmployeesCRUDcontroller {

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<?, ?> comissionColumn;

    @FXML
    private TextField comissionTextField;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<?, ?> departmentColumn;

    @FXML
    private ToggleGroup departmentGroup;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TextField emailTextField;

    @FXML
    private TableColumn<?, ?> employeeIdColumn;

    @FXML
    private TableColumn<?, ?> fnameColumn;

    @FXML
    private TextField fnameTextField;

    @FXML
    private Button insertBtn;

    @FXML
    private TableColumn<?, ?> lnameColumn;

    @FXML
    private TextField lnameTextField;

    @FXML
    private Button logoutBtn;

    @FXML
    private RadioButton maintenanceRadioBtn;

    @FXML
    private RadioButton managementRadioBtn;

    @FXML
    private TableColumn<?, ?> passwordColumn;

    @FXML
    private TextField passwordTextFIeld;

    @FXML
    private TableColumn<?, ?> phoneColumn;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button readBtn;

    @FXML
    private RadioButton salesRadioButton;

    @FXML
    private TableView<?> table;

    @FXML
    private Button updateBtn;

    @FXML
    void deleteEmployee(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void insertEmployee(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void readEmployee(ActionEvent event) {

    }

    @FXML
    void updateEmployee(ActionEvent event) {

    }

}
