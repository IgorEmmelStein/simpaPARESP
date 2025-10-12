/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import classes.Familia;
import util.ConnectionFactory;
import util.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FamiliaDAO {

    private static final String SQL_INSERT
            = "INSERT INTO familia (fk_cod_pessoa, qnt_integrantes_fam, renda_familiar_total, bolsa_familia, "
            + "endereco_familia, bairro_familia, residencia, valor_aluguel, telefone_familia) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE
            = "UPDATE familia SET qnt_integrantes_fam=?, renda_familiar_total=?, bolsa_familia=?, "
            + "endereco_familia=?, bairro_familia=?, residencia=?, valor_aluguel=?, telefone_familia=? "
            + "WHERE pk_cod_familia=?";

    private static final String SQL_DELETE
            = "DELETE FROM familia WHERE pk_cod_familia=?";

    private static final String SQL_SELECT_BY_ALUNO_ID
            = "SELECT * FROM familia WHERE fk_cod_pessoa = ?";

    // --- Métodos CRUD ---
    /**
     * Insere um novo registro de Família no banco de dados.
     *
     * @param familia O objeto Familia a ser persistido.
     * @return O ID (pk_cod_familia) gerado.
     * @throws DBException Se ocorrer um erro durante a inserção.
     */
    public int inserir(Familia familia) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int idGerado = 0;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, familia.getFkCodPessoa());
            st.setInt(2, familia.getQtdIntegrantes());
            st.setDouble(3, familia.getRendaFamiliarTotal());
            st.setDouble(4, familia.getValorBolsaFamilia());
            st.setString(5, familia.getEndereco());
            st.setString(6, familia.getBairro());
            st.setString(7, familia.getTipoResidencia());
            st.setDouble(8, familia.getValorAluguel());
            st.setString(9, familia.getTelefoneContato());

            st.executeUpdate();

            rs = st.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }
            return idGerado;

        } catch (SQLException e) {
            throw new DBException("Erro ao cadastrar família no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    /**
     * Atualiza um registro de Família existente no banco de dados.
     *
     * @param familia O objeto Familia com os dados atualizados.
     * @return true se a atualização foi bem-sucedida.
     */
    public boolean atualizar(Familia familia) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_UPDATE);

            st.setInt(1, familia.getQtdIntegrantes());
            st.setDouble(2, familia.getRendaFamiliarTotal());
            st.setDouble(3, familia.getValorBolsaFamilia());
            st.setString(4, familia.getEndereco());
            st.setString(5, familia.getBairro());
            st.setString(6, familia.getTipoResidencia());
            st.setDouble(7, familia.getValorAluguel());
            st.setString(8, familia.getTelefoneContato());
            st.setInt(9, familia.getId()); // WHERE pk_cod_familia

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar família no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    /**
     * Busca os dados da Família associada a um Aluno específico.
     *
     * @param alunoId O ID (pk_cod_pessoa) do Aluno.
     * @return O objeto Familia preenchido ou null.
     */
    public Familia buscarPorAlunoId(int alunoId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Familia familia = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_ALUNO_ID);
            st.setInt(1, alunoId);

            rs = st.executeQuery();

            if (rs.next()) {
                familia = mapearFamilia(rs);
            }
            return familia;
        } catch (SQLException e) {
            throw new DBException("Erro ao buscar família por ID do aluno. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    // --- Métodos Auxiliares ---
    /**
     * Método auxiliar privado para mapear o ResultSet em um objeto Familia.
     */
    private Familia mapearFamilia(ResultSet rs) throws SQLException {
        Familia familia = new Familia();

        familia.setId(rs.getInt("pk_cod_familia"));
        familia.setFkCodPessoa(rs.getInt("fk_cod_pessoa"));
        familia.setQtdIntegrantes(rs.getInt("qnt_integrantes_fam"));
        familia.setRendaFamiliarTotal(rs.getDouble("renda_familiar_total"));
        familia.setValorBolsaFamilia(rs.getDouble("bolsa_familia"));
        familia.setEndereco(rs.getString("endereco_familia"));
        familia.setBairro(rs.getString("bairro_familia"));
        familia.setTipoResidencia(rs.getString("residencia"));
        familia.setValorAluguel(rs.getDouble("valor_aluguel"));
        familia.setTelefoneContato(rs.getString("telefone_familia"));

        return familia;
    }

    // NOTA: O método 'excluir' é raramente usado para famílias, pois a exclusão de um aluno 
    // deve levar a uma inativação, não a uma exclusão em cascata. O método DELETE foi fornecido 
    // acima, mas deve ser usado com cautela.
}
