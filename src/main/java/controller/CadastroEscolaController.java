/*
 * Click nbfs:
 * Click nbfs:
 */
package controller;

import classes.Escola;
import java.net.URL;
import util.DBException; 
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

/**
 * FXML Controller class
 *
 * @author Tiago
 */
public class CadastroEscolaController implements Initializable {

    @FXML
    private Button criarButton;

    @FXML
    private TextField nomeTextField;

    private EscolaService escolaService = new EscolaService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        criarButton.setOnAction(event -> {
            handleCriarButtonAction(event);
        });
    }

    private void handleCriarButtonAction(ActionEvent event) {
        String nomeEscola = nomeTextField.getText();

        try {

            if (nomeEscola == null || nomeEscola.trim().isEmpty()) {
                throw new BusinessException("O nome da escola não pode ser vazio.");
            }

            Escola novaEscola = new Escola();

            novaEscola.setId(new Random().nextInt(1000) + 2);

            novaEscola.setNome(nomeEscola);

            novaEscola.setSerie("");

            escolaService.salvar(novaEscola);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Sucesso!");
            alert.setContentText("A escola '" + nomeEscola + "' foi cadastrada.");
            alert.showAndWait();

            nomeTextField.clear();

        } catch (BusinessException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Erro de Validação");
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
