package model.exceptions;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pablo
 */
public class ValidacaoException extends RuntimeException{

    private Map<String, String> errors = new HashMap<>();

    public ValidacaoException(String msg) {
        super(msg);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void adicionarErro(String campo, String mensagem) {
        errors.put(campo, mensagem);
    }
    
}
