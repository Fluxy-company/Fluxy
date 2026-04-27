import { PDFDocument } from "pdf-lib";
import { listar_banco } from "./util";
import {gerarPdf} from "./util";

export async function carregarCardapio() {
  const cardapios = await listar_banco("cardapio/");
  return await cardapios.json();
}

export async function gerarOrcamentoPDF(evento) {
    const corpo = {
        nomeEvento: evento.nomeEvento,
        qtdPessoas: evento.qtdPessoas,
        cardapiosSelecionados: evento.cardapiosSelecionados.map((c) => ({
            nome: c.nome,
            valorPorPessoa: c.valorPorPessoa,
        })),
        funcionarios: evento.funcionarios.map((f) => ({
            cargo: f.cargo,
        })),
    };

    const resp = await gerarPdf("orcamento/pdf", corpo);
    if (!resp) throw new Error("Erro ao gerar PDF");

    const blob = await resp.blob(); 
    baixarPDF(blob, evento.nomeEvento);
}

function baixarPDF(blob, nomeArquivo) {
    const url = URL.createObjectURL(blob); 
    const link = document.createElement("a");
    link.href = url;
    link.download = `${nomeArquivo}.pdf`;
    link.click();
    URL.revokeObjectURL(url);
}