package controller;

import classes.Administrador;
import classes.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent; // Adicionado
import javafx.fxml.FXML;
import javafx.fxml.Initializable; // Adicionado
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color; // Adicionado

// --- Imports corretos da sua arquitetura ---
import service.AdministradorService; //
import util.BusinessException; //
import util.DBException; //

/**
 * Controller para a tela de Cadastro de Usuário.
 * ATENÇÃO: Este código assume que o FXML possui os campos:
 * txtNomeUsuario, txtCpf (para o admin), txtSenha, txtSenha1 (confirmação).
 * Se o FXML não tiver o txtCpf, este código falhará.
 */
public class CadastroUsuarioController implements Initializable {

    @FXML
    private Button btnCadastrar; // ID do seu novo código

    @FXML
    private Label lblMensagem;

    @FXML
    private TextField txtNomeUsuario; // ID do seu novo código

    @FXML
    private PasswordField txtSenha; // ID do seu novo código

    @FXML
    private PasswordField txtSenha1; // ID do seu novo código
    
    // --- CAMPOS QUE FALTAM NO SEU FXML, MAS SÃO OBRIGATÓRIOS ---
    // Você PRECISA adicionar estes campos no FXML para o código funcionar
    @FXML
    private TextField txtCpf; 
    
    @FXML
    private TextField txtTelefone;
    
    // (Também faltam os RadioButtons de perfil, mas vamos focar no Admin por enquanto)
    
    private AdministradorService administradorService;

    // Construtor
    public CadastroUsuarioController() {
        this.administradorService = new AdministradorService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblMensagem.setVisible(false);
    }


    @FXML
    void handleCadastrar(ActionEvent event) {
        lblMensagem.setVisible(false);

        try {
            // 1. Obter dados dos campos
            String nome = txtNomeUsuario.getText().trim();
            String senha = txtSenha.getText();
            String senhaConfirma = txtSenha1.getText();
            
            // --- VALIDAÇÃO CRÍTICA ---
            // O FXML que você usou como base não tem CPF, mas o seu Service/DAO *exige* um.
            if (txtCpf == null) {
                 throw new BusinessException("Erro de FXML: O campo 'txtCpf' não foi injetado. Verifique o Scene Builder.");
            }
            
            String cpf = txtCpf.getText().replaceAll("[^0-9]", ""); // Limpa o CPF
            String telefone = (txtTelefone != null) ? txtTelefone.getText() : ""; // Telefone é opcional

            // 2. Validações
            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || senhaConfirma.isEmpty()) {
                throw new BusinessException("Nome, CPF e Senha são obrigatórios!");
            }

            if (!senha.equals(senhaConfirma)) {
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
            novoAdmin.setCpf(cpf); // CPF é obrigatório para o DAO
            novoAdmin.setTelefone(telefone);

            

            // 6. Chamar o SERVICE (Forma correta)
            administradorService.criarUsuario(novoAdmin, usuarioLogado);

            // 7. Sucesso
            lblMensagem.setText("Usuário cadastrado com sucesso!");
            lblMensagem.setTextFill(Color.GREEN);
            lblMensagem.setVisible(true);
            limparCampos();

        } catch (BusinessException | DBException e) { //
            lblMensagem.setText(e.getMessage());
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setVisible(true);
        } catch (Exception e) {
            lblMensagem.setText("Erro inesperado: " + e.getMessage());
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setVisible(true);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNomeUsuario.setText("");
        txtSenha.setText("");
        txtSenha1.setText("");
        if (txtCpf != null) txtCpf.setText("");
        if (txtTelefone != null) txtTelefone.setText("");
    }
}