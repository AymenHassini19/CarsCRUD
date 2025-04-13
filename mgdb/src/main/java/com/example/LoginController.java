package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.io.IOException;

import org.bson.Document;

public class LoginController {

    public static Employee loggedInEmployee = null;

    
    private static final String URI = "mongodb+srv://aymen:aymen@java.doab6cu.mongodb.net/?retryWrites=true&w=majority&appName=java";
    private static final String DATABASE_NAME = "cars";
    private static final String COLLECTION_NAME = "employees";

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
        String email = EmailTextField.getText().trim();
        String password = PasswordTextField.getText();

        // Basic empty field check.
        if(email.isEmpty() || password.isEmpty()){
            errorLabel.setText("Please provide both email and password.");
            return;
        }

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Find the employee document by email.
            Document employee = collection.find(Filters.eq("email", email)).first();

            if(employee == null) {
                // No such employee found.
                errorLabel.setText("Email or password is incorrect.");
                return;
            }

            String storedPassword = employee.getString("password");
            if(!storedPassword.equals(password)) {
                // Password does not match.
                errorLabel.setText("Email or password is incorrect.");
                return;
            }

            String department = employee.getString("department");
            if(department == null || !department.equalsIgnoreCase("management")){
                // Employee exists but is not part of the management department.
                errorLabel.setText("Only users from the management department can log in.");
                return;
            }

            // If all checks passed
            errorLabel.setText("");
            loggedInEmployee = new Employee(
                employee.getString("fname"),
                employee.getString("lname"),
                employee.getString("email"),
                employee.getString("phoneNumber"),
                employee.getString("password"),
                employee.getString("department"),
                employee.getDouble("comissionRate"));


                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent newRoot = loader.load();
                    Scene currentScene = ((Node) event.getSource()).getScene();
                    currentScene.setRoot(newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            // Add logic for loading the next view or post-login actions here.

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during login.");
        }
    }
}
