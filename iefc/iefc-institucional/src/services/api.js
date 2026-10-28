const BASE_URL = (import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1').replace(/\/+$/, '')

let tokenAtual = null

export function setAuthToken(token) {
  tokenAtual = token
}

export class ApiError extends Error {
  constructor(message, { status, campos } = {}) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.campos = campos ?? null
  }
}

function montarMensagem(status, dados) {
  
  if (dados?.erros && Object.keys(dados.erros).length > 0) {
    return dados.mensagem || 'Um ou mais campos inválidos. Verifique os detalhes.'
  }
  
  if (typeof dados?.details === 'string' && dados.details) {
    return dados.details
  }
  if (typeof dados?.message === 'string' && dados.message) {
    return dados.message
  }

  switch (status) {
    case 401:
      return 'Email ou senha inválidos.'
    case 403:
      return 'Você não tem permissão para realizar esta ação.'
    case 409:
      return 'Este e-mail já está cadastrado.'
    case 429:
      return 'Muitas requisições. Aguarde um instante e tente novamente.'
    case 0:
      return 'Não foi possível conectar ao servidor. Verifique sua conexão e tente novamente.'
    default:
      return 'Ocorreu um erro inesperado. Tente novamente.'
  }
}

export async function api(path, { method = 'GET', body, auth = true, isForm = false } = {}) {
  const headers = {}
  if (!isForm) headers['Content-Type'] = 'application/json'
  if (auth && tokenAtual) headers.Authorization = `Bearer ${tokenAtual}`

  let response
  try {
    response = await fetch(`${BASE_URL}${path}`, {
      method,
      headers,
      body: body === undefined ? undefined : isForm ? body : JSON.stringify(body),
    })
  } catch {
    throw new ApiError(montarMensagem(0), { status: 0 })
  }

  const texto = await response.text()
  let dados = null
  if (texto) {
    try {
      dados = JSON.parse(texto)
    } catch {
      dados = { message: texto }
    }
  }

  if (!response.ok) {
    if (response.status === 401 && auth) {
      window.dispatchEvent(new CustomEvent('iefc:sessao-expirada'))
    }
    if (response.status === 429) {
      window.dispatchEvent(
        new CustomEvent('iefc:toast', { detail: { tipo: 'erro', mensagem: montarMensagem(429, dados) } })
      )
    }
    throw new ApiError(montarMensagem(response.status, dados), {
      status: response.status,
      campos: dados?.erros,
    })
  }

  return dados
}
