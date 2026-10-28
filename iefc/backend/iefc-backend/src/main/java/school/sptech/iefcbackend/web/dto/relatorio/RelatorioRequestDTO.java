package school.sptech.iefcbackend.web.dto.relatorio;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RelatorioRequestDTO {

    private String ano;
    private String depoimentoDiretoraPresidente;
    private String depoimentoDiretoraOperacional;
    private MultipartFile fotoDiretoraPres;
    private MultipartFile fotoDiretoraOp;
    private String estruturaOrganizacional;
    private String introducaoSobreIefc;
    private String pilarEducacao;
    private List<MembroEquipeDTO> equipe;
    private List<EventoRelatorioDTO> eventos;
    private String textoLumina;
    private String textoPilarPesquisa;
    private String textoPesquisaBloco2;
    private String totalEventos;
    private String participantesDiretos;
    private String beneficiariosIndiretos;
    private List<DepoimentoDTO> depoimentos;
    private String textoPresencaDigital;
    private String textoParceiras;
    private String transparenciaBloco1;
    private String transparenciaBloco2;
    private String consideracoesBloco1;
    private String consideracoesBloco2;

    public RelatorioRequestDTO() {
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getDepoimentoDiretoraPresidente() {
        return depoimentoDiretoraPresidente;
    }

    public void setDepoimentoDiretoraPresidente(String depoimentoDiretoraPresidente) {
        this.depoimentoDiretoraPresidente = depoimentoDiretoraPresidente;
    }

    public String getDepoimentoDiretoraOperacional() {
        return depoimentoDiretoraOperacional;
    }

    public void setDepoimentoDiretoraOperacional(String depoimentoDiretoraOperacional) {
        this.depoimentoDiretoraOperacional = depoimentoDiretoraOperacional;
    }

    public MultipartFile getFotoDiretoraPres() {
        return fotoDiretoraPres;
    }

    public void setFotoDiretoraPres(MultipartFile fotoDiretoraPres) {
        this.fotoDiretoraPres = fotoDiretoraPres;
    }

    public MultipartFile getFotoDiretoraOp() {
        return fotoDiretoraOp;
    }

    public void setFotoDiretoraOp(MultipartFile fotoDiretoraOp) {
        this.fotoDiretoraOp = fotoDiretoraOp;
    }

    public String getEstruturaOrganizacional() {
        return estruturaOrganizacional;
    }

    public void setEstruturaOrganizacional(String estruturaOrganizacional) {
        this.estruturaOrganizacional = estruturaOrganizacional;
    }

    public String getIntroducaoSobreIefc() {
        return introducaoSobreIefc;
    }

    public void setIntroducaoSobreIefc(String introducaoSobreIefc) {
        this.introducaoSobreIefc = introducaoSobreIefc;
    }

    public String getPilarEducacao() {
        return pilarEducacao;
    }

    public void setPilarEducacao(String pilarEducacao) {
        this.pilarEducacao = pilarEducacao;
    }

    public List<MembroEquipeDTO> getEquipe() {
        return equipe;
    }

    public void setEquipe(List<MembroEquipeDTO> equipe) {
        this.equipe = equipe;
    }

    public List<EventoRelatorioDTO> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoRelatorioDTO> eventos) {
        this.eventos = eventos;
    }

    public String getTextoLumina() {
        return textoLumina;
    }

    public void setTextoLumina(String textoLumina) {
        this.textoLumina = textoLumina;
    }

    public String getTextoPilarPesquisa() {
        return textoPilarPesquisa;
    }

    public void setTextoPilarPesquisa(String textoPilarPesquisa) {
        this.textoPilarPesquisa = textoPilarPesquisa;
    }

    public String getTextoPesquisaBloco2() {
        return textoPesquisaBloco2;
    }

    public void setTextoPesquisaBloco2(String textoPesquisaBloco2) {
        this.textoPesquisaBloco2 = textoPesquisaBloco2;
    }

    public String getTotalEventos() {
        return totalEventos;
    }

    public void setTotalEventos(String totalEventos) {
        this.totalEventos = totalEventos;
    }

    public String getParticipantesDiretos() {
        return participantesDiretos;
    }

    public void setParticipantesDiretos(String participantesDiretos) {
        this.participantesDiretos = participantesDiretos;
    }

    public String getBeneficiariosIndiretos() {
        return beneficiariosIndiretos;
    }

    public void setBeneficiariosIndiretos(String beneficiariosIndiretos) {
        this.beneficiariosIndiretos = beneficiariosIndiretos;
    }

    public List<DepoimentoDTO> getDepoimentos() {
        return depoimentos;
    }

    public void setDepoimentos(List<DepoimentoDTO> depoimentos) {
        this.depoimentos = depoimentos;
    }

    public String getTextoPresencaDigital() {
        return textoPresencaDigital;
    }

    public void setTextoPresencaDigital(String textoPresencaDigital) {
        this.textoPresencaDigital = textoPresencaDigital;
    }

    public String getTextoParceiras() {
        return textoParceiras;
    }

    public void setTextoParceiras(String textoParceiras) {
        this.textoParceiras = textoParceiras;
    }

    public String getTransparenciaBloco1() {
        return transparenciaBloco1;
    }

    public void setTransparenciaBloco1(String transparenciaBloco1) {
        this.transparenciaBloco1 = transparenciaBloco1;
    }

    public String getTransparenciaBloco2() {
        return transparenciaBloco2;
    }

    public void setTransparenciaBloco2(String transparenciaBloco2) {
        this.transparenciaBloco2 = transparenciaBloco2;
    }

    public String getConsideracoesBloco1() {
        return consideracoesBloco1;
    }

    public void setConsideracoesBloco1(String consideracoesBloco1) {
        this.consideracoesBloco1 = consideracoesBloco1;
    }

    public String getConsideracoesBloco2() {
        return consideracoesBloco2;
    }

    public void setConsideracoesBloco2(String consideracoesBloco2) {
        this.consideracoesBloco2 = consideracoesBloco2;
    }
}