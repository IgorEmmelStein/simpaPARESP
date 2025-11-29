package controller;

import classes.Administrador;
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
import service.FamiliaService;

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

    private AlunoService alunoService = new AlunoService();
    private EscolaService escolaService = new EscolaService();
    private List<Escola> listaEscolas;
    private FamiliaService familiaService = new FamiliaService();

    private Aluno alunoEmEdicao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        carregarEscolas();
        configurarEventos();
        aplicarPermissoes();
    }

    public void setAlunoEmEdicao(Aluno aluno) {
        this.alunoEmEdicao = aluno;
        if (aluno != null) {
            carregarDadosAluno(aluno);
        } else {

            dataAcolhimentoPicker.setValue(LocalDate.now());
        }
    }

    private void carregarDadosAluno(Aluno aluno) {

        nomeCompletoTextField.setText(aluno.getNome());
        cpfTextField.setText(aluno.getCpf());
        nisTextField.setText(aluno.getNumNIS());
        vestuarioTextField.setText(aluno.getTamanhoVestuario());
        calcadoTextField.setText(String.valueOf(aluno.getTamanhoCalcado()));

        if (aluno.getDataNascimento() != null) {
            LocalDate dataNascLocal = aluno.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dataNascimentoPicker.setValue(dataNascLocal);
            calcularIdade(dataNascLocal);
        }
        if (aluno.getDataAcolhimento() != null) {
            dataAcolhimentoPicker.setValue(aluno.getDataAcolhimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        sexoComboBox.setValue(aluno.getSexo().equals("M") ? "Masculino" : (aluno.getSexo().equals("F") ? "Feminino" : "Outro"));
        turnoComboBox.setValue(aluno.getTurno());
        formaAcessoComboBox.setValue(aluno.getFormaAcesso());
        termoImagemComboBox.setValue(aluno.isTermoImagemAssinado() ? "Autorizado" : "Não Autorizado");
        projetoFeriasComboBox.setValue(aluno.isProjetoFerias() ? "Sim" : "Não");

        transporteComboBox.setValue(aluno.getTransporte());
        turmaComboBox.setValue(aluno.getTurma());

        listaEscolas.stream()
                .filter(e -> e.getId() == aluno.getFkCodEscola())
                .findFirst()
                .ifPresent(escola -> escolaComboBox.setValue(escola.toString()));

        anotacoesTextArea.setText(aluno.getObservacoesMedicas());
    }

    private void calcularIdade(LocalDate dataNasc) {
        if (dataNasc != null) {
            Period periodo = Period.between(dataNasc, LocalDate.now());
            idadeTextField.setText(String.valueOf(periodo.getYears()));
        } else {
            idadeTextField.setText("");
        }
    }

    private void carregarCombos() {
        sexoComboBox.getItems().addAll("Masculino", "Feminino", "Outro");
        turnoComboBox.getItems().addAll("Manhã", "Tarde", "Noite");
        formaAcessoComboBox.getItems().addAll("Demanda Espontânea", "Encaminhamento", "Busca Ativa");
        projetoFeriasComboBox.getItems().addAll("Sim", "Não");
        termoImagemComboBox.getItems().addAll("Autorizado", "Não Autorizado");
        vulnerabilidade1ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");
        vulnerabilidade2ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");

        if (serieComboBox != null) {
            serieComboBox.getItems().addAll("Pré", "1º ano", "2º ano", "3º ano", "4º ano", "5º ano");
        }
        if (transporteComboBox != null) {
            transporteComboBox.getItems().addAll("Van", "A pé", "Carro", "Carona", "Ônibus Escolar", "Outro");
        }

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

    private void configurarEventos() {
        salvarButton.setOnAction(event -> salvarAluno());
        dataNascimentoPicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            calcularIdade(newValue);
        });

        // 1. AÇÃO FAMÍLIA (Adicionar Detalhes)
        if (adicionarDetalhesButton != null) {
            adicionarDetalhesButton.setOnAction(event -> {
                abrirTelaDetalhes("TelaCadastroFamilia", "Cadastro de Detalhes Familiares");
            });
        }

        // 2. AÇÃO SAÚDE (AGORA DEVE FUNCIONAR)
        if (adicionarSaudeButton != null) {
            adicionarSaudeButton.setOnAction(event -> {
                abrirTelaDetalhes("TelaCadastroSaude", "Cadastro de Detalhes de Saúde");
            });
        }

        // 3. AÇÃO PARENTE
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

    private void abrirTelaDetalhes(String fxmlFileName, String title) {

        // 1. Verifica se o aluno principal já foi salvo
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

            // 2. Lógica de Seleção e Mapeamento de Controller
            if ("TelaCadastroFamilia".equals(fxmlFileName)) {
                // Mapeia para o Controller de Família (Detalhes)
                controller.CadastroFamiliaController controller = loader.getController();
                controller.setAlunoId(alunoEmEdicao.getId());

            } else if ("TelaCadastroSaude".equals(fxmlFileName)) {
                // Mapeia para o Controller de Saúde
                controller.CadastroSaudeController controller = loader.getController();
                // Passa o objeto aluno que contém os dados de saúde (para carregar/atualizar)
                controller.setAluno(alunoEmEdicao);

            } else if ("TelaCadastroParente".equals(fxmlFileName)) {

                // LÓGICA DE PARENTE: Requer o ID da Família que acabamos de corrigir no DAO
                int familiaId = familiaService.buscarIdPorAlunoId(alunoEmEdicao.getId());

                if (familiaId == 0) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Atenção");
                    alert.setHeaderText("Família Pendente");
                    alert.setContentText("A Família do aluno ainda não foi cadastrada. Por favor, adicione os Detalhes Familiares primeiro.");
                    alert.showAndWait();
                    return;
                }

                // Mapeia para o Controller de Parente
                controller.CadastroParenteController controller = loader.getController();
                controller.setFamiliaId(familiaId);
            }

            // 3. Abre o Modal
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
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Execução");
            alert.setHeaderText("Falha ao processar detalhes");
            alert.setContentText("Erro inesperado: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void salvarAluno() {

        Usuario usuarioCriador = LoginController.getUsuarioLogado();

        try {
            if (usuarioCriador == null || usuarioCriador.getId() == 0) {
                throw new BusinessException("É necessário estar logado para cadastrar/editar alunos.");
            }
            if (!validarCamposAluno()) {
                throw new BusinessException("Preencha todos os campos obrigatórios (Nome, CPF, Datas, Sexo, Vestuário, Calçado e Escola).");
            }

            Aluno alunoASalvar = alunoEmEdicao != null ? alunoEmEdicao : new Aluno();

            alunoASalvar.setFkCodAdmin(usuarioCriador.getId());

            LocalDate dataNascLocal = dataNascimentoPicker.getValue();
            LocalDate dataAcolLocal = dataAcolhimentoPicker.getValue();

            alunoASalvar.setDataNascimento(Date.from(dataNascLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            alunoASalvar.setDataAcolhimento(Date.from(dataAcolLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            String escolaSelecionadaNome = escolaComboBox.getValue();
            Optional<Escola> escolaOpt = getEscolaSelecionada(escolaSelecionadaNome);
            if (escolaOpt.isEmpty()) {
                throw new BusinessException("Selecione uma escola válida.");
            }
            alunoASalvar.setFkCodEscola(escolaOpt.get().getId());

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

            alunoASalvar.setProjetoFerias(projetoFeriasComboBox.getValue().equals("Sim"));
            alunoASalvar.setTermoImagemAssinado(termoImagemComboBox.getValue().equals("Autorizado"));

            String sexoSelecionado = sexoComboBox.getValue();
            alunoASalvar.setSexo(sexoSelecionado.equals("Masculino") ? "M" : (sexoSelecionado.equals("Feminino") ? "F" : "O"));

            if (alunoASalvar.getId() != 0) {

                alunoService.atualizar(alunoASalvar);
            } else {

                alunoService.salvar(alunoASalvar);

                this.alunoEmEdicao = alunoASalvar;
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Sucesso!");
            alert.setContentText("O aluno '" + alunoASalvar.getNome() + "' foi salvo e agora pode ter detalhes adicionados.");
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
    
    private void aplicarPermissoes() {
        Usuario usuario = LoginController.getUsuarioLogado();
        if (usuario == null) return;

        if (usuario instanceof Administrador) {
            Administrador admin = (Administrador) usuario;

            
            boolean acessoSaude = admin.isAdmin() || admin.isSaude();
            adicionarSaudeButton.setDisable(!acessoSaude);

            boolean acessoSocial = admin.isAdmin() || admin.isSocial();
            adicionarDetalhesButton.setDisable(!acessoSocial);
        }
    }
}
