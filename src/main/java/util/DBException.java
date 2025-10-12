/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author igore
 */
public class DBException extends RuntimeException {

    // Identificador de serialização
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que aceita uma mensagem de erro.
     * @param msg A mensagem de erro que descreve a falha no acesso ao DB.
     */
    public DBException(String msg) {
        super(msg);
    }
    
    /**
     * Construtor que aceita a mensagem de erro e a causa original da exceção.
     * Isso é útil para encapsular exceções SQL nativas.
     * @param msg A mensagem de erro.
     * @param cause A exceção original (ex: SQLException).
     */
    public DBException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
