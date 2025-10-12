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
    private String cpf; 
    private String telefone;
    private String nome;
    // O atributo 'senha' do ER será mapeado para 'senhaHash' na classe Usuario.
    // O atributo 'nivelAcesso' do diagrama de classes pode ser inferido por esta classe.
    
    // --- Construtores ---

    public Administrador() {
        super();
    }
    
    // Construtor completo
    public Administrador(int id, String nomeUsuario, String senhaHash, 
                         String cpf, String telefone, String nome) {
        
        super(id, nomeUsuario, senhaHash);
        this.cpf = cpf;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
