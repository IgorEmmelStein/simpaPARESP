package controller;

import classes.Aluno;
import java.io.IOException;
import service.AlunoService;
import service.RelatorioService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller responsável pela lógica da tela de Consulta/Listagem de Alunos
 * (Tela 4). Versão mínima para garantir a abertura da tela.
 */
public class ConsultaController implements Initializable {

    @FXML
    private TextField txtPesquisar;
    @FXML
    private TableView<Aluno> tabelaAlunos;

    @FXML
    private TableColumn<Aluno, Integer> colID;
    @FXML
    private TableColumn<Aluno, String> colNome;
    @FXML
    private TableColumn<Aluno, String> colCPF;
    @FXML
    private TableColumn<Aluno, String> colDtAcolhimento;
    @FXML
    private TableColumn<Aluno, String> colTurno;
    @FXML
    private TableColumn<Aluno, Boolean> colVacinacao;
    @FXML
    private TableColumn<Aluno, String> colTurma;

    @FXML
    private Button btnExcluirAluno;
    @FXML
    private Button btnInserirAluno;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnExportar;
    @FXML
    private Button btnAddEscola;
    @FXML
    private Button btnAddUser;

    private AlunoService alunoService;
    private RelatorioService relatorioService;

    public ConsultaController() {
        this.alunoService = new AlunoService();
        this.relatorioService = new RelatorioService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        colDtAcolhimento.setCellValueFactory(new PropertyValueFactory<>("dataAcolhimento"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));

        colVacinacao.setCellValueFactory(new PropertyValueFactory<>("vacinacaoEmDia"));
        colTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));

        carregarAlunos();
    }

    private void carregarAlunos() {
        try {

            List<Aluno> listaAlunos = alunoService.listarTodosAlunos();

            ObservableList<Aluno> observableList = FXCollections.observableArrayList(listaAlunos);

            tabelaAlunos.setItems(observableList);

        } catch (Exception e) {

            System.err.println("ERRO ao carregar alunos para a tabela: " + e.getMessage());

        }
    }

    @FXML
    private void handleIncluir() {
        abrirNovaTela("TelaCadastroAluno", "Cadastro de Novo Aluno");
    }

    @FXML
    private void handleAddUser() {
        abrirNovaTela("TelaCadastroUsuario", "Cadastrar Novo Usuário");
    }

    private void abrirNovaTela(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/simpa/" + fxmlFileName + ".fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + fxmlFileName);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBuscar() {
        System.out.println("Buscar (Vazio)");
    }

    @FXML
    private void handleExcluir() {
        System.out.println("Excluir (Vazio)");
    }

    @FXML
    private void handleFiltrar() {
        System.out.println("Filtrar (Vazio)");
    }

    @FXML
    private void handleExportar() {
        System.out.println("Exportar (Vazio)");
    }

    @FXML
    private void handleAddEscola() {
        System.out.println("Add Escola (Vazio)");
    }
}
