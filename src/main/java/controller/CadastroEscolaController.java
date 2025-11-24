/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import classes.Escola;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import service.EscolaService;
import util.BusinessException;
import util.DBException;
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
    
    private EscolaService escolaService = new EscolaService();

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

        try {
            // 1. Validação de campo (Controller)
            if (nomeEscola == null || nomeEscola.trim().isEmpty()) {
                throw new BusinessException("Por favor, preencha o nome da escola.");
            }
            
            // 2. Mapeamento e Geração de ID
            Escola novaEscola = new Escola();
            // CUIDADO: ID deve ser gerado manualmente e ser único!
            // Usamos Random para gerar um ID (Melhoria futura: buscar o último ID no DB)
            novaEscola.setId(new Random().nextInt(1000) + 5); 
            
            novaEscola.setNome(nomeEscola);
            // Série não está na tela, mas é NOT NULL. Setamos um valor padrão seguro.
            novaEscola.setSerie("N/A"); 

            // 3. Chama o Service para salvar
            escolaService.salvar(novaEscola); 

            // 4. Sucesso
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Sucesso!");
            alert.setContentText("A escola '" + nomeEscola + "' foi cadastrada.");
            alert.showAndWait();
            
            nomeTextField.clear();

        } catch (BusinessException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Erro de Validação/Negócio");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            
        } catch (DBException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Banco de Dados");
            alert.setHeaderText("Falha ao Inserir Escola");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
}