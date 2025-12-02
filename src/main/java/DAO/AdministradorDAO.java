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
import java.sql.Statement;

/**
 * acesso e manipulação de dados da entidade Administrador no banco de dados.
 */
public class AdministradorDAO {
    
    private static final String SQL_SELECT_BY_NOME = 
        "SELECT pk_cod_admin, nome, senha, telefone, perm_saude, perm_social, perm_admin " +
        "FROM administrador WHERE nome = ?";
    
    private static final String SQL_INSERT =
        "INSERT INTO administrador (senha, telefone, nome, perm_saude, perm_social, perm_admin) " +
        "VALUES (?,?,?,?,?,?)";
    
    private static final String SQL_UPDATE = 
        "UPDATE administrador SET " +
        "senha=?, telefone=?, nome=? " +
        "WHERE pk_cod_admin=?";
    
    private static final String SQL_DELETE =
        "DELETE FROM administrador WHERE pk_cod_admin = ?";
    
    
    public Administrador buscarPorNome(String nome) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_SELECT_BY_NOME);
            st.setString(1, nome); 
            
            rs = st.executeQuery(); 
            
            if (rs.next()) {
                Administrador admin = new Administrador();
                
                admin.setId(rs.getInt("pk_cod_admin"));
                admin.setSenhaHash(rs.getString("senha"));
                admin.setNome(rs.getString("nome"));
                admin.setTelefone(rs.getString("telefone"));
                
                admin.setPermSaude(rs.getBoolean("perm_saude"));
                admin.setPermSocial(rs.getBoolean("perm_social"));
                admin.setPermAdmin(rs.getBoolean("perm_admin"));
                
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

    public void inserir(Administrador administrador) throws DBException {
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            st = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, administrador.getSenhaHash()); 
            
            st.setString(2, administrador.getTelefone());  
            
            st.setString(3, administrador.getNome());
            
            st.setBoolean(4, administrador.isSaude());
            
            st.setBoolean(5, administrador.isSocial());
            
            st.setBoolean(6, administrador.isAdmin());
            st.executeUpdate();
            
            
            
        } catch (SQLException e) {
            throw new DBException("Erro ao inserir administrador: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn, st);
        }
    }
    
 
}

