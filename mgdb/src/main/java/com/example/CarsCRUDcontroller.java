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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.mongodb.client.*;
import org.bson.Document;



public class CarsCRUDcontroller {

    ObservableList<Car> carsList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<?, ?> brandColumn;

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
        populateTable();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                brandTextField.setText(newSelection.getBrand());
                modelTextField.setText(newSelection.getModel());
                colorTextField.setText(newSelection.getColor());
                yearTextField.setText(String.valueOf(newSelection.getYear()));
                priceTextFIeld.setText(String.valueOf(newSelection.getPrice()));
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
                Car car = new Car(
                        doc.getString("brand"),
                        doc.getString("model"),
                        doc.getString("color"),
                        doc.getInteger("year"),
                        doc.getDouble("price")
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

    }

    @FXML
    void insertCar(ActionEvent event) {

    String brand = brandTextField.getText().trim();
    String model = modelTextField.getText().trim();
    String color = colorTextField.getText().trim();
    String yearText = yearTextField.getText().trim();
    String priceText = priceTextFIeld.getText().trim();


    if (brand.isEmpty() || model.isEmpty() || color.isEmpty() || yearText.isEmpty() || priceText.isEmpty()) {
        System.out.println("All fields must be filled out.");
        return;
    }

    try {
        int year = Integer.parseInt(yearText);
        double price = Double.parseDouble(priceText);

        Car newCar = new Car(brand, model, color, year, price);

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document newCarDoc = new Document("brand", newCar.getBrand())
                    .append("model", newCar.getModel())
                    .append("color", newCar.getColor())
                    .append("year", newCar.getYear())
                    .append("price", newCar.getPrice());

            collection.insertOne(newCarDoc);
            System.out.println("Car inserted successfully!");
        }

        brandTextField.clear();
        modelTextField.clear();
        colorTextField.clear();
        yearTextField.clear();
        priceTextFIeld.clear();

        populateTable();

    } catch (NumberFormatException e) {
        
        System.out.println("Year or Price must be a valid number.");
    }

    }

    @FXML
    void readCar(ActionEvent event) {

    String brand = brandTextField.getText().toLowerCase();
    String model = modelTextField.getText().toLowerCase();
    String color = colorTextField.getText().toLowerCase();
    String yearText = yearTextField.getText();
    String priceText = priceTextFIeld.getText();

    ObservableList<Car> filteredCars = FXCollections.observableArrayList();

    for (Car car : carsList) {
        boolean matches = true;

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
            }
        }

        if (matches) {
            filteredCars.add(car);
        }
    }

    table.setItems(filteredCars);

    }

    @FXML
    void updateCar(ActionEvent event) {

    }

}
