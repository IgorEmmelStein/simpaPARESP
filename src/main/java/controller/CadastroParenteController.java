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
import java.util.ResourceBundle;

public class CadastroParenteController implements Initializable {

    @FXML
    private TextField txtNome;
    @FXML
    private ComboBox<String> comboTipoParente;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtVinculoAfetivo;
    @FXML
    private TextField txtOcupacao;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtResponsavelLegal;
    @FXML
    private TextField txtPessoaAutorizada;
    @FXML
    private TextArea txtAnotacoes;

    @FXML
    private Button btnSalvar;

    private IntegranteFamiliaService integranteService = new IntegranteFamiliaService();
    private int familiaId = 0;

    public void setFamiliaId(int familiaId) {
        this.familiaId = familiaId;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboTipoParente.getItems().addAll(
                "Mãe", "Pai", "Avó", "Avô", "Tia", "Tio", "Irmã", "Irmão",
                "Responsável", "Outro"
        );

        btnSalvar.setOnAction(event -> salvar());
    }

    private void salvar() {
        Usuario usuarioLogado = LoginController.getUsuarioLogado();

        try {
            if (usuarioLogado == null) {
                throw new BusinessException("É necessário logar para salvar dados.");
            }
            if (familiaId == 0) {
                throw new BusinessException("Não foi possível identificar a família. Cadastre os Detalhes da Família primeiro.");
            }

            IntegranteFamilia novoIntegrante = new IntegranteFamilia();
            novoIntegrante.setFkCodFamilia(familiaId);

            novoIntegrante.setNome(txtNome.getText());
            novoIntegrante.setCpf(txtCpf.getText());

            novoIntegrante.setParentesco(comboTipoParente.getValue());
            novoIntegrante.setOcupacao(txtOcupacao.getText());
            novoIntegrante.setEndereco(txtEndereco.getText());
            novoIntegrante.setTelefone(txtTelefone.getText());

            boolean vinculoAfetivo = !txtVinculoAfetivo.getText().trim().isEmpty();
            boolean respLegal = !txtResponsavelLegal.getText().trim().isEmpty();
            boolean pessoaAutorizada = !txtPessoaAutorizada.getText().trim().isEmpty();

            novoIntegrante.setVinculoAfetivo(vinculoAfetivo);
            novoIntegrante.seteResponsavelLegal(respLegal);
            novoIntegrante.setePessoaAutorizada(pessoaAutorizada);

            novoIntegrante.setAnotacoes(txtAnotacoes.getText());

            integranteService.salvar(novoIntegrante, usuarioLogado);

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setContentText("Parente/Integrante salvo com sucesso. Tu podes adicionar o próximo.");
            sucesso.showAndWait();

            limparCamposParente();

        } catch (BusinessException | DBException e) {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Falha ao Salvar Parente");
            erro.setContentText(e.getMessage());
            erro.showAndWait();
        }
    }

    private void limparCamposParente() {
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
    }
}
