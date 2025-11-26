/*
 * Click nbfs:
 * Click nbfs:
 */
package classes;

/**
 * Representa o núcleo familiar do aluno, com foco em dados de vulnerabilidade
 * social. Mapeia a tabela 'familia' no banco de dados (RF006).
 */
public class Familia {

    private int id;
    private int fkCodPessoa;
    private int qtdIntegrantes;
    private double rendaFamiliarTotal;
    private double valorBolsaFamilia;
    private String anotacoes;

    private String endereco;
    private String bairro;
    private String tipoResidencia;
    private double valorAluguel;
    private String telefoneContato;

    private Aluno aluno;

    public Familia() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFkCodPessoa() {
        return fkCodPessoa;
    }

    public void setFkCodPessoa(int fkCodPessoa) {
        this.fkCodPessoa = fkCodPessoa;
    }

    public int getQtdIntegrantes() {
        return qtdIntegrantes;
    }

    public void setQtdIntegrantes(int qtdIntegrantes) {
        this.qtdIntegrantes = qtdIntegrantes;
    }

    public double getRendaFamiliarTotal() {
        return rendaFamiliarTotal;
    }

    public void setRendaFamiliarTotal(double rendaFamiliarTotal) {
        this.rendaFamiliarTotal = rendaFamiliarTotal;
    }

    public double getValorBolsaFamilia() {
        return valorBolsaFamilia;
    }

    public void setValorBolsaFamilia(double valorBolsaFamilia) {
        this.valorBolsaFamilia = valorBolsaFamilia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTipoResidencia() {
        return tipoResidencia;
    }

    public void setTipoResidencia(String tipoResidencia) {
        this.tipoResidencia = tipoResidencia;
    }

    public double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }
}
