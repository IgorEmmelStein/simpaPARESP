package com.mycompany.simpa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Ponto de entrada principal da aplicação SIMPA (JavaFX).
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Altera para carregar a tela de Login
        scene = new Scene(loadFXML("Login"), 600, 400);
        stage.setTitle("SIMPA - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Assume que o FXML está diretamente em src/main/resources/com/mycompany/simpa/
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();

    }


public static void main(String[] args) {
        launch();
    }
}
