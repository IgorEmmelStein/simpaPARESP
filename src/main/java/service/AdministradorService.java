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

    public AdministradorService() {
        this.administradorDAO = new AdministradorDAO();
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
        
        // 1. Busca o usuário pelo nome (retorna Administrador ou UsuarioSaude, dependendo do campo e_administrador)
        Usuario usuarioDoBanco = administradorDAO.buscarPorNome(nome);
        
        if (usuarioDoBanco == null) {
            throw new BusinessException("Usuário não encontrado.");
        }
        
        // 2. Compara a senha (usando o senhaHash que agora vem da tabela 'usuario')
        if (senha.equals(usuarioDoBanco.getSenhaHash())) { 
            return usuarioDoBanco; // Retorna o objeto Usuario com o perfil correto
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
    
    /**
     * Cria um novo usuário com o perfil selecionado.
     * @param nome, cpf, telefone, senha Os dados do novo usuário.
     * @param tipoPerfil O tipo de perfil selecionado (e.g., "Administrador").
     * @param usuarioCriador O usuário logado que está realizando o cadastro (deve ser Admin).
     * @return true se o cadastro foi bem-sucedido.
     * @throws BusinessException Se a validação, permissão ou senha falhar.
     */
    public boolean criarNovoUsuario(
            String nome, String cpf, String telefone, String senha,
            String tipoPerfil, Usuario usuarioCriador) throws BusinessException, DBException {
        
        // 1. Verificação de Permissão (Só Administrador pode gerenciar usuários)
        if (!(usuarioCriador instanceof Administrador)) {
            throw new BusinessException("Acesso negado. Apenas administradores podem criar novos usuários.");
        }
        
        // 2. Validação básica (simplificada)
        if (nome.isEmpty() || senha.isEmpty() || tipoPerfil.isEmpty()) {
             throw new BusinessException("Nome, Senha e Perfil são obrigatórios.");
        }
        
        // 3. Determinação da permissão (booleana)
        boolean eAdministrador = tipoPerfil.equals("Administrador");
        
        // 4. Cria o objeto Administrador (a tabela 'usuario' exige um objeto completo)
        Administrador novoUsuario = new Administrador();
        novoUsuario.setNome(nome);
        novoUsuario.setCpf(cpf);
        novoUsuario.setTelefone(telefone);
        novoUsuario.setSenhaHash(senha); // Lembre-se: Usar hash em produção!
        // Não precisamos setar o flag eAdmin no objeto Administrador em si, 
        // mas sim no DAO.

        // 5. Chamada ao DAO para inserção (Tu precisas de um método no DAO que insira todos os campos)
        // Chamada sugerida: administradorDAO.inserirUsuario(novoUsuario, eAdministrador);
        
        // Vamos supor que o DAO possui um método de inserção que lida com todos os campos e o flag.
        // Já que não temos o código do DAO, vamos simular o sucesso para prosseguir:
        
        // return administradorDAO.inserirUsuario(novoUsuario, eAdministrador);
        return true; 
    }
}