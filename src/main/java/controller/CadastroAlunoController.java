package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import classes.Escola; 
import java.util.List;
import java.util.Optional;
import service.EscolaService; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import service.AlunoService;
import util.DBException;

public class CadastroAlunoController implements Initializable {

    @FXML
    private Button acessarAnexosButton;

    @FXML
    private Button adicionarDetalhesButton;

    @FXML
    private Button adicionarSaudeButton;

    @FXML
    private Button adicionarParenteButon;

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
    
    private AlunoService alunoService = new AlunoService();
    private EscolaService escolaService = new EscolaService(); // NOVO SERVIÇO
    private List<Escola> listaEscolas;


    // -------------------------------------------------------------------------
    // MÉTODO CHAMADO AO INICIAR A TELA
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        configurarEventos();
        carregarEscolas();
    }
    
    private void carregarEscolas() {
        try {
            // 1. Obtém a lista de objetos Escola do serviço
            listaEscolas = escolaService.listarTodas(); 
            
            // 2. Converte para lista de Strings para o ComboBox
            ObservableList<String> nomesEscolas = FXCollections.observableArrayList();
            for (Escola escola : listaEscolas) {
                // NOTA: Para exibir apenas o nome, o toString() da classe Escola deve retornar apenas o nome.
                nomesEscolas.add(escola.toString()); 
            }
            
            // 3. Define os itens no ComboBox
            escolaComboBox.setItems(nomesEscolas);
            
        } catch (DBException e) {
            System.err.println("Erro ao carregar escolas do banco de dados: " + e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Carga");
            alert.setHeaderText("Falha ao carregar escolas");
            alert.setContentText("Não foi possível carregar a lista de escolas do sistema.");
            alert.showAndWait();
        }
    }
    
    private Optional<Escola> getEscolaSelecionada(String nomeEscola) {
        if (listaEscolas == null || nomeEscola == null) return Optional.empty();
        return listaEscolas.stream()
                .filter(e -> e.toString().equals(nomeEscola))
                .findFirst();
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

    private void abrirTela(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/simpa/" + fxml));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir a tela: " + fxml);
        }
    }
    
    private void configurarEventos() {

        
        salvarButton.setOnAction(event -> salvarAluno());

        adicionarDetalhesButton.setOnAction(event -> {
            abrirTela("TelaCadastroFamilia.fxml");   // tela de detalhes
        });

        adicionarSaudeButton.setOnAction(event -> {
            abrirTela("TelaCadastroSaude.fxml");
        });

        adicionarParenteButon.setOnAction(event -> {
            abrirTela("TelaCadastroParente.fxml");
        });

        acessarAnexosButton.setOnAction(event -> {
            // ignorado conforme solicitado
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
