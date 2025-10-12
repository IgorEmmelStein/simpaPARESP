/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author igore
 */
public class UsuarioVulnerabilidadeSocial extends Usuario {

    // --- Construtores ---

    public UsuarioVulnerabilidadeSocial() {
        super();
    }
    
    // Construtor principal
    public UsuarioVulnerabilidadeSocial(int id, String nomeUsuario, String senhaHash) {
        super(id, nomeUsuario, senhaHash);
    }
    
    // --- Métodos de Permissão (Sobrescrita e Específicas) ---
    
    // Permissão de CONSULTA (Leitura)
    // Este perfil precisa consultar dados gerais para referência.
    @Override
    public boolean podeConsultarGeral() {
        return true; 
    }

    // Permissão de ALTERAÇÃO e INCLUSÃO (Escrita)
    // Não pode alterar ou incluir dados gerais (como saúde, ou dados de matrícula principais).
    @Override
    public boolean podeAlterarIncluirGeral() {
        return false; 
    }

    // Permissão de EXCLUSÃO (Deleção)
    @Override
    public boolean podeExcluirGeral() {
        // Exclusão é restrita ao Administrador.
        return false;
    }
    
    // --- Permissões Específicas de VULNERABILIDADE SOCIAL (RF006) ---
    
    /**
     * Permissão para ver dados sensíveis de vulnerabilidade social e familiar.
     */
    public boolean podeVerVulnerabilidadeSocial() {
        return true; 
    }

    /**
     * Permissão para alterar ou incluir dados familiares, de residência e integrantes (RF006).
     */
    public boolean podeAlterarIncluirVulnerabilidadeSocial() {
        return true;
    }
}
