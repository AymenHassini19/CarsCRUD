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

import com.example.exception.EmptyLoginFieldsException;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.InvalidEmailFormatException;
import com.example.exception.UnauthorizedDepartmentException;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.io.IOException;
import java.util.regex.Pattern;

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

        try {
            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                throw new EmptyLoginFieldsException();
            }
            // Verify email format
            if (!isValidEmail(email)) {
                throw new InvalidEmailFormatException();
            }

            // Connect to MongoDB
            try (MongoClient mongoClient = MongoClients.create(URI)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                // Find the employee document by email.
                Document employee = collection.find(Filters.eq("email", email)).first();
                // Validate credentials
                if (employee == null || !employee.getString("password").equals(password)) {
                    throw new InvalidCredentialsException();
                }
                // Validate department
                String department = employee.getString("department");
                if (department == null || !"management".equalsIgnoreCase(department)) {
                    throw new UnauthorizedDepartmentException();
                }

                // Successful login
                loggedInEmployee = new Employee(
                    employee.getString("fname"),
                    employee.getString("lname"),
                    employee.getString("email"),
                    employee.getString("phone"),
                    employee.getString("password"),
                    department,
                    employee.getDouble("commissionRate")
                );

                // Load dashboard view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent newRoot = loader.load();
                Scene currentScene = ((Node) event.getSource()).getScene();
                currentScene.setRoot(newRoot);
            }

        } catch (EmptyLoginFieldsException e) {
            errorLabel.setText(e.getMessage());
        } catch (InvalidEmailFormatException e) {
            errorLabel.setText(e.getMessage());
        } catch (InvalidCredentialsException e) {
            errorLabel.setText(e.getMessage());
        } catch (UnauthorizedDepartmentException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during login.");
        }
    }

    /**
     * Simple email format validation using regex.
     */
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
