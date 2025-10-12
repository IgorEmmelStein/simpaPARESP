/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 * Representa o núcleo familiar do aluno, com foco em dados de vulnerabilidade social.
 * Mapeia a tabela 'familia' no banco de dados (RF006).
 */
public class Familia {
    
    // --- Atributos (do Diagrama ER) ---
    private int id;                 // pk_cod_familia
    private int fkCodPessoa;        // fk_cod_pessoa (Chave estrangeira do Aluno)
    private int qtdIntegrantes;     // qnt_integrantes_fam
    private double rendaFamiliarTotal; // renda_familiar_total
    private double valorBolsaFamilia;  // bolsa_familia (DECIMAL(8,2))
    
    // Endereço e Moradia
    private String endereco;        // endereco_familia
    private String bairro;          // bairro_familia
    private String tipoResidencia;  // residencia
    private double valorAluguel;    // valor_aluguel
    private String telefoneContato; // telefone_familia

    // --- Relacionamentos de Objetos ---
    // Representa o aluno ao qual esta família está associada
    private Aluno aluno; 
    
    // A lista de integrantes da família é mantida na classe IntegranteFamilia

    // --- Construtores ---
    
    public Familia() {
    }
    
    // Construtor completo (omitido por brevidade)
    // ...

    // --- Getters e Setters (Obrigatórios) ---
    
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
}
