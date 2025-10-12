/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import classes.IntegranteFamilia;
import util.ConnectionFactory;
import util.DBException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo acesso e manipulação de dados da entidade IntegranteFamilia.
 */
public class IntegranteFamiliaDAO {

    private static final String SQL_INSERT = 
        "INSERT INTO integrante_familia (fk_cod_familia, nome, cpf, parentesco, vinculo_afetivo, " +
        "ocupacao, endereco, telefone, resp_legal, pessoa_autorizada) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE = 
        "UPDATE integrante_familia SET fk_cod_familia=?, nome=?, cpf=?, parentesco=?, vinculo_afetivo=?, " +
        "ocupacao=?, endereco=?, telefone=?, resp_legal=?, pessoa_autorizada=? " +
        "WHERE pk_cod_integrante=?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM integrante_familia WHERE pk_cod_integrante=?";
    
    private static final String SQL_SELECT_BY_FAMILIA_ID = 
        "SELECT * FROM integrante_familia WHERE fk_cod_familia = ? ORDER BY nome";

    // --- Métodos CRUD ---

    /**
     * Insere um novo registro de Integrante de Família no banco de dados.
     * @param integrante O objeto IntegranteFamilia a ser persistido.
     * @return O ID (pk_cod_integrante) gerado.
     * @throws DBException Se ocorrer um erro durante a inserção.
     */
    public int inserir(IntegranteFamilia integrante) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int idGerado = 0;
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            
            st.setInt(1, integrante.getFkCodFamilia());
            st.setString(2, integrante.getNome());
            st.setString(3, integrante.getCpf());
            st.setString(4, integrante.getParentesco());
            st.setInt(5, integrante.iseVinculoAfetivo() ? 1 : 0);
            st.setString(6, integrante.getOcupacao());
            st.setString(7, integrante.getEndereco());
            st.setString(8, integrante.getTelefone());
            st.setInt(9, integrante.iseResponsavelLegal() ? 1 : 0);
            st.setInt(10, integrante.isePessoaAutorizada() ? 1 : 0);
            
            st.executeUpdate();
            
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }
            return idGerado;
            
        } catch (SQLException e) {
            throw new DBException("Erro ao cadastrar integrante de família no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    /**
     * Atualiza um registro de Integrante de Família existente.
     * @param integrante O objeto IntegranteFamilia com os dados atualizados.
     * @return true se a atualização foi bem-sucedida.
     */
    public boolean atualizar(IntegranteFamilia integrante) {
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_UPDATE);
            
            st.setInt(1, integrante.getFkCodFamilia());
            st.setString(2, integrante.getNome());
            st.setString(3, integrante.getCpf());
            st.setString(4, integrante.getParentesco());
            st.setInt(5, integrante.iseVinculoAfetivo() ? 1 : 0);
            st.setString(6, integrante.getOcupacao());
            st.setString(7, integrante.getEndereco());
            st.setString(8, integrante.getTelefone());
            st.setInt(9, integrante.iseResponsavelLegal() ? 1 : 0);
            st.setInt(10, integrante.isePessoaAutorizada() ? 1 : 0);
            st.setInt(11, integrante.getId()); // WHERE pk_cod_integrante
            
            return st.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar integrante de família no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }
    
    /**
     * Exclui permanentemente um registro de Integrante de Família.
     * @param id O ID (pk_cod_integrante) do integrante a ser excluído.
     * @return true se a exclusão foi bem-sucedida.
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
            throw new DBException("Erro ao excluir integrante de família. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }

    /**
     * Lista todos os integrantes associados a uma Família específica.
     * @param familiaId O ID da família (pk_cod_familia).
     * @return Uma lista de objetos IntegranteFamilia.
     */
    public List<IntegranteFamilia> listarPorFamiliaId(int familiaId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<IntegranteFamilia> listaIntegrantes = new ArrayList<>();
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_FAMILIA_ID);
            st.setInt(1, familiaId);
            
            rs = st.executeQuery(); 
            
            while (rs.next()) {
                IntegranteFamilia integrante = mapearIntegrante(rs);
                listaIntegrantes.add(integrante);
            }
            return listaIntegrantes;
        } catch (SQLException e) {
            throw new DBException("Erro ao listar integrantes de família. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }
    
    // --- Métodos Auxiliares ---
    
    private IntegranteFamilia mapearIntegrante(ResultSet rs) throws SQLException {
        IntegranteFamilia integrante = new IntegranteFamilia();
        
        integrante.setId(rs.getInt("pk_cod_integrante"));
        integrante.setFkCodFamilia(rs.getInt("fk_cod_familia"));
        
        // Atributos herdados de Pessoa
        integrante.setNome(rs.getString("nome"));
        integrante.setCpf(rs.getString("cpf"));
        integrante.setTelefone(rs.getString("telefone"));
        
        // Atributos específicos
        integrante.setParentesco(rs.getString("parentesco"));
        integrante.setVinculoAfetivo(rs.getInt("vinculo_afetivo") == 1);
        integrante.setOcupacao(rs.getString("ocupacao"));
        integrante.setEndereco(rs.getString("endereco"));
        integrante.seteResponsavelLegal(rs.getInt("resp_legal") == 1);
        integrante.setePessoaAutorizada(rs.getInt("pessoa_autorizada") == 1);
        
        return integrante;
    }
}
