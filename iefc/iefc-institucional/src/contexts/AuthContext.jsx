import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as authApi from '../services/auth'
import { ApiError, setAuthToken } from '../services/api'
import { decodeJwt } from '../services/jwt'
import { useToast } from './ToastContext'

const CHAVE_SESSAO = 'iefc:sessao'
const ANTECEDENCIA_AVISO_MS = 60_000

const AuthContext = createContext(null)

function lerSessaoSalva() {
  try {
    const bruto = localStorage.getItem(CHAVE_SESSAO)
    if (!bruto) return null
    const sessao = JSON.parse(bruto)
    if (!sessao?.token || !sessao?.expiraEm || sessao.expiraEm <= Date.now()) {
      localStorage.removeItem(CHAVE_SESSAO)
      return null
    }
    return sessao
  } catch {
    return null
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null)
  const [usuario, setUsuario] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const navigate = useNavigate()
  const { mostrarToast } = useToast()
  const timersRef = useRef({ aviso: null, expiracao: null })

  const limparTimers = useCallback(() => {
    clearTimeout(timersRef.current.aviso)
    clearTimeout(timersRef.current.expiracao)
  }, [])

  const limparSessao = useCallback(() => {
    limparTimers()
    localStorage.removeItem(CHAVE_SESSAO)
    setAuthToken(null)
    setToken(null)
    setUsuario(null)
  }, [limparTimers])

  const agendarExpiracao = useCallback(
    (expiraEm) => {
      limparTimers()
      const restante = expiraEm - Date.now()
      if (restante <= 0) return

      if (restante > ANTECEDENCIA_AVISO_MS) {
        timersRef.current.aviso = setTimeout(() => {
          mostrarToast({ tipo: 'aviso', mensagem: 'Sua sessão vai expirar em 1 minuto.' })
        }, restante - ANTECEDENCIA_AVISO_MS)
      }

      timersRef.current.expiracao = setTimeout(() => {
        limparSessao()
        navigate('/login?expirou=1')
      }, restante)
    },
    [limparTimers, limparSessao, mostrarToast, navigate]
  )

  const salvarSessao = useCallback(
    (novoToken, expiraEmSegundos) => {
      const expiraEm = Date.now() + expiraEmSegundos * 1000
      localStorage.setItem(CHAVE_SESSAO, JSON.stringify({ token: novoToken, expiraEm }))
      setAuthToken(novoToken)
      setToken(novoToken)
      agendarExpiracao(expiraEm)
    },
    [agendarExpiracao]
  )

  const carregarPerfil = useCallback(async (tokenAtual) => {
    const claims = decodeJwt(tokenAtual)
    if (!claims?.sub) throw new ApiError('Sessão inválida.', { status: 401 })
    const perfil = await authApi.buscarUsuarioPorId(claims.sub)
    setUsuario(perfil)
    return perfil
  }, [])

  useEffect(() => {
    const sessao = lerSessaoSalva()
    if (!sessao) {
      setCarregando(false)
      return
    }
    setAuthToken(sessao.token)
    setToken(sessao.token)
    agendarExpiracao(sessao.expiraEm)
    carregarPerfil(sessao.token)
      .catch(() => limparSessao())
      .finally(() => setCarregando(false))
  }, [])

  useEffect(() => {
    function aoExpirar() {
      limparSessao()
      navigate('/login?expirou=1')
    }
    window.addEventListener('iefc:sessao-expirada', aoExpirar)
    return () => window.removeEventListener('iefc:sessao-expirada', aoExpirar)
  }, [limparSessao, navigate])

  const login = useCallback(
    async (email, senha) => {
      try {
        const resposta = await authApi.login(email, senha)
        salvarSessao(resposta.token, resposta.expiraEm)
        await carregarPerfil(resposta.token)
        return { ok: true }
      } catch (erro) {
        limparSessao()
        return { ok: false, erro: erro instanceof ApiError ? erro : new ApiError('Erro ao entrar. Tente novamente.') }
      }
    },
    [salvarSessao, carregarPerfil, limparSessao]
  )

  const cadastrarAluno = useCallback(
    async (dados) => {
      try {
        await authApi.cadastrarAluno(dados)
      } catch (erro) {
        return { ok: false, erro: erro instanceof ApiError ? erro : new ApiError('Erro ao cadastrar. Tente novamente.') }
      }
      return login(dados.email, dados.senha)
    },
    [login]
  )

  const cadastrarColaborador = useCallback(async (dados) => {
    try {
      await authApi.cadastrarColaborador(dados)
      return { ok: true, pendente: true }
    } catch (erro) {
      return { ok: false, erro: erro instanceof ApiError ? erro : new ApiError('Erro ao cadastrar. Tente novamente.') }
    }
  }, [])

  const sair = useCallback(() => {
    limparSessao()
  }, [limparSessao])

  const roles = usuario?.roles ?? []
  const ehAdmin = roles.includes('ADMIN')
  const ehColaborador = roles.includes('COLABORADOR')
  const ehAluno = roles.includes('ALUNO')

  const value = useMemo(
    () => ({
      token,
      usuario,
      roles,
      carregando,
      estaLogado: Boolean(token && usuario),
      ehAdmin,
      ehColaborador,
      ehAluno,
      podeGerir: ehAdmin || ehColaborador,
      login,
      cadastrarAluno,
      cadastrarColaborador,
      sair,
    }),
    [token, usuario, carregando, ehAdmin, ehColaborador, ehAluno, login, cadastrarAluno, cadastrarColaborador, sair]
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const contexto = useContext(AuthContext)
  if (!contexto) throw new Error('useAuth deve ser usado dentro de <AuthProvider>')
  return contexto
}
