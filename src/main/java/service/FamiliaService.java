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

    public void atualizar(Familia familia, Usuario usuarioLogado) throws BusinessException, DBException {

        // 1. Verificação de Permissão: Somente quem pode alterar geral (Administrador ou Vulnerabilidade Social)
        if (!usuarioLogado.podeAlterarIncluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para alterar detalhes de família.");
        }

        // 2. Validação de Regra de Negócio: O ID da família deve ser válido para UPDATE.
        if (familia.getId() <= 0) {
            throw new BusinessException("Impossível atualizar: ID da família inválido ou ausente.");
        }

        // NOTA: É recomendável que as validações de campos obrigatórios (Renda, Endereço, Telefone) 
        // sejam replicadas aqui do método 'salvar'.
        // 3. Chamada ao DAO
        try {
            if (!familiaDAO.atualizar(familia)) {
                throw new BusinessException("Falha ao atualizar a família: Registro não encontrado no banco de dados.");
            }
        } catch (DBException e) {
            // Trata erro de DB (ex: constraint violation)
            throw new DBException("Falha ao atualizar a família no DB. Detalhe: " + e.getMessage(), e);
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

        // 1. Verificação de Permissão (Regra de Negócio: Somente Admin ou perfil com acesso pode ler dados)
        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para consultar detalhes de família.");
        }

        // 2. Chamada ao DAO para buscar o objeto completo
        return familiaDAO.buscarPorAlunoId(alunoId);
    }

    public int buscarIdPorAlunoId(int alunoId) throws DBException {
        // Chama o método da DAO que busca apenas o ID
        return familiaDAO.buscarIdPorAlunoId(alunoId);
    }
}
