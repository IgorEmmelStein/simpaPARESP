package controller;

import classes.Familia;
import classes.Usuario;
import service.FamiliaService;
import util.BusinessException;
import util.DBException;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import classes.Usuario;

public class CadastroFamiliaController implements Initializable {

    @FXML
    private TextField integrantesTextField;
    @FXML
    private TextField rendaTextField;
    @FXML
    private ComboBox<String> bolsaFamiliaComboBox;
    @FXML
    private TextField enderecoTextField;
    @FXML
    private TextField bairroTextField;
    @FXML
    private TextField logradouroTextField;
    @FXML
    private TextField aluguelTextField;
    @FXML
    private TextField telefoneTextField;
    @FXML
    private TextArea anotacoesTextArea;
    @FXML
    private Button salvarButton;

    private FamiliaService familiaService = new FamiliaService();
    private int alunoId = 0;
    private Familia familiaEmEdicao = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCombos();
        salvarButton.setOnAction(event -> salvarFamilia());
    }

    private void carregarCombos() {
        bolsaFamiliaComboBox.getItems().addAll("Sim", "Não");
    }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
        
        // Tenta buscar se já existe uma família para este aluno
        if (alunoId > 0) {
            try {
                // 1. Obtém o usuário logado para a verificação de permissão no Service
                Usuario usuarioLogado = controller.LoginController.getUsuarioLogado(); 
                
                // 2. CORREÇÃO: Passa o usuário logado como segundo argumento
                Familia familiaExistente = familiaService.buscarPorAlunoId(alunoId, usuarioLogado);
                
                if (familiaExistente != null) {
                    this.familiaEmEdicao = familiaExistente;
                    carregarDadosFamilia(familiaEmEdicao);
                } else {
                    // Prepara para uma nova inserção
                    this.familiaEmEdicao = new Familia();
                }
            } catch (BusinessException | DBException e) {
                // Se a permissão falhar ou o DB falhar, exibe o alerta
                exibirAlerta(AlertType.ERROR, "Erro ao Carregar Família", "Falha ao buscar dados de família existentes: " + e.getMessage());
            }
        }
    }
    
    private void carregarDadosFamilia(Familia familia) {

        integrantesTextField.setText(String.valueOf(familia.getQtdIntegrantes()));
        rendaTextField.setText(String.valueOf(familia.getRendaFamiliarTotal()));
        enderecoTextField.setText(familia.getEndereco());
        bairroTextField.setText(familia.getBairro());
        logradouroTextField.setText(familia.getTipoResidencia());
        aluguelTextField.setText(String.valueOf(familia.getValorAluguel()));
        telefoneTextField.setText(familia.getTelefoneContato());
        anotacoesTextArea.setText(familia.getAnotacoes());

        bolsaFamiliaComboBox.setValue(familia.getValorBolsaFamilia() > 0.00 ? "Sim" : "Não");
    }

    private void salvarFamilia() {
        Usuario usuarioLogado = LoginController.getUsuarioLogado();

        try {
            if (usuarioLogado == null) {
                throw new BusinessException("É necessário logar para salvar dados.");
            }
            if (alunoId <= 0) {
                throw new BusinessException("A família deve ser associada a um aluno válido.");
            }
            if (!validarCampos()) {
                throw new BusinessException("Preencha todos os campos obrigatórios.");
            }

            Familia familiaASalvar = familiaEmEdicao != null ? familiaEmEdicao : new Familia();
            familiaASalvar.setFkCodPessoa(alunoId);

            int integrantes = Integer.parseInt(integrantesTextField.getText());
            double renda = Double.parseDouble(rendaTextField.getText());
            double aluguel = aluguelTextField.getText().isEmpty() ? 0.00 : Double.parseDouble(aluguelTextField.getText());

            boolean recebeBolsa = "Sim".equals(bolsaFamiliaComboBox.getValue());
            familiaASalvar.setValorBolsaFamilia(recebeBolsa ? 0.01 : 0.00);

            familiaASalvar.setQtdIntegrantes(integrantes);
            familiaASalvar.setRendaFamiliarTotal(renda);
            familiaASalvar.setEndereco(enderecoTextField.getText());
            familiaASalvar.setBairro(bairroTextField.getText());
            familiaASalvar.setTipoResidencia(logradouroTextField.getText());
            familiaASalvar.setValorAluguel(aluguel);
            familiaASalvar.setTelefoneContato(telefoneTextField.getText());
            familiaASalvar.setAnotacoes(anotacoesTextArea.getText());

            familiaService.salvar(familiaASalvar, usuarioLogado);

            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Dados familiares salvos com sucesso!");

            this.familiaEmEdicao = familiaASalvar;

            Stage stage = (Stage) salvarButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            exibirAlerta(AlertType.WARNING, "Atenção", "Verifique se os campos numéricos (Integrantes, Renda, Aluguel) estão preenchidos corretamente.");
        } catch (BusinessException | DBException e) {
            exibirAlerta(AlertType.ERROR, "Falha ao Salvar", e.getMessage());
        }
    }

    private boolean validarCampos() {
        return !(integrantesTextField.getText().isEmpty()
                || rendaTextField.getText().isEmpty()
                || enderecoTextField.getText().isEmpty()
                || bairroTextField.getText().isEmpty()
                || logradouroTextField.getText().isEmpty()
                || telefoneTextField.getText().isEmpty());
    }

    private void exibirAlerta(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
