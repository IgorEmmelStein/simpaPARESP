package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class CadastroFamiliaController implements Initializable {

    // -------------------------
    // CAMPOS DO FXML
    // -------------------------

    @FXML
    private TextField integrantesTextField;

    @FXML
    private TextField rendaTextField;

    @FXML
    private ComboBox<String> bolsaFamiliaComboBox;

    @FXML
    private TextField enderecoTextField;

    @FXML
    private TextField bairroTextField;

    @FXML
    private TextField logradouroTextField;

    @FXML
    private TextField aluguelTextField;

    @FXML
    private TextField telefoneTextField;

    @FXML
    private TextArea anotacoesTextArea;

    @FXML
    private Button salvarButton;


    // -------------------------
    // MÉTODO INITIALIZE
    // -------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        carregarCombos();

        salvarButton.setOnAction(event -> salvarFamilia());
    }


    // -------------------------
    // CARREGA ITEMS DO COMBOBOX
    // -------------------------
    private void carregarCombos() {
        bolsaFamiliaComboBox.getItems().addAll(
                "Sim",
                "Não"
        );
    }


    // -------------------------
    // SALVAR FAMÍLIA (MÉTODO PRINCIPAL)
    // -------------------------
    private void salvarFamilia() {

        if (!validarCampos()) {
            System.out.println("⚠ Preencha todos os campos obrigatórios!");
            return;
        }

        // Aqui você montaria um objeto da classe Família
        // Exemplo (só ilustrativo):
        //
        // Familia f = new Familia();
        // f.setIntegrantes(Integer.parseInt(integrantesTextField.getText()));
        // f.setRenda(Double.parseDouble(rendaTextField.getText()));
        // f.setBolsaFamilia(bolsaFamiliaComboBox.getValue());
        // ...
        //
        // new FamiliaDAO().salvar(f);

        System.out.println("✔ Família salva com sucesso!");
        limparCampos();
    }


    // -------------------------
    // VALIDA CAMPOS
    // -------------------------
    private boolean validarCampos() {

        return !(integrantesTextField.getText().isEmpty()
                || rendaTextField.getText().isEmpty()
                || bolsaFamiliaComboBox.getValue() == null
                || enderecoTextField.getText().isEmpty()
                || bairroTextField.getText().isEmpty()
                || logradouroTextField.getText().isEmpty()
                || aluguelTextField.getText().isEmpty()
                || telefoneTextField.getText().isEmpty());
    }


    // -------------------------
    // LIMPAR CAMPOS APÓS SALVAR
    // -------------------------
    private void limparCampos() {
        integrantesTextField.clear();
        rendaTextField.clear();
        bolsaFamiliaComboBox.setValue(null);
        enderecoTextField.clear();
        bairroTextField.clear();
        logradouroTextField.clear();
        aluguelTextField.clear();
        telefoneTextField.clear();
        anotacoesTextArea.clear();
    }

}

