/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.AlunoDAO;

import classes.Aluno; 
import classes.Usuario;
import java.util.List;
import util.BusinessException;
import util.DBException;

public class AlunoService {
    private AlunoDAO alunoDao;
    
    public AlunoService(){
        this.alunoDao = new AlunoDAO();
    }
    
    public List<Aluno> listarTodosAlunos() throws DBException {
        return alunoDao.listarTodos(); 
    }
    /**
     * Busca alunos no banco de dados com base em um critério de filtro.
     * @param criterio O texto de busca (nome, CPF, NIS, etc.).
     * @return Lista de alunos filtrada.
     */
    public List<Aluno> buscarAlunosPorCriterio(String criterio) throws DBException {
        // O AlunoDAO já tem o método buscarPorCriterio implementado.
        return alunoDao.buscarPorCriterio(criterio);
    }
    
    public boolean excluir(int alunoId, Usuario usuarioLogado) throws BusinessException, DBException {
        
        if (usuarioLogado == null || !usuarioLogado.podeExcluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para inativar alunos.");
        }
        
        // A DAO.excluir executa o UPDATE aluno SET status = 0
        boolean sucesso = alunoDao.excluir(alunoId); 
        
        if (!sucesso) {
            throw new BusinessException("Falha ao inativar aluno: Registro não encontrado ou já inativo.");
        }
        
        return true;
    }
}
