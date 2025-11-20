/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.AlunoDAO;

import classes.Aluno; 
import java.util.List;

public class AlunoService {
    private AlunoDAO alunoDao;
    
    public AlunoService(){
        this.alunoDao = new AlunoDAO();
    }
    
    public List<Aluno> listarTodosAlunos() {
        return alunoDao.listarTodos();
    }
}
