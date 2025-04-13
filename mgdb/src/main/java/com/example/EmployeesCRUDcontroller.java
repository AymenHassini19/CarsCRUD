package com.example;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.io.IOException;

public class EmployeesCRUDcontroller {

    ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    private RadioButton NoneRadioBtn;

    @FXML
    private TableView<Employee> table;

    @FXML
    private TableColumn<Employee, String> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> fnameColumn;

    @FXML
    private TableColumn<Employee, String> lnameColumn;

    @FXML
    private TableColumn<Employee, String> emailColumn;

    @FXML
    private TableColumn<Employee, String> phoneColumn;

    @FXML
    private TableColumn<Employee, String> passwordColumn;

    @FXML
    private TableColumn<Employee, String> departmentColumn;

    @FXML
    private TableColumn<Employee, Double> comissionColumn;

    @FXML
    private TextField fnameTextField;

    @FXML
    private TextField lnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField passwordTextFIeld;

    @FXML
    private TextField comissionTextField;

    @FXML
    private ToggleGroup departmentGroup;

    @FXML
    private RadioButton maintenanceRadioBtn;

    @FXML
    private RadioButton managementRadioBtn;

    @FXML
    private RadioButton salesRadioButton;

    @FXML
    private Button insertBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button readBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button logoutBtn;

    private static final String URI = "mongodb+srv://aymen:aymen@java.doab6cu.mongodb.net/?retryWrites=true&w=majority&appName=java";
    private static final String DATABASE_NAME = "cars"; 
    private static final String COLLECTION_NAME = "employees";

