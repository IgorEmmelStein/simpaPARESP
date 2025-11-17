/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import classes.Escola;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.ConnectionFactory;
import util.DBException;

/**
 * Classe responsável pelo acesso e manipulação de dados da entidade Escola no
 * banco de dados. (RF014: cadastrar, editar e excluir escolas).
 */
public class EscolaDAO {

    private static final String SQL_INSERT
            = "INSERT INTO escola (pk_cod_escola, nome, serie) VALUES (?, ?, ?)";

    // NOTA: O campo pk_cod_escola não é AUTO_INCREMENT no teu script SQL, 
    // então a inserção deve fornecer o valor da chave primária.
    private static final String SQL_UPDATE
            = "UPDATE escola SET nome=?, serie=? WHERE pk_cod_escola=?";

    private static final String SQL_DELETE
            = "DELETE FROM escola WHERE pk_cod_escola=?";

    private static final String SQL_SELECT_ALL
            = "SELECT pk_cod_escola, nome, serie FROM escola ORDER BY nome";

    private static final String SQL_SELECT_BY_ID
            = "SELECT pk_cod_escola, nome, serie FROM escola WHERE pk_cod_escola=?";

    // --- Métodos CRUD ---
    /**
     * Insere um novo registro de Escola no banco de dados (RF014).
     *
     */
    public boolean inserir(Escola escola) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INSERT);

            st.setInt(1, escola.getId()); // PK deve ser fornecida
            st.setString(2, escola.getNome());
            st.setString(3, escola.getSerie());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            // Se houver erro de chave primária duplicada, por exemplo.
            throw new DBException("Erro ao cadastrar escola. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    /**
     * Atualiza um registro de Escola existente no banco de dados
     */
    public boolean atualizar(Escola escola) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_UPDATE);

            st.setString(1, escola.getNome());
            st.setString(2, escola.getSerie());
            st.setInt(3, escola.getId()); // WHERE

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar escola. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    /**
     * Exclui permanentemente um registro de Escola do banco de dados
     */
    public boolean excluir(int id) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_DELETE);

            st.setInt(1, id);

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            
            throw new DBException("Erro ao excluir escola. Certifique-se de que não há alunos associados. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    /**
     * Lista todas as Escolas no banco de dados.
     */
    public List<Escola> listarTodos() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Escola> listaEscolas = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_ALL);
            rs = st.executeQuery();

            while (rs.next()) {
                Escola escola = new Escola(
                        rs.getInt("pk_cod_escola"),
                        rs.getString("nome"),
                        rs.getString("serie")
                );
                listaEscolas.add(escola);
            }
            return listaEscolas;

        } catch (SQLException e) {
            throw new DBException("Erro ao listar escolas. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    /**
     * Busca uma Escola pelo ID.
     */
    public Escola buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_ID);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new Escola(
                        rs.getInt("pk_cod_escola"),
                        rs.getString("nome"),
                        rs.getString("serie")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DBException("Erro ao buscar escola por ID. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }
}
