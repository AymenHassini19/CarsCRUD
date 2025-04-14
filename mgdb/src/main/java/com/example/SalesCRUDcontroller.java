package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class SalesCRUDcontroller {

    List<Car> carsList = new ArrayList<>();
    List<Employee> salespersonsList = new ArrayList<>();
    List<Client> clientsList = new ArrayList<>();

    ObservableList<Sale> salesList = FXCollections.observableArrayList();


    private static final String URI = "mongodb+srv://aymen:aymen@java.doab6cu.mongodb.net/?retryWrites=true&w=majority&appName=java";
    private static final String DATABASE_NAME = "cars";

    @FXML
    private Button backBtn;

    @FXML
    private ComboBox<Car> carComboBox;

    @FXML
    private TableColumn<?, ?> carIdColumn;

    @FXML
    private ComboBox<Client> clientComboBox;

    @FXML
    private TableColumn<?, ?> clientIdColumn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<?, ?> fullPriceColumn;



    @FXML
    private TableColumn<?, ?> fullyPaidColumn;

    @FXML
    private ToggleGroup fullyPaidGroup;

    @FXML
    private TableColumn<?, ?> initialDepositColumn;

    @FXML
    private TextField initialDepositTextFIeld;

    @FXML
    private Button insertBtn;

    @FXML
    private TableColumn<?, ?> interestRateColumn;

    @FXML
    private TextField interestRateTextFIeld;

    @FXML
    private TableColumn<?, ?> leaseDurationColumn;

    @FXML
    private TextField leaseDurationTextFIeld;

    @FXML
    private Button logoutBtn;

    @FXML
    private TableColumn<?, ?> monthlyPaymentColumn;


    @FXML
    private TableColumn<?, ?> monthsRemainingColumn;

    @FXML
    private TextField monthsRemainingTextFIeld;

    @FXML
    private RadioButton noRadioBtn;

    @FXML
    private RadioButton noneRadioBtn;

    @FXML
    private Button readBtn;

    @FXML
    private TableColumn<?, ?> saleIdColumn;

    @FXML
    private ComboBox<Employee> salespersonComboBox;

    @FXML
    private TableColumn<?, ?> salespersonIdColumn;

    @FXML
    private TableView<Sale> table;

    @FXML
    private Button updateBtn;

    @FXML
    private RadioButton yesRadioButton;

    @FXML
    public void initialize() {
        saleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        carIdColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        salespersonIdColumn.setCellValueFactory(new PropertyValueFactory<>("salespersonId"));
        fullPriceColumn.setCellValueFactory(new PropertyValueFactory<>("fullPrice"));
        initialDepositColumn.setCellValueFactory(new PropertyValueFactory<>("initialDeposit"));
        interestRateColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        monthlyPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyPayment"));
        leaseDurationColumn.setCellValueFactory(new PropertyValueFactory<>("leaseDuration"));
        monthsRemainingColumn.setCellValueFactory(new PropertyValueFactory<>("monthsRemaining"));
        fullyPaidColumn.setCellValueFactory(new PropertyValueFactory<>("isFullyPaid"));

        populateTable();
        populateCarsList();
        populateClientsList();
        populateSalespersonsList();



        carComboBox.setItems(FXCollections.observableArrayList(carsList));
        salespersonComboBox.setItems(FXCollections.observableArrayList(salespersonsList));
        clientComboBox.setItems(FXCollections.observableArrayList(clientsList));

        
    }

    private void populateTable() {
        
        salesList.clear();
        
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("sales");
            
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {
                String id = doc.getObjectId("_id").toString().substring(doc.getObjectId("_id").toString().length() - 5);
                String carId = doc.getObjectId("carId").toString().substring(doc.getObjectId("carId").toString().length() - 5);
                String clientId = doc.getObjectId("clientId").toString().substring(doc.getObjectId("clientId").toString().length() - 5);
                String employeeId = doc.getObjectId("employeeId").toString().substring(doc.getObjectId("employeeId").toString().length() - 5);
                Sale sale = new Sale(id,
                        carId,
                        clientId,
                        employeeId,
                        doc.getDouble("fullPrice"),
                        doc.getDouble("initialDeposit"),
                        doc.getDouble("interestRate"),
                        doc.getDouble("monthlyPayment"),
                        doc.getInteger("leaseDuration"),
                        doc.getInteger("monthsRemaining"),
                        doc.getBoolean("isFullyPaid")
                );
                salesList.add(sale);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        table.setItems(salesList);
    }

    @FXML
    void handleCarCombo(ActionEvent event) {

    }

    @FXML
    void handleClientCombo(ActionEvent event) {

    }

    @FXML
    void handleSalespersonCombo(ActionEvent event) {

    }

    @FXML
    void deleteSale(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void insertSale(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void readSale(ActionEvent event) {
    }

    @FXML
    void updateSale(ActionEvent event) {

    }


    private void populateCarsList() {
        
        carsList.clear();
        
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("cars");
            
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
        
    }

    private void populateSalespersonsList() {
        salespersonsList.clear();

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("employees");

            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {

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
                if (emp.getDepartment().toLowerCase().contains("sales")){
                salespersonsList.add(emp);
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateClientsList() {
        clientsList.clear();

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("clients");

            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {

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
                clientsList.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCarAvailability(Car car, boolean available) {

        String URI = "mongodb+srv://aymen:aymen@java.doab6cu.mongodb.net/?retryWrites=true&w=majority&appName=java";
        String DATABASE_NAME = "cars";
        String COLLECTION_NAME = "cars";
        
        
        String regexPattern = car.getId() + "$";

        
        Document query = new Document("$expr",
                new Document("$regexMatch", 
                        new Document("input", new Document("$toString", "$_id"))
                                .append("regex", regexPattern)
                )
        );

        
        Document update = new Document("$set", new Document("availibility", available));

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            collection.updateOne(query, update);
            System.out.println("Car availability updated successfully for id ending with: " + car.getId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating car availability for id ending with: " + car.getId());
        }
    }

}
