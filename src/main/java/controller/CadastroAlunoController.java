package controller;

import classes.Aluno;
import classes.Escola;
import classes.Usuario;
import service.AlunoService;
import service.EscolaService;
import util.BusinessException;
import util.DBException;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period; // Para calcular idade
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CadastroAlunoController implements Initializable {

    // --- Componentes FXML ---
    @FXML
    private Button acessarAnexosButton;
    @FXML
    private Button adicionarDetalhesButton;
    @FXML
    private Button adicionarSaudeButton1;
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

    // Assumindo que tu mudaste no FXML para ComboBox (melhor para validação)
    @FXML
    private ComboBox<String> serieComboBox;
    @FXML
    private ComboBox<String> transporteComboBox;

    @FXML
    private ComboBox<String> sexoComboBox;
    @FXML
    private ComboBox<String> termoImagemComboBox;
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

    // --- Serviços e Dados Dinâmicos ---
    private AlunoService alunoService = new AlunoService();
    private EscolaService escolaService = new EscolaService();
    private List<Escola> listaEscolas;

    // Objeto aluno sendo criado ou editado (ID será > 0 em edição)
    private Aluno alunoEmEdicao;

    // -------------------------------------------------------------------------
    // MÉTODOS DE INICIALIZAÇÃO
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        carregarEscolas();
        configurarEventos();
    }

    // Método que recebe o objeto Aluno da tela de Consulta (Edição)
    public void setAlunoEmEdicao(Aluno aluno) {
        this.alunoEmEdicao = aluno;
        if (aluno != null) {
            carregarDadosAluno(aluno);
        } else {
            // Novo cadastro
            dataAcolhimentoPicker.setValue(LocalDate.now());
        }
    }

    // Carrega os dados do objeto Aluno nos campos do formulário
    private void carregarDadosAluno(Aluno aluno) {

        // 1. Campos Principais
        nomeCompletoTextField.setText(aluno.getNome());
        cpfTextField.setText(aluno.getCpf());
        nisTextField.setText(aluno.getNumNIS());
        vestuarioTextField.setText(aluno.getTamanhoVestuario());
        calcadoTextField.setText(String.valueOf(aluno.getTamanhoCalcado()));

        // 2. Datas e Cálculo de Idade
        if (aluno.getDataNascimento() != null) {
            LocalDate dataNascLocal = aluno.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dataNascimentoPicker.setValue(dataNascLocal);
            calcularIdade(dataNascLocal);
        }
        if (aluno.getDataAcolhimento() != null) {
            dataAcolhimentoPicker.setValue(aluno.getDataAcolhimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        // 3. ComboBoxes
        sexoComboBox.setValue(aluno.getSexo().equals("M") ? "Masculino" : (aluno.getSexo().equals("F") ? "Feminino" : "Outro"));
        turnoComboBox.setValue(aluno.getTurno());
        formaAcessoComboBox.setValue(aluno.getFormaAcesso());
        termoImagemComboBox.setValue(aluno.isTermoImagemAssinado() ? "Autorizado" : "Não Autorizado");
        projetoFeriasComboBox.setValue(aluno.isProjetoFerias() ? "Sim" : "Não");

        // Transporte e Turma
        transporteComboBox.setValue(aluno.getTransporte());
        turmaComboBox.setValue(aluno.getTurma());

        // 4. Escola (RESOLVE O ERRO DE IFPRESENT)
        listaEscolas.stream()
                .filter(e -> e.getId() == aluno.getFkCodEscola())
                .findFirst()
                .ifPresent(escola -> escolaComboBox.setValue(escola.toString()));

        // 5. Anotações (usando observacoesMedicas como campo geral)
        anotacoesTextArea.setText(aluno.getObservacoesMedicas());
    }

    // Resolve o erro 'cannot find symbol'
    private void calcularIdade(LocalDate dataNasc) {
        if (dataNasc != null) {
            Period periodo = Period.between(dataNasc, LocalDate.now());
            idadeTextField.setText(String.valueOf(periodo.getYears()));
        } else {
            idadeTextField.setText("");
        }
    }

    // -------------------------------------------------------------------------
    // CARREGAR COMBOBOX E ESCOLAS
    // -------------------------------------------------------------------------
    private void carregarCombos() {
        sexoComboBox.getItems().addAll("Masculino", "Feminino", "Outro");
        turnoComboBox.getItems().addAll("Manhã", "Tarde", "Noite");
        formaAcessoComboBox.getItems().addAll("Demanda Espontânea", "Encaminhamento", "Busca Ativa");
        projetoFeriasComboBox.getItems().addAll("Sim", "Não");
        termoImagemComboBox.getItems().addAll("Autorizado", "Não Autorizado");
        vulnerabilidade1ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");
        vulnerabilidade2ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");

        // Sugestões de Série
        if (serieComboBox != null) {
            serieComboBox.getItems().addAll("Pré", "1º ano", "2º ano", "3º ano", "4º ano", "5º ano");
        }
        if (transporteComboBox != null) {
            transporteComboBox.getItems().addAll("Van", "A pé", "Carro", "Carona", "Ônibus Escolar", "Outro");
        }

        // Turma 
        turmaComboBox.getItems().addAll("A", "B", "C", "D", "E");
    }

    private void carregarEscolas() {
        try {
            listaEscolas = escolaService.listarTodas();
            ObservableList<String> nomesEscolas = FXCollections.observableArrayList();
            for (Escola escola : listaEscolas) {
                nomesEscolas.add(escola.toString());
            }
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
        if (listaEscolas == null || nomeEscola == null) {
            return Optional.empty();
        }
        return listaEscolas.stream()
                .filter(e -> e.toString().equals(nomeEscola))
                .findFirst();
    }

    // -------------------------------------------------------------------------
    // CONFIGURAR EVENTOS
    // -------------------------------------------------------------------------
    private void configurarEventos() {
        salvarButton.setOnAction(event -> salvarAluno());

        // Listener para cálculo de idade
        dataNascimentoPicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            calcularIdade(newValue);
        });

        // AÇÃO 1: Adicionar Detalhes (Família)
        adicionarDetalhesButton.setOnAction(event -> {
            abrirTelaDetalhes("TelaCadastroFamilia", "Cadastro de Detalhes Familiares");
        });

        // AÇÃO 2: Adicionar Saúde (Assumindo que o ID do FXML é adicionarSaudeButton1)
        if (adicionarSaudeButton1 != null) {
            adicionarSaudeButton1.setOnAction(event -> {
                abrirTelaDetalhes("TelaCadastroSaude", "Cadastro de Detalhes de Saúde");
            });
        }

        // AÇÃO 3: Adicionar Parente
        if (adicionarParenteButon != null) {
            adicionarParenteButon.setOnAction(event -> {
                abrirTelaDetalhes("TelaCadastroParente", "Cadastro de Parente");
            });
        }

        if (acessarAnexosButton != null) {
            acessarAnexosButton.setOnAction(event -> {
                System.out.println("Acessar Anexos (Ainda não implementado)");
            });
        }
    }

    // Lógica para abrir os modais de Saúde, Família, etc.
    private void abrirTelaDetalhes(String fxmlFileName, String title) {

        // Verifica se é edição e se o aluno principal já foi salvo
        if (alunoEmEdicao == null || alunoEmEdicao.getId() == 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Salve o Aluno Primeiro");
            alert.setContentText("Tu deves salvar o cadastro principal do aluno antes de adicionar Detalhes, Saúde ou Parentes.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/simpa/" + fxmlFileName + ".fxml"));
            Parent root = loader.load();

            // Passa os dados necessários para os controllers filhos
            if ("TelaCadastroFamilia".equals(fxmlFileName)) {
                controller.CadastroFamiliaController controller = loader.getController();
                controller.setAlunoId(alunoEmEdicao.getId());
            } else if ("TelaCadastroSaude".equals(fxmlFileName)) {
                controller.CadastroSaudeController controller = loader.getController();
                controller.setAluno(alunoEmEdicao);
            } else if ("TelaCadastroParente".equals(fxmlFileName)) {
                // ** MELHORIA: Tu terias que buscar o ID da Família aqui antes de passar. **
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Atenção");
                alert.setHeaderText("Funcionalidade Pendente");
                alert.setContentText("Para cadastrar parentes, a família do aluno deve ser buscada primeiro.");
                alert.showAndWait();
                return;
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + fxmlFileName);
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Carregamento");
            alert.setHeaderText("Falha ao Abrir Tela");
            alert.setContentText("Ocorreu um erro ao tentar abrir o formulário de detalhes.");
            alert.showAndWait();
        }
    }

    // -------------------------------------------------------------------------
    // LÓGICA PRINCIPAL DE SALVAMENTO (INSERT/UPDATE)
    // -------------------------------------------------------------------------
    private void salvarAluno() {

        Usuario usuarioCriador = LoginController.getUsuarioLogado();

        try {
            if (usuarioCriador == null || usuarioCriador.getId() == 0) {
                throw new BusinessException("É necessário estar logado para cadastrar/editar alunos.");
            }
            if (!validarCamposAluno()) { // Usa o método de validação
                throw new BusinessException("Preencha todos os campos obrigatórios (Nome, CPF, Datas, Sexo, Vestuário, Calçado e Escola).");
            }

            // 1. Mapeamento para o objeto Aluno (Cria um novo ou usa o de edição)
            Aluno alunoASalvar = alunoEmEdicao != null ? alunoEmEdicao : new Aluno(); // Corrigido a variável

            // 2. Coleta e Mapeamento
            alunoASalvar.setFkCodAdmin(usuarioCriador.getId());

            // Datas
            LocalDate dataNascLocal = dataNascimentoPicker.getValue();
            LocalDate dataAcolLocal = dataAcolhimentoPicker.getValue();

            alunoASalvar.setDataNascimento(Date.from(dataNascLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            alunoASalvar.setDataAcolhimento(Date.from(dataAcolLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            // FK Escola
            String escolaSelecionadaNome = escolaComboBox.getValue();
            Optional<Escola> escolaOpt = getEscolaSelecionada(escolaSelecionadaNome);
            if (escolaOpt.isEmpty()) {
                throw new BusinessException("Selecione uma escola válida.");
            }
            alunoASalvar.setFkCodEscola(escolaOpt.get().getId());

            // Campos Principais e Combos
            alunoASalvar.setNome(nomeCompletoTextField.getText());
            alunoASalvar.setCpf(cpfTextField.getText());
            alunoASalvar.setNumNIS(nisTextField.getText());
            alunoASalvar.setTamanhoVestuario(vestuarioTextField.getText());
            alunoASalvar.setTamanhoCalcado(Integer.parseInt(calcadoTextField.getText()));
            alunoASalvar.setFormaAcesso(formaAcessoComboBox.getValue());
            alunoASalvar.setTurno(turnoComboBox.getValue());
            alunoASalvar.setTurma(turmaComboBox.getValue());
            alunoASalvar.setTransporte(transporteComboBox.getValue());
            alunoASalvar.setObservacoesMedicas(anotacoesTextArea.getText());
            alunoASalvar.setStatus(1);

            // Mapeamento Booleanos/TINYINT (Sim/Não)
            alunoASalvar.setProjetoFerias(projetoFeriasComboBox.getValue().equals("Sim"));
            alunoASalvar.setTermoImagemAssinado(termoImagemComboBox.getValue().equals("Autorizado"));

            // Mapeamento Sexo (CHAR(1))
            String sexoSelecionado = sexoComboBox.getValue();
            alunoASalvar.setSexo(sexoSelecionado.equals("Masculino") ? "M" : (sexoSelecionado.equals("Feminino") ? "F" : "O"));

            // 3. Lógica de Persistência
            if (alunoASalvar.getId() != 0) {
                // EDIÇÃO 
                throw new BusinessException("Funcionalidade de EDIÇÃO (UPDATE) ainda não implementada no Service/DAO.");
            } else {
                // NOVO CADASTRO
                alunoService.salvar(alunoASalvar);
                // Atualiza o objeto de edição com o ID gerado (ESSENCIAL)
                this.alunoEmEdicao = alunoASalvar;
            }

            // 4. Sucesso
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Sucesso!");
            alert.setContentText("O aluno '" + alunoASalvar.getNome() + "' foi salvo e agora pode ter detalhes adicionados."); // Corrigido o typo!
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Formato de Campo Inválido");
            alert.setContentText("Campos numéricos (Ex: Calçado) estão em formato inválido.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao Salvar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // -------------------------------------------------------------------------
    // VALIDAÇÃO AUXILIAR
    // -------------------------------------------------------------------------
    /**
     * Validação centralizada dos campos obrigatórios da tela principal.
     */
    private boolean validarCamposAluno() {
        return !(nomeCompletoTextField.getText().isEmpty()
                || cpfTextField.getText().isEmpty()
                || escolaComboBox.getValue() == null
                || dataNascimentoPicker.getValue() == null
                || dataAcolhimentoPicker.getValue() == null
                || formaAcessoComboBox.getValue() == null
                || sexoComboBox.getValue() == null
                || vestuarioTextField.getText().isEmpty()
                || calcadoTextField.getText().isEmpty());
    }
}
