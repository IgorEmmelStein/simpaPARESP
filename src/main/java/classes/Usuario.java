/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author igore
 */
public abstract class Usuario {

    // --- Atributos de Login/Identificação ---
    
    // O ID deve ser mapeado ao PK da tabela 'administrador' no DB (pk_cod_admin).
    private int id; 
    private String nomeUsuario; 
    // Armazena o hash da senha (por segurança), não a senha em texto plano.
    private String senhaHash;   

    // --- Construtores ---

    public Usuario() {
    }
    
    // Construtor principal (para uso nas subclasses)
    public Usuario(int id, String nomeUsuario, String senhaHash) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.senhaHash = senhaHash;
    }
    
    // --- Métodos de Permissão (Comportamento/Lógica de Controle de Acesso) ---
    
    /**
     * Tenta realizar o login. A lógica de verificação de hash será implementada
     * na camada Service/DAO, que tem acesso ao DB.
     * @param senha A senha fornecida pelo usuário.
     * @return true se o login for bem-sucedido, false caso contrário.
     */
    public boolean fazerLogin(String senha) {
        return false; 
    }

    // --- Métodos de Permissão (Abstratos - Devem ser implementados nas subclasses) ---
    
    // Permissão de CONSULTA (Leitura) - RF004, RF009
    /**
     * Verifica se o usuário tem permissão para realizar consultas gerais.
     */
    public abstract boolean podeConsultarGeral();

    // Permissão de ALTERAÇÃO e INCLUSÃO (Escrita) - RF001, RF002, RF005, RF006, RF007, RF008
    /**
     * Verifica se o usuário tem permissão para incluir ou alterar dados gerais.
     */
    public abstract boolean podeAlterarIncluirGeral();

    // Permissão de EXCLUSÃO (Deleção) - RF003
    /**
     * Verifica se o usuário tem permissão para excluir cadastros gerais.
     */
    public abstract boolean podeExcluirGeral();
    
    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
}
