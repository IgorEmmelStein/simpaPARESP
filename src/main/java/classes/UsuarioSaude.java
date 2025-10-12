/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author igore
 */
public class UsuarioSaude extends Usuario {

    // --- Construtores ---

    public UsuarioSaude() {
        super();
    }
    
    // Construtor principal 
    public UsuarioSaude(int id, String nomeUsuario, String senhaHash) {
        super(id, nomeUsuario, senhaHash);
    }
    
    // --- Métodos de Permissão (Sobrescrita e Específicas) ---
    
    // Permissão de CONSULTA (Leitura)
    // Geralmente, perfis específicos podem consultar tudo o que for geral.
    @Override
    public boolean podeConsultarGeral() {
        return true; 
    }

    // Permissão de ALTERAÇÃO e INCLUSÃO (Escrita)
    // Este perfil só pode editar dados que sejam de sua área, mas não é um Administrador.
    @Override
    public boolean podeAlterarIncluirGeral() {
        // Retorna false para inclusão/alteração de dados gerais (ex.: endereço, matrícula)
        return false; 
    }

    // Permissão de EXCLUSÃO (Deleção)
    @Override
    public boolean podeExcluirGeral() {
        // Nenhum perfil, exceto Administrador, deve ter essa permissão por padrão.
        return false;
    }
    
    // --- Permissões Específicas de SAÚDE ---
    
    /**
     * Permissão para ver dados sensíveis de saúde (medicações, alergias, vacinação).
     */
    public boolean podeVerSaude() {
        return true; 
    }

    /**
     * Permissão para alterar ou incluir dados de saúde (RF005).
     */
    public boolean podeAlterarIncluirSaude() {
        return true;
    }
    
    // Os métodos 'podeVerSaude' e 'podeAlterarIncluirSaude' serão usados
    // na camada Service para controlar o acesso antes de realizar a operação no DAO.
}
