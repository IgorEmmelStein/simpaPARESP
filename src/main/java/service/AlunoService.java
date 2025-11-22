/*
 * Click nbfs:
 * Click nbfs:
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

    public AlunoService() {
        this.alunoDao = new AlunoDAO();
    }

    public List<Aluno> listarTodosAlunos() throws DBException {
        return alunoDao.listarTodos();
    }

    public void salvar(Aluno aluno) throws BusinessException, DBException {

        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new BusinessException("O Nome Completo é obrigatório.");
        }
        if (aluno.getCpf() == null || aluno.getCpf().length() < 11) {
            throw new BusinessException("O CPF é obrigatório e deve ter 11 dígitos.");
        }
        if (aluno.getDataAcolhimento() == null) {
            throw new BusinessException("A Data de Acolhimento é obrigatória.");
        }
        if (aluno.getFkCodEscola() <= 0) {
            throw new BusinessException("A Escola deve ser selecionada.");
        }

        if (aluno.getFkCodAdmin() <= 0) {
            throw new BusinessException("O registro deve ter um administrador criador associado.");
        }

        try {

            alunoDao.inserir(aluno);
        } catch (DBException e) {

            if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("cpf_unique")) {
                throw new BusinessException("Falha na inserção: O CPF " + aluno.getCpf() + " já está cadastrado para outro aluno.", e);
            }
            // Se não for chave duplicada, é outro erro de DB
            throw new DBException("Falha ao persistir o aluno no sistema. Detalhes: " + e.getMessage(), e);          
            
        }
    }

    /**
     * Busca alunos no banco de dados com base em um critério de filtro.
     *
     * @param criterio O texto de busca (nome, CPF, NIS, etc.).
     * @return Lista de alunos filtrada.
     */
    public List<Aluno> buscarAlunosPorCriterio(String criterio) throws DBException {

        return alunoDao.buscarPorCriterio(criterio);
    }

    public boolean inativarAluno(int alunoId, Usuario usuarioLogado) throws BusinessException, DBException {

        if (!usuarioLogado.podeExcluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para inativar alunos.");
        }

        boolean sucesso = alunoDao.inativar(alunoId);

        if (!sucesso) {
            throw new BusinessException("Falha ao inativar aluno: Registro não encontrado ou já inativo.");
        }

        return true;
        
        
    }
    
}
