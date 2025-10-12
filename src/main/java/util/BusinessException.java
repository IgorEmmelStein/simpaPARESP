/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

public class BusinessException extends RuntimeException {

    // Identificador de serialização 
    private static final long serialVersionUID = 1L;

    /**
     * Construtor padrão que aceita uma mensagem de erro.
     * @param msg A mensagem de erro que descreve a falha na regra de negócio.
     */
    public BusinessException(String msg) {
        super(msg);
    }
    
    /**
     * Construtor que aceita a mensagem de erro e a causa original da exceção.
     * @param msg A mensagem de erro.
     * @param cause A exceção original que causou este erro (opcional).
     */
    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}