import { api } from './api'

export function contarPendentes() {
  return api('/usuarios/pendentes/paginado?size=1')
}

export function listarColaboradoresPorStatus(status, pagina = 0, tamanho = 10) {
  return api(`/usuarios/colaboradores/status/${status}?page=${pagina}&size=${tamanho}`)
}

export function listarPendentes(pagina = 0, tamanho = 10) {
  return api(`/usuarios/pendentes/paginado?page=${pagina}&size=${tamanho}`)
}

export function aprovarColaborador(id) {
  return api(`/usuarios/${id}/aprovar`, { method: 'PUT' })
}

export function reprovarColaborador(id) {
  return api(`/usuarios/${id}/reprovar`, { method: 'PUT' })
}

export function listarAdministradores() {
  return api('/usuarios/admin-users')
}

export function cadastrarAdministrador({ nome, email, senha }) {
  return api('/usuarios/admin', { method: 'POST', body: { nome, email, senha, role: 'ADMIN' } })
}
