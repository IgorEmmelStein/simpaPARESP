/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author igore
 */
/**
 * Superclasse que representa uma Pessoa no contexto geral do SIMPA.
 * Contém atributos básicos de identificação.
 * (Base para Aluno e IntegranteFamilia).
 */
public class Pessoa {
    
    // --- Atributos base (do Diagrama de Classes e ER)  ---
    
    private int id; // Usado para mapear PKs (pk_cod_pessoa em aluno, pk_cod_integrante em integrante_familia)
    private String nome;
    private String cpf;
    private String telefone;

    // --- Construtores ---

    public Pessoa() {
    }

    public Pessoa(int id, String nome, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    // --- Getters e Setters (conforme Diagrama de Classes)  ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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
}
