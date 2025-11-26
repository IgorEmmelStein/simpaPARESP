/*
 * Click nbfs:
 * Click nbfs:
 */
package service;

import DAO.FamiliaDAO;
import classes.Administrador;
import classes.Familia;
import classes.Usuario;
import classes.UsuarioVulnerabilidadeSocial;
import util.BusinessException;
import util.DBException;

public class FamiliaService {

    private FamiliaDAO familiaDAO;

    public FamiliaService() {
        this.familiaDAO = new FamiliaDAO();
    }

    public void salvar(Familia familia, Usuario usuarioLogado) throws BusinessException, DBException {

        if (!(usuarioLogado instanceof UsuarioVulnerabilidadeSocial) && !(usuarioLogado instanceof Administrador)) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerenciar dados sociais.");
        }

        validarCamposObrigatorios(familia);

        try {
            if (familia.getId() == 0) {

                int novoId = familiaDAO.inserir(familia);
                familia.setId(novoId);
            } else {

                if (!familiaDAO.atualizar(familia)) {
                    throw new BusinessException("Falha ao atualizar família: Registro não encontrado.");
                }
            }
        } catch (DBException e) {
            throw new DBException("Falha ao salvar os dados familiares no sistema.", e);
        }
    }

    private void validarCamposObrigatorios(Familia familia) throws BusinessException {
        if (familia.getFkCodPessoa() <= 0) {
            throw new BusinessException("A família deve estar associada a um aluno válido.");
        }
        if (familia.getQtdIntegrantes() <= 0) {
            throw new BusinessException("O número de integrantes da família deve ser maior que zero.");
        }
        if (familia.getRendaFamiliarTotal() <= 0) {
            throw new BusinessException("A Renda Familiar Total é obrigatória.");
        }
        if (familia.getEndereco() == null || familia.getEndereco().trim().isEmpty()) {
            throw new BusinessException("O Endereço e o Bairro são obrigatórios.");
        }

    }

    public Familia buscarPorAlunoId(int alunoId, Usuario usuarioLogado) throws BusinessException, DBException {
        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para consultar dados sociais.");
        }

        Familia familia = familiaDAO.buscarPorAlunoId(alunoId);

        return familia;
    }
}
