/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

public class IntegranteFamilia extends Pessoa {
    
    // --- Atributos específicos (do Diagrama ER) ---
    private int fkCodFamilia;       // fk_cod_familia (Chave estrangeira para a Família)
    private String parentesco;
    private boolean vinculoAfetivo;  // vinculo_afetivo (TINYINT)
    private String ocupacao;
    private String endereco;        // Endereço específico, se diferente do da família
    private boolean eResponsavelLegal; // resp_legal (TINYINT)
    private boolean ePessoaAutorizada; // pessoa_autorizada (TINYINT - RF007)

    // --- Construtores ---
    
    public IntegranteFamilia() {
        super();
    }
    
    // Construtor completo (omitido por brevidade)
    // ...

    // --- Getters e Setters (Obrigatórios) ---

    // Getters/Setters de nome, cpf, telefone herdados de Pessoa
    
    public int getFkCodFamilia() {
        return fkCodFamilia;
    }

    public void setFkCodFamilia(int fkCodFamilia) {
        this.fkCodFamilia = fkCodFamilia;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public boolean iseVinculoAfetivo() {
        return vinculoAfetivo;
    }

    public void setVinculoAfetivo(boolean vinculoAfetivo) {
        this.vinculoAfetivo = vinculoAfetivo;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean iseResponsavelLegal() {
        return eResponsavelLegal;
    }

    public void seteResponsavelLegal(boolean eResponsavelLegal) {
        this.eResponsavelLegal = eResponsavelLegal;
    }

    public boolean isePessoaAutorizada() {
        return ePessoaAutorizada;
    }

    public void setePessoaAutorizada(boolean ePessoaAutorizada) {
        this.ePessoaAutorizada = ePessoaAutorizada;
    }
}
