/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.FamiliaDAO;
import classes.Administrador;
import classes.Familia;
import classes.Usuario;
import classes.UsuarioVulnerabilidadeSocial;
import util.BusinessException;
import util.DBException;

/**
 * Classe responsável pelas regras de negócio e validações da entidade Família e dados sociais.
 * (RF006: registro de dados familiares).
 */
public class FamiliaService {

    private FamiliaDAO familiaDAO;

    public FamiliaService() {
        this.familiaDAO = new FamiliaDAO();
    }
    
    /**
     * Valida e salva os dados de Família (inserção ou atualização).
     * @param familia O objeto Familia a ser salvo.
     * @param usuarioLogado O usuário que está tentando realizar a operação.
     * @throws BusinessException Se alguma regra de negócio for violada.
     */
    public void salvar(Familia familia, Usuario usuarioLogado) throws BusinessException, DBException {
        
        // 1. Verificação de Permissão (RF012)
        if (!(usuarioLogado instanceof UsuarioVulnerabilidadeSocial) && !(usuarioLogado instanceof Administrador)) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerenciar dados sociais.");
        }
        
        // 2. Validação dos Dados Essenciais (RF006)
        validarCamposObrigatorios(familia);
        
        // 3. Lógica de Negócio (Inserir ou Atualizar)
        try {
            if (familia.getId() == 0) {
                // Inserir novo registro de família
                int novoId = familiaDAO.inserir(familia);
                familia.setId(novoId);
            } else {
                // Atualizar registro de família
                if (!familiaDAO.atualizar(familia)) {
                    throw new BusinessException("Falha ao atualizar família: Registro não encontrado.");
                }
            }
        } catch (DBException e) {
            throw new DBException("Falha ao salvar os dados familiares no sistema.", e);
        }
    }
    
    /**
     * Validação dos campos que são NOT NULL no teu script SQL.
     */
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
        // ... (Tu podes adicionar mais validações aqui, como formato de telefone, etc.)
    }
    
    // --- Métodos de Consulta ---
    
    /**
     * Busca os dados da família pelo ID do aluno.
     */
    public Familia buscarPorAlunoId(int alunoId, Usuario usuarioLogado) throws BusinessException, DBException {
        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para consultar dados sociais.");
        }
        
        Familia familia = familiaDAO.buscarPorAlunoId(alunoId);
        
        // Se a família existe, é necessário carregar os integrantes também (lógica de agregação)
        // if (familia != null) {
            // integranteFamiliaService.listarPorFamiliaId(familia.getId());
        // }
        
        return familia;
    }
}