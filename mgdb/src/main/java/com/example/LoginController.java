package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class LoginController {

    // MongoDB connection constants.
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
                errorLabel.setText("wrong credentials");
                return;
            }

            String storedPassword = employee.getString("password");
            if(!storedPassword.equals(password)) {
                // Password does not match.
                errorLabel.setText("wrong credentials");
                return;
            }

            String department = employee.getString("department");
            if(department == null || !department.equalsIgnoreCase("management")){
                // Employee exists but is not part of the management department.
                errorLabel.setText("You do not have the right to login");
                return;
            }

            // If all checks passed
            errorLabel.setText("");
            System.out.println("login successful!");
            // Add logic for loading the next view or post-login actions here.

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during login.");
        }
    }
}
