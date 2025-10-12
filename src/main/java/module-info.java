module com.mycompany.simpa {
    
    // Linha necessária para usar JDBC (java.sql)
    requires java.sql; 
    
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.simpa to javafx.fxml;
    exports com.mycompany.simpa;
}
