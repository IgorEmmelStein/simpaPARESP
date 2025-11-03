/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import classes.Usuario;
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
    // O usuário logado será armazenado aqui após o sucesso
    private static Usuario usuarioLogado; 

    public LoginController() {
        // Instancia o serviço na inicialização do Controller
        this.administradorService = new AdministradorService();
    }
    
    /**
     * Ação disparada ao clicar no botão 'Entrar'.
     */
    @FXML
    private void handleLogin() throws IOException {
        String nome = txtNomeUsuario.getText(); // Usamos o nome como login
        String senha = txtSenha.getText();
        
        lblErro.setVisible(false); // <--- O código correto

        try {
            // Chama a lógica de negócio na camada Service
            usuarioLogado = administradorService.login(nome, senha);
            
            lblErro.setText("Login bem-sucedido!");
            lblErro.setTextFill(Color.GREEN);
            lblErro.setVisible(true);
            
            // Se o login for bem-sucedido:
            Parent parent = FXMLLoader.load(getClass().getResource("TelaConsulta.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Consulta de Clientes");
            stage.setScene(scene);
            stage.show();
                
            // 1. Mensagem de Sucesso (Opcional)
           
            
            // 2. Lógica de Navegação:
            // Troca de tela para a área de consulta/gestão (Tela 4)
            // ex: carregarTelaConsulta();
            
        } catch (BusinessException e) {
            // Erro de Regra de Negócio (ex: Usuário ou Senha inválida)
            lblErro.setText(e.getMessage());
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
            
        } catch (DBException e) {
            // Erro de Banco de Dados
            lblErro.setText("Erro de sistema: Falha na comunicação com o banco de dados.");
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
            e.printStackTrace();
        }
    }
    
    // Método estático para outros Controllers acessarem o usuário logado
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
