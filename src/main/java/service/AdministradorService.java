package service;

import DAO.AdministradorDAO;
import classes.Administrador;
import classes.Usuario;
import util.BusinessException;
import util.DBException;
import org.mindrot.jbcrypt.BCrypt; 

public class AdministradorService {
    
    private AdministradorDAO administradorDAO;

    public AdministradorService() {
        this.administradorDAO = new AdministradorDAO();
    }
    
    // --- LOGIN SEGURO ---
    public Usuario login(String nome, String senha) throws BusinessException, DBException {
        if (nome == null || senha == null || nome.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new BusinessException("Login e senha são obrigatórios.");
        }
        
        Administrador adminDoBanco = administradorDAO.buscarPorNome(nome);
        
        if (adminDoBanco == null) {
            throw new BusinessException("Usuário não encontrado.");
        }
        
        // VERIFICAÇÃO COM BCRYPT (Compara senha pura com o hash do banco)
        if (BCrypt.checkpw(senha, adminDoBanco.getSenhaHash())) {
            return adminDoBanco; 
        } else {
            throw new BusinessException("Senha inválida.");
        }
    }
    
    // --- CRIAR USUÁRIO COM HASH ---
    public void criarUsuario(Administrador novoAdmin, Usuario usuarioCriador) throws BusinessException, DBException {
        
        // (Validações básicas omitidas para brevidade, mantenha as que já tinha)
        if (!(usuarioCriador instanceof Administrador)) {
            throw new BusinessException("Acesso negado.");
        }
        
        // GERA O HASH ANTES DE SALVAR
        String senhaPura = novoAdmin.getSenhaHash();
        String hash = BCrypt.hashpw(senhaPura, BCrypt.gensalt());
        novoAdmin.setSenhaHash(hash);
        
        // Chama o DAO para inserir
        administradorDAO.inserir(novoAdmin);
    }
}