    @FXML
    public void initialize() {
        // Setup TableView columns with the property names used in the Employee class.
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fnameColumn.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lnameColumn.setCellValueFactory(new PropertyValueFactory<>("lname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        comissionColumn.setCellValueFactory(new PropertyValueFactory<>("commissionRate"));

        // Populate the table with data from the database.
        populateTable();

        // Add a selection listener to update the text fields and department radio buttons when a row is selected.
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fnameTextField.setText(newSelection.getFname());
                lnameTextField.setText(newSelection.getLname());
                emailTextField.setText(newSelection.getEmail());
                phoneTextField.setText(newSelection.getPhoneNumber());
                passwordTextFIeld.setText(newSelection.getPassword());
                comissionTextField.setText(String.valueOf(newSelection.getCommissionRate()));

                // Set the department radio button based on the employee's department.
                String dept = newSelection.getDepartment().toLowerCase();
                if (dept.equals("sales")) {
                    salesRadioButton.setSelected(true);
                } else if (dept.equals("management")) {
                    managementRadioBtn.setSelected(true);
                } else if (dept.equals("maintenance")) {
                    maintenanceRadioBtn.setSelected(true);
                } else if (dept.equals("none")) {
                    NoneRadioBtn.setSelected(true);
                } else {
                    departmentGroup.selectToggle(null);
                }
            }
        });
    }

    private void populateTable() {
        employeeList.clear();

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {
                // Extract an abbreviated id from the document's ObjectId.
                String id = doc.getObjectId("_id").toString();
                id = id.substring(id.length() - 5);

                Employee emp = new Employee(
                        id,
                        doc.getString("fname"),
                        doc.getString("lname"),
                        doc.getString("email"),
                        doc.getString("phone"),
                        doc.getString("password"),
                        doc.getString("department"),
                        doc.getDouble("commissionRate")
                );
                employeeList.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(employeeList);
    }

    @FXML
    void insertEmployee(ActionEvent event) {
        // Retrieve values from text fields.
        String fname = fnameTextField.getText().trim();
        String lname = lnameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String password = passwordTextFIeld.getText().trim();
        String commissionText = comissionTextField.getText().trim();

        // Determine the selected department for insertion.
        // In this context, if NoneRadioBtn is selected, we assign "none".
        String department = "";
        if (salesRadioButton.isSelected()) {
            department = "sales";
        } else if (managementRadioBtn.isSelected()) {
            department = "management";
        } else if (maintenanceRadioBtn.isSelected()) {
            department = "maintenance";
        } else if (NoneRadioBtn.isSelected()) {
            department = "";
        }

        // Validate that none of the required fields are empty.
        // Note: For insertion/update, we expect a valid department (even "none" is valid).
        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                password.isEmpty() || commissionText.isEmpty() || department.isEmpty()) {
            System.out.println("All fields must be filled out.");
            return;
        }

        try {
            double commissionRate = Double.parseDouble(commissionText);

            // Create a new Employee object.
            Employee newEmployee = new Employee(fname, lname, email, phone, password, department, commissionRate);

            try (MongoClient mongoClient = MongoClients.create(URI)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                Document newEmpDoc = new Document("fname", newEmployee.getFname())
                        .append("lname", newEmployee.getLname())
                        .append("email", newEmployee.getEmail())
                        .append("phone", newEmployee.getPhoneNumber())
                        .append("password", newEmployee.getPassword())
                        .append("department", newEmployee.getDepartment())
                        .append("commissionRate", newEmployee.getCommissionRate());

                collection.insertOne(newEmpDoc);
                System.out.println("Employee inserted successfully!");
            }

            clearFields();
            populateTable();

        } catch (NumberFormatException e) {
            System.out.println("Commission Rate must be a valid number.");
        }
    }

    @FXML
    void updateEmployee(ActionEvent event) {
        Employee selectedEmployee = table.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            String fname = fnameTextField.getText().trim();
            String lname = lnameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String phone = phoneTextField.getText().trim();
            String password = passwordTextFIeld.getText().trim();
            String commissionText = comissionTextField.getText().trim();

            // Determine the selected department for update.
            // For update, if NoneRadioBtn is selected, assign "none".
            String department = "";
            if (salesRadioButton.isSelected()) {
                department = "sales";
            } else if (managementRadioBtn.isSelected()) {
                department = "management";
            } else if (maintenanceRadioBtn.isSelected()) {
                department = "maintenance";
            } else if (NoneRadioBtn.isSelected()) {
                department = "";
            }

            if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    password.isEmpty() || commissionText.isEmpty() || department.isEmpty()) {
                System.out.println("All fields must be filled out.");
                return;
            }

            try {
                double commissionRate = Double.parseDouble(commissionText);

                Employee updatedEmployee = new Employee(fname, lname, email, phone, password, department, commissionRate);

                try (MongoClient mongoClient = MongoClients.create(URI)) {
                    MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                    MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                    // Build the query using the selected employee's current data.
                    Document query = new Document("fname", selectedEmployee.getFname())
                            .append("lname", selectedEmployee.getLname())
                            .append("email", selectedEmployee.getEmail())
                            .append("phone", selectedEmployee.getPhoneNumber())
                            .append("password", selectedEmployee.getPassword())
                            .append("department", selectedEmployee.getDepartment())
                            .append("commissionRate", selectedEmployee.getCommissionRate());

                    // Build the update document with the new values.
                    Document update = new Document("$set", new Document("fname", updatedEmployee.getFname())
                            .append("lname", updatedEmployee.getLname())
                            .append("email", updatedEmployee.getEmail())
                            .append("phone", updatedEmployee.getPhoneNumber())
                            .append("password", updatedEmployee.getPassword())
                            .append("department", updatedEmployee.getDepartment())
                            .append("commissionRate", updatedEmployee.getCommissionRate()));

                    collection.updateOne(query, update);
                    System.out.println("Employee updated successfully!");

                    populateTable();
                    clearFields();
                }
            } catch (NumberFormatException e) {
                System.out.println("Commission Rate must be a valid number.");
            }
        } else {
            System.out.println("No employee selected to update.");
        }
    }

    @FXML
    void deleteEmployee(ActionEvent event) {
        Employee selectedEmployee = table.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            System.out.println("Deleting employee: " + selectedEmployee.getFname() + " " + selectedEmployee.getLname());

            try (MongoClient mongoClient = MongoClients.create(URI)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                Document query = new Document("fname", selectedEmployee.getFname())
                        .append("lname", selectedEmployee.getLname())
                        .append("email", selectedEmployee.getEmail())
                        .append("phone", selectedEmployee.getPhoneNumber())
                        .append("password", selectedEmployee.getPassword())
                        .append("department", selectedEmployee.getDepartment())
                        .append("commissionRate", selectedEmployee.getCommissionRate());

                collection.deleteOne(query);
                System.out.println("Employee deleted successfully!");

                clearFields();
                populateTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No employee selected to delete.");
        }
    }

    @FXML
    void readEmployee(ActionEvent event) {
        // Get filter criteria from the text fields.
        String fname = fnameTextField.getText().toLowerCase();
        String lname = lnameTextField.getText().toLowerCase();
        String email = emailTextField.getText().toLowerCase();
        String phone = phoneTextField.getText().toLowerCase();
        String password = passwordTextFIeld.getText().toLowerCase();
        String commissionText = comissionTextField.getText();
        
        // Determine the department for filtering.
        // For reading, if "None" is selected, we use an empty string so no department filter is applied.
        String department = "";
        if (salesRadioButton.isSelected()) {
            department = "sales";
        } else if (managementRadioBtn.isSelected()) {
            department = "management";
        } else if (maintenanceRadioBtn.isSelected()) {
            department = "maintenance";
        } else if (NoneRadioBtn.isSelected()) {
            department = ""; // show all departments if None is chosen as filter criteria
        }

        ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList();

        for (Employee emp : employeeList) {
            boolean matches = true;

            if (!fname.isEmpty() && !emp.getFname().toLowerCase().contains(fname)) {
                matches = false;
            }
            if (!lname.isEmpty() && !emp.getLname().toLowerCase().contains(lname)) {
                matches = false;
            }
            if (!email.isEmpty() && !emp.getEmail().toLowerCase().contains(email)) {
                matches = false;
            }
            if (!phone.isEmpty() && !emp.getPhoneNumber().toLowerCase().contains(phone)) {
                matches = false;
            }
            if (!password.isEmpty() && !emp.getPassword().toLowerCase().contains(password)) {
                matches = false;
            }
            if (!commissionText.isEmpty()) {
                try {
                    double commissionRate = Double.parseDouble(commissionText);
                    if (emp.getCommissionRate() != commissionRate) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    matches = false;
                }
            }
            // If a specific department filter is applied (i.e. not empty), then filter by department.
            if (!department.isEmpty() && !emp.getDepartment().toLowerCase().contains(department)) {
                matches = false;
            }

            if (matches) {
                filteredEmployees.add(emp);
            }
        }

        table.setItems(filteredEmployees);
    }

    @FXML
    void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent newRoot = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(newRoot);
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

    // Helper method to clear all input fields and reset the department selection.
    private void clearFields() {
        fnameTextField.clear();
        lnameTextField.clear();
        emailTextField.clear();
        phoneTextField.clear();
        passwordTextFIeld.clear();
        comissionTextField.clear();
        departmentGroup.selectToggle(null);
    }
}
