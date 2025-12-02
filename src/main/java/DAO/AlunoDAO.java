/*
 * Click nbfs:
 * Click nbfs:
 */
package DAO;

import classes.Aluno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.ConnectionFactory;
import util.DBException;

/**
 * Classe responsável pelo acesso e manipulação de dados da entidade Aluno no
 * banco de dados.
 */
public class AlunoDAO {

    private static final String SQL_INSERT
            = "INSERT INTO aluno ("
            + "fk_cod_admin, fk_cod_escola, cpf, data_acolhimento, form_acesso, vacinacao, termo_imagem, "
            + "turno, transporte, data_nasc, proj_ferias, nome, sexo, tamanho_vest, tamanho_calc, turma, "
            + "num_nis, med_paresp, alergias, observacoes, intervencoes, evolucoes, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID
            = "SELECT * FROM aluno WHERE pk_cod_pessoa = ?";

    private static final String SQL_SELECT_ALL
            = "SELECT * FROM aluno WHERE status = 1 ORDER BY nome";

    private static final String SQL_UPDATE
            = "UPDATE aluno SET "
            + "fk_cod_admin=?, fk_cod_escola=?, cpf=?, data_acolhimento=?, form_acesso=?, "
            + "vacinacao=?, termo_imagem=?, turno=?, transporte=?, data_nasc=?, "
            + "proj_ferias=?, nome=?, sexo=?, tamanho_vest=?, tamanho_calc=?, turma=?, "
            + "num_nis=?, med_paresp=?, alergias=?, observacoes=?, intervencoes=?, evolucoes=?, status=? "
            + "WHERE pk_cod_pessoa=?";

    private static final String SQL_INATIVAR
            = "UPDATE aluno SET status = 0 WHERE pk_cod_pessoa = ?";

    private static final String SQL_SEARCH
            = "SELECT * FROM aluno WHERE status = 1 AND ("
            + "CAST(pk_cod_pessoa AS CHAR) LIKE ? "
            + "OR nome LIKE ? "
            + "OR cpf LIKE ? "
            + "OR turno LIKE ? "
            + "OR turma LIKE ? "
            + "OR DATE_FORMAT(data_acolhimento,'%d/%m/%Y') LIKE ? "
            // FILTRO VACINAÇÃO
            + "OR ( ? LIKE '%dia%' AND vacinacao = 1 ) "
            + "OR ( ? LIKE '%atrasad%' AND vacinacao = 0 ) "
            + "OR ( ? LIKE '%vacina%' AND vacinacao IN (0,1) ) "
            + ") ORDER BY nome";

    private static final String SQL_DELETE
            = "UPDATE aluno SET status = 0 WHERE pk_cod_pessoa = ?";

