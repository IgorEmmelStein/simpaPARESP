/*
 * Click nbfs:
 * Click nbfs:
 */
package service;

import DAO.FamiliaDAO;
import DAO.IntegranteFamiliaDAO;
import java.util.ArrayList;
import DAO.AlunoDAO;
import DAO.FamiliaDAO;
import DAO.IntegranteFamiliaDAO;
import classes.Aluno;
import classes.Familia;
import classes.IntegranteFamilia;
import classes.Usuario;
import classes.UsuarioVulnerabilidadeSocial;
import java.io.File;
import util.BusinessException;
import util.DBException;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Period;
import java.text.SimpleDateFormat;

public class RelatorioService {

    private AlunoDAO alunoDAO;

    public RelatorioService() {
        this.alunoDAO = new AlunoDAO();
    }

    public List<Aluno> gerarRelatorioComFiltros(String filtro, Usuario usuarioLogado) throws BusinessException, DBException {

        if (!usuarioLogado.podeConsultarGeral()) {
            throw new BusinessException("Acesso negado. O teu perfil não tem permissão para gerar relatórios.");
        }

        if (filtro == null || filtro.trim().isEmpty()) {
            return alunoDAO.listarTodos();
        }

        if (filtro.equalsIgnoreCase("atraso vacinal")) {

            return alunoDAO.buscarPorCriterio("vacinacao=0");
        }

        return alunoDAO.buscarPorCriterio(filtro);
    }

    private void adicionarCabecalho(Document doc) throws Exception {

        // CORES (você pode ajustar se quiser)
        BaseColor verdeParesp = new BaseColor(31, 122, 63);      // Verde principal
        BaseColor verdeClaro = new BaseColor(214, 227, 204);    // Verde claro
        BaseColor cinza = new BaseColor(90, 90, 90);            // Cinza institucional

        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, verdeParesp);
        Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, cinza);
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, cinza);

        // BARRA SUPERIOR COLORIDA
        PdfPTable faixa = new PdfPTable(1);
        faixa.setWidthPercentage(100);
        PdfPCell faixaCell = new PdfPCell();
        faixaCell.setBackgroundColor(verdeParesp);
        faixaCell.setFixedHeight(12);
        faixaCell.setBorder(Rectangle.NO_BORDER);
        faixa.addCell(faixaCell);
        doc.add(faixa);

        doc.add(new Paragraph("\n"));

        // TÍTULO
        Paragraph pTitulo = new Paragraph("PARESP", titulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(pTitulo);

        Paragraph pSub = new Paragraph("Relatório Completo do Aluno", subtitulo);
        pSub.setAlignment(Element.ALIGN_CENTER);
        doc.add(pSub);

        // DATA
        String dataHora = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(new java.util.Date());

        Paragraph pData = new Paragraph("Gerado em: " + dataHora, dataFont);
        pData.setAlignment(Element.ALIGN_CENTER);
        doc.add(pData);

        doc.add(new Paragraph("\n"));

        // LINHA DE DESTAQUE
        PdfPTable linha = new PdfPTable(1);
        linha.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(verdeParesp);
        cell.setBorderWidth(2);
        cell.setFixedHeight(10);

        linha.addCell(cell);
        doc.add(linha);

        doc.add(new Paragraph("\n"));
    }

    public File exportarDados(List<Aluno> dados, String formato) throws BusinessException {

        if (dados == null || dados.isEmpty()) {
            throw new BusinessException("Não há dados para exportar.");
        }

        if (!formato.equalsIgnoreCase("PDF")) {
            throw new BusinessException("Formato não suportado. Use apenas PDF.");
        }

        try {
            String desktop = System.getProperty("user.home") + "\\Desktop";
            File arquivo = new File(desktop + "\\relatorio_alunos.pdf");

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(arquivo));

            document.open();
            adicionarCabecalho(document);

            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph("RELATÓRIO DE ALUNOS", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(7);
            tabela.setWidthPercentage(100);

            Stream.of("ID", "Nome", "CPF", "Data", "Turno", "Vacinação", "Turma")
                    .forEach(coluna -> {
                        PdfPCell header = new PdfPCell(new Phrase(coluna));
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabela.addCell(header);
                    });

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Aluno aluno : dados) {
                tabela.addCell(String.valueOf(aluno.getId()));
                tabela.addCell(aluno.getNome());
                tabela.addCell(aluno.getCpf());
                tabela.addCell(sdf.format(aluno.getDataAcolhimento()));
                tabela.addCell(aluno.getTurno());
                tabela.addCell(aluno.isVacinacaoEmDia() ? "Em dia" : "Atrasada");
                tabela.addCell(aluno.getTurma());
            }

            document.add(tabela);
            document.close();

            return arquivo;

        } catch (Exception e) {
            throw new BusinessException("Erro ao gerar PDF: " + e.getMessage());
        }
    }

