package controller;

import classes.Aluno;
import classes.Usuario;
import classes.Administrador;
import java.io.File;
import java.io.IOException;
import service.AlunoService;
import service.RelatorioService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.TableCell;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.BusinessException;

/**
 * Controller responsável pela lógica da tela de Consulta/Listagem de Alunos
 * (Tela 4). Versão mínima para garantir a abertura da tela.
 */
public class ConsultaController implements Initializable {

    private Aluno alunoEditando;

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
    private TableColumn<Aluno, java.util.Date> colDtAcolhimento;
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
    private Button btnExportarCompleto;
    @FXML
    private Button btnAddEscola;
    @FXML
    private Button btnAddUser;
    
    @FXML 
    private Label lblBoasVindas;

    private Usuario usuarioLogado;

    private AlunoService alunoService;
    private RelatorioService relatorioService;

    public ConsultaController() {
        this.alunoService = new AlunoService();
        this.relatorioService = new RelatorioService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        configurarColunas();

        carregarAlunos(null);

        tabelaAlunos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Aluno alunoSelecionado = tabelaAlunos.getSelectionModel().getSelectedItem();
                if (alunoSelecionado != null) {
                    abrirTelaCadastroAluno(alunoSelecionado);
                }
            }
        });

        txtPesquisar.textProperty().addListener((obs, oldValue, newValue) -> {
            carregarAlunos(newValue);
        });

        btnFiltrar.setVisible(false);
        btnFiltrar.setManaged(false);

        btnAddUser.setVisible(false);
        btnAddUser.setManaged(false);
        
        Usuario usuario = LoginController.getUsuarioLogado();
        if (usuario != null && lblBoasVindas != null) {
            // Pega o nome e coloca na tela
            lblBoasVindas.setText("Olá, " + usuario.getNomeUsuario() + "!");
        }

        aplicarPermissoes();

    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        verificarPermissoes();
    }

    private void verificarPermissoes() {

        if (this.usuarioLogado instanceof Administrador) {
            btnAddUser.setVisible(true);
            btnAddUser.setManaged(true);
        } else {

            btnAddUser.setVisible(false);
            btnAddUser.setManaged(false);
        }
    }

    private void configurarColunas() {

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        colDtAcolhimento.setCellValueFactory(new PropertyValueFactory<>("dataAcolhimento"));
        configurarFormatoData(colDtAcolhimento);

        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colVacinacao.setCellValueFactory(new PropertyValueFactory<>("vacinacaoEmDia"));
        colTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
    }

    private void configurarFormatoData(TableColumn<Aluno, java.util.Date> coluna) {

        SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd/MM/yyyy");

        coluna.setCellFactory(column -> {
            return new TableCell<Aluno, Date>() {
                @Override
                protected void updateItem(Date data, boolean empty) {
                    super.updateItem(data, empty);

                    if (empty || data == null) {
                        setText(null);
                    } else {

                        setText(formatoBrasileiro.format(data));
                    }
                }
            };
        });
    }

    private void carregarAlunos(String criterio) {

        try {
            List<Aluno> listaAlunos;

            if (criterio == null || criterio.trim().isEmpty()) {

                listaAlunos = alunoService.listarTodosAlunos();
            } else {

                listaAlunos = alunoService.buscarAlunosPorCriterio(criterio);
            }

            ObservableList<Aluno> observableList = FXCollections.observableArrayList(listaAlunos);
            tabelaAlunos.setItems(observableList);

            System.out.println("DEBUG: Carregados " + listaAlunos.size() + " alunos com o critério: " + criterio);

        } catch (Exception e) {
            System.err.println("ERRO FATAL ao carregar alunos. Verifique a conexão com o DB e o mapeamento no DAO.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleIncluir() {

        abrirNovaTela("TelaCadastroAluno", "Cadastro de Novo Aluno");

        carregarAlunos(txtPesquisar.getText());
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
    private void handleInativar() {

        Aluno alunoSelecionado = tabelaAlunos.getSelectionModel().getSelectedItem();

        if (alunoSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum Aluno Selecionado");
            alert.setContentText("Por favor, selecione um aluno na tabela para inativar.");
            alert.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Inativação");
        confirmacao.setHeaderText("Inativar Aluno: " + alunoSelecionado.getNome());
        confirmacao.setContentText("Tens certeza que desejas inativar este aluno? Esta ação não pode ser desfeita facilmente.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                Usuario usuarioLogado = LoginController.getUsuarioLogado();

                if (usuarioLogado == null) {
                    throw new BusinessException("Usuário não autenticado.");
                }

                alunoService.inativarAluno(alunoSelecionado.getId(), usuarioLogado);

                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Sucesso");
                sucesso.setHeaderText(null);
                sucesso.setContentText("Aluno '" + alunoSelecionado.getNome() + "' inativado com sucesso.");
                sucesso.showAndWait();

                carregarAlunos(txtPesquisar.getText());

            } catch (BusinessException e) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro de Permissão/Negócio");
                erro.setHeaderText("Falha na Inativação");
                erro.setContentText(e.getMessage());
                erro.showAndWait();
            } catch (Exception e) {
                System.err.println("Erro inesperado ao inativar aluno: " + e.getMessage());
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro de Sistema");
                erro.setHeaderText("Erro Inesperado");
                erro.setContentText("Ocorreu um erro ao comunicar com o sistema.");
                erro.showAndWait();
            }
        }

    }

    @FXML
    private void handleExcluir() {
        System.out.println("Excluir (Vazio)");
    }

    @FXML
    private void handleFiltrar() {
        carregarAlunos(txtPesquisar.getText());
    }

    @FXML
    private void handleExportar() {
        try {
            String filtro = txtPesquisar.getText();

            Usuario usuario = LoginController.getUsuarioLogado();

            List<Aluno> alunos = relatorioService.gerarRelatorioComFiltros(filtro, usuario);

            File pdf = relatorioService.exportarDados(alunos, "PDF");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Gerado");
            alert.setHeaderText("Relatório gerado com sucesso!");
            alert.setContentText("Arquivo salvo como: " + pdf.getAbsolutePath());
            alert.showAndWait();

            java.awt.Desktop.getDesktop().open(pdf);

        } catch (BusinessException e) {

            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Falha ao exportar");
            erro.setContentText(e.getMessage());
            erro.showAndWait();

        } catch (Exception e) {

            e.printStackTrace();
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Erro inesperado");
            erro.setContentText("Erro ao gerar o relatório.");
            erro.showAndWait();
        }
    }

    @FXML
    private void handleExportarCompleto() {

        Aluno aluno = tabelaAlunos.getSelectionModel().getSelectedItem();

        if (aluno == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum aluno selecionado");
            alert.setContentText("Selecione um aluno na tabela para exportar.");
            alert.show();
            return;
        }

        try {
            File pdf = relatorioService.exportarAlunoCompleto(aluno);
            java.awt.Desktop.getDesktop().open(pdf);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEscola() {
        abrirNovaTela("TelaCadastroEscola", "Cadastrar Nova Escola");
    }

    private void abrirTelaCadastroAluno(Aluno aluno) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/simpa/TelaCadastroAluno.fxml"));
            Parent root = loader.load();

            // ESSENCIAL: Passar o objeto Aluno para o Controller da tela de cadastro
            CadastroAlunoController controller = loader.getController();
            controller.setAlunoEmEdicao(aluno); // Vamos criar este método no Controller

            Stage stage = new Stage();
            stage.setTitle("Edição de Aluno: " + aluno.getNome());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recarrega a tabela após a edição/fechamento
            carregarAlunos(txtPesquisar.getText());

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de Edição de Aluno.");
            e.printStackTrace();
        }
    }

    private void aplicarPermissoes() {
        Usuario usuario = LoginController.getUsuarioLogado();

        // se não tiver usuário logado, bloqueia tudo
        if (usuario == null) {
            btnExcluirAluno.setDisable(true);
            btnAddUser.setVisible(false);
            btnAddEscola.setVisible(false);
            return;
        }

//        if (usuario instanceof classes.Administrador) {
//            classes.Administrador admin = (classes.Administrador) usuario;
//
//            // verifica se este usuário tem a flag isAdmin ativada
//            boolean permissao = admin.isAdmin();
//
//            btnExcluirAluno.setDisable(!permissao);
//            btnAddUser.setVisible(permissao);
//            btnAddUser.setManaged(permissao);
//            btnAddEscola.setVisible(permissao);
//            btnAddEscola.setManaged(permissao);
//
//        }
        if (usuario instanceof classes.Administrador) {
            classes.Administrador admin = (classes.Administrador) usuario;
            
            boolean isAdmin = admin.isAdmin();
            boolean isSaude = admin.isSaude();
            boolean isSocial = admin.isSocial();
            
            if(isAdmin){
                btnExcluirAluno.setDisable(false);
                
                btnAddUser.setVisible(true);
                btnAddUser.setManaged(true);
                
                btnAddEscola.setVisible(true);
                btnAddEscola.setManaged(true);
            }else{
                btnExcluirAluno.setDisable(true); 
                
                btnAddUser.setVisible(false);     
                btnAddUser.setManaged(false);     
                
                btnAddEscola.setVisible(false);   
                btnAddEscola.setManaged(false);
            }
            
            boolean temAcessoOperacional = isAdmin || isSaude || isSocial;

            if (temAcessoOperacional) {
                btnInserirAluno.setDisable(false);
                btnExportar.setDisable(false);
                if (btnExportarCompleto != null) btnExportarCompleto.setDisable(false);

            } else {

                btnInserirAluno.setDisable(true);
                btnExportar.setDisable(true);
                if (btnExportarCompleto != null) btnExportarCompleto.setDisable(true);
            }
        }
    }
}
