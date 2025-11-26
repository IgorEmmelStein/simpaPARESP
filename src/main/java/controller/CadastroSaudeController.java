package controller;

import classes.Aluno;
import classes.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import service.AlunoService;
import util.BusinessException;
import util.DBException;

public class CadastroSaudeController implements Initializable {

    private Aluno aluno = new Aluno();
    private AlunoService alunoService = new AlunoService();

    @FXML
    private TextArea txtMedicamentos;
    @FXML
    private TextArea txtAlergias;
    @FXML
    private TextArea txtObsMedicas;
    @FXML
    private DatePicker vacinacaoDatePicker;
    @FXML
    private Button salvarButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        salvarButton.setOnAction(event -> salvar());
    }

    private void salvar() {
        Usuario usuarioLogado = LoginController.getUsuarioLogado();

        try {
            if (usuarioLogado == null) {
                throw new BusinessException("É necessário logar para salvar dados.");
            }
            if (aluno.getId() <= 0) {
                throw new BusinessException("É necessário associar os dados a um aluno existente.");
            }

            aluno.setMedicamentosUso(txtMedicamentos.getText());
            aluno.setAlergias(txtAlergias.getText());
            aluno.setObservacoesMedicas(txtObsMedicas.getText());

            LocalDate dataVacinacaoLocal = vacinacaoDatePicker.getValue();
            Date dataVacinacao = null;
            boolean emDia = false;

            if (dataVacinacaoLocal != null) {
                dataVacinacao = Date.from(dataVacinacaoLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
                aluno.setDataVacinacao(dataVacinacao);
                aluno.setVacinacaoEmDia(true);
                emDia = alunoService.verificarVacinacaoEmDia(dataVacinacao);
            }
            aluno.setDataVacinacao(dataVacinacao);
            aluno.setVacinacaoEmDia(emDia);

            alunoService.atualizarSaude(aluno, usuarioLogado);

            Alert sucesso = new Alert(AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setContentText("Dados de Saúde atualizados no sistema.");
            sucesso.showAndWait();

            Stage stage = (Stage) salvarButton.getScene().getWindow();
            stage.close();

        } catch (BusinessException | DBException e) {
            Alert erro = new Alert(AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Falha ao Salvar Saúde");
            erro.setContentText(e.getMessage());
            erro.showAndWait();
        }
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;

        if (aluno != null) {
            txtMedicamentos.setText(aluno.getMedicamentosUso());
            txtAlergias.setText(aluno.getAlergias());
            txtObsMedicas.setText(aluno.getObservacoesMedicas());

            if (aluno.getDataVacinacao() != null) {
                vacinacaoDatePicker.setValue(aluno.getDataVacinacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }
    }

}
