module com.mycompany.simpa {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.simpa to javafx.fxml;
    exports com.mycompany.simpa;
}
