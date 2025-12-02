package controller;

import classes.IntegranteFamilia;
import classes.Usuario;
import service.IntegranteFamiliaService;
import util.BusinessException;
import util.DBException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

public class CadastroParenteController implements Initializable {

    // --- FXML FIELDS ---
    @FXML
    private TextField txtNome;
    @FXML
    private ComboBox<String> comboTipoParente;
    @FXML
    private TextField txtCpf;
    @FXML
    private CheckBox chkVinculoAfetivo;
    @FXML
    private TextField txtOcupacao;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TextField txtTelefone;
    @FXML
    private CheckBox chkResponsavelLegal;
    @FXML
    private CheckBox chkPessoaAutorizada;
    @FXML
    private TextArea txtAnotacoes;

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnExcluir;

    // --- TABELA E COLUNAS (Para carregar os parentes) ---
    @FXML
    private TableView<IntegranteFamilia> tabelaIntegrantes;
    @FXML
    private TableColumn<IntegranteFamilia, String> colNome;
    @FXML
    private TableColumn<IntegranteFamilia, String> colParentesco;
    @FXML
    private TableColumn<IntegranteFamilia, Boolean> colRespLegal;
    @FXML
    private TableColumn<IntegranteFamilia, Boolean> colPessoaAutorizada;
    @FXML
    private TableColumn<IntegranteFamilia, String> colCPF;
    @FXML
    private TableColumn<IntegranteFamilia, String> colTelefone;
    @FXML
    private TableColumn<IntegranteFamilia, String> colEndereco;
   

    // --- SERVIÇOS E DADOS ---
    private IntegranteFamiliaService integranteService = new IntegranteFamiliaService();
    private int familiaId = 0;

    // Rastreia o objeto selecionado para saber se é UPDATE ou INSERT
    private IntegranteFamilia integranteSelecionado;

    // -------------------------------------------------------------------------
    // SETUP E INICIALIZAÇÃO
    // -------------------------------------------------------------------------
    public void setFamiliaId(int familiaId) {
        this.familiaId = familiaId;
        if (familiaId > 0) {
            carregarIntegrantes();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColunas();

        btnSalvar.setOnAction(event -> salvar());
        btnExcluir.setOnAction(event -> handleExcluir());

        // Listener para carregamento dos dados no formulário (Edição/Clique)
        tabelaIntegrantes.setOnMouseClicked(event -> {
            // Clique simples para seleção/edição
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                IntegranteFamilia integrante = tabelaIntegrantes.getSelectionModel().getSelectedItem();
                if (integrante != null) {
                    preencherCampos(integrante);
                }
            }
        });

        comboTipoParente.getItems().addAll(
                "Mãe", "Pai", "Avó", "Avô", "Tia", "Tio", "Irmã", "Irmão",
                "Responsável", "Outro"
        );
    }

