package controller;

import classes.Aluno;
import classes.Usuario;
import service.AlunoService;
import util.BusinessException;
import util.DBException;

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

public class CadastroSaudeController implements Initializable {

    private AlunoService alunoService = new AlunoService();

    @FXML private TextArea txtMedicamentos;
    @FXML private TextArea txtAlergias;
    @FXML private TextArea txtObsMedicas;
    @FXML private DatePicker vacinacaoDatePicker;
    @FXML private Button salvarButton;

    // Variável que armazena o Aluno que está a ser editado
    private Aluno alunoEmEdicao; 

    // -------------------------------------------------------------------------
    // MÉTODOS DE INICIALIZAÇÃO E SETUP
    // -------------------------------------------------------------------------
    @Override // A CORREÇÃO CRÍTICA PARA AS LINHAS 42-44
    public void initialize(URL url, ResourceBundle rb) {
        salvarButton.setOnAction(event -> salvar());
    }

    /**
     * Recebe o objeto Aluno da tela pai e carrega os dados
     */
    public void setAluno(Aluno aluno) {
        // Inicializa a variável de edição com o aluno recebido
        this.alunoEmEdicao = aluno; 

        if (alunoEmEdicao != null) {
            // Carrega os dados existentes (se houver)
            txtMedicamentos.setText(alunoEmEdicao.getMedicamentosUso());
            txtAlergias.setText(alunoEmEdicao.getAlergias());
            txtObsMedicas.setText(alunoEmEdicao.getObservacoesMedicas());

            // Carrega a data de vacinação
            if (alunoEmEdicao.getDataVacinacao() != null) {
                vacinacaoDatePicker.setValue(alunoEmEdicao.getDataVacinacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }
    }

    // -------------------------------------------------------------------------
    // LÓGICA DE PERSISTÊNCIA
    // -------------------------------------------------------------------------
    private void salvar() {
        Usuario usuarioLogado = controller.LoginController.getUsuarioLogado();

        try {
            // 1. Verificações (CORRIGIDO: A estrutura agora é legível pelo compilador)
            if (usuarioLogado == null) {
                throw new BusinessException("É necessário logar para salvar dados.");
            }
            if (alunoEmEdicao == null || alunoEmEdicao.getId() <= 0) {
                throw new BusinessException("É necessário associar os dados a um aluno existente. Salve o cadastro principal primeiro.");
            }

            // 2. Mapeamento dos dados da tela para o objeto Aluno existente
            // CORREÇÃO: Uso de txtMedicamentos, txtAlergias, txtObsMedicas (nomes FXML)
            alunoEmEdicao.setMedicamentosUso(txtMedicamentos.getText());
            alunoEmEdicao.setAlergias(txtAlergias.getText());
            alunoEmEdicao.setObservacoesMedicas(txtObsMedicas.getText());

            // 3. Mapeamento da Data de Vacinação e Lógica de 'Em Dia'
            LocalDate dataVacinacaoLocal = vacinacaoDatePicker.getValue();
            Date dataVacinacao = null;
            boolean emDia = false;

            if (dataVacinacaoLocal != null) {
                dataVacinacao = Date.from(dataVacinacaoLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

                // Regra de Negócio: Chama o Service para verificar se está dentro do prazo de 1 ano
                emDia = alunoService.verificarVacinacaoEmDia(dataVacinacao);
            }

            alunoEmEdicao.setDataVacinacao(dataVacinacao); // Coluna DATE
            alunoEmEdicao.setVacinacaoEmDia(emDia); // Coluna TINYINT (1 ou 0)

            // 4. Chamada ao Service para persistir (UPDATE específico na tabela ALUNO)
            // NOTA: O método 'atualizarSaude' deve estar implementado em AlunoService.java
            alunoService.atualizarSaude(alunoEmEdicao, usuarioLogado);

            // 5. Sucesso
            Alert sucesso = new Alert(AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setContentText("Dados de Saúde atualizados com sucesso.");
            sucesso.showAndWait();

            // Fecha a tela
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
}