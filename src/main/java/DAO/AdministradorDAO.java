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
 * Classe responsável pelo acesso e manipulação de dados da entidade Administrador no banco de dados.
 * Inclui o método de autenticação (login).
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
     * Tenta autenticar um usuário usando o CPF (como nome de usuário) e a senha (hash).
     * @param cpf O CPF do usuário para login.
     * @return O objeto Administrador preenchido se a busca for bem-sucedida, ou null.
     * @throws DBException Se ocorrer um erro durante a consulta.
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
                
                // Mapeamento
                admin.setId(rs.getInt("pk_cod_admin"));
                admin.setCpf(rs.getString("cpf"));
                admin.setSenhaHash(rs.getString("senha"));
                admin.setNome(rs.getString("nome"));
                admin.setTelefone(rs.getString("telefone"));
                
                // Definindo o nomeUsuario como o nome (para simplificar)
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
                
                // Mapeamento
                admin.setId(rs.getInt("pk_cod_admin"));
                admin.setCpf(rs.getString("cpf"));
                admin.setSenhaHash(rs.getString("senha"));
                admin.setNome(rs.getString("nome"));
                admin.setTelefone(rs.getString("telefone"));
                
                // Definindo o nomeUsuario como o nome (para simplificar)
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

    
    // Próximos métodos incluem inserir(), atualizar(), etc. para gerenciar administradores,
    // mas o método de busca por CPF é o mais crítico para o LOGIN.
}

