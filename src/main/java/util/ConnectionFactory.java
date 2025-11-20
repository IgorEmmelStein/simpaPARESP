/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement; // Adicionado para fechar o PreparedStatement

/**
 * Classe responsável por estabelecer e gerenciar a conexão com o banco de dados.
 */
public class ConnectionFactory {

    // --- Configurações do Banco de Dados ---
    private static final String URL = "jdbc:mysql://localhost:3306/paresp?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; 

    /**
     * Tenta estabelecer uma conexão com o banco de dados.
     * @return Objeto Connection ativo.
     * @throws DBException se houver falha na conexão.
     */
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER); 
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new DBException("Driver do banco de dados não encontrado. Verifique as dependências.", e);
        } catch (SQLException e) {
            throw new DBException("Erro ao conectar com o banco de dados. " 
                + "Verifique as configurações e se o servidor está ativo. Mensagem: " + e.getMessage(), e);
        }
    }

    // --- Métodos de Fechamento de Recursos (Sobrecarga) ---

    /**
     * Fecha a conexão se estiver aberta e não for nula.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DBException("Erro ao fechar a conexão com o banco de dados.", e);
            }
        }
    }
    
    /**
     * Fecha o Statement (ou PreparedStatement) e a Connection de forma segura.
     */
    public static void closeConnection(Connection conn, Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DBException("Erro ao fechar o Statement.", e);
            }
        }
        closeConnection(conn);
    }
    
    /**
     * Fecha o ResultSet, o Statement e a Connection de forma segura.
     * ESTE MÉTODO CORRIGE O TEU ERRO NA ALUNODAO.
     */
    public static void closeConnection(Connection conn, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DBException("Erro ao fechar o ResultSet.", e);
            }
        }
        closeConnection(conn, st);
    }
}