/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.IntegranteFamiliaDAO;
import classes.IntegranteFamilia;
import classes.Usuario;
import classes.UsuarioVulnerabilidadeSocial;
import util.BusinessException;
import util.DBException;
import java.util.List;

public class IntegranteFamiliaService {

    private IntegranteFamiliaDAO integranteFamiliaDAO;

    public IntegranteFamiliaService() {
        this.integranteFamiliaDAO = new IntegranteFamiliaDAO();
    }

    /**
     * Valida e salva um Integrante de Família (inserção ou atualização).
     *
     *
     */
    public void salvar(IntegranteFamilia integrante, Usuario usuarioLogado) throws BusinessException, DBException {

        //Verificação de Permissão (RF012)
        if (!(usuarioLogado instanceof UsuarioVulnerabilidadeSocial) && !usuarioLogado.podeAlterarIncluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerenciar integrantes da família.");
        }

        // validação dos Dados Essenciais
        validarCamposObrigatorios(integrante);

        // logica de Negócio (Inserir ou Atualizar)
        try {
            if (integrante.getId() == 0) {
                // Inserir novo integrante
                int novoId = integranteFamiliaDAO.inserir(integrante);
                integrante.setId(novoId);
            } else {
                // Atualizar integrante
                if (!integranteFamiliaDAO.atualizar(integrante)) {
                    throw new BusinessException("Falha ao atualizar integrante: Registro não encontrado.");
                }
            }
        } catch (DBException e) {
            throw new DBException("Falha ao salvar o integrante da família no sistema.", e);
        }
    }

    public void atualizar(IntegranteFamilia integrante, Usuario usuarioLogado) throws BusinessException, DBException {

        // Verificação de Permissão 
        if (!usuarioLogado.podeAlterarIncluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para alterar integrantes da família.");
        }

        // validação de Regra de Negócio
        if (integrante.getId() <= 0) {
            throw new BusinessException("Impossível atualizar: ID do integrante inválido ou ausente.");
        }

        //Validação dos Dados Essenciais (Reaproveita o método de validação)
        validarCamposObrigatorios(integrante);

        // Chamada ao DAO
        try {
            if (!integranteFamiliaDAO.atualizar(integrante)) { // Chama o método atualizar do DAO
                throw new BusinessException("Falha ao atualizar o integrante: Registro não encontrado no banco de dados.");
            }
        } catch (DBException e) {
            // Trata erro de DB
            throw new DBException("Falha ao atualizar o integrante da família no DB. Detalhe: " + e.getMessage(), e);
        }
    }

    public void inserir(IntegranteFamilia integrante, Usuario usuarioLogado) throws BusinessException, DBException {

        // Verificação de Permissão (RF012)
        // O usuário precisa ser de Vulnerabilidade Social ou Administrador para inserir
        if (!(usuarioLogado instanceof UsuarioVulnerabilidadeSocial) && !usuarioLogado.podeAlterarIncluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerenciar integrantes da família.");
        }

        // Validação dos Dados Essenciais (Reaproveita a validação)
        validarCamposObrigatorios(integrante);

        // lógica de Inserção
        try {
            // Assume que o ID é 0 para inserção
            int novoId = integranteFamiliaDAO.inserir(integrante);

            if (novoId > 0) {
                // ESSENCIAL: Captura e atribui o ID gerado para a tela usar
                integrante.setId(novoId);
            }
        } catch (DBException e) {
            throw new DBException("Falha ao inserir o integrante da família no sistema.", e);
        }
    }

    /**
     * Validação dos campos obrigatórios.
     */
    private void validarCamposObrigatorios(IntegranteFamilia integrante) throws BusinessException {
        if (integrante.getNome() == null || integrante.getNome().trim().isEmpty()) {
            throw new BusinessException("O campo Nome do integrante é obrigatório.");
        }
        if (integrante.getParentesco() == null || integrante.getParentesco().trim().isEmpty()) {
            throw new BusinessException("O Parentesco é obrigatório.");
        }
        // Regra de Negócio: Se for o Responsável Legal, o CPF e Telefone de contato devem ser válidos.
        if (integrante.isResponsavelLegal()) {
            if (integrante.getCpf() == null || integrante.getCpf().length() != 11) {
                throw new BusinessException("O Responsável Legal deve ter um CPF válido.");
            }
            if (integrante.getTelefone() == null || integrante.getTelefone().length() < 8) {
                throw new BusinessException("O Responsável Legal deve ter um Telefone de contato válido.");
            }
        }
    }

    
    /**
     * Lista todos os integrantes associados a uma Família.
     */
    public List<IntegranteFamilia> listarPorFamiliaId(int familiaId, Usuario usuarioLogado) throws BusinessException, DBException {
        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para consultar dados de integrantes.");
        }
        return integranteFamiliaDAO.listarPorFamiliaId(familiaId);
    }
    //exlcuir parentes
   
    public void excluir(int integranteId, Usuario usuarioLogado) throws BusinessException, DBException {
        // Permissão para excluir deve ser alta (Admin ou Social, dependendo da política)
        if (!usuarioLogado.podeAlterarIncluirGeral() && !usuarioLogado.podeExcluirGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para excluir integrantes.");
        }
        try {
            if (!integranteFamiliaDAO.excluir(integranteId)) {
                throw new BusinessException("Falha ao excluir integrante: Registro não encontrado.");
            }
        } catch (DBException e) {
            throw new DBException("Falha ao excluir o integrante no sistema.", e);
        }
    }
}
