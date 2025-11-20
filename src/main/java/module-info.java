module com.mycompany.simpa {

    requires java.sql; 
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.graphics; 

    opens com.mycompany.simpa to javafx.fxml;
    opens classes to javafx.base;
    exports com.mycompany.simpa;

    opens controller to javafx.fxml;
}
