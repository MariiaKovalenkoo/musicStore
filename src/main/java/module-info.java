module com.storeproject.musicstore {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.storeproject.musicstore to javafx.fxml;
    exports com.storeproject.musicstore;
    exports com.storeproject.musicstore.data;
    exports com.storeproject.musicstore.model;
    exports com.storeproject.musicstore.exceptions;
    exports com.storeproject.musicstore.interfaces;
    opens com.storeproject.musicstore.interfaces to javafx.fxml;
    opens com.storeproject.musicstore.data to javafx.fxml;
}