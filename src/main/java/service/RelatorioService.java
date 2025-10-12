/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.AlunoDAO;
import DAO.IntegranteFamiliaDAO;
import classes.Aluno;
import classes.IntegranteFamilia;
import classes.Usuario;
import classes.UsuarioVulnerabilidadeSocial;
import java.io.File;
import util.BusinessException;
import util.DBException;
import java.util.List;

public class RelatorioService {

    private AlunoDAO alunoDAO;
    // Outras DAOs seriam necessárias para relatórios complexos (ex.: FamiliaDAO, EscolaDAO)

    public RelatorioService() {
        this.alunoDAO = new AlunoDAO();
    }
    
    /**
     * Gera uma lista de alunos com base em filtros específicos (RF009).
     * @param filtro O critério de filtro (ex.: "vacinacao em atraso", "turno Manhã").
     * @param usuarioLogado O usuário que está tentando realizar a operação.
     * @return Lista de alunos filtrados.
     */
    public List<Aluno> gerarRelatorioComFiltros(String filtro, Usuario usuarioLogado) throws BusinessException, DBException {
        
        // 1. Verificação de Permissão (Consulta Geral)
        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerar relatórios.");
        }
        
        // 2. Lógica de Filtragem (Mapeamento do filtro para a DAO)
        // Nota: Esta é uma simplificação. Em um sistema real, o DAO teria métodos específicos 
        // para cada filtro (ex.: findByVacinacaoAtraso, findByTurno).
        
        // Por enquanto, usaremos o buscarPorCriterio como base para filtros simples.
        if (filtro == null || filtro.trim().isEmpty()) {
            return alunoDAO.listarTodos();
        }
        
        // Exemplo de como um filtro específico pode ser tratado:
        if (filtro.equalsIgnoreCase("atraso vacinal")) {
            // Se houvesse um método específico: 
            // return alunoDAO.buscarAtrasoVacinal(); 
            
            // Usando o critério genérico por enquanto:
            return alunoDAO.buscarPorCriterio("vacinacao=0"); // Busca por valor no DB (simplificado)
        }
        
        // Caso não seja um filtro específico, usa a busca por nome/NIS
        return alunoDAO.buscarPorCriterio(filtro);
    }
    
    /**
     * Exporta os dados de uma lista de Alunos para um formato específico (RF010).
     * @param dados A lista de alunos a ser exportada.
     * @param formato O formato desejado ("PDF" ou "EXCEL").
     * @return O arquivo File gerado.
     * @throws BusinessException Se a exportação falhar ou o formato for inválido.
     */
    public File exportarDados(List<Aluno> dados, String formato) throws BusinessException {
        
        if (dados == null || dados.isEmpty()) {
            throw new BusinessException("A lista de dados para exportação está vazia.");
        }
        
        if (formato.equalsIgnoreCase("EXCEL")) {
            // Lógica para exportar para Excel (usando Apache POI, por exemplo)
            // File arquivoExcel = ExportUtil.gerarExcel(dados);
            // return arquivoExcel;
            return new File("relatorio_simpa.xlsx");
            
        } else if (formato.equalsIgnoreCase("PDF")) {
            // Lógica para exportar para PDF (usando iText, por exemplo)
            // File arquivoPdf = ExportUtil.gerarPDF(dados);
            // return arquivoPdf;
            return new File("relatorio_simpa.pdf");
            
        } else {
            throw new BusinessException("Formato de exportação inválido. Use 'EXCEL' ou 'PDF'.");
        }
    }
}