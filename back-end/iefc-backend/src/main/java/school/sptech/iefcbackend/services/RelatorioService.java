package school.sptech.iefcbackend.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import school.sptech.iefcbackend.dto.relatorio.RelatorioRequestDTO;
import school.sptech.iefcbackend.dto.relatorio.MembroEquipeDTO;
import school.sptech.iefcbackend.dto.relatorio.EventoRelatorioDTO;
import school.sptech.iefcbackend.dto.relatorio.DepoimentoDTO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class RelatorioService {

    private static final Logger log = LoggerFactory.getLogger(RelatorioService.class);

    private static final Map<String, float[]> COR_FUNDO_CAMPO = new HashMap<>() {{

        // depoimento diretoras
        put("depoimentoDiretoraPres", hex("#ffffff"));
        put("depoimentoDiretoraOpe",  hex("#ffffff"));
        // estrutura organizacional
        put("text_3", hex("#ffffff"));
        //sobre o iefc e pilar de educação
        put("text_5",           hex("#dce8ed"));
        put("pilar de educação", hex("#ffffff"));

        // eventos e atividades
        put("titulo",      hex("#0d3d5a"));
        put("tipo",        hex("#ffffff"));
        put("data",        hex("#ffffff"));
        put("local",       hex("#ffffff"));
        put("ministrante", hex("#ffffff"));
        put("descricao",   hex("#ffffff"));

        put("titulo_2",      hex("#0d3d5a"));
        put("tipo_2",        hex("#ffffff"));
        put("data_2",        hex("#ffffff"));
        put("local_2",       hex("#ffffff"));
        put("ministrante_2", hex("#ffffff"));
        put("descricao_2",   hex("#ffffff"));

        put("text_22", hex("#0d3d5a")); // titulo
        put("text_19", hex("#ffffff")); // tipo
        put("text_24", hex("#ffffff")); // data
        put("text_23", hex("#ffffff")); // local
        put("text_20", hex("#ffffff")); // descricao

        put("text_29", hex("#e8a020")); // titulo
        put("text_30", hex("#ffffff")); // tipo
        put("text_32", hex("#ffffff")); // data
        put("text_31", hex("#ffffff")); // local
        put("text_34", hex("#ffffff")); // descricao

        put("text_40", hex("#0d3d5a")); // titulo
        put("text_36", hex("#ffffff")); // tipo
        put("text_38", hex("#ffffff")); // data
        put("text_37", hex("#ffffff")); // local
        put("text_39", hex("#ffffff")); // ministrante
        put("text_35", hex("#ffffff")); // descricao

        put("text_62", hex("#0d3d5a")); // titulo
        put("text_64", hex("#ffffff")); // tipo
        put("text_66", hex("#ffffff")); // data
        put("text_65", hex("#ffffff")); // local
        put("text_67", hex("#ffffff")); // ministrante
        put("text_63", hex("#ffffff")); // descricao

        put("text_68", hex("#e8a020")); // titulo
        put("text_69", hex("#ffffff")); // tipo
        put("text_72", hex("#ffffff")); // data
        put("text_71", hex("#ffffff")); // local
        put("text_73", hex("#ffffff")); // ministrante
        put("text_70", hex("#ffffff")); // descricao

        put("text_79", hex("#0d3d5a")); // titulo
        put("text_77", hex("#ffffff")); // tipo
        put("text_74", hex("#ffffff")); // data
        put("text_76", hex("#ffffff")); // local
        put("text_75", hex("#ffffff")); // ministrante
        put("text_78", hex("#ffffff")); // descricao

        put("text_121", hex("#e8a020")); // titulo
        put("text_122", hex("#ffffff")); // tipo
        put("text_126", hex("#ffffff")); // data
        put("text_127", hex("#ffffff")); // local
        put("text_125", hex("#ffffff")); // ministrante
        put("text_124", hex("#ffffff")); // descricao

        put("text_119", hex("#0d3d5a")); // titulo
        put("text_115", hex("#ffffff")); // tipo
        put("text_117", hex("#ffffff")); // data
        put("text_116", hex("#ffffff")); // local
        put("text_120", hex("#ffffff")); // ministrante
        put("text_118", hex("#ffffff")); // descricao

        put("text_114", hex("#e8a020")); // titulo
        put("text_110", hex("#ffffff")); // tipo
        put("text_112", hex("#ffffff")); // data
        put("text_111", hex("#ffffff")); // local
        put("text_113", hex("#ffffff")); // ministrante
        put("text_109", hex("#ffffff")); // descricao

        put("text_88", hex("#0d3d5a")); // titulo
        put("text_83", hex("#ffffff")); // tipo
        put("text_87", hex("#ffffff")); // data
        put("text_84", hex("#ffffff")); // local
        put("text_86", hex("#ffffff")); // ministrante
        put("text_85", hex("#ffffff")); // descricao

        put("text_90", hex("#e8a020")); // titulo
        put("text_91", hex("#ffffff")); // tipo
        put("text_93", hex("#ffffff")); // data
        put("text_92", hex("#ffffff")); // local
        put("text_94", hex("#ffffff")); // ministrante
        put("text_89", hex("#ffffff")); // descricao

        put("text_100", hex("#0d3d5a")); // titulo
        put("text_96",  hex("#ffffff")); // tipo
        put("text_99",  hex("#ffffff")); // data
        put("text_97",  hex("#ffffff")); // local
        put("text_98",  hex("#ffffff")); // ministrante
        put("text_95",  hex("#ffffff")); // descricao

        put("text_101", hex("#e8a020")); // titulo
        put("text_102", hex("#ffffff")); // tipo
        put("text_103", hex("#ffffff")); // data
        put("text_104", hex("#ffffff")); // local
        put("text_105", hex("#ffffff")); // ministrante
        put("text_106", hex("#ffffff")); // descricao

        put("text_81", hex("#0d3d5a")); // titulo
        put("text_80", hex("#ffffff")); // descricao
        put("text_82", hex("#ffffff"));

        //pilar pesquisa
        put("text_60", hex("#ffffff"));
        put("text_61", hex("#ffffff"));
        //metricas
        put("text_57", hex("#ffffff"));
        put("text_58", hex("#ffffff"));
        put("text_59", hex("#ffffff"));

        //depoimento dos participantes
        put("text_51", hex("#bed5b9")); // posição 3 — cinza mais escuro
        put("text_52", hex("#cfe3e4")); // posição 2 — cinza mais claro
        put("text_49", hex("#dce8ed")); // posição 6
        put("text_53", hex("#dce8ed")); // posição 4
        put("text_50", hex("#dce8ed")); // posição 5
        put("text_47", hex("#faf3e3")); // posição 1
        put("text_55", hex("#dce8ed")); // posição 7
        put("text_48", hex("#ccdde4")); // posição 8
        put("text_54", hex("#ccdde4")); // posição 9
        put("text_56", hex("#dce8ed")); // posição 10

        //presença digital
        put("text_46", hex("#ffffff"));
        //parcerias
        put("text_45", hex("#dce8ed"));
        //transparencia
        //bloco 1
        put("text_44", hex("#6fb9b8"));
        //bloco 2
        put("text_43", hex("#6fb9b8"));

        //considerações finais
        put("text_42", hex("#f8efe0"));
        put("text_41", hex("#f8efe0"));
    }};

    private static final Map<String, float[]> COR_TEXTO_CAMPO = new HashMap<>() {{
        //titulos eventos azuis
        put("depoimentoDiretoraPres", hex("#ffffff"));
        put("depoimentoDiretoraOpe",  hex("#ffffff"));
        put("titulo",    hex("#FAF9F6"));
        put("text_22",   hex("#ffffff"));
        put("text_29",   hex("#ffffff"));
        put("titulo_2",  hex("#FAF9F6"));
        put("text_40",   hex("#ffffff"));
        put("text_62",   hex("#ffffff"));
        put("text_68",   hex("#ffffff"));
        put("text_79",   hex("#ffffff"));
        put("text_121",  hex("#ffffff"));
        put("text_119",  hex("#ffffff"));
        put("text_114",  hex("#ffffff"));
        put("text_88",   hex("#ffffff"));
        put("text_90",   hex("#ffffff"));
        put("text_100",  hex("#ffffff"));
        put("text_101",  hex("#ffffff"));
        put("text_81",   hex("#ffffff"));
    }};

    private static final String CAMPO_DEPOIMENTO_PRES = "depoimentoDiretoraPres";
    private static final String CAMPO_DEPOIMENTO_OP   = "depoimentoDiretoraOpe";

    private static final String CAMPO_ESTRUTURA_ORG = "text_3";

    private static final String CAMPO_SOBRE_IEFC    = "text_5";
    private static final String CAMPO_PILAR_EDU     = "pilar de educação";

    private static final String EV1_TITULO      = "titulo";
    private static final String EV1_TIPO        = "tipo";
    private static final String EV1_DATA        = "data";
    private static final String EV1_LOCAL       = "local";
    private static final String EV1_MINISTRANTE = "ministrante";
    private static final String EV1_DESCRICAO   = "descricao";

    private static final String EV2_TITULO      = "titulo_2";
    private static final String EV2_TIPO        = "tipo_2";
    private static final String EV2_DATA        = "data_2";
    private static final String EV2_LOCAL       = "local_2";
    private static final String EV2_MINISTRANTE = "ministrante_2";
    private static final String EV2_DESCRICAO   = "descricao_2";


    private static final String[] EV3 = {"text_22", "text_19", "text_24", "text_23", null,         "text_20"};
    private static final String[] EV4 = {"text_29", "text_30", "text_32", "text_31", null,         "text_34"};
    private static final String[] EV5 = {"text_40", "text_36", "text_38", "text_37", "text_39",    "text_35"};
    private static final String[] EV7 = {"text_62", "text_64", "text_66", "text_65", "text_67", "text_63"};
    private static final String[] EV8 = {"text_68", "text_69", "text_72", "text_71", "text_73", "text_70"};
    private static final String[] EV9 = {"text_79", "text_77", "text_74", "text_76", "text_75", "text_78"};

    private static final String[] EV10 = {"text_121", "text_122", "text_126", "text_127", "text_125", "text_124"};
    private static final String[] EV11 = {"text_119", "text_115", "text_117", "text_116", "text_120", "text_118"};
    private static final String[] EV12 = {"text_114", "text_110", "text_112", "text_111", "text_113", "text_109"};

    private static final String[] EV13 = {"text_88",  "text_83", "text_87",  "text_84",  "text_86",  "text_85"};
    private static final String[] EV14 = {"text_90",  "text_91", "text_93",  "text_92",  "text_94",  "text_89"};
    private static final String[] EV15 = {"text_100", "text_96", "text_99",  "text_97",  "text_98",  "text_95"};
    private static final String[] EV16 = {"text_101", "text_102","text_103", "text_104", "text_105", "text_106"};

    private static final String[] EV17 = {"text_81", null, null, null, null, "text_80"};
    private static final String CAMPO_LUMINA = "text_82";

    private static final String CAMPO_PESQUISA_BLOCO1 = "text_60";
    private static final String CAMPO_PESQUISA_BLOCO2 = "text_61";

    private static final String CAMPO_TOTAL_EVENTOS         = "text_57";
    private static final String CAMPO_PARTICIPANTES_DIRETOS = "text_58";
    private static final String CAMPO_BENEFICIARIOS         = "text_59";

    private static final String[] CAMPOS_DEPOIMENTOS = {
            "text_51", "text_52",
            "text_49", "text_53",
            "text_50",
            "text_47", "text_55",
            "text_48",
            "text_54", "text_56"
    };

    private static final String CAMPO_PRESENCA_DIGITAL = "text_46";

    private static final String CAMPO_PARCEIRAS = "text_45";

    private static final String CAMPO_TRANSPARENCIA_1 = "text_44";
    private static final String CAMPO_TRANSPARENCIA_2 = "text_43";

    private static final String CAMPO_CONSIDERACOES_1 = "text_42";
    private static final String CAMPO_CONSIDERACOES_2 = "text_41";

    private static final String[][] TODOS_EVENTOS_CAMPOS = {
            null, null,
            EV3, EV4, EV5,
            EV7, EV8, EV9,
            EV10, EV11, EV12,
            EV13, EV14, EV15, EV16,
            EV17
    };

    public byte[] gerarPdf(RelatorioRequestDTO dto) {
        try (InputStream templateStream = new ClassPathResource(
                "templates/template_iefc.pdf").getInputStream();
             PDDocument doc = Loader.loadPDF(templateStream.readAllBytes())) {

            PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new RuntimeException("PDF não possui AcroForm — verifique o template.");
            }
            acroForm.setNeedAppearances(true);

            preencherCampo(acroForm, CAMPO_DEPOIMENTO_PRES, dto.getDepoimentoDiretoraPresidente());
            preencherCampo(acroForm, CAMPO_DEPOIMENTO_OP,   dto.getDepoimentoDiretoraOperacional());

            if (dto.getFotoDiretoraPres() != null && !dto.getFotoDiretoraPres().isEmpty()) {
                substituirImagemNaPagina(doc, 4, 0, dto.getFotoDiretoraPres().getBytes(), true);
            }
            if (dto.getFotoDiretoraOp() != null && !dto.getFotoDiretoraOp().isEmpty()) {
                substituirImagemNaPagina(doc, 4, 1, dto.getFotoDiretoraOp().getBytes(), true);
            }

            preencherCampo(acroForm, CAMPO_ESTRUTURA_ORG, dto.getEstruturaOrganizacional());

            preencherCampo(acroForm, CAMPO_SOBRE_IEFC, dto.getIntroducaoSobreIefc());
            preencherCampo(acroForm, CAMPO_PILAR_EDU,  dto.getPilarEducacao());

            if (dto.getEquipe() != null) {
                List<MembroEquipeDTO> pesquisa = dto.getEquipe().stream()
                        .filter(m -> "pesquisa".equalsIgnoreCase(m.getCategoria()))
                        .toList();
                List<MembroEquipeDTO> educacao = dto.getEquipe().stream()
                        .filter(m -> "educacao".equalsIgnoreCase(m.getCategoria()))
                        .toList();

                for (int i = 0; i < Math.min(pesquisa.size(), 6); i++) {
                    MembroEquipeDTO m = pesquisa.get(i);
                    if (m.getFoto() != null && !m.getFoto().isEmpty()) {
                        substituirImagemNaPagina(doc, 6, i, m.getFoto().getBytes(), true);
                    }
                }
                for (int i = 0; i < Math.min(educacao.size(), 4); i++) {
                    MembroEquipeDTO m = educacao.get(i);
                    if (m.getFoto() != null && !m.getFoto().isEmpty()) {
                        substituirImagemNaPagina(doc, 7, i, m.getFoto().getBytes(), true);
                    }
                }
            }

            if (dto.getEventos() != null && !dto.getEventos().isEmpty()) {
                List<EventoRelatorioDTO> eventos = dto.getEventos().stream()
                        .sorted(Comparator.comparingInt(e -> {
                            try { return Integer.parseInt(e.getTrimestre()); }
                            catch (Exception ex) { return 99; }
                        }))
                        .toList();

                preencherEvento(acroForm, eventos, 0, EV1_TITULO, EV1_TIPO, EV1_DATA,
                        EV1_LOCAL, EV1_MINISTRANTE, EV1_DESCRICAO);
                preencherEvento(acroForm, eventos, 1, EV2_TITULO, EV2_TIPO, EV2_DATA,
                        EV2_LOCAL, EV2_MINISTRANTE, EV2_DESCRICAO);
                preencherEventoArr(acroForm, eventos, 2, EV3);
                preencherEventoArr(acroForm, eventos, 3, EV4);
                preencherEventoArr(acroForm, eventos, 4, EV5);
                preencherEventoArr(acroForm, eventos, 5, EV7);
                preencherEventoArr(acroForm, eventos, 6, EV8);
                preencherEventoArr(acroForm, eventos, 7, EV9);
                preencherEventoArr(acroForm, eventos, 8,  EV10);
                preencherEventoArr(acroForm, eventos, 9,  EV11);
                preencherEventoArr(acroForm, eventos, 10, EV12);
                preencherEventoArr(acroForm, eventos, 11, EV13);
                preencherEventoArr(acroForm, eventos, 12, EV14);
                preencherEventoArr(acroForm, eventos, 13, EV15);
                preencherEventoArr(acroForm, eventos, 14, EV16);

                preencherEventoArr(acroForm, eventos, 15, EV17);

                int[] fotoPaginas  = {13, 13, 14, 14, 14, 14, 14, 15, 15, 15, 16, 16, 16, 17, 17, 17, 17, 18};
                int[] fotoIndices  = { 2,  3,  1,  4,  8, 20, 25,  5,  8, 12,  9, 12, 15,  3,  5, 15, 18,  1};

                for (int i = 0; i < Math.min(eventos.size(), fotoPaginas.length); i++) {
                    EventoRelatorioDTO ev = eventos.get(i);
                    if (ev.getFoto() != null && !ev.getFoto().isEmpty()) {
                        try {
                            substituirImagemNaPagina(doc, fotoPaginas[i], fotoIndices[i],
                                    ev.getFoto().getBytes(), false);
                        } catch (Exception ex) {
                            log.warn("Erro ao inserir foto do evento {}: {}", i, ex.getMessage());
                        }
                    }
                }
            }

            preencherCampo(acroForm, CAMPO_LUMINA, dto.getTextoLumina());

            preencherCampo(acroForm, CAMPO_PESQUISA_BLOCO1, dto.getTextoPilarPesquisa());
            preencherCampo(acroForm, CAMPO_PESQUISA_BLOCO2, dto.getTextoPesquisaBloco2());

            preencherCampo(acroForm, CAMPO_TOTAL_EVENTOS,         dto.getTotalEventos());
            preencherCampo(acroForm, CAMPO_PARTICIPANTES_DIRETOS, dto.getParticipantesDiretos());
            preencherCampo(acroForm, CAMPO_BENEFICIARIOS,         dto.getBeneficiariosIndiretos());

            if (dto.getDepoimentos() != null) {
                for (int i = 0; i < Math.min(dto.getDepoimentos().size(), CAMPOS_DEPOIMENTOS.length); i++) {
                    DepoimentoDTO dep = dto.getDepoimentos().get(i);
                    preencherCampo(acroForm, CAMPOS_DEPOIMENTOS[i], dep.getTexto());
                }
            }

            preencherCampo(acroForm, CAMPO_PRESENCA_DIGITAL, dto.getTextoPresencaDigital());

            preencherCampo(acroForm, CAMPO_PARCEIRAS, dto.getTextoParceiras());

            preencherCampo(acroForm, CAMPO_TRANSPARENCIA_1, dto.getTransparenciaBloco1());
            preencherCampo(acroForm, CAMPO_TRANSPARENCIA_2, dto.getTransparenciaBloco2());

            preencherCampo(acroForm, CAMPO_CONSIDERACOES_1, dto.getConsideracoesBloco1());
            preencherCampo(acroForm, CAMPO_CONSIDERACOES_2, dto.getConsideracoesBloco2());

            acroForm.flatten();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar PDF: " + e.getMessage(), e);
        }
    }

    private void preencherCampo(PDAcroForm acroForm, String nomeCampo, String valor) {
        if (nomeCampo == null || valor == null || valor.isBlank()) return;
        try {
            PDField field = acroForm.getField(nomeCampo);
            if (field instanceof PDTextField tf) {
                PDAnnotationWidget widget = tf.getWidgets().isEmpty() ? null : tf.getWidgets().get(0);
                if (widget != null) {
                    widget.getCOSObject().removeItem(org.apache.pdfbox.cos.COSName.AP);
                    PDAppearanceCharacteristicsDictionary appearChar =
                            widget.getAppearanceCharacteristics();
                    if (appearChar == null) {
                        appearChar = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
                        widget.setAppearanceCharacteristics(appearChar);


                    }
                    float[] rgb = COR_FUNDO_CAMPO.getOrDefault(nomeCampo, new float[]{1f, 1f, 1f});
                    appearChar.setBackground(new PDColor(rgb, PDDeviceRGB.INSTANCE));

                    float[] corTexto = COR_TEXTO_CAMPO.getOrDefault(nomeCampo, new float[]{0f, 0f, 0f});
                    tf.setDefaultStyleString(
                            "/Helv 9 Tf " + corTexto[0] + " " + corTexto[1] + " " + corTexto[2] + " rg"
                    );

                }

                tf.setMultiline(true);
                tf.setValue(valor);


            } else if (field != null) {
                field.setValue(valor);
            } else {
                log.debug("Campo AcroForm não encontrado: '{}'", nomeCampo);
            }
        } catch (Exception ex) {
            log.warn("Erro ao preencher campo '{}': {}", nomeCampo, ex.getMessage());
        }


    }

    private void preencherEvento(PDAcroForm acroForm,
                                 List<EventoRelatorioDTO> eventos, int idx,
                                 String cTitulo, String cTipo, String cData,
                                 String cLocal, String cMinistrante, String cDescricao) {
        if (idx >= eventos.size()) return;
        EventoRelatorioDTO ev = eventos.get(idx);
        preencherCampo(acroForm, cTitulo,      ev.getTitulo());
        preencherCampo(acroForm, cTipo,        ev.getTipo());
        preencherCampo(acroForm, cData,        ev.getData());
        preencherCampo(acroForm, cLocal,       ev.getLocal());
        preencherCampo(acroForm, cMinistrante, ev.getMinistrante());
        preencherCampo(acroForm, cDescricao,   ev.getDescricao());
    }

    private void preencherEventoArr(PDAcroForm acroForm,
                                    List<EventoRelatorioDTO> eventos,
                                    int idx, String[] campos) {
        if (idx >= eventos.size() || campos == null) return;
        EventoRelatorioDTO ev = eventos.get(idx);
        String[] valores = {ev.getTitulo(), ev.getTipo(), ev.getData(),
                ev.getLocal(), ev.getMinistrante(), ev.getDescricao()};
        for (int i = 0; i < Math.min(campos.length, valores.length); i++) {
            preencherCampo(acroForm, campos[i], valores[i]);
        }
    }

    private void substituirImagemNaPagina(PDDocument doc, int pageIndex,
                                          int imageIndex, byte[] novaImgBytes,
                                          boolean circular) {
        try {
            PDPage page = doc.getPage(pageIndex);
            PDResources resources = page.getResources();
            int contador = 0;

            for (COSName key : resources.getXObjectNames()) {
                PDXObject xobj = resources.getXObject(key);
                if (xobj instanceof PDImageXObject imagemOriginal) {
                    if (contador == imageIndex) {
                        float largOrig = imagemOriginal.getWidth();
                        float altOrig  = imagemOriginal.getHeight();

                        byte[] bytesProcessados = processarImagem(novaImgBytes,
                                (int) largOrig, (int) altOrig, circular);

                        PDImageXObject novaImagem = PDImageXObject.createFromByteArray(
                                doc, bytesProcessados, "foto_" + pageIndex + "_" + imageIndex);

                        resources.put(key, novaImagem);
                        return;
                    }
                    contador++;
                }
            }
            log.warn("Imagem de índice {} não encontrada na página {}", imageIndex, pageIndex + 1);
        } catch (Exception ex) {
            log.warn("Erro ao substituir imagem pg={} idx={}: {}", pageIndex + 1, imageIndex, ex.getMessage());
        }
    }

    private byte[] processarImagem(byte[] imgBytes, int targetW, int targetH, boolean circular) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imgBytes));
            if (original == null) return imgBytes;

            float targetRatio = (float) targetW / targetH;
            float srcRatio    = (float) original.getWidth() / original.getHeight();
            int srcX = 0, srcY = 0;
            int srcW = original.getWidth(), srcH = original.getHeight();

            if (srcRatio > targetRatio) {
                srcW = Math.round(original.getHeight() * targetRatio);
                srcX = (original.getWidth() - srcW) / 2;
            } else if (srcRatio < targetRatio) {
                srcH = Math.round(original.getWidth() / targetRatio);
                srcY = (original.getHeight() - srcH) / 2;
            }

            if (circular) {
                int size = Math.min(srcW, srcH);
                BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = result.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setClip(new Ellipse2D.Float(0, 0, size, size));
                g2d.drawImage(original, 0, 0, size, size, srcX, srcY, srcX + size, srcY + size, null);
                g2d.dispose();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(result, "png", baos);
                return baos.toByteArray();
            } else {
                BufferedImage cropped = new BufferedImage(srcW, srcH, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = cropped.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(original, 0, 0, srcW, srcH, srcX, srcY, srcX + srcW, srcY + srcH, null);
                g2d.dispose();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(cropped, "jpeg", baos);
                return baos.toByteArray();
            }
        } catch (Exception ex) {
            log.warn("Não foi possível processar imagem: {}", ex.getMessage());
            return imgBytes;
        }
    }

    private static float[] hex(String hex) {
        return new float[]{
                Integer.parseInt(hex.substring(1, 3), 16) / 255f,
                Integer.parseInt(hex.substring(3, 5), 16) / 255f,
                Integer.parseInt(hex.substring(5, 7), 16) / 255f
        };
    }
}