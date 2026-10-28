import { api } from './api'

export function login(email, senha) {
  return api('/login', { method: 'POST', body: { email, senha }, auth: false })
}

export function cadastrarAluno({ nome, email, senha }) {
  return api('/usuarios', { method: 'POST', body: { nome, email, senha }, auth: false })
}

export function cadastrarColaborador({ nome, email, senha }) {
  return api('/usuarios/colaboradores', { method: 'POST', body: { nome, email, senha }, auth: false })
}

export function buscarUsuarioPorId(id) {
  return api(`/usuarios/${id}`)
}
