package service;

import DAO.AlunoDAO;
import classes.Aluno;
import classes.Usuario;
import classes.UsuarioSaude;
import java.util.Date;
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

            if (aluno.getId() == 0) {
                int novoId = alunoDao.inserir(aluno);

                if (novoId > 0) {
                    aluno.setId(novoId);
                }
            } else {
                alunoDao.atualizar(aluno);
            }

        } catch (DBException e) {

            if (e.getMessage() != null
                    && e.getMessage().toLowerCase().contains("duplicate")) {

                throw new BusinessException(
                        "Falha: O CPF " + aluno.getCpf() + " já está cadastrado.",
                        e
                );
            }

            throw new DBException(
                    "Falha ao salvar o aluno. Detalhes: " + e.getMessage(),
                    e
            );
        }
    }

    public boolean verificarVacinacaoEmDia(Date dataVacinacao) {
        if (dataVacinacao == null) {
            return false;
        }

        long umAnoEmMillis = 365L * 24 * 60 * 60 * 1000;

        long dataLimite = System.currentTimeMillis() - umAnoEmMillis;

        return dataVacinacao.getTime() > dataLimite;
    }

    public void excluir(int id, Usuario usuarioLogado) throws BusinessException, DBException {

        if (usuarioLogado == null || !usuarioLogado.podeExcluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para excluir alunos.");
        }

        if (id <= 0) {
            throw new BusinessException("ID de aluno inválido.");
        }

        try {

            boolean sucesso = alunoDao.excluir(id);

            if (!sucesso) {
                throw new BusinessException("Erro: Aluno não encontrado ou já excluído.");
            }

        } catch (DBException e) {
            throw new DBException("Erro ao excluir aluno no sistema.", e);
        }
    }

    public List<Aluno> buscarAlunosPorCriterio(String criterio) throws DBException {
        return alunoDao.buscarPorCriterio(criterio);
    }

    public boolean inativarAluno(int alunoId, Usuario usuarioLogado)
            throws BusinessException, DBException {

        if (!usuarioLogado.podeExcluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para inativar alunos.");
        }

        boolean sucesso = alunoDao.inativar(alunoId);

        if (!sucesso) {
            throw new BusinessException("Falha ao inativar aluno: Registro não encontrado ou já inativo.");
        }

        return true;
    }

    public void atualizarSaude(Aluno aluno, Usuario usuarioLogado) throws BusinessException, DBException {

        if (!(usuarioLogado instanceof UsuarioSaude) && !usuarioLogado.podeAlterarIncluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerenciar dados de saúde.");
        }

        if (aluno.getId() <= 0) {
            throw new BusinessException("É necessário um aluno válido para atualizar os dados de saúde.");
        }

        if (!alunoDao.atualizarSaude(aluno)) {
            throw new BusinessException("Falha ao atualizar a saúde do aluno: Aluno não encontrado.");
        }
    }
}