    private void configurarColunas() {
        // Usar o nome da propriedade padronizado
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colParentesco.setCellValueFactory(new PropertyValueFactory<>("parentesco"));
        colRespLegal.setCellValueFactory(new PropertyValueFactory<>("responsavelLegal"));
        colPessoaAutorizada.setCellValueFactory(new PropertyValueFactory<>("pessoaAutorizada"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        // Formatação do Booleano (True/False para Sim/Não)
        colRespLegal.setCellFactory(column -> {
            return new TableCell<IntegranteFamilia, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "Sim" : "Não"); // Formatação para Sim/Não
                    }
                }
            };
        });
        colPessoaAutorizada.setCellFactory(column -> new TableCell<IntegranteFamilia, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : (item ? "Sim" : "Não"));
            }
        });
    }

    // -------------------------------------------------------------------------
    // LÓGICA DE CARREGAMENTO E EDIÇÃO
    // ----------------------------------------------------------------------
    private void carregarIntegrantes() {
        try {
            if (familiaId <= 0) {
                return;
            }

            Usuario usuarioLogado = LoginController.getUsuarioLogado();

            List<IntegranteFamilia> lista = integranteService.listarPorFamiliaId(familiaId, usuarioLogado);

            ObservableList<IntegranteFamilia> observableList = FXCollections.observableArrayList(lista);
            tabelaIntegrantes.setItems(observableList);

        } catch (BusinessException | DBException e) {
            exibirAlerta(AlertType.ERROR, "Erro ao Carregar Tabela", e.getMessage());
        }
    }

    /**
     * Preenche os campos do formulário para edição ao clicar em uma linha.
     */
    private void preencherCampos(IntegranteFamilia integrante) {
        // Armazena a referência para a operação de UPDATE
        this.integranteSelecionado = integrante;

        txtNome.setText(integrante.getNome());
        txtCpf.setText(integrante.getCpf());
        txtTelefone.setText(integrante.getTelefone());

        comboTipoParente.setValue(integrante.getParentesco());
        txtOcupacao.setText(integrante.getOcupacao());
        txtEndereco.setText(integrante.getEndereco());
        txtAnotacoes.setText(integrante.getAnotacoes());

        // LÓGICA DE LIMPEZA PARA FALSE: Se o booleano for FALSE, o campo de texto fica vazio.
        chkVinculoAfetivo.setSelected(integrante.isVinculoAfetivo());
        chkResponsavelLegal.setSelected(integrante.isResponsavelLegal());
        chkPessoaAutorizada.setSelected(integrante.isPessoaAutorizada());
    }

    // -------------------------------------------------------------------------
    // LÓGICA DE PERSISTÊNCIA E AÇÕES
    // -------------------------------------------------------------------------
    // Método que o FXML Loader procurou:
    @FXML
    public void limparCamposParente() {
        // Chama o método auxiliar que limpa o formulário e a referência de edição
        limparCampos();
    }

    private void limparCampos() {
        this.integranteSelecionado = null; // Remove a referência
        txtNome.clear();
        comboTipoParente.setValue(null);
        txtCpf.clear();
        chkVinculoAfetivo.setSelected(false);
        txtOcupacao.clear();
        txtEndereco.clear();
        txtTelefone.clear();
        chkResponsavelLegal.setSelected(false);
        chkPessoaAutorizada.setSelected(false);
        txtAnotacoes.clear();

        tabelaIntegrantes.getSelectionModel().clearSelection(); // Desseleciona a linha
    }

    private void handleExcluir() {
        // Lógica de exclusão
        IntegranteFamilia integranteSelecionado = tabelaIntegrantes.getSelectionModel().getSelectedItem();

        if (integranteSelecionado == null) {
            exibirAlerta(AlertType.WARNING, "Atenção", "Selecione um parente para excluir.");
            return;
        }

        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir: " + integranteSelecionado.getNome());
        confirmacao.setContentText("Tens certeza que desejas excluir este parente?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                Usuario usuarioLogado = LoginController.getUsuarioLogado();
                integranteService.excluir(integranteSelecionado.getId(), usuarioLogado);
                exibirAlerta(AlertType.INFORMATION, "Sucesso", "Parente excluído.");
                carregarIntegrantes(); // Recarrega a tabela
            } catch (BusinessException | DBException e) {
                exibirAlerta(AlertType.ERROR, "Falha na Exclusão", e.getMessage());
            }
        }
    }

    private void salvar() {

        // Decidir se é INSERÇÃO ou ATUALIZAÇÃO, usando a referência guardada
        IntegranteFamilia integrante = integranteSelecionado;

        if (integrante == null) {
            
            integrante = new IntegranteFamilia();
            integrante.setFkCodFamilia(this.familiaId);
        }

        try {
            // Mapeamento e Validação
            integrante.setNome(txtNome.getText());
            integrante.setCpf(txtCpf.getText());
            integrante.setTelefone(txtTelefone.getText());

            integrante.setParentesco(comboTipoParente.getValue());
            integrante.setOcupacao(txtOcupacao.getText());
            integrante.setEndereco(txtEndereco.getText());
            integrante.setAnotacoes(txtAnotacoes.getText());

            // Mapeamento dos Booleanos (Sim/Não para TRUE/FALSE)
            integrante.setVinculoAfetivo(chkVinculoAfetivo.isSelected());
            integrante.setResponsavelLegal(chkResponsavelLegal.isSelected());
            integrante.setPessoaAutorizada(chkPessoaAutorizada.isSelected());

            Usuario usuarioLogado = LoginController.getUsuarioLogado();

            // Chamar o serviço de acordo com a operação
            String mensagem;
            if (integrante.getId() == 0) {
                // Novoparente
                integranteService.inserir(integrante, usuarioLogado);
                mensagem = "Parente adicionado com sucesso!";
            } else {
                // Atualização de paremte existente
                integranteService.atualizar(integrante, usuarioLogado);
                mensagem = "Parente atualizado com sucesso!";
            }

            exibirAlerta(AlertType.INFORMATION, "Sucesso", mensagem);

            limparCampos(); // Limpa o formulário
            carregarIntegrantes(); // Recarrega a tabela para mostrar o resultado

        } catch (BusinessException | DBException e) {
            exibirAlerta(AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    private void exibirAlerta(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
