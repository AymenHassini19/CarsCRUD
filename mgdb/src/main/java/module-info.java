module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mongodb.driver.sync.client;  
    requires org.mongodb.bson;  
    requires org.mongodb.driver.core;
    requires javafx.graphics;
    requires javafx.base;  

    opens com.example to javafx.fxml;
    exports com.example;
}
