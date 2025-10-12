/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 * Representa uma Escola que os alunos frequentam.
 * Mapeia a tabela 'escola' no banco de dados (RF014).
 */
public class Escola {
    
    // --- Atributos (do Diagrama ER) ---
    private int id;     // pk_cod_escola [cite: 192]
    private String nome; // nome VARCHAR(45) [cite: 193]
    private String serie; // serie VARCHAR(3) [cite: 194]

    // --- Construtores ---

    public Escola() {
    }

    public Escola(int id, String nome, String serie) {
        this.id = id;
        this.nome = nome;
        this.serie = serie;
    }

    // --- Getters e Setters ---

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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    // --- Métodos de Negócio (Lógica de Modelagem) ---
    // Estes métodos (cadastrar, editar, buscar) serão implementados na camada Service, 
    // usando o DAO internamente, conforme o plano de arquitetura.
    // Estão aqui apenas como referência do Diagrama de Classes.

    /**
     * Retorna uma representação amigável do objeto.
     */
    @Override
    public String toString() {
        return nome + " - " + serie;
    }
}
