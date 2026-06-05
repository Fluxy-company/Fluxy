package school.sptech.iefcbackend.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import school.sptech.iefcbackend.dto.relatorio.EventoRelatorioDTO;
import school.sptech.iefcbackend.dto.relatorio.MembroEquipeDTO;
import school.sptech.iefcbackend.dto.relatorio.RelatorioRequestDTO;

import java.io.ByteArrayOutputStream;
import java.util.*;


@Service
public class RelatorioService {

    private final TemplateEngine templateEngine;


    public RelatorioService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] gerarPdf(RelatorioRequestDTO dto) {
        try {
            Context ctx = criarContexto(dto);
            String html = templateEngine.process("relatorio-iefc", ctx);
            return renderizarPdf(html);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    private String sanitizarHtml(String texto) {
        if (texto == null) return "";
      
        texto = texto.replace("&nbsp;", "&#160;");
        texto = texto.replace("&quot;", "&#34;");
        texto = texto.replace("&apos;", "&#39;");
        return texto;
    }

    private Context criarContexto(RelatorioRequestDTO dto) throws Exception {
        Context ctx = new Context(new Locale("pt", "BR"));

    
        String ano = dto.getAno() != null && !dto.getAno().trim().isEmpty() ? dto.getAno() : "2025";
        String introducao = sanitizarHtml(dto.getIntroducao() != null && !dto.getIntroducao().trim().isEmpty() 
            ? dto.getIntroducao() 
            : "<p>O Instituto Educacional Futuro da Ciência (IEFC) é uma organização sem fins lucrativos dedicada à promoção da educação científica e pesquisa.</p>");
        
        String missao = sanitizarHtml(dto.getMissao() != null && !dto.getMissao().trim().isEmpty() 
            ? dto.getMissao() 
            : "Levar mais LUZ sobre a Ciência.");
        
        String proposito = sanitizarHtml(dto.getProposito() != null && !dto.getProposito().trim().isEmpty() 
            ? dto.getProposito() 
            : "Desenvolver e promover o conhecimento científico em benefício da sociedade.");
        
        String depoimentoDiretoraPresidente = sanitizarHtml(dto.getDepoimentoDiretoraPresidente() != null && !dto.getDepoimentoDiretoraPresidente().trim().isEmpty()
            ? dto.getDepoimentoDiretoraPresidente()
            : "<p>Depoimento da Diretora Presidente não informado.</p>");
        
        String depoimentoDiretoraOperacional = sanitizarHtml(dto.getDepoimentoDiretoraOperacional() != null && !dto.getDepoimentoDiretoraOperacional().trim().isEmpty()
            ? dto.getDepoimentoDiretoraOperacional()
            : "<p>Depoimento da Diretora Operacional não informado.</p>");
        
        String introAtividades = sanitizarHtml(dto.getIntroAtividades() != null && !dto.getIntroAtividades().trim().isEmpty()
            ? dto.getIntroAtividades()
            : "<p>Atividades realizadas ao longo do período.</p>");
        
        String textoParceiras = sanitizarHtml(dto.getTextoParceiras() != null && !dto.getTextoParceiras().trim().isEmpty()
            ? dto.getTextoParceiras()
            : "<p>Parcerias estratégicas que viabilizaram nossas ações.</p>");
        
        String textoTransparencia = sanitizarHtml(dto.getTextoTransparencia() != null && !dto.getTextoTransparencia().trim().isEmpty()
            ? dto.getTextoTransparencia()
            : "<p>O Instituto mantém práticas permanentes de transparência e regularidade administrativa.</p>");
        
        String consideracoesFinais = sanitizarHtml(dto.getConsideracoesFinais() != null && !dto.getConsideracoesFinais().trim().isEmpty()
            ? dto.getConsideracoesFinais()
            : "<p>Considerações finais do relatório de atividades.</p>");

        ctx.setVariable("ano", ano);
        ctx.setVariable("introducao", introducao);
        ctx.setVariable("missao", missao);
        ctx.setVariable("proposito", proposito);
        ctx.setVariable("depoimentoDiretoraPresidente", depoimentoDiretoraPresidente);
        ctx.setVariable("depoimentoDiretoraOperacional", depoimentoDiretoraOperacional);
        ctx.setVariable("fotoDiretoraPres", toBase64(dto.getFotoDiretoraPres()));
        ctx.setVariable("fotoDiretoraOp", toBase64(dto.getFotoDiretoraOp()));

        List<Map<String, Object>> equipe = new ArrayList<>();
        if (dto.getEquipe() != null && !dto.getEquipe().isEmpty()) {
            for (MembroEquipeDTO m : dto.getEquipe()) {
                Map<String, Object> map = new HashMap<>();
                map.put("nome", m.getNome() != null ? m.getNome() : "Não informado");
                map.put("cargo", m.getCargo() != null ? m.getCargo() : "Membro");
                map.put("bio", sanitizarHtml(m.getBio() != null ? m.getBio() : "Descrição não informada"));
                map.put("categoria", m.getCategoria() != null ? m.getCategoria() : "educacao");
                map.put("foto", toBase64(m.getFoto()));
                equipe.add(map);
            }
        }
        ctx.setVariable("equipe", equipe);

        ctx.setVariable("introAtividades", introAtividades);
        List<Map<String, Object>> eventos = new ArrayList<>();
        if (dto.getEventos() != null && !dto.getEventos().isEmpty()) {
            for (EventoRelatorioDTO ev : dto.getEventos()) {
                Map<String, Object> map = new HashMap<>();
                map.put("trimestre", ev.getTrimestre() != null ? ev.getTrimestre() : "1º Trimestre");
                map.put("titulo", ev.getTitulo() != null ? ev.getTitulo() : "Evento sem título");
                map.put("subtitulo", ev.getSubtitulo() != null ? sanitizarHtml(ev.getSubtitulo()) : "");
                map.put("tipo", ev.getTipo() != null ? ev.getTipo() : "Palestra");
                map.put("data", ev.getData() != null ? ev.getData() : "Data não informada");
                map.put("local", ev.getLocal() != null ? ev.getLocal() : "Local não informado");
                map.put("ministrante", ev.getMinistrante() != null ? ev.getMinistrante() : "A informar");
                map.put("descricao", sanitizarHtml(ev.getDescricao() != null ? ev.getDescricao() : "Descrição não disponível"));
                map.put("foto", toBase64(ev.getFoto()));
                eventos.add(map);
            }
        }
        ctx.setVariable("eventos", eventos);

        ctx.setVariable("totalEventos", dto.getTotalEventos() != null ? dto.getTotalEventos() : 0);
        ctx.setVariable("participantesDirectos", dto.getParticipantesDirectos() != null ? dto.getParticipantesDirectos() : 0);
        ctx.setVariable("beneficiariosIndiretos", dto.getBeneficiariosIndiretos() != null ? dto.getBeneficiariosIndiretos() : "0");
        
        List<Map<String, String>> depoimentos = new ArrayList<>();
        if (dto.getDepoimentos() != null && !dto.getDepoimentos().isEmpty()) {
            for (var dep : dto.getDepoimentos()) {
                Map<String, String> map = new HashMap<>();
                String texto = dep.getTexto() != null && !dep.getTexto().trim().isEmpty() 
                    ? sanitizarHtml(dep.getTexto()) 
                    : "Depoimento não fornecido";
                map.put("texto", texto);
                depoimentos.add(map);
            }
        }
        ctx.setVariable("depoimentos", depoimentos);
        
        ctx.setVariable("seguidoresInstagram", "14.800");
        ctx.setVariable("usuariosPlataforma", "1.602");
        ctx.setVariable("destaqueCursos", "<p>Cursos e palestras com elevada adesão e significativa repercussão acadêmica.</p>");

        ctx.setVariable("textoParceiras", textoParceiras);
        List<String> empresas = new ArrayList<>();
        if (dto.getEmpresasParceiras() != null && !dto.getEmpresasParceiras().isEmpty()) {
            empresas.addAll(dto.getEmpresasParceiras());
        }
        ctx.setVariable("empresasParceiras", empresas);

        ctx.setVariable("textoTransparencia", textoTransparencia);
        ctx.setVariable("consideracoesFinais", consideracoesFinais);

        return ctx;
    }

    private String toBase64(org.springframework.web.multipart.MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return null;
        String mime = file.getContentType() != null ? file.getContentType() : "image/png";
        String encoded = Base64.getEncoder().encodeToString(file.getBytes());
        return "data:" + mime + ";base64," + encoded;
    }

    private byte[] renderizarPdf(String html) throws Exception {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, "/");
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        }
    }
}