import { useState } from "react";
import DashboardLayout from "../components/layout/DashboardLayout";
import { gerarRelatorioPdf, downloadPdfBlob } from "../services/relatorioApi";

const STEPS = [
    "Informações Gerais",
    "Diretoras",
    "Equipe",
    "Atividades",
    "Resultados",
    "Textos Institucionais",
    "Envio",
];

async function comprimirImagem(file, maxWidth = 1280, qualidade = 0.82) {
    return new Promise((resolve) => {
        if (!file || !file.type?.startsWith("image/")) {
            resolve(file);
            return;
        }
        const reader = new FileReader();
        reader.onload = (event) => {
            const img = new Image();
            img.onload = () => {
                const scale = Math.min(1, maxWidth / img.width);
                const canvas = document.createElement("canvas");
                canvas.width = Math.max(1, Math.round(img.width * scale));
                canvas.height = Math.max(1, Math.round(img.height * scale));
                const ctx = canvas.getContext("2d");
                if (!ctx) { resolve(file); return; }
                ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
                canvas.toBlob((blob) => {
                    if (!blob) { resolve(file); return; }
                    resolve(new File([blob], file.name || "imagem.jpg", { type: "image/jpeg" }));
                }, "image/jpeg", qualidade);
            };
            img.onerror = () => resolve(file);
            img.src = event.target?.result;
        };
        reader.onerror = () => resolve(file);
        reader.readAsDataURL(file);
    });
}

