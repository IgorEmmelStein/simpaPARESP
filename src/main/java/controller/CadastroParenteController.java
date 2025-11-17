package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CadastroParenteController implements Initializable {

    // FXML fields
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> comboTipoParente;
    @FXML private TextField txtCpf;
    @FXML private TextField txtVinculoAfetivo;
    @FXML private TextField txtOcupacao;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtResponsavelLegal;
    @FXML private TextField txtPessoaAutorizada;
    @FXML private TextArea txtAnotacoes;

    @FXML private Button btnSalvar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Preenchendo o combo
        comboTipoParente.getItems().addAll(
                "Mãe", "Pai", "Avó", "Avô", "Tia", "Tio", "Irmã", "Irmão",
                "Responsável", "Outro"
        );

        // Ação do botão Salvar
        btnSalvar.setOnAction(event -> salvar());
    }

    private void salvar() {

        System.out.println("=== Cadastro de Parente ===");
        System.out.println("Nome: " + txtNome.getText());
        System.out.println("Tipo: " + comboTipoParente.getValue());
        System.out.println("CPF: " + txtCpf.getText());
        System.out.println("Vínculo Afetivo: " + txtVinculoAfetivo.getText());
        System.out.println("Ocupação: " + txtOcupacao.getText());
        System.out.println("Endereço: " + txtEndereco.getText());
        System.out.println("Telefone: " + txtTelefone.getText());
        System.out.println("Responsável Legal: " + txtResponsavelLegal.getText());
        System.out.println("Pessoa Autorizada: " + txtPessoaAutorizada.getText());
        System.out.println("Notas: " + txtAnotacoes.getText());

        // A parte do DAO será implementada depois
    }
}
