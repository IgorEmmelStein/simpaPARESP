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

    private Aluno alunoEditando = null;

    // --- Componentes FXML ---
    @FXML private Button acessarAnexosButton;
    @FXML private Button adicionarDetalhesButton;
    @FXML private Button adicionarSaudeButton1;
    @FXML private Button adicionarSaudeButton2;
    @FXML private TextArea anotacoesTextArea;
    @FXML private TextField calcadoTextField;
    @FXML private TextField cpfTextField;
    @FXML private DatePicker dataAcolhimentoPicker;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private ComboBox<Escola> escolaComboBox;
    @FXML private ComboBox<String> formaAcessoComboBox;
    @FXML private TextField idadeTextField;
    @FXML private TextField integrantesFamiliaTextField;
    @FXML private TextField nisTextField;
    @FXML private TextField nomeCompletoTextField;
    @FXML private ComboBox<String> projetoFeriasComboBox;
    @FXML private Button salvarButton;
    @FXML private ComboBox<String> serieComboBox;
    @FXML private ComboBox<String> transporteComboBox;
    @FXML private ComboBox<String> sexoComboBox;
    @FXML private ComboBox<String> termoImagemComboBox;
    @FXML private ComboBox<String> turmaComboBox;
    @FXML private ComboBox<String> turnoComboBox;
    @FXML private TextField vestuarioTextField;
    @FXML private ComboBox<String> vulnerabilidade1ComboBox;
    @FXML private ComboBox<String> vulnerabilidade2ComboBox;

    // Serviços
    private AlunoService alunoService = new AlunoService();
    private EscolaService escolaService = new EscolaService();
    private List<Escola> listaEscolas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        carregarEscolas();
        configurarEventos();
    }

    // ---------------------------------------------------------
    // 🟦 CARREGAR ALUNO PARA EDIÇÃO
    // ---------------------------------------------------------
    public void carregarAluno(Aluno aluno) {

        this.alunoEditando = aluno;

        nomeCompletoTextField.setText(aluno.getNome());
        cpfTextField.setText(aluno.getCpf());
        cpfTextField.setDisable(true);

        // Datas
        if (aluno.getDataNascimento() != null) {
            dataNascimentoPicker.setValue(
                aluno.getDataNascimento().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
            );
            dataNascimentoPicker.setDisable(true);
        }

        if (aluno.getDataAcolhimento() != null) {
            dataAcolhimentoPicker.setValue(
                aluno.getDataAcolhimento().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
            );
            dataAcolhimentoPicker.setDisable(true);
        }

        // Turno
        turnoComboBox.setValue(aluno.getTurno());

        // Escola → funciona agora porque ComboBox é <Escola>
        escolaComboBox.setValue(aluno.getEscola());

        // Sexo
        switch (aluno.getSexo()) {
            case "M": sexoComboBox.setValue("Masculino"); break;
            case "F": sexoComboBox.setValue("Feminino"); break;
            default:  sexoComboBox.setValue("Outro"); break;
        }
        sexoComboBox.setDisable(true);

        // Forma acesso
        formaAcessoComboBox.setValue(aluno.getFormaAcesso());
        formaAcessoComboBox.setDisable(true);

        projetoFeriasComboBox.setValue(aluno.isProjetoFerias() ? "Sim" : "Não");
        termoImagemComboBox.setValue(aluno.isTermoImagemAssinado() ? "Autorizado" : "Não Autorizado");

        transporteComboBox.setValue(aluno.getTransporte());
        vestuarioTextField.setText(aluno.getTamanhoVestuario());
        calcadoTextField.setText(String.valueOf(aluno.getTamanhoCalcado()));
        turmaComboBox.setValue(aluno.getTurma());
        nisTextField.setText(aluno.getNumNIS());
        anotacoesTextArea.setText(aluno.getObservacoesMedicas());
    }

    // ---------------------------------------------------------
    // 🟦 CARREGA COMBOS
    // ---------------------------------------------------------
    private void carregarCombos() {

        sexoComboBox.getItems().addAll("Masculino", "Feminino", "Outro");
        turnoComboBox.getItems().addAll("Manhã", "Tarde", "Noite");
        formaAcessoComboBox.getItems().addAll("Demanda Espontânea", "Encaminhamento", "Busca Ativa");
        projetoFeriasComboBox.getItems().addAll("Sim", "Não");
        termoImagemComboBox.getItems().addAll("Autorizado", "Não Autorizado");

        vulnerabilidade1ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");
        vulnerabilidade2ComboBox.getItems().addAll("Baixa renda", "Violência doméstica", "Deficiência", "Trabalho infantil");

        serieComboBox.getItems().addAll(
                "Pré", "1º ano", "2º ano", "3º ano", "4º ano",
                "5º ano", "6º ano", "7º ano", "8º ano", "9º ano"
        );

        transporteComboBox.getItems().addAll("Van", "A pé", "Carro", "Carona", "Ônibus Escolar", "Outro");
        turmaComboBox.getItems().addAll("A", "B", "C", "D", "E");
    }

    // ---------------------------------------------------------
    // 🟦 CARREGA ESCOLAS (agora monta diretamente ComboBox<Escola>)
    // ---------------------------------------------------------
    private void carregarEscolas() {
        try {
            listaEscolas = escolaService.listarTodas();
            escolaComboBox.setItems(FXCollections.observableArrayList(listaEscolas));
        } catch (DBException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao carregar escolas");
            alert.setContentText("Não foi possível carregar escolas do banco.");
            alert.show();
        }
    }

    // ---------------------------------------------------------
    // 🟦 SALVAR / EDITAR
    // ---------------------------------------------------------
    private void salvarAluno() {

        try {

            Usuario usuario = LoginController.getUsuarioLogado();
            if (usuario == null)
                throw new BusinessException("Usuário não logado.");

            Escola escolaSelecionada = escolaComboBox.getValue();
            if (escolaSelecionada == null)
                throw new BusinessException("Selecione uma escola.");

            LocalDate dataNasc = dataNascimentoPicker.getValue();
            if (dataNasc == null)
                throw new BusinessException("Data de nascimento obrigatória.");

            Aluno a = (alunoEditando == null) ? new Aluno() : alunoEditando;

            a.setFkCodAdmin(usuario.getId());
            a.setFkCodEscola(escolaSelecionada.getId());
            a.setEscola(escolaSelecionada);
            a.setNome(nomeCompletoTextField.getText());
            a.setCpf(cpfTextField.getText());

            a.setDataNascimento(Date.from(dataNasc.atStartOfDay(
                    ZoneId.systemDefault()).toInstant()));

            LocalDate acol = dataAcolhimentoPicker.getValue();
            a.setDataAcolhimento(acol != null ?
                    Date.from(acol.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    : new Date());

            a.setFormaAcesso(formaAcessoComboBox.getValue());
            a.setTurno(turnoComboBox.getValue());

            // Sexo
            String sexo = sexoComboBox.getValue();
            if (sexo.equals("Masculino")) a.setSexo("M");
            else if (sexo.equals("Feminino")) a.setSexo("F");
            else a.setSexo("O");

            a.setProjetoFerias(projetoFeriasComboBox.getValue().equals("Sim"));
            a.setTermoImagemAssinado(termoImagemComboBox.getValue().equals("Autorizado"));
            a.setTransporte(transporteComboBox.getValue());
            a.setTamanhoVestuario(vestuarioTextField.getText());

            // Calçado
            String calcado = calcadoTextField.getText();
            a.setTamanhoCalcado(calcado.isBlank() ? 0 : Integer.parseInt(calcado));

            a.setTurma(turmaComboBox.getValue());
            a.setNumNIS(nisTextField.getText());
            a.setObservacoesMedicas(anotacoesTextArea.getText());

            a.setStatus(1);

            alunoService.salvar(a);

            // Fecha janela
            Stage st = (Stage) salvarButton.getScene().getWindow();
            st.close();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setHeaderText("Sucesso");
            ok.setContentText("Aluno salvo com sucesso!");
            ok.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao salvar");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    // ---------------------------------------------------------
    // EVENTOS
    // ---------------------------------------------------------
    private void configurarEventos() {

        salvarButton.setOnAction(e -> salvarAluno());

        dataNascimentoPicker.valueProperty().addListener((obs, o, novo) -> {
            if (novo != null) {
                Period p = Period.between(novo, LocalDate.now());
                idadeTextField.setText(String.valueOf(p.getYears()));
            }
        });
    }
}