export default function GerarRelatorio() {
    const [step, setStep] = useState(0);
    const [loading, setLoading] = useState(false);
    const [mensagem, setMensagem] = useState(null);

    // ── Estado de cada seção ──
    const [ano, setAno] = useState(new Date().getFullYear().toString());

    const [diretoras, setDiretoras] = useState({
        depoimentoDiretoraPresidente: "",
        fotoDiretoraPres: null,
        depoimentoDiretoraOperacional: "",
        fotoDiretoraOp: null,
    });

    const [equipe, setEquipe] = useState([]);

    const [eventos, setEventos] = useState([]);

    const [resultados, setResultados] = useState({
        totalEventos: "",
        participantesDiretos: "",
        beneficiariosIndiretos: "",
        depoimentos: [],
    });

    const [textos, setTextos] = useState({
        estruturaOrganizacional: "",
        introducaoSobreIefc: "",
        pilarEducacao: "",
        textoPilarPesquisa: "",
        textoPesquisaBloco2: "",
        textoLumina: "",
        textoPresencaDigital: "",
        textoParceiras: "",
        transparenciaBloco1: "",
        transparenciaBloco2: "",
        consideracoesBloco1: "",
        consideracoesBloco2: "",
    });

    // ── Montar FormData ──
    async function buildFormData() {
        const fd = new FormData();

        fd.append("ano", ano);

        // Diretoras
        fd.append("depoimentoDiretoraPresidente", diretoras.depoimentoDiretoraPresidente);
        fd.append("depoimentoDiretoraOperacional", diretoras.depoimentoDiretoraOperacional);
        if (diretoras.fotoDiretoraPres) {
            const f = await comprimirImagem(diretoras.fotoDiretoraPres);
            fd.append("fotoDiretoraPres", f, f.name || "diretora-presidente.jpg");
        }
        if (diretoras.fotoDiretoraOp) {
            const f = await comprimirImagem(diretoras.fotoDiretoraOp);
            fd.append("fotoDiretoraOp", f, f.name || "diretora-operacional.jpg");
        }

        // Equipe
        for (let i = 0; i < equipe.length; i++) {
            const m = equipe[i];
            fd.append(`equipe[${i}].nome`, m.nome);
            fd.append(`equipe[${i}].cargo`, m.cargo);
            fd.append(`equipe[${i}].bio`, m.bio || "");
            fd.append(`equipe[${i}].categoria`, m.categoria);
            if (m.foto) {
                const f = await comprimirImagem(m.foto);
                fd.append(`equipe[${i}].foto`, f, f.name || `membro-${i}.jpg`);
            }
        }

        // Eventos
        for (let i = 0; i < eventos.length; i++) {
            const e = eventos[i];
            fd.append(`eventos[${i}].trimestre`, e.trimestre);
            fd.append(`eventos[${i}].titulo`, e.titulo);
            fd.append(`eventos[${i}].data`, e.data);
            fd.append(`eventos[${i}].local`, e.local || "");
            fd.append(`eventos[${i}].ministrante`, e.ministrante || "");
            fd.append(`eventos[${i}].tipo`, e.tipo || "");
            fd.append(`eventos[${i}].descricao`, e.descricao || "");
            if (e.foto) {
                const f = await comprimirImagem(e.foto);
                fd.append(`eventos[${i}].foto`, f, f.name || `evento-${i}.jpg`);
            }
        }

        // Resultados
        if (resultados.totalEventos?.trim()) fd.append("totalEventos", resultados.totalEventos);
        if (resultados.participantesDiretos?.trim()) fd.append("participantesDiretos", resultados.participantesDiretos);
        if (resultados.beneficiariosIndiretos?.trim()) fd.append("beneficiariosIndiretos", resultados.beneficiariosIndiretos);
        resultados.depoimentos.forEach((d, i) => fd.append(`depoimentos[${i}].texto`, d.texto));

        // Textos institucionais
        Object.entries(textos).forEach(([key, val]) => {
            if (val?.trim()) fd.append(key, val);
        });

        return fd;
    }

    async function handleBaixar() {
        if (!ano?.trim()) {
            setMensagem({ tipo: "erro", texto: "Informe o ano do relatório." });
            return;
        }
        setLoading(true); setMensagem(null);
        try {
            const fd = await buildFormData();
            const blob = await gerarRelatorioPdf(fd);
            downloadPdfBlob(blob, `Relatorio_IEFC_${ano}.pdf`);
            setMensagem({ tipo: "sucesso", texto: "PDF gerado com sucesso!" });
        } catch (e) {
            console.error("[GerarRelatorio] Erro:", e);
            setMensagem({ tipo: "erro", texto: e.message });
        } finally {
            setLoading(false);
        }
    }

    // ── Helpers de estilo ──
    const inputCls = "w-full border border-gray-200 px-3 py-2 text-sm focus:outline-none focus:border-[#2b9e95]";
    const labelCls = "block text-xs font-medium text-gray-600 mb-1";
    const textaCls = `${inputCls} resize-none`;
    const btnNavy = "!bg-[#0e3f5c] text-white px-4 py-2 text-sm cursor-pointer hover:opacity-90";
    const btnTeal = "!bg-[#2b9e95] text-white px-4 py-2 text-sm cursor-pointer hover:opacity-90";
    const cardCls = "border border-gray-200 p-4 space-y-2";

    return (
        <DashboardLayout>
            {/* Barra de progresso */}
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-[#0e3f5c]">Gerar Relatório de Atividades</h1>
                <div className="flex gap-1 mt-4 flex-wrap">
                    {STEPS.map((s, i) => (
                        <button key={i} onClick={() => setStep(i)}
                            className={`px-3 py-1 text-xs font-semibold border cursor-pointer
                                ${i === step ? "!bg-[#0e3f5c] text-white border-[#0e3f5c]"
                                    : i < step ? "!bg-[#2b9e95] text-white border-[#2b9e95]"
                                        : "text-gray-400 border-gray-200"}`}>
                            {i + 1}. {s}
                        </button>
                    ))}
                </div>
            </div>

            {mensagem && (
                <div className={`mb-4 p-3 text-sm border ${mensagem.tipo === "sucesso"
                    ? "bg-green-50 border-green-200 text-green-800"
                    : "bg-red-50 border-red-200 text-red-800"}`}>
                    {mensagem.texto}
                </div>
            )}

            {/* ── Step 1: Ano ── */}
            {step === 0 && (
                <div className="max-w-xs">
                    <label className={labelCls}>Ano do relatório</label>
                    <input type="text" value={ano} onChange={e => setAno(e.target.value)}
                        className={inputCls} placeholder="2025" />
                </div>
            )}

            {/* ── Step 2: Diretoras ── */}
            {step === 1 && (
                <div className="space-y-6 max-w-2xl">
                    {[
                        { label: "Diretora Presidente", fotoKey: "fotoDiretoraPres", depKey: "depoimentoDiretoraPresidente" },
                        { label: "Diretora Operacional", fotoKey: "fotoDiretoraOp", depKey: "depoimentoDiretoraOperacional" },
                    ].map(({ label, fotoKey, depKey }) => (
                        <div key={fotoKey} className={cardCls}>
                            <h3 className="font-bold text-[#0e3f5c] text-sm">{label}</h3>
                            <div>
                                <label className={labelCls}>Foto (circular, ~80×80 px)</label>
                                <input type="file" accept="image/*" className="text-xs"
                                    onChange={e => setDiretoras(d => ({ ...d, [fotoKey]: e.target.files[0] }))} />
                                {diretoras[fotoKey] && (
                                    <span className="text-xs text-green-600 ml-2">✓ {diretoras[fotoKey].name}</span>
                                )}
                            </div>
                            <div>
                                <label className={labelCls}>Depoimento</label>
                                <textarea rows={6} value={diretoras[depKey]}
                                    onChange={e => setDiretoras(d => ({ ...d, [depKey]: e.target.value }))}
                                    className={textaCls} />
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* ── Step 3: Equipe ── */}
            {step === 2 && (
                <div className="space-y-4">
                    <p className="text-xs text-gray-500">
                        Adicione até <strong>6 membros de pesquisa</strong> e até <strong>4 de educação</strong>.
                        A ordem determina a posição no PDF.
                    </p>
                    {equipe.map((m, i) => (
                        <div key={i} className={cardCls}>
                            <div className="grid grid-cols-3 gap-2">
                                <input placeholder="Nome" value={m.nome} className={inputCls}
                                    onChange={e => { const eq = [...equipe]; eq[i] = { ...eq[i], nome: e.target.value }; setEquipe(eq); }} />
                                <input placeholder="Cargo" value={m.cargo} className={inputCls}
                                    onChange={e => { const eq = [...equipe]; eq[i] = { ...eq[i], cargo: e.target.value }; setEquipe(eq); }} />
                                <select value={m.categoria} className={inputCls}
                                    onChange={e => { const eq = [...equipe]; eq[i] = { ...eq[i], categoria: e.target.value }; setEquipe(eq); }}>
                                    <option value="pesquisa">Pesquisa</option>
                                    <option value="educacao">Educação</option>
                                </select>
                            </div>
                            <textarea placeholder="Bio (texto que aparece ao lado da foto)" rows={2} value={m.bio}
                                onChange={e => { const eq = [...equipe]; eq[i] = { ...eq[i], bio: e.target.value }; setEquipe(eq); }}
                                className={textaCls} />
                            <div className="flex justify-between items-center">
                                <div>
                                    <label className={labelCls}>Foto do membro</label>
                                    <input type="file" accept="image/*" className="text-xs"
                                        onChange={e => { const eq = [...equipe]; eq[i] = { ...eq[i], foto: e.target.files[0] }; setEquipe(eq); }} />
                                    {m.foto && <span className="text-xs text-green-600 ml-1">✓ {m.foto.name}</span>}
                                </div>
                                <button onClick={() => setEquipe(equipe.filter((_, idx) => idx !== i))}
                                    className="text-red-500 text-xs self-end">✕ Remover</button>
                            </div>
                        </div>
                    ))}
                    <button onClick={() => setEquipe([...equipe, { nome: "", cargo: "", bio: "", categoria: "pesquisa", foto: null }])}
                        className={btnNavy}>+ Adicionar membro</button>
                </div>
            )}

            {/* ── Step 4: Atividades ── */}
            {step === 3 && (
                <div className="space-y-4">
                    <p className="text-xs text-gray-500">
                        O PDF suporta até <strong>17 eventos</strong> distribuídos nas páginas 14–19.
                        Ordene por trimestre; a ordem da lista determina a posição no PDF.
                    </p>
                    {eventos.map((ev, i) => (
                        <div key={i} className={cardCls}>
                            <div className="grid grid-cols-2 gap-2">
                                <select value={ev.trimestre} className={inputCls}
                                    onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], trimestre: e.target.value }; setEventos(ev2); }}>
                                    <option value="1">1º Trimestre</option>
                                    <option value="2">2º Trimestre</option>
                                    <option value="3">3º Trimestre</option>
                                    <option value="4">4º Trimestre</option>
                                </select>
                                <input placeholder="Título" value={ev.titulo} className={inputCls}
                                    onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], titulo: e.target.value }; setEventos(ev2); }} />
                                <input placeholder="Data (ex: 13 a 17 de janeiro)" value={ev.data} className={inputCls}
                                    onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], data: e.target.value }; setEventos(ev2); }} />
                                <input placeholder="Local" value={ev.local} className={inputCls}
                                    onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], local: e.target.value }; setEventos(ev2); }} />
                                <input placeholder="Ministrante" value={ev.ministrante} className={inputCls}
                                    onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], ministrante: e.target.value }; setEventos(ev2); }} />
                                <input placeholder="Tipo (ex: Atividade Presencial)" value={ev.tipo || ""} className={inputCls}
                                    onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], tipo: e.target.value }; setEventos(ev2); }} />
                            </div>
                            <textarea placeholder="Descrição" value={ev.descricao} rows={2}
                                onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], descricao: e.target.value }; setEventos(ev2); }}
                                className={textaCls} />
                            <div className="flex justify-between items-center">
                                <div>
                                    <label className={labelCls}>Foto do evento</label>
                                    <input type="file" accept="image/*" className="text-xs"
                                        onChange={e => { const ev2 = [...eventos]; ev2[i] = { ...ev2[i], foto: e.target.files[0] }; setEventos(ev2); }} />
                                    {ev.foto && <span className="text-xs text-green-600 ml-1">✓ {ev.foto.name}</span>}
                                </div>
                                <button onClick={() => setEventos(eventos.filter((_, idx) => idx !== i))}
                                    className="text-red-500 text-xs self-end">✕ Remover</button>
                            </div>
                        </div>
                    ))}
                    <button onClick={() => setEventos([...eventos, { trimestre: "1", titulo: "", data: "", local: "", ministrante: "", tipo: "", descricao: "", foto: null }])}
                        className={btnNavy}>+ Adicionar atividade</button>
                </div>
            )}

            {/* ── Step 5: Resultados ── */}
            {step === 4 && (
                <div className="space-y-6 max-w-3xl">
                    <div className="grid grid-cols-3 gap-4">
                        {[
                            { label: "Total de eventos", key: "totalEventos", placeholder: "ex: 10" },
                            { label: "Participantes diretos", key: "participantesDiretos", placeholder: "ex: 800" },
                            { label: "Beneficiários indiretos", key: "beneficiariosIndiretos", placeholder: "ex: 10 mil" },
                        ].map(({ label, key, placeholder }) => (
                            <div key={key}>
                                <label className={labelCls}>{label}</label>
                                <input type="text" value={resultados[key]} placeholder={placeholder}
                                    onChange={e => setResultados(r => ({ ...r, [key]: e.target.value }))}
                                    className={inputCls} />
                            </div>
                        ))}
                    </div>

                    <div>
                        <h3 className="font-medium text-[#0e3f5c] text-sm mb-2">
                            Depoimentos de participantes <span className="font-normal text-gray-400">(máx. 10)</span>
                        </h3>
                        {resultados.depoimentos.map((d, i) => (
                            <div key={i} className="flex gap-2 mb-2">
                                <textarea rows={2} value={d.texto} placeholder={`Depoimento ${i + 1}`}
                                    onChange={e => {
                                        const deps = [...resultados.depoimentos];
                                        deps[i] = { texto: e.target.value };
                                        setResultados(r => ({ ...r, depoimentos: deps }));
                                    }}
                                    className={`flex-1 ${textaCls}`} />
                                <button onClick={() => setResultados(r => ({ ...r, depoimentos: r.depoimentos.filter((_, idx) => idx !== i) }))}
                                    className="text-red-500 text-xs self-start mt-1">✕</button>
                            </div>
                        ))}
                        {resultados.depoimentos.length < 10 && (
                            <button onClick={() => setResultados(r => ({ ...r, depoimentos: [...r.depoimentos, { texto: "" }] }))}
                                className={btnNavy}>+ Adicionar depoimento</button>
                        )}
                    </div>
                </div>
            )}

            {/* ── Step 6: Textos Institucionais ── */}
            {step === 5 && (
                <div className="space-y-5 max-w-3xl">
                    <p className="text-xs text-gray-500">
                        Estes textos preenchem blocos fixos do PDF. Deixe em branco para manter o conteúdo original do template.
                    </p>

                    {[
                        { label: "Estrutura Organizacional (pág. 6)", key: "estruturaOrganizacional", rows: 8 },
                        { label: "Sobre o IEFC — intro (pág. 13)", key: "introducaoSobreIefc", rows: 8 },
                        { label: "Pilar de Educação (pág. 13)", key: "pilarEducacao", rows: 6 },
                        { label: "Pesquisa Científica — Bloco 1 (pág. 20)", key: "textoPilarPesquisa", rows: 5 },
                        { label: "Pesquisa Científica — Bloco 2 (pág. 20)", key: "textoPesquisaBloco2", rows: 5 },
                        { label: "Programa Lúmina (pág. 19)", key: "textoLumina", rows: 8 },
                        { label: "Presença Digital (pág. 24)", key: "textoPresencaDigital", rows: 8 },
                        { label: "Parcerias (pág. 26)", key: "textoParceiras", rows: 6 },
                        { label: "Transparência — Bloco 1 (pág. 28)", key: "transparenciaBloco1", rows: 5 },
                        { label: "Transparência — Bloco 2 (pág. 28)", key: "transparenciaBloco2", rows: 5 },
                        { label: "Considerações Finais — Bloco 1 (pág. 30)", key: "consideracoesBloco1", rows: 6 },
                        { label: "Considerações Finais — Bloco 2 (pág. 30)", key: "consideracoesBloco2", rows: 6 },
                    ].map(({ label, key, rows }) => (
                        <div key={key}>
                            <label className={labelCls}>{label}</label>
                            <textarea rows={rows} value={textos[key]}
                                onChange={e => setTextos(t => ({ ...t, [key]: e.target.value }))}
                                className={textaCls} />
                        </div>
                    ))}
                </div>
            )}

            {/* ── Step 7: Envio ── */}
            {step === 6 && (
                <div className="space-y-6 max-w-lg">
                    <div className="bg-gray-50 border p-5 space-y-2">
                        <h3 className="font-bold text-[#0e3f5c] text-sm">Resumo</h3>
                        <p className="text-xs text-gray-600">Ano: <strong>{ano}</strong></p>
                        <p className="text-xs text-gray-600">Eventos: <strong>{eventos.length}</strong></p>
                        <p className="text-xs text-gray-600">Membros da equipe: <strong>{equipe.length}</strong></p>
                        <p className="text-xs text-gray-600">Depoimentos: <strong>{resultados.depoimentos.length}</strong></p>
                    </div>
                    <button onClick={handleBaixar} disabled={loading}
                        className={`${btnNavy} px-8 py-3 font-semibold disabled:opacity-50`}>
                        {loading ? "Gerando PDF…" : "⬇ Baixar PDF"}
                    </button>
                </div>
            )}

            {/* Navegação */}
            <div className="flex justify-between mt-10 pt-6 border-t border-gray-200">
                <button onClick={() => setStep(s => Math.max(0, s - 1))} disabled={step === 0}
                    className="px-6 py-2 border text-sm disabled:opacity-30 cursor-pointer">← Anterior</button>
                {step < STEPS.length - 1 && (
                    <button onClick={() => setStep(s => s + 1)}
                        className={`${btnNavy} px-6 py-2`}>Próximo →</button>
                )}
            </div>
        </DashboardLayout>
    );
}