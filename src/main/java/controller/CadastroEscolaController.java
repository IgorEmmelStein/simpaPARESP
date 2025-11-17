/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
// ----------------------------

/**
 * FXML Controller class
 *
 * @author Tiago
 */
public class CadastroEscolaController implements Initializable {

    // --- Componentes Injetados pelo FXML ---
    
    @FXML
    private Button criarButton;

    @FXML
    private TextField nomeTextField;

    // --- Métodos ---
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Define a ação do botão "Criar"
        criarButton.setOnAction(event -> {
            handleCriarButtonAction(event);
        });
    }    
    
    /**
     * Método chamado quando o botão 'criarButton' é pressionado.
     */
    private void handleCriarButtonAction(ActionEvent event) {
        String nomeEscola = nomeTextField.getText();

        // Verifica se o campo está vazio (após remover espaços em branco)
        if (nomeEscola == null || nomeEscola.trim().isEmpty()) {
            
            // --- ALERTA DE CAMPO VAZIO ---
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Campo Obrigatório");
            alert.setContentText("Por favor, preencha o nome da escola.");
            
            // Exibe o alerta e espera o usuário fechá-lo
            alert.showAndWait();
            // --------------------------------
            
        } else {
            
            // Se o campo estiver preenchido, prossiga com a lógica
            System.out.println("Tentando criar escola com o nome: " + nomeEscola);
            
            // TODO: Adicione sua lógica para salvar a nova escola no banco de dados
            // Ex: EscolaDAO.salvar(new Escola(nomeEscola));
            
            // --- ALERTA DE SUCESSO (Opcional, mas recomendado) ---
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Sucesso!");
            alert.setContentText("A escola '" + nomeEscola + "' foi cadastrada.");
            alert.showAndWait();
            
            // Opcional: Limpar o campo após o sucesso
            nomeTextField.clear();
            // ----------------------------------------------------
        }
    }
}