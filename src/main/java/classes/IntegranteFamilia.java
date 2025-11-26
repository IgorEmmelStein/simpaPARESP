/*
 * Click nbfs:
 * Click nbfs:
 */
package classes;

public class IntegranteFamilia extends Pessoa {

    private int fkCodFamilia;
    private String parentesco;
    private boolean vinculoAfetivo;
    private String ocupacao;
    private String endereco;
    private boolean eResponsavelLegal;
    private boolean ePessoaAutorizada;
    private String anotacoes;

    public IntegranteFamilia() {
        super();
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

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
