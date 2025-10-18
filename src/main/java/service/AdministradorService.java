/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;

import DAO.AdministradorDAO;
import classes.Administrador;
import classes.Usuario;
import util.BusinessException;
import util.DBException;

/**
 * Classe responsável pela lógica de Autenticação, Login e Gestão de Usuários/Perfis.
 * (RF011 e RF012).
 */
public class AdministradorService {
    
    private AdministradorDAO administradorDAO;
    // Classe utilitária para hashing de senhas, que assumimos existir
    // private HashUtil hashUtil; 

    public AdministradorService() {
        this.administradorDAO = new AdministradorDAO();
        // this.hashUtil = new HashUtil(); // Instancia o utilitário de hash
    }
    
    /** 
     * Tenta autenticar um usuário no sistema.
     * @param cpf O CPF (usuário) fornecido.
     * @param senha A senha em texto plano fornecida.
     * @return O objeto Usuario (Administrador) logado.
     * @throws BusinessException Se as credenciais estiverem inválidas.
     * @throws DBException Se houver erro de comunicação com o banco.
     */
    public Usuario login(String nome, String senha) throws BusinessException, DBException {
        if (nome == null || senha == null || nome.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new BusinessException("Login e senha são obrigatórios.");
        }
        
        // 1. Busca o administrador pelo CPF
        Administrador adminDoBanco = administradorDAO.buscarPorNome(nome);
        
        if (adminDoBanco == null) {
            throw new BusinessException("Usuário não encontrado.");
        }
        
        // 2. Compara a senha (aqui deveria usar um utilitário de hash seguro)
        // Por enquanto, faremos uma comparação simples (NÃO RECOMENDADO EM PRODUÇÃO!)
        
        // if (hashUtil.verificarSenha(senha, adminDoBanco.getSenhaHash())) {
        if (senha.equals(adminDoBanco.getSenhaHash())) { // Substituir pela lógica de hash real
            // Lógica de mapeamento de Perfil (RF012):
            // O Admin logado é retornado com o objeto completo e todas as permissões ativas.
            return adminDoBanco; 
        } else {
            throw new BusinessException("Senha inválida.");
        }
    }
    
    /**
     * Cria um novo usuário (RF012: Criar diferentes perfis de usuário).
     * Nota: No teu ER, todos os usuários estão na tabela 'administrador'.
     * A lógica real pode exigir criar um Administrador, UsuarioSaude, etc., e persistir.
     * @param novoAdmin O Administrador a ser cadastrado.
     * @param usuarioCriador O Administrador que está realizando o cadastro.
     * @throws BusinessException Se a validação ou permissão falhar.
     */
    public void criarUsuario(Administrador novoAdmin, Usuario usuarioCriador) throws BusinessException, DBException {
        // 1. Verificação de Permissão (Só Administrador pode gerenciar usuários)
        if (!(usuarioCriador instanceof Administrador) || !((Administrador) usuarioCriador).podeGerenciarUsuarios()) {
            throw new BusinessException("Acesso negado. Apenas administradores podem criar novos usuários.");
        }
        
        // 2. Validação básica
        if (novoAdmin.getNome() == null || novoAdmin.getCpf() == null || novoAdmin.getSenhaHash() == null) {
            throw new BusinessException("Nome, CPF e Senha são obrigatórios para o cadastro de usuário.");
        }
        
        // 3. Hash da senha (se a senhaHash for a senha em texto plano)
        // novoAdmin.setSenhaHash(hashUtil.gerarHash(novoAdmin.getSenhaHash()));
        
        // 4. Inserção (implementação futura na AdministradorDAO)
        // administradorDAO.inserir(novoAdmin);
        
        // Por enquanto, apenas a lógica de regras de negócio.
    }
}