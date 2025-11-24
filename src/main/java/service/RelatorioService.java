/*
 * Click nbfs:
 * Click nbfs:
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

    public RelatorioService() {
        this.alunoDAO = new AlunoDAO();
    }

    public List<Aluno> gerarRelatorioComFiltros(String filtro, Usuario usuarioLogado) throws BusinessException, DBException {

        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerar relatórios.");
        }

        if (filtro == null || filtro.trim().isEmpty()) {
            return alunoDAO.listarTodos();
        }

        if (filtro.equalsIgnoreCase("atraso vacinal")) {

            return alunoDAO.buscarPorCriterio("vacinacao=0");
        }

        return alunoDAO.buscarPorCriterio(filtro);
    }

    public File exportarDados(List<Aluno> dados, String formato) throws BusinessException {

        if (dados == null || dados.isEmpty()) {
            throw new BusinessException("A lista de dados para exportação está vazia.");
        }

        if (formato.equalsIgnoreCase("EXCEL")) {

            return new File("relatorio_simpa.xlsx");

        } else if (formato.equalsIgnoreCase("PDF")) {

            return new File("relatorio_simpa.pdf");

        } else {
            throw new BusinessException("Formato de exportação inválido. Use 'EXCEL' ou 'PDF'.");
        }
    }
}
