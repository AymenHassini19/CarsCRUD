package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox clientsVbox;

    @FXML
    private VBox employeesVbox;

    @FXML
    private VBox inventoryVbox;

    @FXML
    private Button logoutBtn;

    @FXML
    private VBox salesVbox;

    @FXML
    public void initialize() {
        if (LoginController.loggedInEmployee != null) {
            welcomeLabel.setText("Welcome, " + LoginController.loggedInEmployee.getFullName());
        }
    }

    @FXML
    void handleClientsClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clientsCRUD.fxml"));
            Parent newRoot = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleEmployeesClick(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("employeesCRUD.fxml"));
            Parent newRoot = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleInventoryClick(MouseEvent event) {
   
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("carsCRUD.fxml"));
            Parent newRoot = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSalesClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("salesCRUD.fxml"));
            Parent newRoot = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(newRoot, 1100, 700));
            newStage.setTitle("Best Lease sales Management");
            newStage.getIcons().add(new Image(getClass().getResource("/com/example/pictures/icon.png").toExternalForm()));

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void logout(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent newRoot = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
