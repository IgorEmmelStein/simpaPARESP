/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

    private static final String SQL_DELETE
            = "UPDATE aluno SET status = 0 WHERE pk_cod_pessoa = ?";

    // Consulta para RF004: Busca flexível por nome, cpf, nis, etc.
    private static final String SQL_SEARCH
            = "SELECT * FROM aluno WHERE status = 1 AND ("
            + "nome LIKE ? OR num_nis LIKE ? OR form_acesso LIKE ? OR cpf LIKE ? "
            + "OR alergias LIKE ? OR med_paresp LIKE ?) "
            + "ORDER BY nome";

    // --- Métodos CRUD (CREATE) ---
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

    // CRUD
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
            // SQL_SELECT_ALL = "SELECT * FROM aluno WHERE status = 1 ORDER BY nome"
            st = conn.prepareStatement(SQL_SELECT_ALL);
            rs = st.executeQuery();

            while (rs.next()) {
                // Aqui o erro poderia estar quebrando a leitura.
                // Agora, mapeamos com o novo método robusto.
                Aluno aluno = mapearAluno(rs);
                listaAlunos.add(aluno);
            }
            return listaAlunos;
        } catch (SQLException e) {
            // Se falhar, o erro será exibido
            throw new DBException("Erro ao listar todos os alunos. Detalhe: " + e.getMessage(), e);
        } finally {
            // Certifique-se de usar o ConnectionFactory correto para fechar
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
        List<Aluno> listaAlunos = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SEARCH);

            String parametroBusca = "%" + criterio + "%";

            st.setString(1, parametroBusca);
            st.setString(2, parametroBusca);
            st.setString(3, parametroBusca);
            st.setString(4, parametroBusca);
            st.setString(5, parametroBusca);
            st.setString(6, parametroBusca);

            rs = st.executeQuery();

            while (rs.next()) {
                Aluno aluno = mapearAluno(rs);
                listaAlunos.add(aluno);
            }
            return listaAlunos;

        } catch (SQLException e) {
            throw new DBException("Erro ao buscar alunos por critério. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    // CRUD
    public boolean atualizar(Aluno aluno) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_UPDATE);

            // Mapeamento dos campos (23 parâmetros)
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

            // A cláusula WHERE (24º parâmetro)
            st.setInt(24, aluno.getId());

            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar aluno no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    // --- CRUD (DELETE - Soft Delete) ---
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

    // --- Método Auxiliar de Mapeamento (Reuso) ---
    private Aluno mapearAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();

        // Atributos de Pessoa (Herdados)
        aluno.setId(rs.getInt("pk_cod_pessoa"));
        aluno.setNome(rs.getString("nome"));
        aluno.setCpf(rs.getString("cpf"));
        // Não mapeamos telefone na tabela 'aluno', mas está na classe 'Pessoa'

        // Chaves Estrangeiras
        aluno.setFkCodAdmin(rs.getInt("fk_cod_admin"));
        aluno.setFkCodEscola(rs.getInt("fk_cod_escola"));

        // Atributos de Cadastro e Logística (datas e booleanos)
        // Usamos rs.getDate().getTime() para converter para java.util.Date
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

        // Atributos de Saúde e Acompanhamento
        aluno.setMedicamentosUso(rs.getString("med_paresp"));
        aluno.setAlergias(rs.getString("alergias"));
        aluno.setObservacoesMedicas(rs.getString("observacoes"));
        aluno.setIntervencoes(rs.getString("intervencoes"));
        aluno.setEvolucoes(rs.getString("evolucoes"));

        return aluno;
    }
}
