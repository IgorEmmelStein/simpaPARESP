package controller;

import classes.Aluno;
import service.AlunoService;
import service.RelatorioService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsável pela lógica da tela de Consulta/Listagem de Alunos (Tela 4).
 * Versão mínima para garantir a abertura da tela.
 */
public class ConsultaController implements Initializable {

    // --- Componentes do FXML (Obrigatoriamente declarados para injeção) ---
    @FXML private TextField txtPesquisar; 
    @FXML private TableView<Aluno> tabelaAlunos;
    
    // As colunas que causavam o crash (devem ser declaradas)
    @FXML private TableColumn<Aluno, Integer> colID;
    @FXML private TableColumn<Aluno, String> colNome;
    @FXML private TableColumn<Aluno, String> colCPF;
    @FXML private TableColumn<Aluno, String> colDtAcolhimento; 
    @FXML private TableColumn<Aluno, String> colTurno;
    @FXML private TableColumn<Aluno, Boolean> colVacinacao;
    @FXML private TableColumn<Aluno, String> colTurma;
    
    // Botões (Ações 'onAction' do FXML)
    @FXML private Button btnExcluirAluno; 
    @FXML private Button btnInserirAluno;
    @FXML private Button btnFiltrar;
    @FXML private Button btnExportar;
    @FXML private Button btnAddEscola;
    @FXML private Button btnAddUser;
    
    // Services (Obrigatórios no construtor)
    private AlunoService alunoService;
    private RelatorioService relatorioService;
    
    public ConsultaController() {
        this.alunoService = new AlunoService();
        this.relatorioService = new RelatorioService();
    }
    
    // --- Inicialização Mínima: Não faz NADA (Elimina o NullPointerException) ---
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ISTO DEVE FICAR VAZIO AGORA!
    }

    // --- Métodos de Ação Mínimos para evitar LoadException ---
    @FXML private void handleBuscar() { System.out.println("Buscar (Vazio)"); }
    @FXML private void handleIncluir() { System.out.println("Incluir (Vazio)"); }
    @FXML private void handleExcluir() { System.out.println("Excluir (Vazio)"); }
    @FXML private void handleFiltrar() { System.out.println("Filtrar (Vazio)"); }
    @FXML private void handleExportar() { System.out.println("Exportar (Vazio)"); }
    @FXML private void handleAddEscola() { System.out.println("Add Escola (Vazio)"); }
    @FXML private void handleAddUser() { System.out.println("Add Usuário (Vazio)"); }
}