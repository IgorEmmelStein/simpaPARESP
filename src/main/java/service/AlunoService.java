/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.AlunoDAO;

/**
 *
 * @author igore
 */
public class AlunoService {
    private AlunoDAO alunoDao;
    
    public AlunoService(){
        this.alunoDao = new AlunoDAO();
    }
}
