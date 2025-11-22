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
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    // -------------------------------------------------------------------------
    // MÉTODOS DE INICIALIZAÇÃO
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        carregarEscolas();
        configurarEventos();
    }

    private void calcularIdade(LocalDate dataNasc) {
        if (dataNasc != null) {
            Period periodo = Period.between(dataNasc, LocalDate.now());
            idadeTextField.setText(String.valueOf(periodo.getYears()));
        } else {
            idadeTextField.setText("");
        }
    }

    private void configurarEventos() {
        salvarButton.setOnAction(event -> salvarAluno());

        adicionarDetalhesButton.setOnAction(event -> {
            abrirTelaDetalhes("TelaCadastroFamilia", "Cadastro de Detalhes Familiares");
        });

        adicionarSaudeButton1.setOnAction(event -> {
            abrirTelaDetalhes("TelaCadastroSaude", "Cadastro de Detalhes de Saúde");
        });

        adicionarSaudeButton2.setOnAction(event -> {
            abrirTelaDetalhes("TelaCadastroSaude", "Cadastro de Detalhes de Saúde");
        });

        acessarAnexosButton.setOnAction(event -> {
            System.out.println("Acessar Anexos (Ainda não implementado)");
        });

        dataNascimentoPicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            calcularIdade(newValue);
        });
    }

    private void carregarCombos() {
        sexoComboBox.getItems().addAll("Masculino", "Feminino", "Outro");
        turnoComboBox.getItems().addAll("Manhã", "Tarde", "Noite");
        formaAcessoComboBox.getItems().addAll("Demanda Espontânea", "Encaminhamento", "Busca Ativa");
        projetoFeriasComboBox.getItems().addAll("Sim", "Não");
        termoImagemComboBox.getItems().addAll("Autorizado", "Não Autorizado");
        vulnerabilidade1ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");
        vulnerabilidade2ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");

        // NOVO: Sugestões de Série
        serieComboBox.getItems().addAll(
                "Pré", "1º ano", "2º ano", "3º ano", "4º ano",
                "5º ano", "6º ano", "7º ano", "8º ano", "9º ano"
        );

        // NOVO: Formas de Transporte
        transporteComboBox.getItems().addAll(
                "Van", "A pé", "Carro", "Carona", "Ônibus Escolar", "Outro"
        );

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

    private void abrirTelaDetalhes(String fxmlFileName, String title) {
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Carregamento");
            alert.setHeaderText("Falha ao Abrir Tela");
            alert.setContentText("Ocorreu um erro ao tentar abrir o formulário de detalhes.");
            alert.showAndWait();
        }
    }

    // -------------------------------------------------------------------------
    // LÓGICA PRINCIPAL DE SALVAMENTO
    // -------------------------------------------------------------------------
    private void salvarAluno() {

        try {
            // 0. Pré-requisitos
            Usuario usuarioCriador = LoginController.getUsuarioLogado();
            if (usuarioCriador == null || usuarioCriador.getId() == 0) {
                System.err.println("DEBUG: Falha de Permissão. Usuário não logado ou ID 0.");
                throw new BusinessException("É necessário estar logado para cadastrar alunos.");
            }

            // 1. Coleta de Dados
            String nome = nomeCompletoTextField.getText();
            String cpf = cpfTextField.getText();
            String escolaSelecionadaNome = escolaComboBox.getValue();
            LocalDate dataNascLocal = dataNascimentoPicker.getValue();

            // DEBUG: LOG DE DADOS CRÍTICOS ANTES DE MAPEAR
            System.out.println("--- DEBUG INSERÇÃO ALUNO ---");
            System.out.println("Nome: " + nome);
            System.out.println("CPF: " + cpf);
            System.out.println("Escola Selecionada: " + escolaSelecionadaNome);

            if (nome.isEmpty() || cpf.isEmpty() || escolaSelecionadaNome == null || dataNascLocal == null) {
                throw new BusinessException("Os campos Nome, CPF, Escola e Data de Nascimento são obrigatórios.");
            }

            // 2. Mapeamento de Chaves Estrangeiras (FKs)
            Optional<Escola> escolaOpt = getEscolaSelecionada(escolaSelecionadaNome);
            if (escolaOpt.isEmpty()) {
                throw new BusinessException("Selecione uma escola válida.");
            }
            int fkCodEscola = escolaOpt.get().getId();
            int fkCodAdmin = usuarioCriador.getId();

            // DEBUG: LOG DE FKs
            System.out.println("FK Admin: " + fkCodAdmin);
            System.out.println("FK Escola ID: " + fkCodEscola);

            // 3. Mapeamento para o objeto Aluno
            Aluno novoAluno = new Aluno();

            // FKs e Herdados
            novoAluno.setFkCodAdmin(fkCodAdmin);
            novoAluno.setFkCodEscola(fkCodEscola);
            novoAluno.setNome(nome);
            novoAluno.setCpf(cpf);

            // Conversão de Data (LocalDate para java.util.Date)
            novoAluno.setDataNascimento(Date.from(dataNascLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            LocalDate dataAcolLocal = dataAcolhimentoPicker.getValue();
            
            novoAluno.setDataAcolhimento((dataAcolLocal != null) ? Date.from(dataAcolLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()) : new Date());

            
            novoAluno.setFormaAcesso(formaAcessoComboBox.getValue());
            
            novoAluno.setTurno(turnoComboBox.getValue());
            
            String sexoSelecionado = sexoComboBox.getValue();
            String sexoDB;

            if (sexoSelecionado == null) {
                throw new BusinessException("O campo Sexo é obrigatório.");
            } else if (sexoSelecionado.equals("Masculino")) {
                sexoDB = "M";
            } else if (sexoSelecionado.equals("Feminino")) {
                sexoDB = "F";   
            } else {
                sexoDB = "O";
            }
            novoAluno.setSexo(sexoDB);
            
            novoAluno.setProjetoFerias(projetoFeriasComboBox.getValue() != null && projetoFeriasComboBox.getValue().equals("Sim"));
            novoAluno.setTermoImagemAssinado(termoImagemComboBox.getValue() != null && termoImagemComboBox.getValue().equals("Autorizado"));
            novoAluno.setVacinacaoEmDia(true);

            // TextFields
            novoAluno.setTransporte(transporteComboBox.getValue());
            String vestuarioText = vestuarioTextField.getText().trim();
            if (vestuarioText.isEmpty()) {
                throw new BusinessException("O campo Tamanho do Vestuário é obrigatório e não pode ser vazio.");
            }
            novoAluno.setTamanhoVestuario(vestuarioText);

            // DEBUG CRÍTICO: VALOR DO CALÇADO
            String calcadoText = calcadoTextField.getText();
            System.out.println("DEBUG (CALÇADO): Valor lido antes da conversão: '" + calcadoText + "'");

            // Conversão numérica segura (Aqui ocorre a exceção)
            novoAluno.setTamanhoCalcado(calcadoText.isEmpty() ? 0 : Integer.parseInt(calcadoText));

            novoAluno.setTurma(turmaComboBox.getValue());
            novoAluno.setNumNIS(nisTextField.getText());
            novoAluno.setObservacoesMedicas(anotacoesTextArea.getText());
            novoAluno.setStatus(1);

            // 4. Salvar no Service
            alunoService.salvar(novoAluno);

            // DEBUG: SUCESSO DE PERSISTÊNCIA
            System.out.println("DEBUG: Aluno salvo com sucesso no banco de dados.");

            // 5. Sucesso
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Sucesso!");
            alert.setContentText("O aluno '" + novoAluno.getNome() + "' foi cadastrado com sucesso.");
            alert.showAndWait();

            // ... (limpar campos)
        } catch (NumberFormatException e) {
            System.err.println("ERRO DE FORMATO: " + e.getMessage());
            System.err.println("Atenção: A exceção ocorreu na conversão de String para número.");
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Erro de Formato");
            alert.setContentText("Campos numéricos (Ex: Calçado) estão em formato inválido. Por favor, use apenas números.");
            alert.showAndWait();
        } catch (BusinessException | DBException e) {
            System.err.println("ERRO DE NEGÓCIO/DB: " + e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao Cadastrar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("ERRO DE DB CRÍTICO: " + e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Banco de Dados");
            alert.setHeaderText("Falha na Inserção (Restrição Violada)");
            alert.setContentText("Não foi possível salvar o aluno. Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
