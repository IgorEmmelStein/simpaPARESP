/*
 * Click nbfs:
 * Click nbfs:
 */
package service;

import DAO.EscolaDAO;
import classes.Escola;
import util.DBException;
import java.util.List;
import util.BusinessException;


public class EscolaService {

    private EscolaDAO escolaDAO;

    public EscolaService() {
        this.escolaDAO = new EscolaDAO();
    }

    public void salvar(Escola escola) throws BusinessException, DBException {

        if (escola.getNome() == null || escola.getNome().trim().isEmpty()) {
            throw new BusinessException("O nome da escola é obrigatório.");
        }

        if (escola.getSerie() == null || escola.getSerie().trim().isEmpty()) {
            escola.setSerie("N/A");
        }

        try {
            if (!escolaDAO.inserir(escola)) {
                throw new DBException("Nenhuma linha afetada ao inserir a escola. Verifique se o ID já existe.");
            }
        } catch (DBException e) {
            throw new DBException("Falha ao salvar a escola no DB. Detalhes: " + e.getMessage(), e);
        }
    }

    public List<Escola> listarTodas() throws DBException {
        return escolaDAO.listarTodos();
    }
}
