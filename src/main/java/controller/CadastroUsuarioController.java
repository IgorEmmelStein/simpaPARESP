// Em src/main/java/controller/CadastroUsuarioController.java
package controller;

import classes.Administrador;
import classes.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.*; //

import service.AdministradorService;
import util.BusinessException;
import util.DBException;

public class CadastroUsuarioController implements Initializable {

    @FXML
    private Button btnCadastrar;

    @FXML
    private Label lblErro;

    @FXML
    private TextField txtNomeUsuario;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private PasswordField txtSenha1;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtTelefone;

    @FXML
    private ComboBox<String> comboTipoUsuario;

    private AdministradorService administradorService;

    public CadastroUsuarioController() {
        this.administradorService = new AdministradorService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Preenche o ComboBox com os perfis disponíveis
        comboTipoUsuario.getItems().addAll(
                "Administrador",
                "Saúde (Perfil Consulta)",
                "Vulnerabilidade Social (Perfil Consulta)"
        );
        comboTipoUsuario.getSelectionModel().selectFirst(); // Seleciona Admin por padrão

        if (lblErro != null) {
            lblErro.setVisible(false);
        }
    }

    @FXML
    void handleCadastrar(ActionEvent event) {
        if (lblErro != null) {
            lblErro.setVisible(false);
        }

        try {

            String nome = txtNomeUsuario.getText().trim();
            String senha = txtSenha.getText();
            String senhaConfirma = txtSenha1.getText();
            String perfilSelecionado = comboTipoUsuario.getValue(); // LER O PERFIL

            // ... (Verificações de CPF e Telefone) ...
            if (perfilSelecionado == null) {
                throw new BusinessException("Selecione um tipo de usuário para cadastro!");
            }

            // ... (Validações de senha e campos vazios) ...
            Usuario usuarioCriador = LoginController.getUsuarioLogado();
            // ... (Verificação de acesso negado) ...

            // NOVO: Chamada ao Service com o TIPO DE PERFIL
            boolean sucesso = administradorService.criarNovoUsuario(
                    nome,
                    txtCpf.getText().replaceAll("[^0-9]", ""),
                    txtTelefone.getText(),
                    senha,
                    perfilSelecionado,
                    usuarioCriador
            );

            if (sucesso) {
                lblErro.setText("Usuário '" + nome + "' cadastrado com sucesso como " + perfilSelecionado + ".");
                lblErro.setTextFill(Color.GREEN);
                lblErro.setVisible(true);
                limparCampos();
            } else {
                throw new BusinessException("Erro ao cadastrar usuário. CPF ou Nome de usuário podem já existir.");
            }

        } catch (BusinessException | DBException e) {
            // ... (Tratamento de erro)
            lblErro.setText(e.getMessage());
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
        } catch (Exception e) {
            // ... (Tratamento de erro inesperado)
            lblErro.setText("Erro inesperado: " + e.getMessage());
            lblErro.setTextFill(Color.RED);
            lblErro.setVisible(true);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNomeUsuario.setText("");
        txtSenha.setText("");
        txtSenha1.setText("");
        if (txtCpf != null) {
            txtCpf.setText("");
        }
        if (txtTelefone != null) {
            txtTelefone.setText("");
        }
        comboTipoUsuario.getSelectionModel().selectFirst(); // Volta para Admin
    }
}
