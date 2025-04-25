module com.example.studentapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;


    // Export packages for FXML controllers and models
    opens com.example.studentapp to javafx.fxml;
    exports com.example.studentapp;
    exports com.example.studentapp.model;
    opens com.example.studentapp.model to javafx.fxml;
}