import { useState, useEffect } from "react";
import DashboardLayout from "../components/layout/DashboardLayout";
import { fetchTemas, criarTema, deletarTema, fetchCursos } from "../services/api";

const API_BASE = "/api/v1";

const EMPTY_FORM = {
  titulo: "",
  descricao: "",
  instrutor: "",
  videoId: "",
  tema: { id: "" },
};

export default function Admin() {
  const [activeTab, setActiveTab] = useState("cursos");
  const [form, setForm] = useState({ ...EMPTY_FORM });
  const [mensagem, setMensagem] = useState(null);
  const [loading, setLoading] = useState(false);

  // Temas state
  const [temas, setTemas] = useState([]);
  const [cursos, setCursos] = useState([]);
  const [novoTema, setNovoTema] = useState("");
  const [temaLoading, setTemaLoading] = useState(false);
  const [temaMensagem, setTemaMensagem] = useState(null);

  useEffect(() => {
    carregarDados();
  }, []);

  async function carregarDados() {
    try {
      const [temasData, cursosData] = await Promise.all([fetchTemas(), fetchCursos()]);
      setTemas(temasData);
      setCursos(cursosData);
    } catch {
      // silent
    }
  }

  function handleChange(e) {
    const { name, value } = e.target;
    if (name === "temaId") {
      setForm((prev) => ({ ...prev, tema: { id: value ? Number(value) : "" } }));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setMensagem(null);

    try {
      const token = localStorage.getItem("token");
      const headers = {
        "Content-Type": "application/json",
        Accept: "application/json",
      };
      if (token) headers["Authorization"] = `Bearer ${token}`;

      const payload = {
        titulo: form.titulo,
        descricao: form.descricao,
        instrutor: form.instrutor,
        videoId: form.videoId,
        tema: form.tema.id ? { id: form.tema.id } : null,
      };

      const resp = await fetch(`${API_BASE}/cursos`, {
        method: "POST",
        headers,
        body: JSON.stringify(payload),
      });

      if (resp.ok) {
        setMensagem({ tipo: "sucesso", texto: "Curso adicionado com sucesso!" });
        setForm({ ...EMPTY_FORM });
        carregarDados();
      } else {
        const erro = await resp.text();
        setMensagem({ tipo: "erro", texto: `Erro ao adicionar curso: ${erro}` });
      }
    } catch {
      setMensagem({ tipo: "erro", texto: "Erro de conexão com o servidor." });
    } finally {
      setLoading(false);
    }
  }

  async function handleCriarTema(e) {
    e.preventDefault();
    if (!novoTema.trim()) return;
    setTemaLoading(true);
    setTemaMensagem(null);
    try {
      await criarTema(novoTema.trim());
      setNovoTema("");
      setTemaMensagem({ tipo: "sucesso", texto: "Tema criado com sucesso!" });
      carregarDados();
    } catch {
      setTemaMensagem({ tipo: "erro", texto: "Erro ao criar tema." });
    } finally {
      setTemaLoading(false);
    }
  }

  async function handleDeletarTema(id) {
    try {
      await deletarTema(id);
      carregarDados();
    } catch {
      setTemaMensagem({ tipo: "erro", texto: "Erro ao deletar tema. Verifique se não há cursos associados." });
    }
  }

  function cursosDoTema(temaId) {
    return cursos.filter((c) => c.tema && c.tema.id === temaId);
  }

  const thumbnailUrl = form.videoId
    ? `https://img.youtube.com/vi/${form.videoId}/hqdefault.jpg`
    : null;

  const temasSelecionado = temas.find((t) => t.id === form.tema.id);

  return (
    <DashboardLayout>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-[#1e2d3d]">Admin — Gerenciar Plataforma</h1>
        <p className="text-gray-500 mt-1">Gerencie cursos e temas da plataforma.</p>
      </div>

      {/* Tab Navigation */}
      <div className="flex gap-1 mb-8 border-b border-gray-200">
        <button
          onClick={() => setActiveTab("cursos")}
          className={`px-6 py-3 !text-sm font-semibold transition-colors cursor-pointer ${
            activeTab === "cursos"
              ? "!bg-[#1e2d3d] text-white"
              : "!bg-transparent text-gray-500 hover:text-gray-700"
          }`}
        >
          Cursos
        </button>
        <button
          onClick={() => setActiveTab("temas")}
          className={`px-6 py-3 !text-sm font-semibold transition-colors cursor-pointer ${
            activeTab === "temas"
              ? "!bg-[#1e2d3d] text-white"
              : "!bg-transparent text-gray-500 hover:text-gray-700"
          }`}
        >
          Temas
        </button>
      </div>

      {/* ===== CURSOS TAB ===== */}
      {activeTab === "cursos" && (
        <>
          {mensagem && (
            <div
              className={`mb-6 p-4 border ${
                mensagem.tipo === "sucesso"
                  ? "bg-green-50 border-green-200 text-green-800"
                  : "bg-red-50 border-red-200 text-red-800"
              }`}
            >
              {mensagem.texto}
            </div>
          )}

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Form */}
            <form onSubmit={handleSubmit} className="lg:col-span-2 bg-white border border-gray-200 p-8">
              <h2 className="text-xl font-bold text-[#1e2d3d] mb-6">Novo Curso</h2>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Título</label>
                  <input
                    type="text"
                    name="titulo"
                    value={form.titulo}
                    onChange={handleChange}
                    required
                    placeholder="Ex: Java Spring Boot: API REST Completa"
                    className="w-full border border-gray-200 px-4 py-3 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
                  <textarea
                    name="descricao"
                    value={form.descricao}
                    onChange={handleChange}
                    required
                    rows={3}
                    placeholder="Descreva o conteúdo do curso..."
                    className="w-full border border-gray-200 px-4 py-3 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white resize-none"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Tema</label>
                  <select
                    name="temaId"
                    value={form.tema.id}
                    onChange={handleChange}
                    required
                    className="w-full border border-gray-200 px-4 py-3 !text-sm text-gray-700 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  >
                    <option value="">Selecione um tema...</option>
                    {temas.map((tema) => (
                      <option key={tema.id} value={tema.id}>{tema.nome}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Instrutor</label>
                  <input
                    type="text"
                    name="instrutor"
                    value={form.instrutor}
                    onChange={handleChange}
                    required
                    placeholder="Ex: Dr. Daniel Silva"
                    className="w-full border border-gray-200 px-4 py-3 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">YouTube Video ID (thumbnail)</label>
                  <input
                    type="text"
                    name="videoId"
                    value={form.videoId}
                    onChange={handleChange}
                    placeholder="Ex: wlYvA2b1BWI"
                    className="w-full border border-gray-200 px-4 py-3 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="mt-8 !bg-[#1e2d3d] text-white px-8 py-3 !text-sm font-semibold hover:!bg-[#2a3f54] transition-colors cursor-pointer disabled:opacity-50"
              >
                {loading ? "Salvando..." : "Adicionar Curso"}
              </button>
            </form>

            {/* Preview */}
            <div className="bg-white border border-gray-200 p-6 self-start">
              <h3 className="text-sm font-bold text-[#1e2d3d] uppercase tracking-wider mb-4">Pré-visualização</h3>

              {thumbnailUrl ? (
                <img src={thumbnailUrl} alt="Thumbnail" className="w-full h-40 object-cover mb-4" />
              ) : (
                <div className="w-full h-40 bg-gray-100 flex items-center justify-center mb-4">
                  <span className="text-gray-400 text-sm">Insira um Video ID para ver a thumbnail</span>
                </div>
              )}

              <h4 className="text-lg font-bold text-[#1e2d3d] leading-snug">
                {form.titulo || "Título do curso"}
              </h4>
              <p className="text-sm text-gray-500 mt-1">{form.instrutor || "Nome do instrutor"}</p>
              <div className="flex flex-wrap gap-2 mt-3">
                {temasSelecionado && (
                  <span className="inline-block px-3 py-1 text-xs font-medium !bg-[#1e2d3d] text-white">
                    {temasSelecionado.nome}
                  </span>
                )}
              </div>
            </div>
          </div>
        </>
      )}

      {/* ===== TEMAS TAB ===== */}
      {activeTab === "temas" && (
        <>
          {temaMensagem && (
            <div
              className={`mb-6 p-4 border ${
                temaMensagem.tipo === "sucesso"
                  ? "bg-green-50 border-green-200 text-green-800"
                  : "bg-red-50 border-red-200 text-red-800"
              }`}
            >
              {temaMensagem.texto}
            </div>
          )}

          {/* Add Theme Form */}
          <form onSubmit={handleCriarTema} className="bg-white border border-gray-200 p-6 mb-8">
            <h2 className="text-xl font-bold text-[#1e2d3d] mb-4">Adicionar Tema</h2>
            <div className="flex gap-4">
              <input
                type="text"
                value={novoTema}
                onChange={(e) => setNovoTema(e.target.value)}
                placeholder="Nome do tema..."
                required
                className="flex-1 border border-gray-200 px-4 py-3 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
              />
              <button
                type="submit"
                disabled={temaLoading}
                className="!bg-[#1e2d3d] text-white px-6 py-3 !text-sm font-semibold hover:!bg-[#2a3f54] transition-colors cursor-pointer disabled:opacity-50"
              >
                {temaLoading ? "Salvando..." : "Adicionar"}
              </button>
            </div>
          </form>

          {/* Themes Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {temas.map((tema) => {
              const cursosNoTema = cursosDoTema(tema.id);
              return (
                <div key={tema.id} className="bg-white border border-gray-200 p-6">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <h3 className="text-lg font-bold text-[#1e2d3d]">{tema.nome}</h3>
                      <p className="text-sm text-gray-500 mt-1">
                        {cursosNoTema.length} {cursosNoTema.length === 1 ? "curso" : "cursos"}
                      </p>
                    </div>
                    <button
                      onClick={() => handleDeletarTema(tema.id)}
                      className="!bg-transparent !p-0 text-gray-400 hover:text-red-500 transition-colors cursor-pointer"
                      title="Deletar tema"
                    >
                      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </div>

                  {cursosNoTema.length > 0 ? (
                    <ul className="space-y-2">
                      {cursosNoTema.map((curso) => (
                        <li key={curso.id} className="flex items-center gap-2 text-sm text-gray-600">
                          <span className="w-1.5 h-1.5 bg-[#1e2d3d] shrink-0" />
                          <span className="truncate">{curso.titulo}</span>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-sm text-gray-400 italic">Nenhum curso neste tema</p>
                  )}
                </div>
              );
            })}

            {temas.length === 0 && (
              <p className="text-gray-400 col-span-full text-center py-12">
                Nenhum tema cadastrado ainda.
              </p>
            )}
          </div>
        </>
      )}
    </DashboardLayout>
  );
}
