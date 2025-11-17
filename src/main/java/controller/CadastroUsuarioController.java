package controller;

import classes.Usuario;
import dao.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.Criptografia;

public class CadastroUsuarioController {

    @FXML
    private Button btnCadastrar;

    @FXML
    private Label lblMensagem;

    @FXML
    private TextField txtNomeUsuario;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private PasswordField txtSenha1;

    @FXML
    void handleCadastrar(ActionEvent event) {

        String nome = txtNomeUsuario.getText().trim();
        String senha = txtSenha.getText();
        String senhaConfirma = txtSenha1.getText();

        // ---- Validações ----
        if (nome.isEmpty() || senha.isEmpty() || senhaConfirma.isEmpty()) {
            lblMensagem.setText("Preencha todos os campos!");
            return;
        }

        if (!senha.equals(senhaConfirma)) {
            lblMensagem.setText("As senhas não conferem!");
            return;
        }

        try {
            // ---- Gera hash da senha ----
            String hashSenha = Criptografia.gerarHash(senha);

            // ---- Cria objeto Usuario (classe abstrata → não instancia diretamente) ----
            // Aqui assumo que existe uma subclasse, ex: Administrador
            Usuario novo = new Administrador(0, nome, hashSenha);

            // ---- Salva no banco ----
            UsuarioDAO dao = new UsuarioDAO();
            boolean sucesso = dao.inserirUsuario(novo);

            if (sucesso) {
                lblMensagem.setText("Usuário cadastrado com sucesso!");
                limparCampos();
            } else {
                lblMensagem.setText("Erro: nome de usuário já existente!");
            }

        } catch (Exception e) {
            lblMensagem.setText("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtNomeUsuario.setText("");
        txtSenha.setText("");
        txtSenha1.setText("");
    }
}
