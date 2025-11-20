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
    private void handleLogin() throws IOException {
        String nome = txtNomeUsuario.getText();
        String senha = txtSenha.getText();

        lblErro.setVisible(false);

        try {
            System.out.println("DEBUG: 1. Tentativa de autenticação para: " + nome);
            usuarioLogado = administradorService.login(nome, senha);
            System.out.println("DEBUG: 2. Login BEM-SUCEDIDO.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/simpa/TelaConsulta.fxml"));
            Parent root = loader.load();

            ConsultaController consultaController = loader.getController();

            consultaController.setUsuarioLogado(usuarioLogado);

            System.out.println("DEBUG: 3. Chamando App.setRoot('TelaConsulta')...");
            App.setRoot("TelaConsulta");

            System.out.println("DEBUG: 4. Stage de Consulta carregado. Fechando Login.");

            Stage stageAtual = (Stage) txtNomeUsuario.getScene().getWindow();
            stageAtual.close();

        } catch (BusinessException e) {
            System.out.println("DEBUG: Falha na autenticação (BusinessException).");
            lblErro.setText(e.getMessage());
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);

        } catch (DBException e) {
            System.out.println("DEBUG: Falha de DB.");
            lblErro.setText("Erro de sistema: Falha na comunicação com o banco de dados.");
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
            e.printStackTrace();
        }
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
