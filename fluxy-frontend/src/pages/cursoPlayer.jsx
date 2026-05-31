import { useState, useEffect, useMemo } from "react";
import { useParams, useSearchParams, Link } from "react-router-dom";
import DashboardLayout from "../components/layout/DashboardLayout";
import {
  fetchCursoPorId, fetchVideosPorCurso, cursoToPlayerFormat,
  getUserId, inscreverUsuario, fetchProgresso,
  marcarAulaConcluida, salvarAnotacao,
} from "../services/api";

const API_BASE = "/api/v1";

const EMPTY_AULA = {
  titulo: "",
  videoId: "",
  duracao: "",
  modulo: "",
  ordem: 1,
  url: "",
};

function getLessonById(course, lessonId) {
  for (const mod of course.modules) {
    for (const lesson of mod.lessons) {
      if (lesson.id === lessonId) return { lesson, module: mod };
    }
  }
  return { lesson: null, module: null };
}

function getFirstUncompleted(course) {
  for (const mod of course.modules) {
    for (const lesson of mod.lessons) {
      if (!lesson.completed) return lesson;
    }
  }
  return course.modules[0]?.lessons[0] || null;
}

export default function CursoPlayer() {
  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const [course, setCourse] = useState(null);
  const [loading, setLoading] = useState(true);

  const [expandedModules, setExpandedModules] = useState({});
  const [notes, setNotes] = useState("");
  const [completedOverrides, setCompletedOverrides] = useState({});

  // Per-lesson notes keyed by lessonId
  const [lessonNotes, setLessonNotes] = useState({});

  // Modal state
  const [showModal, setShowModal] = useState(false);
  const [aulaForm, setAulaForm] = useState({ ...EMPTY_AULA });
  const [aulaLoading, setAulaLoading] = useState(false);
  const [aulaMensagem, setAulaMensagem] = useState(null);

  function loadCourse() {
    setLoading(true);
    const userId = getUserId();

    Promise.all([fetchCursoPorId(id), fetchVideosPorCurso(id)])
      .then(async ([cursoData, videosData]) => {
        // Auto-enroll the user
        if (userId) {
          try { await inscreverUsuario(userId, id); } catch { /* ignore */ }
        }

        let courseObj;
        if (videosData.length > 0) {
          courseObj = cursoToPlayerFormat(cursoData, videosData);
        } else {
          courseObj = {
            id: cursoData.id,
            title: cursoData.titulo,
            status: "em-andamento",
            progress: 0,
            meta: "",
            instructor: {
              name: cursoData.instrutor || "Instrutor",
              role: cursoData.descricao || "",
            },
            modules: [],
          };
        }

        // Load persisted progress
        if (userId && courseObj.modules.length > 0) {
          try {
            const progressoData = await fetchProgresso(userId, id);
            const completedMap = {};
            const notesMap = {};
            for (const p of progressoData) {
              const vid = p.video?.id ?? p.videoId;
              if (vid) {
                completedMap[String(vid)] = p.concluida || false;
                if (p.anotacao) notesMap[String(vid)] = p.anotacao;
              }
            }
            // Apply completion to lessons
            for (const mod of courseObj.modules) {
              for (const lesson of mod.lessons) {
                if (completedMap[lesson.id] !== undefined) {
                  lesson.completed = completedMap[lesson.id];
                }
              }
            }
            setCompletedOverrides({});
            setLessonNotes(notesMap);
          } catch { /* ignore */ }
        }

        setCourse(courseObj);
      })
      .catch(() => {
        setCourse(null);
      })
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    loadCourse();
  }, [id]);

  // All hooks must be called before any early return
  const hasModules = course ? course.modules.length > 0 : false;

  const isLessonCompleted = (lesson) => {
    if (completedOverrides[lesson.id] !== undefined) return completedOverrides[lesson.id];
    return lesson.completed;
  };

  const progress = useMemo(() => {
    if (!course) return 0;
    let total = 0;
    let done = 0;
    for (const mod of course.modules) {
      for (const lesson of mod.lessons) {
        total++;
        if (isLessonCompleted(lesson)) done++;
      }
    }
    return total > 0 ? Math.round((done / total) * 100) : 0;
  }, [course, completedOverrides]);

  // Modal handlers
  function handleAulaChange(e) {
    const { name, value } = e.target;
    setAulaForm((prev) => ({
      ...prev,
      [name]: name === "ordem" ? Number(value) : value,
    }));
  }

  async function handleAulaSubmit(e) {
    e.preventDefault();
    setAulaLoading(true);
    setAulaMensagem(null);

    try {
      const resp = await fetch(`${API_BASE}/cursos/${id}/videos`, {
        method: "POST",
        headers: { "Content-Type": "application/json", Accept: "application/json" },
        body: JSON.stringify(aulaForm),
      });

      if (resp.ok) {
        setAulaMensagem({ tipo: "sucesso", texto: "Aula adicionada com sucesso!" });
        setAulaForm({ ...EMPTY_AULA });
        // Reload course data so new lesson appears
        setTimeout(() => {
          setShowModal(false);
          setAulaMensagem(null);
          loadCourse();
        }, 1200);
      } else {
        const erro = await resp.text();
        setAulaMensagem({ tipo: "erro", texto: `Erro: ${erro}` });
      }
    } catch {
      setAulaMensagem({ tipo: "erro", texto: "Erro de conexão com o servidor." });
    } finally {
      setAulaLoading(false);
    }
  }

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center min-h-[400px]">
          <p className="text-gray-500 text-lg">Carregando curso...</p>
        </div>
      </DashboardLayout>
    );
  }

  if (!course) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center min-h-[400px]">
          <p className="text-gray-500 text-lg">Curso não encontrado.</p>
        </div>
      </DashboardLayout>
    );
  }

  const lessonId = searchParams.get("aula");
  const firstUncompleted = hasModules ? getFirstUncompleted(course) : null;
  const activeLessonId = lessonId || firstUncompleted?.id || course.modules[0]?.lessons[0]?.id;
  const { lesson: activeLesson, module: activeModule } = hasModules
    ? getLessonById(course, activeLessonId)
    : { lesson: null, module: null };

  function toggleModule(idx) {
    setExpandedModules((prev) => ({ ...prev, [idx]: !prev[idx] }));
  }

  function selectLesson(lesson) {
    setSearchParams({ aula: lesson.id });
  }

  function toggleCompleted() {
    if (!activeLesson) return;
    const current = isLessonCompleted(activeLesson);
    const newValue = !current;
    setCompletedOverrides((prev) => ({ ...prev, [activeLesson.id]: newValue }));

    const userId = getUserId();
    if (userId) {
      marcarAulaConcluida(userId, Number(activeLesson.id), newValue).catch(() => {});
    }
  }

  function handleSaveNote() {
    if (!activeLesson) return;
    const userId = getUserId();
    const noteText = lessonNotes[activeLesson.id] || notes;
    if (userId) {
      salvarAnotacao(userId, Number(activeLesson.id), noteText).catch(() => {});
    }
  }

  const activeModuleIdx = course.modules.findIndex((m) => m === activeModule);

  return (
    <DashboardLayout>
      {/* Breadcrumb */}
      <div className="flex items-center gap-2 text-sm text-gray-500 mb-6">
        <Link to="/cursos" className="hover:text-gray-700 transition-colors">
          Cursos
        </Link>
        <span>›</span>
        <span className="text-[#1e2d3d] font-medium">{course.title}</span>
      </div>

      <div className="flex flex-col lg:flex-row gap-6">
        {/* Left: Video + Info */}
        <div className="flex-1 min-w-0">
          {/* Video Embed or Empty State */}
          <div className="relative w-full bg-black" style={{ paddingBottom: "56.25%" }}>
            {activeLesson ? (
              <iframe
                className="absolute inset-0 w-full h-full"
                src={`https://www.youtube.com/embed/${activeLesson.videoId}`}
                title={activeLesson.title}
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowFullScreen
              />
            ) : (
              <div className="absolute inset-0 flex flex-col items-center justify-center text-gray-400 gap-4">
                {!hasModules ? (
                  <>
                    <svg className="w-16 h-16" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z" />
                    </svg>
                    <p className="text-lg">Nenhuma aula disponível</p>
                    <button
                      onClick={() => setShowModal(true)}
                      className="!bg-[#1e2d3d] text-white px-6 py-2 !text-sm font-semibold hover:!bg-[#2a3f55] transition-colors cursor-pointer"
                    >
                      + Adicionar primeira aula
                    </button>
                  </>
                ) : (
                  <p>Selecione uma aula</p>
                )}
              </div>
            )}
          </div>

          {/* Lesson Info + Notes */}
          <div className="mt-6 flex flex-col lg:flex-row gap-6">
            <div className="flex-1">
              {activeLesson && (
                <>
                  <h2 className="text-xl font-bold text-[#1e2d3d]">
                    {activeModule?.title}: {activeLesson.title}
                  </h2>
                  <p className="text-gray-500 text-sm mt-2">
                    Nesta aula, exploraremos os conceitos fundamentais de {activeLesson.title.toLowerCase()} com exemplos práticos e aplicações reais no contexto de análise de dados.
                  </p>
                </>
              )}

              {!activeLesson && !hasModules && (
                <div className="text-center py-8">
                  <h2 className="text-xl font-bold text-[#1e2d3d]">{course.title}</h2>
                  <p className="text-gray-500 text-sm mt-2">
                    Este curso ainda não possui aulas. Adicione a primeira aula para começar.
                  </p>
                </div>
              )}

              {/* Instructor */}
              <div className="flex items-center gap-3 mt-6">
                <div className="w-10 h-10 bg-gray-300 flex items-center justify-center shrink-0">
                  <span className="text-white font-semibold text-sm">
                    {course.instructor.name.charAt(0)}
                  </span>
                </div>
                <div>
                  <p className="text-sm font-semibold text-[#1e2d3d]">{course.instructor.name}</p>
                  <p className="text-xs text-gray-500">{course.instructor.role}</p>
                </div>
              </div>

              {activeLesson && (
                <div className="mt-6 flex flex-col gap-2">
                  <p className="text-xs text-gray-500 flex items-center gap-2">
                    <span>⏱</span> {activeLesson.duration} • Duração da aula
                  </p>
                </div>
              )}
            </div>

            {/* Notes */}
            {hasModules && (
              <div className="lg:w-72 shrink-0">
                <div className="flex items-center justify-between mb-2">
                  <h3 className="text-sm font-bold text-[#1e2d3d] uppercase tracking-wider">Anotações</h3>
                  <button
                    onClick={handleSaveNote}
                    className="!bg-transparent !p-0 !text-xs text-blue-600 hover:text-blue-800 font-semibold cursor-pointer uppercase"
                  >
                    Salvar Nota
                  </button>
                </div>
                <textarea
                  value={activeLesson ? (lessonNotes[activeLesson.id] ?? notes) : notes}
                  onChange={(e) => {
                    if (activeLesson) {
                      setLessonNotes((prev) => ({ ...prev, [activeLesson.id]: e.target.value }));
                    } else {
                      setNotes(e.target.value);
                    }
                  }}
                  placeholder="Escreva suas observações sobre a aula aqui..."
                  className="w-full h-32 border border-gray-200 p-3 !text-sm text-gray-700 placeholder-gray-400 resize-none focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                />
              </div>
            )}
          </div>
        </div>

        {/* Right: Course Content Sidebar */}
        <div className="lg:w-80 shrink-0">
          <div className="bg-white border border-gray-200 overflow-hidden">
            {/* Progress header */}
            <div className="p-4 border-b border-gray-200">
              <div className="flex items-center justify-between mb-2">
                <h3 className="text-sm font-bold text-[#1e2d3d]">Conteúdo do Curso</h3>
                <span className="text-xs text-gray-500">{progress}%</span>
              </div>
              <div className="w-full h-2 bg-gray-200">
                <div
                  className="h-full bg-blue-600 transition-all"
                  style={{ width: `${progress}%` }}
                />
              </div>

              {activeLesson && (
                <button
                  onClick={toggleCompleted}
                  className={`w-full mt-3 py-2 !text-sm font-semibold transition-colors cursor-pointer ${
                    isLessonCompleted(activeLesson)
                      ? "!bg-green-600 text-white hover:!bg-green-700"
                      : "!bg-blue-600 text-white hover:!bg-blue-700"
                  }`}
                >
                  {isLessonCompleted(activeLesson) ? "✓ Aula Concluída" : "Marcar como Concluída"}
                </button>
              )}
            </div>

            {/* Module list */}
            {hasModules ? (
              <div className="divide-y divide-gray-200">
                {course.modules.map((mod, idx) => {
                  const isExpanded = expandedModules[idx] !== undefined ? expandedModules[idx] : idx === activeModuleIdx;
                  const moduleDone = mod.lessons.every((l) => isLessonCompleted(l));

                  return (
                    <div key={idx}>
                      <button
                        onClick={() => toggleModule(idx)}
                        className="!bg-transparent w-full px-4 py-3 flex items-center justify-between text-left cursor-pointer hover:bg-gray-50 transition-colors"
                      >
                        <div className="flex items-center gap-2 min-w-0">
                          {moduleDone && <span className="text-green-600 text-xs">✓</span>}
                          <span className="!text-sm font-medium text-[#1e2d3d] truncate !p-0">{mod.title}</span>
                        </div>
                        <svg
                          className={`w-4 h-4 text-gray-400 shrink-0 transition-transform ${isExpanded ? "rotate-180" : ""}`}
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                        </svg>
                      </button>

                      {isExpanded && (
                        <div className="bg-gray-50">
                          {mod.lessons.map((lesson) => {
                            const isActive = lesson.id === activeLessonId;
                            const done = isLessonCompleted(lesson);

                            return (
                              <button
                                key={lesson.id}
                                onClick={() => selectLesson(lesson)}
                                className={`w-full px-4 py-2 flex items-center gap-3 text-left cursor-pointer transition-colors !text-sm ${
                                  isActive
                                    ? "!bg-blue-50 border-l-2 border-blue-600"
                                    : "!bg-transparent hover:!bg-gray-100"
                                }`}
                              >
                                <span className={`text-xs shrink-0 ${done ? "text-green-600" : "text-gray-400"}`}>
                                  {done ? "✓" : "○"}
                                </span>
                                <div className="min-w-0 flex-1">
                                  <span className={`!text-sm block truncate !p-0 ${isActive ? "font-semibold text-[#1e2d3d]" : "text-gray-600"}`}>
                                    {lesson.title}
                                  </span>
                                </div>
                                <span className="text-xs text-gray-400 shrink-0">{lesson.duration}</span>
                              </button>
                            );
                          })}
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            ) : (
              <div className="p-6 text-center">
                <p className="text-gray-400 text-sm mb-2">Nenhuma aula cadastrada.</p>
              </div>
            )}

            {/* Add lesson button — always visible */}
            <div className="p-4 border-t border-gray-200">
              <button
                onClick={() => setShowModal(true)}
                className="w-full py-2 !text-sm font-semibold !bg-[#1e2d3d] text-white hover:!bg-[#2a3f55] transition-colors cursor-pointer flex items-center justify-center gap-2"
              >
                <span className="text-lg leading-none">+</span> Adicionar nova aula
              </button>
            </div>
          </div>

          {/* Support / Materials */}
          <div className="flex justify-between mt-4">
            <button className="!bg-transparent !p-0 !text-xs text-gray-500 hover:text-gray-700 font-medium cursor-pointer uppercase tracking-wide">
              Suporte
            </button>
            <button className="!bg-transparent !p-0 !text-xs text-gray-500 hover:text-gray-700 font-medium cursor-pointer uppercase tracking-wide">
              Materiais
            </button>
          </div>
        </div>
      </div>

      {/* Modal — Adicionar nova aula */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50" onClick={() => setShowModal(false)}>
          <div className="bg-white w-full max-w-lg mx-4 p-8 border border-gray-200 shadow-lg" onClick={(e) => e.stopPropagation()}>
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-xl font-bold text-[#1e2d3d]">Adicionar nova aula</h2>
              <button
                onClick={() => setShowModal(false)}
                className="!bg-transparent !p-0 text-gray-400 hover:text-gray-600 cursor-pointer text-2xl leading-none"
              >
                ×
              </button>
            </div>

            {aulaMensagem && (
              <div
                className={`mb-4 p-3 border text-sm ${
                  aulaMensagem.tipo === "sucesso"
                    ? "bg-green-50 border-green-200 text-green-800"
                    : "bg-red-50 border-red-200 text-red-800"
                }`}
              >
                {aulaMensagem.texto}
              </div>
            )}

            <form onSubmit={handleAulaSubmit} className="flex flex-col gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Título da aula</label>
                <input
                  type="text"
                  name="titulo"
                  value={aulaForm.titulo}
                  onChange={handleAulaChange}
                  required
                  placeholder="Ex: Introdução ao Spring Boot"
                  className="w-full border border-gray-200 px-4 py-2 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">YouTube Video ID</label>
                  <input
                    type="text"
                    name="videoId"
                    value={aulaForm.videoId}
                    onChange={handleAulaChange}
                    required
                    placeholder="Ex: dQw4w9WgXcQ"
                    className="w-full border border-gray-200 px-4 py-2 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Duração</label>
                  <input
                    type="text"
                    name="duracao"
                    value={aulaForm.duracao}
                    onChange={handleAulaChange}
                    required
                    placeholder="Ex: 15:30"
                    className="w-full border border-gray-200 px-4 py-2 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Módulo</label>
                  <input
                    type="text"
                    name="modulo"
                    value={aulaForm.modulo}
                    onChange={handleAulaChange}
                    required
                    placeholder="Ex: Módulo 1 - Básico"
                    className="w-full border border-gray-200 px-4 py-2 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Ordem</label>
                  <input
                    type="number"
                    name="ordem"
                    value={aulaForm.ordem}
                    onChange={handleAulaChange}
                    min={1}
                    className="w-full border border-gray-200 px-4 py-2 !text-sm text-gray-700 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
                  />
                </div>
              </div>

              {/* Live preview */}
              {aulaForm.videoId && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Preview</label>
                  <img
                    src={`https://img.youtube.com/vi/${aulaForm.videoId}/hqdefault.jpg`}
                    alt="Preview"
                    className="w-full h-36 object-cover border border-gray-200"
                  />
                </div>
              )}

              <div className="flex gap-3 mt-2">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="flex-1 py-2 !text-sm font-semibold border border-gray-200 text-gray-600 hover:!bg-gray-50 !bg-white transition-colors cursor-pointer"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  disabled={aulaLoading}
                  className="flex-1 py-2 !text-sm font-semibold !bg-[#1e2d3d] text-white hover:!bg-[#2a3f55] transition-colors cursor-pointer disabled:opacity-50"
                >
                  {aulaLoading ? "Salvando..." : "Adicionar aula"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}
