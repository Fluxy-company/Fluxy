import { api } from './api'

export function buscarMeusCursos(idUsuario) {
  return api(`/usuarios/${idUsuario}/meus-cursos`)
}

export function listarCursos(pagina = 0, tamanho = 9) {
  return api(`/cursos?page=${pagina}&size=${tamanho}`)
}

export function buscarCurso(id) {
  return api(`/cursos/${id}`)
}

export function criarCurso(curso) {
  return api('/cursos', { method: 'POST', body: curso })
}

export function excluirCurso(id) {
  return api(`/cursos/${id}`, { method: 'DELETE' })
}

export function listarAulas(idCurso) {
  return api(`/cursos/${idCurso}/videos`)
}

export function adicionarAula(idCurso, aula) {
  return api(`/cursos/${idCurso}/videos`, { method: 'POST', body: aula })
}

export function listarTemas() {
  return api('/temas?size=100')
}

export function inscreverEmCurso(idUsuario, idCurso) {
  return api(`/inscricoes/usuario/${idUsuario}/curso/${idCurso}`, { method: 'POST' })
}

export function listarProgresso(idUsuario, idCurso) {
  return api(`/inscricoes/usuario/${idUsuario}/curso/${idCurso}/progresso`)
}

export function marcarAulaConcluida(idUsuario, idVideo, concluida) {
  return api(`/inscricoes/usuario/${idUsuario}/video/${idVideo}/concluir`, {
    method: 'POST',
    body: { concluida },
  })
}