    public int inserir(Aluno aluno) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int idGerado = 0;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, aluno.getFkCodAdmin());
            st.setInt(2, aluno.getFkCodEscola());
            st.setString(3, aluno.getCpf());
            st.setDate(4, new java.sql.Date(aluno.getDataAcolhimento().getTime()));
            st.setString(5, aluno.getFormaAcesso());
            st.setInt(6, aluno.isVacinacaoEmDia() ? 1 : 0);
            st.setInt(7, aluno.isTermoImagemAssinado() ? 1 : 0);
            st.setString(8, aluno.getTurno());
            st.setString(9, aluno.getTransporte());
            st.setDate(10, new java.sql.Date(aluno.getDataNascimento().getTime()));
            st.setInt(11, aluno.isProjetoFerias() ? 1 : 0);
            st.setString(12, aluno.getNome());
            st.setString(13, aluno.getSexo());
            st.setString(14, aluno.getTamanhoVestuario());
            st.setInt(15, aluno.getTamanhoCalcado());
            st.setString(16, aluno.getTurma());
            st.setString(17, aluno.getNumNIS());
            st.setString(18, aluno.getMedicamentosUso());
            st.setString(19, aluno.getAlergias());
            st.setString(20, aluno.getObservacoesMedicas());
            st.setString(21, aluno.getIntervencoes());
            st.setString(22, aluno.getEvolucoes());
            st.setInt(23, aluno.getStatus());

            st.executeUpdate();

            rs = st.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }

            return idGerado;

        } catch (SQLException e) {
            throw new DBException("Erro ao cadastrar aluno no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    public boolean excluir(int id) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_DELETE);
            st.setInt(1, id);

            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new DBException("Erro ao inativar aluno no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    public Aluno buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_ID);
            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                return mapearAluno(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DBException("Erro ao buscar aluno por ID. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    public List<Aluno> listarTodos() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Aluno> listaAlunos = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();

            st = conn.prepareStatement(SQL_SELECT_ALL);
            rs = st.executeQuery();

            while (rs.next()) {

                Aluno aluno = mapearAluno(rs);
                listaAlunos.add(aluno);
            }
            return listaAlunos;
        } catch (SQLException e) {

            throw new DBException("Erro ao listar todos os alunos. Detalhe: " + e.getMessage(), e);
        } finally {

            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    /**
     * Busca alunos ativos por diferentes critérios (nome, num_nis,
     * forma_acesso, saúde)
     */
    public List<Aluno> buscarPorCriterio(String criterio) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Aluno> lista = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SEARCH);

            String busca = "%" + criterio.toLowerCase() + "%";

            // CAMPOS COMUNS
            for (int i = 1; i <= 6; i++) {
                st.setString(i, busca);
            }

            // VACINAÇÃO
            st.setString(7, busca); // em dia
            st.setString(8, busca); // atrasada
            st.setString(9, busca); // vacina

            rs = st.executeQuery();

            while (rs.next()) {
                lista.add(mapearAluno(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new DBException("Erro ao filtrar vacinação: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    public boolean atualizar(Aluno aluno) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_UPDATE);

            st.setInt(1, aluno.getFkCodAdmin());
            st.setInt(2, aluno.getFkCodEscola());
            st.setString(3, aluno.getCpf());
            st.setDate(4, new java.sql.Date(aluno.getDataAcolhimento().getTime()));
            st.setString(5, aluno.getFormaAcesso());
            st.setInt(6, aluno.isVacinacaoEmDia() ? 1 : 0);
            st.setInt(7, aluno.isTermoImagemAssinado() ? 1 : 0);
            st.setString(8, aluno.getTurno());
            st.setString(9, aluno.getTransporte());
            st.setDate(10, new java.sql.Date(aluno.getDataNascimento().getTime()));
            st.setInt(11, aluno.isProjetoFerias() ? 1 : 0);
            st.setString(12, aluno.getNome());
            st.setString(13, aluno.getSexo());
            st.setString(14, aluno.getTamanhoVestuario());
            st.setInt(15, aluno.getTamanhoCalcado());
            st.setString(16, aluno.getTurma());
            st.setString(17, aluno.getNumNIS());
            st.setString(18, aluno.getMedicamentosUso());
            st.setString(19, aluno.getAlergias());
            st.setString(20, aluno.getObservacoesMedicas());
            st.setString(21, aluno.getIntervencoes());
            st.setString(22, aluno.getEvolucoes());
            st.setInt(23, aluno.getStatus());

            st.setInt(24, aluno.getId());

            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar aluno no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    public boolean inativar(int id) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INATIVAR);
            st.setInt(1, id);

            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new DBException("Erro ao inativar aluno no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    private Aluno mapearAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();

        aluno.setId(rs.getInt("pk_cod_pessoa"));
        aluno.setNome(rs.getString("nome"));
        aluno.setCpf(rs.getString("cpf"));

        aluno.setFkCodAdmin(rs.getInt("fk_cod_admin"));
        aluno.setFkCodEscola(rs.getInt("fk_cod_escola"));

        aluno.setDataAcolhimento(new java.util.Date(rs.getDate("data_acolhimento").getTime()));
        aluno.setDataNascimento(new java.util.Date(rs.getDate("data_nasc").getTime()));
        aluno.setVacinacaoEmDia(rs.getInt("vacinacao") == 1);
        aluno.setTermoImagemAssinado(rs.getInt("termo_imagem") == 1);
        aluno.setProjetoFerias(rs.getInt("proj_ferias") == 1);
        aluno.setStatus(rs.getInt("status"));

        aluno.setFormaAcesso(rs.getString("form_acesso"));
        aluno.setTurno(rs.getString("turno"));
        aluno.setTransporte(rs.getString("transporte"));
        aluno.setSexo(rs.getString("sexo"));
        aluno.setTamanhoVestuario(rs.getString("tamanho_vest"));
        aluno.setTamanhoCalcado(rs.getInt("tamanho_calc"));
        aluno.setTurma(rs.getString("turma"));
        aluno.setNumNIS(rs.getString("num_nis"));

        aluno.setMedicamentosUso(rs.getString("med_paresp"));
        aluno.setAlergias(rs.getString("alergias"));
        aluno.setObservacoesMedicas(rs.getString("observacoes"));
        aluno.setIntervencoes(rs.getString("intervencoes"));
        aluno.setEvolucoes(rs.getString("evolucoes"));

        return aluno;
    }

    private static final String SQL_UPDATE_SAUDE
            = "UPDATE aluno SET med_paresp=?, alergias=?, observacoes=? WHERE pk_cod_pessoa=?";

    public boolean atualizarSaude(Aluno aluno) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_UPDATE_SAUDE);

            st.setString(1, aluno.getMedicamentosUso());
            st.setString(2, aluno.getAlergias());
            st.setString(3, aluno.getObservacoesMedicas());
            st.setInt(4, aluno.getId()); // WHERE pk_cod_pessoa

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar dados de saúde no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }
}
