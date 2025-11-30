package controller;

import classes.Administrador;
import classes.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; 
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import service.AdministradorService;
import util.BusinessException;
import util.DBException;

public class CadastroUsuarioController implements Initializable {

    @FXML 
    private TextField txtNomeUsuario;
    @FXML 
    private PasswordField txtSenha;
    @FXML 
    private PasswordField txtSenha1;
    @FXML 
    private Label lblMensagem;
    
    @FXML private TextField txtTelefone;
    
    @FXML 
    private CheckBox cbAdmin;
    @FXML 
    private CheckBox cbSaude;
    @FXML 
    private CheckBox cbSocial;

    private AdministradorService service;

    
    private AdministradorService administradorService;

    // Construtor
    public CadastroUsuarioController() {
        this.administradorService = new AdministradorService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.service = new AdministradorService();
    }

    @FXML
    void handleCadastrar(ActionEvent event) {
        lblMensagem.setVisible(false);

        try {
            String nome = txtNomeUsuario.getText();
            String senha = txtSenha.getText();
            String confirmacao = txtSenha1.getText();
            
            if (!senha.equals(confirmacao)) {
                lblMensagem.setText("As senhas não conferem.");
                lblMensagem.setTextFill(Color.RED);
                return;
            }

            String telefone = (txtTelefone != null) ? txtTelefone.getText() : ""; // Telefone é opcional

            // 2. Validações
            if (nome.isEmpty() || senha.isEmpty() || confirmacao.isEmpty()) {
                throw new BusinessException("Nome e Senha são obrigatórios!");
            }

            if (!senha.equals(confirmacao)) {
                throw new BusinessException("As senhas não conferem!");
            }

            // 3. Obter usuário logado (para permissão)
            Usuario usuarioLogado = LoginController.getUsuarioLogado(); //
            if (usuarioLogado == null) {
                throw new BusinessException("Acesso negado. Faça login como Administrador.");
            }

            // 4. Preparar o objeto Administrador
            Administrador novoAdmin = new Administrador();
            novoAdmin.setNome(nome);
            novoAdmin.setSenhaHash(senha); 
            
            novoAdmin.setPermAdmin(cbAdmin.isSelected());
            novoAdmin.setPermSaude(cbSaude.isSelected());
            novoAdmin.setPermSocial(cbSocial.isSelected());
            
            if (!novoAdmin.isAdmin() && !novoAdmin.isSaude() && !novoAdmin.isSocial()) {
                 throw new BusinessException("Selecione pelo menos um perfil de acesso.");
            }
            
            if(txtTelefone != null) novoAdmin.setTelefone(txtTelefone.getText());

            Usuario logado = LoginController.getUsuarioLogado();

            service.criarUsuario(novoAdmin, logado);

            lblMensagem.setText("Sucesso!");
            lblMensagem.setTextFill(Color.GREEN);
            lblMensagem.setVisible(true);
            cbAdmin.setSelected(false);
            cbSaude.setSelected(false);
            cbSocial.setSelected(false);
            limparCampos();

        } catch (BusinessException | DBException e) {
            lblMensagem.setText("Erro inesperado: " + e.getMessage());
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setVisible(true);
            e.printStackTrace();
        } 
    }

    private void limparCampos() {
        txtNomeUsuario.clear();
        txtSenha.clear();
        txtSenha1.clear();
    }
}