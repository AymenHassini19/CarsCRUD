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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.example.exception.AllFieldsRequiredException;
import com.example.exception.NoItemSelectedException;
import com.mongodb.client.*;

import java.io.IOException;

import org.bson.Document;

public class CarsCRUDcontroller {

    ObservableList<Car> carsList = FXCollections.observableArrayList();

    @FXML
    private CheckBox availableCheck;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Label errorLabel;

    
    @FXML
    private TableColumn<?, ?> availibilityColumn;

    @FXML
    private TableColumn<?, ?> carIdColumn;

    @FXML
    private TableColumn<?, ?> brandColumn;

    @FXML
    private TextField carIdTextField;

    @FXML
    private TextField brandTextField;

    @FXML
    private TableColumn<?, ?> colorColumn;

    @FXML
    private TextField colorTextField;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button insertBtn;

    @FXML
    private TableColumn<?, ?> modelColumn;

    @FXML
    private TextField modelTextField;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TextField priceTextFIeld;

    @FXML
    private Button readBtn;

    @FXML
    private TableView<Car> table;

    @FXML
    private Button updateBtn;

    @FXML
    private TableColumn<?, ?> yearColumn;

    @FXML
    private TextField yearTextField;

    private static final String URI = "mongodb+srv://aymen:aymen@java.doab6cu.mongodb.net/?retryWrites=true&w=majority&appName=java";
    private static final String DATABASE_NAME = "cars";
    private static final String COLLECTION_NAME = "cars";

