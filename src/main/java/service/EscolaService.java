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

/**
 *
 * @author igore
 */
public class EscolaService {

    private EscolaDAO escolaDAO;

    public EscolaService() {
        this.escolaDAO = new EscolaDAO();
    }

    /**
     * Valida e salva uma nova Escola (RF014).
     *
     * @param escola O objeto Escola a ser salvo.
     * @return true se a inserção for bem-sucedida.
     */
    public boolean salvar(Escola escola) throws BusinessException, DBException {

        if (escola.getNome() == null || escola.getNome().trim().isEmpty()) {
            throw new BusinessException("O nome da escola é obrigatório.");
        }

        if (escola.getSerie() == null || escola.getSerie().trim().isEmpty()) {
            escola.setSerie("N/A");
        }

        try {

            return escolaDAO.inserir(escola);
        } catch (DBException e) {
            throw new DBException("Falha ao salvar a escola no sistema. Detalhe: " + e.getMessage(), e);
        }
    }

    public List<Escola> listarTodas() throws DBException {
        return escolaDAO.listarTodos();
    }
}
