/*
 * Click nbfs:
 * Click nbfs:
 */
package controller;

import classes.Usuario;
import com.mycompany.simpa.App;
import java.io.IOException;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import service.AdministradorService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.BusinessException;
import util.DBException;

public class LoginController {

    private static Scene scene;

    @FXML
    private TextField txtNomeUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Button btnEntrar;
    @FXML
    private Label lblErro;

    private AdministradorService administradorService;

    private static Usuario usuarioLogado;

    public LoginController() {

        this.administradorService = new AdministradorService();
    }

    @FXML
    private void handleLogin() {
        String nome = txtNomeUsuario.getText();
        String senha = txtSenha.getText();

        lblErro.setVisible(false);

        try {
            System.out.println("DEBUG: 1. Tentativa de autenticação para: " + nome);
            
            //Autentica
            usuarioLogado = administradorService.login(nome, senha);
            
            System.out.println("DEBUG: 2. Login BEM-SUCEDIDO.");

            //Troca a tela 
            System.out.println("DEBUG: 3. Chamando App.setRoot('TelaConsulta')...");
            App.setRoot("TelaConsulta");
         

        } catch (BusinessException e) {
            lblErro.setText(e.getMessage());
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
            
        } catch (Exception e) { // Captura DBException e IOException
            System.out.println("DEBUG: Erro técnico.");
            e.printStackTrace();
            lblErro.setText("Erro: " + e.getMessage());
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
        } 
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
