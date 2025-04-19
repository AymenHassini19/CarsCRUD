package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

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
    private static final String SALES_COLL = "sales";

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
        fullyPaidColumn.setCellValueFactory(new PropertyValueFactory<>("fullyPaid"));

        populateTable();
        populateCarsList();
        populateClientsList();
        populateSalespersonsList();



        carComboBox.setItems(FXCollections.observableArrayList(carsList));
        salespersonComboBox.setItems(FXCollections.observableArrayList(salespersonsList));
        clientComboBox.setItems(FXCollections.observableArrayList(clientsList));

        table.getSelectionModel().selectedItemProperty().addListener((o, oldS, sel) -> {
            if (sel == null) return;
            // select in ComboBoxes
            carsList.stream()
               .filter(c -> c.getId().equals(sel.getCarId()))
               .findFirst().ifPresent(c -> carComboBox.getSelectionModel().select(c));
            clientsList.stream()
               .filter(c -> c.getId().equals(sel.getClientId()))
               .findFirst().ifPresent(c -> clientComboBox.getSelectionModel().select(c));
            salespersonsList.stream()
               .filter(e -> e.getId().equals(sel.getSalespersonId()))
               .findFirst().ifPresent(e -> salespersonComboBox.getSelectionModel().select(e));

            initialDepositTextFIeld.setText(String.valueOf(sel.getInitialDeposit()));
            interestRateTextFIeld .setText(String.valueOf(sel.getInterestRate()));
            leaseDurationTextFIeld .setText(String.valueOf(sel.getLeaseDuration()));
            monthsRemainingTextFIeld.setText(String.valueOf(sel.getMonthsRemaining()));

            if (sel.isFullyPaid()) {
                yesRadioButton.setSelected(true);
            } else {
                noRadioBtn.setSelected(true);
            }
        });

        
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
                        doc.getBoolean("FullyPaid")
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

        Sale sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            System.out.println("No sale to delete.");
            return;
        }

        // build query by sale _id
        String saleId    = sel.getId();
        String exprRegex = saleId + "$";
        Document query = new Document("$expr",
            new Document("$regexMatch", 
              new Document("input", new Document("$toString","$_id"))
                     .append("regex", exprRegex)
            )
        );

        try (MongoClient client = MongoClients.create(URI)) {
            client.getDatabase(DATABASE_NAME)
                  .getCollection(SALES_COLL)
                  .deleteOne(query);
        }
        // release the car
        carsList.stream()
            .filter(c -> c.getId().equals(sel.getCarId()))
            .findFirst().ifPresent(c -> updateCarAvailability(c, true));

        clearForm();
        populateTable();
        System.out.println("Sale deleted.");

    }

    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void insertSale(ActionEvent event) {

        Car selectedCar         = carComboBox.getValue();
        Client selectedClient   = clientComboBox.getValue();
        Employee selectedSalesp = salespersonComboBox.getValue();
        if (selectedCar==null||selectedClient==null||selectedSalesp==null) {
            System.out.println("Must select car, client, and salesperson.");
            return;
        }

        if (!selectedCar.getAvailibility()) {
            System.out.println("Selected car is not available for sale.");
            return;
        }

        try {
            double fullPrice = selectedCar.getPrice();
            double initDep   = Double.parseDouble(initialDepositTextFIeld.getText());
            double irate     = Double.parseDouble(interestRateTextFIeld.getText());
            int    duration  = Integer.parseInt(leaseDurationTextFIeld.getText());
            int    remaining = Integer.parseInt(monthsRemainingTextFIeld.getText());

            boolean fullyPaid = (remaining == 0);

            double principal = fullPrice - initDep;
            double monthlyPayment;
            if (irate == 0) {
                monthlyPayment = principal / duration;
            } else {
                double r = irate/100.0/12.0;
                monthlyPayment = Math.round((principal * r) / (1 - Math.pow(1+r, -duration)));
            }

            // build doc
            Document doc = new Document("carId",   new ObjectId("000000000000000000000000".substring(0, 24 - selectedCar.getId().length()) + selectedCar.getId()))
                         .append("clientId", new ObjectId("000000000000000000000000".substring(0, 24 - selectedClient.getId().length()) + selectedClient.getId()))
                         .append("employeeId", new ObjectId("000000000000000000000000".substring(0, 24 - selectedSalesp.getId().length()) + selectedSalesp.getId()))
                         .append("fullPrice",   fullPrice)
                         .append("initialDeposit", initDep)
                         .append("interestRate",   irate)
                         .append("monthlyPayment", monthlyPayment)
                         .append("leaseDuration",  duration)
                         .append("monthsRemaining", remaining)
                         .append("FullyPaid",    fullyPaid);

            try (MongoClient client = MongoClients.create(URI)) {
                client.getDatabase(DATABASE_NAME)
                      .getCollection(SALES_COLL)
                      .insertOne(doc);
            }

            // mark this car unavailable
            updateCarAvailability(selectedCar, false);
            populateCarsList();
            carComboBox.setItems(FXCollections.observableArrayList(carsList)); 
            clearForm();
            populateTable();
            System.out.println("Sale inserted.");
        } catch (NumberFormatException ex) {
            System.out.println("Numeric fields invalid.");
        }

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void readSale(ActionEvent event) {
        ObservableList<Sale> filteredSales = FXCollections.observableArrayList();

    String selectedCarId = (carComboBox.getValue() != null) ? carComboBox.getValue().getId() : "";
    String selectedClientId = (clientComboBox.getValue() != null) ? clientComboBox.getValue().getId() : "";
    String selectedSalespersonId = (salespersonComboBox.getValue() != null) ? salespersonComboBox.getValue().getId() : "";

    String initialDepositStr = initialDepositTextFIeld.getText().trim();
    String interestRateStr = interestRateTextFIeld.getText().trim();
    String leaseDurationStr = leaseDurationTextFIeld.getText().trim();
    String monthsRemainingStr = monthsRemainingTextFIeld.getText().trim();

    boolean filterFullyPaid = fullyPaidGroup.getSelectedToggle() != null && !noneRadioBtn.isSelected();
    boolean fullyPaidValue = yesRadioButton.isSelected(); // true if yesRadioButton is selected

    for (Sale sale : salesList) {
        boolean matches = true;

        if (!selectedCarId.isEmpty() && !sale.getCarId().equals(selectedCarId)) {
            matches = false;
        }
        if (!selectedClientId.isEmpty() && !sale.getClientId().equals(selectedClientId)) {
            matches = false;
        }
        if (!selectedSalespersonId.isEmpty() && !sale.getSalespersonId().equals(selectedSalespersonId)) {
            matches = false;
        }

        if (!initialDepositStr.isEmpty()) {
            try {
                double val = Double.parseDouble(initialDepositStr);
                if (sale.getInitialDeposit() != val) matches = false;
            } catch (NumberFormatException e) {
                matches = false;
            }
        }

        if (!interestRateStr.isEmpty()) {
            try {
                double val = Double.parseDouble(interestRateStr);
                if (sale.getInterestRate() != val) matches = false;
            } catch (NumberFormatException e) {
                matches = false;
            }
        }

        if (!leaseDurationStr.isEmpty()) {
            try {
                int val = Integer.parseInt(leaseDurationStr);
                if (sale.getLeaseDuration() != val) matches = false;
            } catch (NumberFormatException e) {
                matches = false;
            }
        }

        if (!monthsRemainingStr.isEmpty()) {
            try {
                int val = Integer.parseInt(monthsRemainingStr);
                if (sale.getMonthsRemaining() != val) matches = false;
            } catch (NumberFormatException e) {
                matches = false;
            }
        }

        if (filterFullyPaid && sale.isFullyPaid() != fullyPaidValue) {
            matches = false;
        }

        if (matches) {
            filteredSales.add(sale);
        }
    }

    table.setItems(filteredSales);
    clearForm();
        
    }

    @FXML
    void updateSale(ActionEvent event) {

        Sale sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            System.out.println("No sale selected to update.");
            return;
        }
    
        // 1. Read new form values
        Car newCar       = carComboBox.getValue();
        Client newClient = clientComboBox.getValue();
        Employee newSalesperson = salespersonComboBox.getValue();
        String initDepStr  = initialDepositTextFIeld.getText().trim();
        String irateStr    = interestRateTextFIeld.getText().trim();
        String durationStr = leaseDurationTextFIeld.getText().trim();
        String remainingStr= monthsRemainingTextFIeld.getText().trim();
    
        // 2. Validate
        if (newCar == null || newClient == null || newSalesperson == null
            || initDepStr.isEmpty() || irateStr.isEmpty()
            || durationStr.isEmpty() || remainingStr.isEmpty()) {
            System.out.println("All fields must be filled out.");
            return;
        }
    
        try {
            // 3. Parse and recalc
            double fullPrice      = newCar.getPrice();
            double initDep        = Double.parseDouble(initDepStr);
            double irate          = Double.parseDouble(irateStr);
            int    duration       = Integer.parseInt(durationStr);
            int    monthsRemaining= Integer.parseInt(remainingStr);
            boolean fullyPaid     = (monthsRemaining == 0);
    
            double principal = fullPrice - initDep;
            double monthlyPayment;
            if (irate == 0) {
                monthlyPayment = principal / duration;
            } else {
                double r = irate/100.0/12.0;
                monthlyPayment = Math.round((principal * r) / (1 - Math.pow(1 + r, -duration)));
            }
    
            // 4. Build MongoDB query & update
            String regex = sel.getId() + "$";
            Document query = new Document("$expr",
                new Document("$regexMatch",
                    new Document("input", new Document("$toString","$_id"))
                          .append("regex", regex)
                )
            );
    
            // helper to build a full 24‑hex ObjectId from the 5‑char suffix
            java.util.function.Function<String,ObjectId> toOid = suffix ->
                new ObjectId("000000000000000000000000".substring(0, 24 - suffix.length()) + suffix);
    
            Document setFields = new Document("carId",        toOid.apply(newCar.getId()))
                .append("clientId",   toOid.apply(newClient.getId()))
                .append("employeeId", toOid.apply(newSalesperson.getId()))
                .append("fullPrice",      fullPrice)
                .append("initialDeposit", initDep)
                .append("interestRate",   irate)
                .append("monthlyPayment", monthlyPayment)
                .append("leaseDuration",  duration)
                .append("monthsRemaining", monthsRemaining)
                .append("FullyPaid",      fullyPaid);
    
            try (MongoClient client = MongoClients.create(URI)) {
                client.getDatabase(DATABASE_NAME)
                      .getCollection(SALES_COLL)
                      .updateOne(query, new Document("$set", setFields));
            }
    
            // 5. If car changed, flip availabilities
            if (!sel.getCarId().equals(newCar.getId())) {
                // restore old car
                carsList.stream()
                        .filter(c -> c.getId().equals(sel.getCarId()))
                        .findFirst()
                        .ifPresent(c -> updateCarAvailability(c, true));
    
                // reserve new car
                updateCarAvailability(newCar, false);
            }
    
            // 6. Refresh UI
            populateCarsList();
            carComboBox.setItems(FXCollections.observableArrayList(carsList));
            clearForm();
            populateTable();
            System.out.println("Sale updated successfully!");
    
        } catch (NumberFormatException ex) {
            System.out.println("Numeric fields invalid.");
        }

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

                carsList.add(car);}
            
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

    private void clearForm() {
        carComboBox.getSelectionModel().clearSelection();
        clientComboBox.getSelectionModel().clearSelection();
        salespersonComboBox.getSelectionModel().clearSelection();
        initialDepositTextFIeld.clear();
        interestRateTextFIeld.clear();
        leaseDurationTextFIeld.clear();
        monthsRemainingTextFIeld.clear();
        noneRadioBtn.setSelected(true);
    }


}
