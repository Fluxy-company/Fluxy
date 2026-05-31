const API_BASE = "/api/v1";

function getHeaders() {
  const headers = {
    "Content-Type": "application/json",
    Accept: "application/json",
  };
  const token = localStorage.getItem("token");
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  return headers;
}

// ---- Usuário (extraído do JWT) ----

export function getUserId() {
  const token = localStorage.getItem("token");
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.sub ? Number(payload.sub) : null;
  } catch {
    return null;
  }
}

export function getUserRoles() {
  const token = localStorage.getItem("token");
  if (!token) return [];
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.escopo ? payload.escopo.split(" ") : [];
  } catch {
    return [];
  }
}

export function isAdmin() {
  return getUserRoles().includes("ROLE_ADMIN");
}

// ---- Cursos ----

export async function fetchCursos() {
  const resp = await fetch(`${API_BASE}/cursos`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao buscar cursos");
  return resp.json();
}

export async function fetchCursoPorId(id) {
  const resp = await fetch(`${API_BASE}/cursos/${id}`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Curso não encontrado");
  return resp.json();
}

// ---- Videos ----

export async function fetchVideosPorCurso(cursoId) {
  const resp = await fetch(`${API_BASE}/cursos/${cursoId}/videos`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao buscar videos do curso");
  return resp.json();
}

export async function fetchTodosVideos() {
  const resp = await fetch(`${API_BASE}/videos`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao buscar videos");
  return resp.json();
}

// ---- Temas ----

export async function fetchTemas() {
  const resp = await fetch(`${API_BASE}/temas`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao buscar temas");
  return resp.json();
}

export async function criarTema(nome) {
  const resp = await fetch(`${API_BASE}/temas`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify({ nome }),
  });
  if (!resp.ok) throw new Error("Erro ao criar tema");
  return resp.json();
}

export async function deletarTema(id) {
  const resp = await fetch(`${API_BASE}/temas/${id}`, {
    method: "DELETE",
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao deletar tema");
}

// ---- Helpers: transform API data → frontend format ----

export function cursoToCatalogFormat(curso) {
  return {
    id: curso.id,
    title: curso.titulo,
    category: curso.tema?.nome || "Sem tema",
    instructor: curso.instrutor || "Instrutor",
    totalLessons: curso.totalAulas || 0,
    videoId: curso.videoId || "",
    description: curso.descricao || "",
    tema: curso.tema || null,
  };
}

export function videoToLessonFormat(video) {
  return {
    id: String(video.id),
    title: video.titulo,
    videoId: video.videoId || "",
    duration: video.duracao || "00:00",
    completed: false,
  };
}

export function videosToModulesFormat(videos) {
  const moduleMap = {};
  for (const video of videos) {
    const modName = video.modulo || "Geral";
    if (!moduleMap[modName]) {
      moduleMap[modName] = [];
    }
    moduleMap[modName].push(videoToLessonFormat(video));
  }
  return Object.entries(moduleMap).map(([title, lessons]) => ({
    title,
    lessons,
  }));
}

export function cursoToPlayerFormat(curso, videos) {
  const modules = videosToModulesFormat(videos);
  const totalLessons = modules.reduce((sum, m) => sum + m.lessons.length, 0);
  const completedLessons = modules.reduce(
    (sum, m) => sum + m.lessons.filter((l) => l.completed).length,
    0
  );
  const progress = totalLessons > 0 ? Math.round((completedLessons / totalLessons) * 100) : 0;

  return {
    id: curso.id,
    title: curso.titulo,
    status: "em-andamento",
    progress,
    meta: "",
    instructor: {
      name: curso.instrutor || "Instrutor",
      role: curso.descricao || "",
    },
    modules,
  };
}

// ---- Inscrições ----

export async function inscreverUsuario(usuarioId, cursoId) {
  const resp = await fetch(`${API_BASE}/inscricoes/usuario/${usuarioId}/curso/${cursoId}`, {
    method: "POST",
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao inscrever no curso");
  return resp.json();
}

export async function fetchInscricoes(usuarioId) {
  const resp = await fetch(`${API_BASE}/inscricoes/usuario/${usuarioId}`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao buscar inscrições");
  return resp.json();
}

export async function verificarInscricao(usuarioId, cursoId) {
  const resp = await fetch(`${API_BASE}/inscricoes/usuario/${usuarioId}/curso/${cursoId}`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao verificar inscrição");
  const data = await resp.json();
  return data.inscrito;
}

// ---- Progresso de Aulas ----

export async function marcarAulaConcluida(usuarioId, videoId, concluida) {
  const resp = await fetch(`${API_BASE}/inscricoes/usuario/${usuarioId}/video/${videoId}/concluir`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify({ concluida }),
  });
  if (!resp.ok) throw new Error("Erro ao marcar aula");
  return resp.json();
}

export async function salvarAnotacao(usuarioId, videoId, anotacao) {
  const resp = await fetch(`${API_BASE}/inscricoes/usuario/${usuarioId}/video/${videoId}/anotacao`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify({ anotacao }),
  });
  if (!resp.ok) throw new Error("Erro ao salvar anotação");
  return resp.json();
}

export async function fetchProgresso(usuarioId, cursoId) {
  const resp = await fetch(`${API_BASE}/inscricoes/usuario/${usuarioId}/curso/${cursoId}/progresso`, {
    headers: getHeaders(),
  });
  if (!resp.ok) throw new Error("Erro ao buscar progresso");
  return resp.json();
}
