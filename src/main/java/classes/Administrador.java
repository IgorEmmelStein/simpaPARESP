/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author igore
 */
public class Administrador extends Usuario {

    // --- Atributos específicos do Administrador (do ER) ---
    private String telefone;
    private String nome;
    private boolean permSaude;
    private boolean permSocial;
    private boolean permAdmin;
    // --- Construtores ---

    public Administrador() {
        super();
    }
    
    // Construtor completo
    public Administrador(int id, String nomeUsuario, String senhaHash, 
                         String telefone, String nome) {
        
        super(id, nomeUsuario, senhaHash);
        this.telefone = telefone;
        this.nome = nome;
    }

    // --- Sobrescrita dos Métodos de Permissão (RF012) ---

    // Administrador pode consultar (RF004, RF009)
    @Override
    public boolean podeConsultarGeral() {
        return true; 
    }

    // Administrador pode alterar e incluir (RF001, RF002, RF005-RF008)
    @Override
    public boolean podeAlterarIncluirGeral() {
        return true;
    }

    // Administrador pode excluir (RF003)
    @Override
    public boolean podeExcluirGeral() {
        return true;
    }
    
    // --- Permissões Específicas (do Diagrama de Classes de Permissão) ---
    
    public boolean podeGerenciarUsuarios() {
        // Corresponde a 'Criar diferentes perfis de usuário' (RF012)
        return true;
    }

    public boolean podeExcluirTudo() {
        // Permissão de exclusão total (a mais sensível)
        return true;
    }
    
    // --- Getters e Setters dos atributos específicos ---
    
    public boolean isSaude() { 
        return permSaude; 
    }
    public boolean isSocial() { 
        return permSocial; 
    }
    public boolean isAdmin() { 
        return permAdmin; 
    }
    
    public void setPermSaude(boolean permSaude) { 
        this.permSaude = permSaude; 
    }
    public void setPermSocial(boolean permSocial) {
        this.permSocial = permSocial; 
    }
    public void setPermAdmin(boolean permAdmin) { 
        this.permAdmin = permAdmin; 
    }
    
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    // Sobrescreve o setNomeUsuario para usar o nome real (simplificação)
    @Override
    public void setNomeUsuario(String nome) {
        super.setNomeUsuario(nome);
        this.nome = nome;
    }
}
