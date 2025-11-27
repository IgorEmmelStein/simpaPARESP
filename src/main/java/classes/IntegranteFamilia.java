package classes;

public class IntegranteFamilia extends Pessoa {

    private int fkCodFamilia;
    private String parentesco;
    private boolean vinculoAfetivo;
    private String ocupacao;
    private String endereco;

    // CORRIGIDO: Atributos padronizados
    private boolean responsavelLegal;
    private boolean pessoaAutorizada;

    private String anotacoes;

    public IntegranteFamilia() {
        super();
    }

    // --- Getters e Setters de Atributos Específicos ---
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

    // CORRIGIDO: isVinculoAfetivo()
    public boolean isVinculoAfetivo() {
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

    // CORRIGIDO: isResponsavelLegal() e setResponsavelLegal()
    public boolean isResponsavelLegal() {
        return responsavelLegal;
    }

    public void setResponsavelLegal(boolean responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    // CORRIGIDO: isPessoaAutorizada() e setPessoaAutorizada()
    public boolean isPessoaAutorizada() {
        return pessoaAutorizada;
    }

    public void setPessoaAutorizada(boolean pessoaAutorizada) {
        this.pessoaAutorizada = pessoaAutorizada;
    }
}