    @FXML
    public void initialize() {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        carIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        availibilityColumn.setCellValueFactory(new PropertyValueFactory<>("availibility"));
        
        
        
        populateTable();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                carIdTextField.setText(newSelection.getId());
                brandTextField.setText(newSelection.getBrand());
                modelTextField.setText(newSelection.getModel());
                colorTextField.setText(newSelection.getColor());
                yearTextField.setText(String.valueOf(newSelection.getYear()));
                priceTextFIeld.setText(String.valueOf(newSelection.getPrice()));
                availableCheck.setSelected(newSelection.getAvailibility());
            }
        });
    }

        private void populateTable() {
        
        carsList.clear();
        
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {
                String id = doc.getObjectId("_id").toString().substring(doc.getObjectId("_id").toString().length() - 5);
                Car car = new Car(id,
                        doc.getString("brand"),
                        doc.getString("model"),
                        doc.getString("color"),
                        doc.getInteger("year"),
                        doc.getDouble("price"),
                        doc.getBoolean("availibility")
                );
                carsList.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        table.setItems(carsList);
    }

    @FXML
    void deleteCar(ActionEvent event) {
        errorLabel.setText("");

    Car selectedCar = table.getSelectionModel().getSelectedItem();
    
    if (selectedCar != null) {

        System.out.println("Deleting car: " + selectedCar.getBrand() + " " + selectedCar.getModel());

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);


            Document query = new Document("brand", selectedCar.getBrand())
                    .append("model", selectedCar.getModel())
                    .append("color", selectedCar.getColor())
                    .append("year", selectedCar.getYear())
                    .append("price", selectedCar.getPrice());


            collection.deleteOne(query);

            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Car deleted successfully!");

            clearFields();

            populateTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        try{
            throw new NoItemSelectedException("car");
        } catch (NoItemSelectedException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        }
    }

    }

    @FXML
    void insertCar(ActionEvent event) {
        errorLabel.setText("");

    String brand = brandTextField.getText().trim();
    String model = modelTextField.getText().trim();
    String color = colorTextField.getText().trim();
    String yearText = yearTextField.getText().trim();
    String priceText = priceTextFIeld.getText().trim();
    boolean isAvailable = availableCheck.isSelected();
    
    try {

    if (brand.isEmpty() || model.isEmpty() || color.isEmpty() || yearText.isEmpty() || priceText.isEmpty()) {
        throw new AllFieldsRequiredException();
    }

    
        int year = Integer.parseInt(yearText);
        double price = Double.parseDouble(priceText);

        Car newCar = new Car(brand, model, color, year, price,isAvailable);

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document newCarDoc = new Document("brand", newCar.getBrand())
                    .append("model", newCar.getModel())
                    .append("color", newCar.getColor())
                    .append("year", newCar.getYear())
                    .append("price", newCar.getPrice())
                    .append("availibility" , newCar.getAvailibility());

            collection.insertOne(newCarDoc);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Car inserted successfully!");
        }

        clearFields();

        populateTable();

    } catch (AllFieldsRequiredException e) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(e.getMessage());
        } catch (NumberFormatException e) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText("Year or Price must be a valid number.");
    } 

    }

    @FXML
    void readCar(ActionEvent event) {
        errorLabel.setText("");
    
    String id = carIdTextField.getText();
    String brand = brandTextField.getText().toLowerCase();
    String model = modelTextField.getText().toLowerCase();
    String color = colorTextField.getText().toLowerCase();
    String yearText = yearTextField.getText();
    String priceText = priceTextFIeld.getText();
    boolean isAvailable = availableCheck.isSelected();

    ObservableList<Car> filteredCars = FXCollections.observableArrayList();

    for (Car car : carsList) {
        boolean matches = true;

        if (!id.isEmpty() && !car.getId().toLowerCase().contains(id)) {
            matches = false;
        }

        if (!brand.isEmpty() && !car.getBrand().toLowerCase().contains(brand)) {
            matches = false;
        }
        if (!model.isEmpty() && !car.getModel().toLowerCase().contains(model)) {
            matches = false;
        }
        if (!color.isEmpty() && !car.getColor().toLowerCase().contains(color)) {
            matches = false;
        }
        if (!yearText.isEmpty()) {
            try {
                int year = Integer.parseInt(yearText);
                if (car.getYear() != year) {
                    matches = false;
                }
            } catch (NumberFormatException e) {
                matches = false;
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Year or Price must be a valid number.");
            }
        }
        if (!priceText.isEmpty()) {
            try {
                double price = Double.parseDouble(priceText);
                if (car.getPrice() != price) {
                    matches = false;
                }
            } catch (NumberFormatException e) {
                matches = false;
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Year or Price must be a valid number.");
            }
        }

        if (isAvailable && !car.getAvailibility()) { 
            matches = false;
        }

        if (matches) {
            filteredCars.add(car);
        }
    }

    table.setItems(filteredCars);
    clearFields();

    }

    @FXML
    void updateCar(ActionEvent event) {
        errorLabel.setText("");

    Car selectedCar = table.getSelectionModel().getSelectedItem();
    
    if (selectedCar != null) {

        String brand = brandTextField.getText().trim();
        String model = modelTextField.getText().trim();
        String color = colorTextField.getText().trim();
        String yearText = yearTextField.getText().trim();
        String priceText = priceTextFIeld.getText().trim();
        boolean isAvailable = availableCheck.isSelected();

        try {
        if (brand.isEmpty() || model.isEmpty() || color.isEmpty() || yearText.isEmpty() || priceText.isEmpty()) {
            throw new AllFieldsRequiredException();
        }

        

            int year = Integer.parseInt(yearText);
            double price = Double.parseDouble(priceText);


            Car updatedCar = new Car(brand, model, color, year, price, isAvailable);


            try (MongoClient mongoClient = MongoClients.create(URI)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);


                Document query = new Document("brand", selectedCar.getBrand())
                        .append("model", selectedCar.getModel())
                        .append("color", selectedCar.getColor())
                        .append("year", selectedCar.getYear())
                        .append("price", selectedCar.getPrice())
                        .append("availibility", selectedCar.getAvailibility());


                Document update = new Document("$set", new Document("brand", updatedCar.getBrand())
                        .append("model", updatedCar.getModel())
                        .append("color", updatedCar.getColor())
                        .append("year", updatedCar.getYear())
                        .append("price", updatedCar.getPrice())
                        .append("availibility", updatedCar.getAvailibility()));


                collection.updateOne(query, update);
                System.out.println("Car updated successfully!");


                populateTable();


                brandTextField.clear();
                modelTextField.clear();
                colorTextField.clear();
                yearTextField.clear();
                priceTextFIeld.clear();
            }

        } catch (NumberFormatException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Numeric fields invalid.");
        } catch (AllFieldsRequiredException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
                }
    } else {
        try{
        throw new NoItemSelectedException("car");
        } catch (NoItemSelectedException e){
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        }
    }

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
    private void clearFields() {
        carIdTextField.clear();
        brandTextField.clear();
        modelTextField.clear();
        colorTextField.clear();
        yearTextField.clear();
        priceTextFIeld.clear();
        availableCheck.setSelected(false);
    }
}