// outros imports já existentes...
    public File exportarAlunoCompleto(Aluno aluno) throws BusinessException {
        if (aluno == null) {
            throw new BusinessException("Aluno nulo.");
        }

        try {
            String desktop = System.getProperty("user.home") + "\\Desktop";
            File arquivo = new File(desktop + "\\aluno_completo_" + aluno.getId() + ".pdf");

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
            doc.open();
            adicionarCabecalho(doc);

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

            // ====================
            // DADOS DO ALUNO
            // ====================
            doc.add(new Paragraph("DADOS DO ALUNO", titulo));
            doc.add(new Paragraph("Nome: " + safeString(aluno.getNome())));
            doc.add(new Paragraph("CPF: " + safeString(aluno.getCpf())));
            doc.add(new Paragraph("NIS: " + safeString(aluno.getNumNIS())));
            doc.add(new Paragraph("Sexo: " + formatarSexo(aluno.getSexo())));
            doc.add(new Paragraph("Data de Nascimento: " + formatarData(aluno.getDataNascimento())));
            doc.add(new Paragraph("Idade: " + calcularIdadePDF(aluno.getDataNascimento())));
            doc.add(new Paragraph("Tamanho do Vestuário: " + safeString(aluno.getTamanhoVestuario())));
            doc.add(new Paragraph("Tamanho do Calçado: " + String.valueOf(aluno.getTamanhoCalcado())));
            doc.add(new Paragraph("Forma de Acesso: " + safeString(aluno.getFormaAcesso())));
            doc.add(new Paragraph("Transporte: " + safeString(aluno.getTransporte())));
            doc.add(new Paragraph("Projeto Férias: " + (aluno.isProjetoFerias() ? "Sim" : "Não")));
            doc.add(new Paragraph("Termo de Imagem: " + (aluno.isTermoImagemAssinado() ? "Autorizado" : "Não autorizado")));
            doc.add(new Paragraph("Turno: " + safeString(aluno.getTurno())));
            doc.add(new Paragraph("Turma: " + safeString(aluno.getTurma())));
            doc.add(new Paragraph("Data de Acolhimento: " + formatarData(aluno.getDataAcolhimento())));
            doc.add(new Paragraph("\n"));

            // ====================
            // SAÚDE
            // ====================
            doc.add(new Paragraph("DADOS DE SAÚDE", titulo));
            doc.add(new Paragraph("Medicamentos: " + safeString(aluno.getMedicamentosUso())));
            doc.add(new Paragraph("Alergias: " + safeString(aluno.getAlergias())));
            doc.add(new Paragraph("Observações: " + safeString(aluno.getObservacoesMedicas())));
            doc.add(new Paragraph("\n"));

            // ====================
            // FAMÍLIA
            // ====================
            FamiliaDAO familiaDAO = new FamiliaDAO();
            IntegranteFamiliaDAO integranteDAO = new IntegranteFamiliaDAO();

            // buscar família (método da sua classe FamiliaDAO já existe: buscarPorAlunoId)
            Familia familia = null;
            try {
                familia = familiaDAO.buscarPorAlunoId(aluno.getId());
            } catch (DBException dbEx) {
                // não quebra a geração do PDF: registra e continua com família = null
                System.err.println("Aviso: falha ao buscar família do aluno id=" + aluno.getId() + " -> " + dbEx.getMessage());
                familia = null;
            }

            doc.add(new Paragraph("DADOS DA FAMÍLIA", titulo));

            if (familia != null) {
                doc.add(new Paragraph("Endereço: " + safeString(familia.getEndereco())));
                doc.add(new Paragraph("Telefone: " + safeString(familia.getTelefoneContato())));
                doc.add(new Paragraph("Renda: " + String.valueOf(familia.getRendaFamiliarTotal())));
                doc.add(new Paragraph("Bolsa Família: "
                        + (familia.getValorBolsaFamilia() > 0 ? "Sim" : "Não")));
                doc.add(new Paragraph("Observações: " + safeString(familia.getAnotacoes())));
            } else {
                doc.add(new Paragraph("Nenhum dado familiar cadastrado ou erro ao recuperar."));
            }

            doc.add(new Paragraph("\n"));

            // ====================
            // INTEGRANTES
            // ====================
            List<IntegranteFamilia> integrantes = new ArrayList<>();
            if (familia != null) {
                try {
                    integrantes = integranteDAO.listarPorFamiliaId(familia.getId());
                } catch (DBException dbEx) {
                    System.err.println("Aviso: falha ao listar integrantes da família id=" + familia.getId() + " -> " + dbEx.getMessage());
                    integrantes = new ArrayList<>();
                }
            }

            doc.add(new Paragraph("INTEGRANTES DA FAMÍLIA", titulo));
            if (integrantes.isEmpty()) {
                doc.add(new Paragraph("Nenhum integrante cadastrado."));
            } else {
                for (IntegranteFamilia i : integrantes) {
                    doc.add(new Paragraph("Nome: " + safeString(i.getNome())));
                    doc.add(new Paragraph("CPF: " + safeString(i.getCpf())));
                    doc.add(new Paragraph("Parentesco: " + safeString(i.getParentesco())));
                    doc.add(new Paragraph("Telefone: " + safeString(i.getTelefone())));
                    doc.add(new Paragraph("Responsável: " + (i.isResponsavelLegal() ? "Sim" : "Não")));
                    doc.add(new Paragraph("Autorizado: " + (i.isPessoaAutorizada() ? "Sim" : "Não")));
                    doc.add(new Paragraph(" "));
                }
            }

            doc.close();
            return arquivo;

        } catch (Exception e) {
            throw new BusinessException("Erro ao gerar relatório completo: " + e.getMessage());
        }
    }

// utilitário local para evitar NPEs em strings
    private String safeString(String s) {
        return s == null ? "" : s;
    }

    private String formatarSexo(String sexo) {
        if (sexo == null) {
            return "";
        }
        switch (sexo) {
            case "M":
                return "Masculino";
            case "F":
                return "Feminino";
            default:
                return "Outro";
        }
    }

    private String formatarData(Date data) {
        if (data == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }

    private String calcularIdadePDF(Date dataNascimento) {
        if (dataNascimento == null) {
            return "";
        }
        LocalDate nascimento = dataNascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return String.valueOf(Period.between(nascimento, LocalDate.now()).getYears()) + " anos";
    }

}
