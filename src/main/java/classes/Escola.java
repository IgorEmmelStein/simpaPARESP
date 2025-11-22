/*
 * Click nbfs:
 * Click nbfs:
 */
package classes;

/**
 * Representa uma Escola que os alunos frequentam. Mapeia a tabela 'escola' no
 * banco de dados (RF014).
 */
public class Escola {

    private int id;
    private String nome;
    private String serie;

    public Escola() {
    }

    public Escola(int id, String nome, String serie) {
        this.id = id;
        this.nome = nome;
        this.serie = serie;
    }

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

    @Override
    public String toString() {
        return nome; 
    }
}
