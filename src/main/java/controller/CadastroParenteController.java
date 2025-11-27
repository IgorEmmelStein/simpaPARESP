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
    @FXML private Button btnExcluir; 
    
    // --- TABELA E COLUNAS (Para carregar os parentes) ---
    @FXML private TableView<IntegranteFamilia> tabelaIntegrantes;
    @FXML private TableColumn<IntegranteFamilia, String> colNome;
    @FXML private TableColumn<IntegranteFamilia, String> colParentesco;
    @FXML private TableColumn<IntegranteFamilia, Boolean> colRespLegal; 
    @FXML private TableColumn<IntegranteFamilia, String> colCPF;
    // NOTE: Se houver um botão "Limpar Campos" no FXML, ele deve ter onAction="#limparCamposParente"
    
    // --- SERVIÇOS E DADOS ---
    private IntegranteFamiliaService integranteService = new IntegranteFamiliaService();
    private int familiaId = 0;
    
    // NOVO: Rastreia o objeto selecionado para saber se é UPDATE ou INSERT
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
        // CORREÇÃO FINAL: Usar o nome da propriedade padronizado
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colParentesco.setCellValueFactory(new PropertyValueFactory<>("parentesco"));
        colRespLegal.setCellValueFactory(new PropertyValueFactory<>("responsavelLegal")); // Propriedade Padronizada
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf")); 
        
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
    }

    // -------------------------------------------------------------------------
    // LÓGICA DE CARREGAMENTO E EDIÇÃO
    // -------------------------------------------------------------------------
    
    private void carregarIntegrantes() {
        try {
            if (familiaId <= 0) return;
            
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
        // ESSENCIAL: Armazena a referência para a operação de UPDATE
        this.integranteSelecionado = integrante; 
        
        txtNome.setText(integrante.getNome());
        txtCpf.setText(integrante.getCpf());
        txtTelefone.setText(integrante.getTelefone());
        
        comboTipoParente.setValue(integrante.getParentesco());
        txtOcupacao.setText(integrante.getOcupacao());
        txtEndereco.setText(integrante.getEndereco());
        txtAnotacoes.setText(integrante.getAnotacoes());

        // LÓGICA DE LIMPEZA PARA FALSE: Se o booleano for FALSE, o campo de texto fica vazio.
        txtVinculoAfetivo.setText(integrante.isVinculoAfetivo() ? "Sim" : "");
        txtResponsavelLegal.setText(integrante.isResponsavelLegal() ? "Sim" : "");
        txtPessoaAutorizada.setText(integrante.isPessoaAutorizada() ? "Sim" : "");
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
        txtVinculoAfetivo.clear();
        txtOcupacao.clear();
        txtEndereco.clear();
        txtTelefone.clear();
        txtResponsavelLegal.clear();
        txtPessoaAutorizada.clear();
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
        
        // 1. Decidir se é INSERÇÃO ou ATUALIZAÇÃO, usando a referência guardada
        IntegranteFamilia integrante = integranteSelecionado;
        
        if (integrante == null) {
            // É um novo parente: cria o objeto e seta o FK
            integrante = new IntegranteFamilia();
            integrante.setFkCodFamilia(this.familiaId);
        }

        try {
            // 2. Mapeamento e Validação
            integrante.setNome(txtNome.getText());
            integrante.setCpf(txtCpf.getText());
            integrante.setTelefone(txtTelefone.getText());

            integrante.setParentesco(comboTipoParente.getValue());
            integrante.setOcupacao(txtOcupacao.getText());
            integrante.setEndereco(txtEndereco.getText());
            integrante.setAnotacoes(txtAnotacoes.getText());

            // 3. Mapeamento dos Booleanos (Sim/Não para TRUE/FALSE)
            integrante.setVinculoAfetivo("Sim".equalsIgnoreCase(txtVinculoAfetivo.getText()));
            integrante.setResponsavelLegal("Sim".equalsIgnoreCase(txtResponsavelLegal.getText()));
            integrante.setPessoaAutorizada("Sim".equalsIgnoreCase(txtPessoaAutorizada.getText()));

            Usuario usuarioLogado = LoginController.getUsuarioLogado();

            // 4. Chamar o serviço de acordo com a operação
            String mensagem;
            if (integrante.getId() == 0) {
                // Novo parente
                integranteService.inserir(integrante, usuarioLogado);
                mensagem = "Parente adicionado com sucesso!";
            } else {
                // Atualização de parente existente
                integranteService.atualizar(integrante, usuarioLogado);
                mensagem = "Parente atualizado com sucesso!";
            }

            exibirAlerta(AlertType.INFORMATION, "Sucesso", mensagem);
            
            limparCampos(); // Limpa o formulário e a referência ao integrante selecionado
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