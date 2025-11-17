/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import classes.Administrador;
import util.ConnectionFactory;
import util.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * acesso e manipulação de dados da entidade Administrador no banco de dados.
 */
public class AdministradorDAO {

    private static final String SQL_SELECT_BY_CPF = 
        "SELECT pk_cod_admin, nome, cpf, senha, telefone FROM administrador WHERE cpf = ?";
    
    private static final String SQL_SELECT_BY_nome = 
        "SELECT pk_cod_admin, nome, cpf, senha, telefone FROM administrador WHERE nome = ?";
    
    private static final String SQL_INSERT =
        "INSERT INTO administrador (" +
        "cpf, senha, telefone, nome)" +
        "VALUES (?,?,?,?)";
    
    private static final String SQL_UPDATE = 
        "UPDATE administrador SET " +
        "cpf=?, senha=?, telefone=?, nome=? " +
        "WHERE pk_cod_admin=?";
    
    private static final String SQL_DELETE =
        "DELETE FROM administrador WHERE pk_cod_admin = ?";
    /**
     * Autentica um usuário usando o CPF e a senha.
     */
    public Administrador buscarPorCpf(String cpf) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_CPF);
            st.setString(1, cpf); 
            
            rs = st.executeQuery(); 
            
            if (rs.next()) {
                Administrador admin = new Administrador();
                
                admin.setId(rs.getInt("pk_cod_admin"));
                admin.setCpf(rs.getString("cpf"));
                admin.setSenhaHash(rs.getString("senha"));
                admin.setNome(rs.getString("nome"));
                admin.setTelefone(rs.getString("telefone"));
                
                admin.setNomeUsuario(rs.getString("nome")); 

                return admin;
            }
            return null;
        } catch (SQLException e) {
            throw new DBException("Erro ao buscar administrador por CPF no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }
    
    public Administrador buscarPorNome(String nome) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_nome);
            st.setString(1, nome); 
            
            rs = st.executeQuery(); 
            
            if (rs.next()) {
                Administrador admin = new Administrador();
                
                admin.setId(rs.getInt("pk_cod_admin"));
                admin.setCpf(rs.getString("cpf"));
                admin.setSenhaHash(rs.getString("senha"));
                admin.setNome(rs.getString("nome"));
                admin.setTelefone(rs.getString("telefone"));
                
                admin.setNomeUsuario(rs.getString("nome")); 
                
                return admin;
            }
            return null;
        } catch (SQLException e) {
            throw new DBException("Erro ao buscar administrador por nome no DB. Detalhe: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st, rs);
        }
    }

    public boolean inserir(Administrador administrador){
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INSERT);
            
            st.setInt(1, administrador.getId());
            st.setString(2, administrador.getCpf());
            st.setString(3, administrador.getSenhaHash());
            st.setString(4, administrador.getNome());
            st.setString(5, administrador.getTelefone());
                
            st.setString(6, administrador.getNome());
            
            return st.executeUpdate() > 0;
            
        }catch(SQLException e){
            throw new DBException("Erro ao cadastrar administrador. Detalhe: " + e.getMessage(), e);
        }finally{
            ConnectionFactory.closeConnection(conn, st);
        }
    }
    
    
    
    // Próximos métodos incluem inserir(), atualizar(), etc. para gerenciar administradores,
    // mas o método de busca por CPF é o mais crítico para o LOGIN.
}

