package com.example;

import com.example.exception.AllFieldsRequiredException;
import com.example.exception.NoItemSelectedException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.io.IOException;

public class ClientsCRUDcontroller {

    ObservableList<Client> clientList = FXCollections.observableArrayList();

    @FXML
    private TableView<Client> table;

    @FXML
    private TableColumn<Client, String> clientIdColumn;

    @FXML
    private TableColumn<Client, String> fnameColumn;

    @FXML
    private TableColumn<Client, String> lnameColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> phoneColumn;

    @FXML
    private TableColumn<Client, String> addressColumn;

    @FXML
    private TableColumn<Client, String> idCardColumn;

    @FXML
    private TextField fnameTextField;

    @FXML
    private TextField lnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label errorLabel;
    
    @FXML
    private TextField clientIdTextField;

    @FXML
    private TextField addressTextFIeld;

    @FXML
    private TextField idCardTextFIeld;

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

    // MongoDB connection constants
    private static final String URI = "mongodb+srv://aymen:aymen@java.doab6cu.mongodb.net/?retryWrites=true&w=majority&appName=java";
    private static final String DATABASE_NAME = "cars"; // use your database name here
    private static final String COLLECTION_NAME = "clients";

    @FXML
    public void initialize() {
        // Set up table columns to map to Client properties.
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fnameColumn.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lnameColumn.setCellValueFactory(new PropertyValueFactory<>("lname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        idCardColumn.setCellValueFactory(new PropertyValueFactory<>("idCard"));

        // Populate the TableView with data from the database.
        populateTable();

        // Add a selection listener to update text fields when a row is selected.
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clientIdTextField.setText(newSelection.getId());
                fnameTextField.setText(newSelection.getFname());
                lnameTextField.setText(newSelection.getLname());
                emailTextField.setText(newSelection.getEmail());
                phoneTextField.setText(newSelection.getPhoneNumber());
                addressTextFIeld.setText(newSelection.getAddress());
                idCardTextFIeld.setText(newSelection.getIdCard());
            }
        });
    }

    private void populateTable() {
        clientList.clear();

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {
                // Extract an abbreviated id from the document's ObjectId.
                String id = doc.getObjectId("_id").toString();
                id = id.substring(id.length() - 5);

                Client client = new Client(
                        id,
                        doc.getString("fname"),
                        doc.getString("lname"),
                        doc.getString("email"),
                        doc.getString("phone"),
                        doc.getString("address"),
                        doc.getString("idCard")
                );
                clientList.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(clientList);
    }

    @FXML
    void insertClient(ActionEvent event) {
        errorLabel.setText("");
        // Retrieve values from the text fields.
        String fname = fnameTextField.getText().trim();
        String lname = lnameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String address = addressTextFIeld.getText().trim();
        String idCard = idCardTextFIeld.getText().trim();
        try{
        // Validate that all fields are filled.
        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                address.isEmpty() || idCard.isEmpty()) {
            throw new AllFieldsRequiredException();
        }

        Integer.parseInt(phone);
        Integer.parseInt(idCard);

        // Create the new Client object.
        Client newClient = new Client(fname, lname, email, phone, address, idCard);

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document newClientDoc = new Document("fname", newClient.getFname())
                    .append("lname", newClient.getLname())
                    .append("email", newClient.getEmail())
                    .append("phone", newClient.getPhoneNumber())
                    .append("address", newClient.getAddress())
                    .append("idCard", newClient.getIdCard());

            collection.insertOne(newClientDoc);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Client inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearFields();
        populateTable();

    }catch(AllFieldsRequiredException e){
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(e.getMessage());
    }catch(NumberFormatException e) {
        errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Numeric fields invalid.");
    }

       
    }

    @FXML
    void updateClient(ActionEvent event) {
        errorLabel.setText("");
        Client selectedClient = table.getSelectionModel().getSelectedItem();

        try{

        if (selectedClient != null) {
            String fname = fnameTextField.getText().trim();
            String lname = lnameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String phone = phoneTextField.getText().trim();
            String address = addressTextFIeld.getText().trim();
            String idCard = idCardTextFIeld.getText().trim();

            

            if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    address.isEmpty() || idCard.isEmpty()) {
                throw new AllFieldsRequiredException();
            }

            Integer.parseInt(phone);
            Integer.parseInt(idCard);

            Client updatedClient = new Client(fname, lname, email, phone, address, idCard);

            try (MongoClient mongoClient = MongoClients.create(URI)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                // Build the query based on the selected client's current data.
                Document query = new Document("fname", selectedClient.getFname())
                        .append("lname", selectedClient.getLname())
                        .append("email", selectedClient.getEmail())
                        .append("phone", selectedClient.getPhoneNumber())
                        .append("address", selectedClient.getAddress())
                        .append("idCard", selectedClient.getIdCard());

                // Build the update document with the new values.
                Document update = new Document("$set", new Document("fname", updatedClient.getFname())
                        .append("lname", updatedClient.getLname())
                        .append("email", updatedClient.getEmail())
                        .append("phone", updatedClient.getPhoneNumber())
                        .append("address", updatedClient.getAddress())
                        .append("idCard", updatedClient.getIdCard()));

                collection.updateOne(query, update);
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Client updated successfully!");

                populateTable();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new NoItemSelectedException("client");
        }
        } catch(AllFieldsRequiredException e){
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        } catch (NoItemSelectedException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        } catch(NumberFormatException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Numeric fields invalid.");
        }
    }

    @FXML
    void deleteClient(ActionEvent event) {
        errorLabel.setText("");
        Client selectedClient = table.getSelectionModel().getSelectedItem();
        try{
        if (selectedClient != null) {
            System.out.println("Deleting client: " + selectedClient.getFname() + " " + selectedClient.getLname());

            try (MongoClient mongoClient = MongoClients.create(URI)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                Document query = new Document("fname", selectedClient.getFname())
                        .append("lname", selectedClient.getLname())
                        .append("email", selectedClient.getEmail())
                        .append("phone", selectedClient.getPhoneNumber())
                        .append("address", selectedClient.getAddress())
                        .append("idCard", selectedClient.getIdCard());

                collection.deleteOne(query);
            
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Client deleted successfully!");

                clearFields();
                populateTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new NoItemSelectedException("client");
        }
        } catch (NoItemSelectedException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        } 
    }

    @FXML
    void readClient(ActionEvent event) {
        errorLabel.setText("");
        // Filter criteria from the text fields.
        String id = clientIdTextField.getText();
        String fname = fnameTextField.getText().toLowerCase();
        String lname = lnameTextField.getText().toLowerCase();
        String email = emailTextField.getText().toLowerCase();
        String phone = phoneTextField.getText().toLowerCase();
        String address = addressTextFIeld.getText().toLowerCase();
        String idCard = idCardTextFIeld.getText().toLowerCase();

        ObservableList<Client> filteredClients = FXCollections.observableArrayList();

        for (Client client : clientList) {
            boolean matches = true;

            if (!id.isEmpty() && !client.getId().toLowerCase().contains(id)) {
                matches = false;
            }

            if (!fname.isEmpty() && !client.getFname().toLowerCase().contains(fname)) {
                matches = false;
            }
            if (!lname.isEmpty() && !client.getLname().toLowerCase().contains(lname)) {
                matches = false;
            }
            if (!email.isEmpty() && !client.getEmail().toLowerCase().contains(email)) {
                matches = false;
            }
            if (!phone.isEmpty() && !client.getPhoneNumber().toLowerCase().contains(phone)) {
                matches = false;
            }
            if (!address.isEmpty() && !client.getAddress().toLowerCase().contains(address)) {
                matches = false;
            }
            if (!idCard.isEmpty() && !client.getIdCard().toLowerCase().contains(idCard)) {
                matches = false;
            }
            if (matches) {
                filteredClients.add(client);
            }
        }

        table.setItems(filteredClients);
        clearFields();
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

    // Helper method to clear all input fields.
    private void clearFields() {
        clientIdTextField.clear();
        fnameTextField.clear();
        lnameTextField.clear();
        emailTextField.clear();
        phoneTextField.clear();
        addressTextFIeld.clear();
        idCardTextFIeld.clear();
    }
}
