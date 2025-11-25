/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.Date;
import java.util.List; 


/**
 * Representa um Aluno atendido pela PARESP. inter maior teste
 * Herda de Pessoa e contém todos os dados de cadastro e acompanhamento (RF001, RF005, RF008).
 * Mapeia a tabela 'aluno' no banco de dados.
 */
public class Aluno extends Pessoa {
    
    // --- Chaves Estrangeiras (Mapeamento) ---
    private int fkCodAdmin;    
    private int fkCodEscola;   

    // --- Atributos de Cadastro e Logística (RF001) ---
    private Date dataAcolhimento;
    private String descricaoVulnerabilidade;
    private String formaAcesso;     
    private boolean vacinacaoEmDia; 
    private boolean termoImagemAssinado; 
    private String turno;           
    private String transporte;      
    private Date dataNascimento;    
    private boolean projetoFerias;   
    private String sexo;            
    private String tamanhoVestuario; 
    private int tamanhoCalcado;     
    private String turma;           
    private String numNIS;          
    private int status;             

    // --- Atributos de Saúde (RF005) ---
    private String medicamentosUso;  // med_paresp
    private String alergias;         
    private String observacoesMedicas; 

    // --- Atributos de Acompanhamento (RF008) ---
    private String intervencoes;     
    private String evolucoes;        
    
    // --- Relacionamentos de Objetos ---
    private Escola escola;
    private Familia familia; 
    
    // --- Construtores ---
    // (Omitidos, mas devem existir)

    // --- Getters e Setters ---
    
    // Setters e Getters de ID, Nome, CPF, Telefone são herdados de Pessoa
    
    public int getFkCodAdmin() {
        return fkCodAdmin;
    }

    public void setFkCodAdmin(int fkCodAdmin) {
        this.fkCodAdmin = fkCodAdmin;
    }

    public int getFkCodEscola() {
        return fkCodEscola;
    }

    public void setFkCodEscola(int fkCodEscola) {
        this.fkCodEscola = fkCodEscola;
    }

    public Date getDataAcolhimento() {
        return dataAcolhimento;
    }

    public void setDataAcolhimento(Date dataAcolhimento) {
        this.dataAcolhimento = dataAcolhimento;
    }
    
    public String getDescricaoVulnerabilidade() {
        return descricaoVulnerabilidade;
    }

    public void setDescricaoVulnerabilidade(String descricaoVulnerabilidade) {
        this.descricaoVulnerabilidade = descricaoVulnerabilidade;
    }

    public String getFormaAcesso() {
        return formaAcesso;
    }

    public void setFormaAcesso(String formaAcesso) {
        this.formaAcesso = formaAcesso;
    }

    public boolean isVacinacaoEmDia() {
        return vacinacaoEmDia;
    }

    public void setVacinacaoEmDia(boolean vacinacaoEmDia) {
        this.vacinacaoEmDia = vacinacaoEmDia;
    }

    public boolean isTermoImagemAssinado() {
        return termoImagemAssinado;
    }

    public void setTermoImagemAssinado(boolean termoImagemAssinado) {
        this.termoImagemAssinado = termoImagemAssinado;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    // Corrigido para isProjetoFerias()
    public boolean isProjetoFerias() { 
        return projetoFerias;
    }

    public void setProjetoFerias(boolean projetoFerias) {
        this.projetoFerias = projetoFerias;
    }

    // Corrigido para getSexo()
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTamanhoVestuario() {
        return tamanhoVestuario;
    }

    public void setTamanhoVestuario(String tamanhoVestuario) {
        this.tamanhoVestuario = tamanhoVestuario;
    }

    public int getTamanhoCalcado() {
        return tamanhoCalcado;
    }

    public void setTamanhoCalcado(int tamanhoCalcado) {
        this.tamanhoCalcado = tamanhoCalcado;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getNumNIS() {
        return numNIS;
    }

    public void setNumNIS(String numNIS) {
        this.numNIS = numNIS;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMedicamentosUso() {
        return medicamentosUso;
    }

    public void setMedicamentosUso(String medicamentosUso) {
        this.medicamentosUso = medicamentosUso;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getObservacoesMedicas() {
        return observacoesMedicas;
    }

    public void setObservacoesMedicas(String observacoesMedicas) {
        this.observacoesMedicas = observacoesMedicas;
    }

    public String getIntervencoes() {
        return intervencoes;
    }

    public void setIntervencoes(String intervencoes) {
        this.intervencoes = intervencoes;
    }

    public String getEvolucoes() {
        return evolucoes;
    }

    public void setEvolucoes(String evolucoes) {
        this.evolucoes = evolucoes;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }
}