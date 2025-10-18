/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import classes.Usuario;
import javafx.scene.control.Label; 
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import service.AdministradorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import util.BusinessException;
import util.DBException;

public class LoginController {

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
    private void handleLogin() {
        String nome = txtNomeUsuario.getText(); // Usamos o nome como login
        String senha = txtSenha.getText();
        
        lblErro.setVisible(false); // <--- O código correto

        try {
            // Chama a lógica de negócio na camada Service
            usuarioLogado = administradorService.login(nome, senha);
            
            // Se o login for bem-sucedido:
            //troca de tela..
            // 1. Mensagem de Sucesso (Opcional)
            lblErro.setText("Login bem-sucedido!");
            lblErro.setTextFill(Color.GREEN);
            lblErro.setVisible(true);
            
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
