package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class CadastroSaudeController implements Initializable {

    // Variáveis do "modelo"
    private String medicamentosUso;
    private String alergias;
    private String observacoesMedicas;

    // Componentes do FXML
    @FXML private TextArea txtMedicamentos;
    @FXML private TextArea txtAlergias;
    @FXML private TextArea txtObsMedicas;
    @FXML private Button salvarButton;   // novo id do botão

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Evento do botão configurado no controller
        salvarButton.setOnAction(event -> salvar());
    }

    private void salvar() {

        medicamentosUso = txtMedicamentos.getText();
        alergias = txtAlergias.getText();
        observacoesMedicas = txtObsMedicas.getText();

        // Aqui você conecta ao DAO ou salva no banco
        System.out.println("=== Dados de Saúde Salvos ===");
        System.out.println("Medicamentos em uso: " + medicamentosUso);
        System.out.println("Alergias: " + alergias);
        System.out.println("Observações médicas: " + observacoesMedicas);
    }

   
}
