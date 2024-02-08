module com.example.ecm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.ecm.model to javafx.base;

    opens com.example.ecm to javafx.fxml;
    exports com.example.ecm;
}