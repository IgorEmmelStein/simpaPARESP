package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CadastroAlunoController implements Initializable {

    @FXML
    private Button acessarAnexosButton;

    @FXML
    private Button adicionarDetalhesButton;

    @FXML
    private Button adicionarSaudeButton1;

    @FXML
    private Button adicionarSaudeButton2;

    @FXML
    private TextArea anotacoesTextArea;

    @FXML
    private TextField calcadoTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private DatePicker dataAcolhimentoPicker;

    @FXML
    private DatePicker dataNascimentoPicker;

    @FXML
    private ComboBox<String> escolaComboBox;

    @FXML
    private ComboBox<String> formaAcessoComboBox;

    @FXML
    private TextField idadeTextField;

    @FXML
    private TextField integrantesFamiliaTextField;

    @FXML
    private TextField nisTextField;

    @FXML
    private TextField nomeCompletoTextField;

    @FXML
    private ComboBox<String> projetoFeriasComboBox;

    @FXML
    private Button salvarButton;

    @FXML
    private TextField serieTextField;

    @FXML
    private ComboBox<String> sexoComboBox;

    @FXML
    private ComboBox<String> termoImagemComboBox;

    @FXML
    private TextField transporteTextField;

    @FXML
    private ComboBox<String> turmaComboBox;

    @FXML
    private ComboBox<String> turnoComboBox;

    @FXML
    private TextField vestuarioTextField;

    @FXML
    private ComboBox<String> vulnerabilidade1ComboBox;

    @FXML
    private ComboBox<String> vulnerabilidade2ComboBox;


    // -------------------------------------------------------------------------
    // MÉTODO CHAMADO AO INICIAR A TELA
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        configurarEventos();
    }


    // -------------------------------------------------------------------------
    // CARREGAR COMBOBOX (COMO ESCOLA, SEXO, TURNO ETC.)
    // -------------------------------------------------------------------------
    private void carregarCombos() {

        sexoComboBox.getItems().addAll(
                "Masculino",
                "Feminino",
                "Outro"
        );

        turnoComboBox.getItems().addAll(
                "Manhã",
                "Tarde",
                "Noite"
        );

        formaAcessoComboBox.getItems().addAll(
                "Demanda Espontânea",
                "Encaminhamento",
                "Busca Ativa"
        );

        escolaComboBox.getItems().addAll(
                "Municipal",
                "Estadual",
                "Particular"
        );

        projetoFeriasComboBox.getItems().addAll(
                "Sim",
                "Não"
        );

        termoImagemComboBox.getItems().addAll(
                "Autorizado",
                "Não Autorizado"
        );

        vulnerabilidade1ComboBox.getItems().addAll(
                "Baixa renda",
                "Violência doméstica",
                "Deficiência",
                "Trabalho infantil"
        );

        vulnerabilidade2ComboBox.getItems().addAll(
                "Baixa renda",
                "Violência doméstica",
                "Deficiência",
                "Trabalho infantil"
        );
    }


    // -------------------------------------------------------------------------
    // CONFIGURAR EVENTOS DOS BOTÕES
    // -------------------------------------------------------------------------
    private void configurarEventos() {

        salvarButton.setOnAction(event -> salvarAluno());

        adicionarDetalhesButton.setOnAction(event -> {
            // ação do botão
        });

        adicionarSaudeButton1.setOnAction(event -> {
            // ação do botão
        });

        adicionarSaudeButton2.setOnAction(event -> {
            // ação do botão
        });

        acessarAnexosButton.setOnAction(event -> {
            // abrir anexos
        });
    }


    // -------------------------------------------------------------------------
    // SALVAR ALUNO (AQUI SERÁ IMPLEMENTADO O DAO)
    // -------------------------------------------------------------------------
    private void salvarAluno() {

        String nome = nomeCompletoTextField.getText();
        String cpf = cpfTextField.getText();

        // exemplo de validação
        if (nome.isEmpty() || cpf.isEmpty()) {
            System.out.println("Preencha todos os campos obrigatórios.");
            return;
        }

        // Aqui você irá montar o objeto Aluno e enviar para o DAO
        System.out.println("Salvando aluno...");
    }

}
