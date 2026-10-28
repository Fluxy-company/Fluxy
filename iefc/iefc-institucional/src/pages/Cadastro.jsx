import { useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import Button from '../components/Button'
import Layout from '../components/Layout'
import { useAuth } from '../contexts/AuthContext'

const TIPOS = [
  {
    valor: 'estudante',
    titulo: 'Estudante',
    desc: 'Quero acompanhar cursos e conteúdos do IEFC.',
    icon: 'ph-student',
  },
  {
    valor: 'colaborador',
    titulo: 'Colaborador',
    desc: 'Faço parte da equipe IEFC.',
    icon: 'ph-identification-badge',
  },
]

export default function Cadastro() {
  const navigate = useNavigate()
  const { cadastrarAluno, cadastrarColaborador } = useAuth()

  const [form, setForm] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: '',
    tipo: 'estudante',
  })
  const [erro, setErro] = useState('')
  const [enviando, setEnviando] = useState(false)
  const [cadastroPendente, setCadastroPendente] = useState(false)

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }))

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')

    if (form.senha.length < 6) {
      setErro('A senha deve ter pelo menos 6 caracteres.')
      return
    }
    if (form.senha !== form.confirmarSenha) {
      setErro('As senhas não coincidem.')
      return
    }

    setEnviando(true)
    const dados = { nome: form.nome, email: form.email, senha: form.senha }
    const resultado =
      form.tipo === 'colaborador' ? await cadastrarColaborador(dados) : await cadastrarAluno(dados)
    setEnviando(false)

    if (!resultado.ok) {
      if (resultado.erro.status !== 429) setErro(resultado.erro.message)
      return
    }

    if (resultado.pendente) {
      setCadastroPendente(true)
      return
    }

    navigate('/', { replace: true })
  }

  return (
    <Layout>
      <div className="min-h-screen bg-navy flex items-center justify-center px-4 font-body py-10">
        <div className="w-full max-w-[460px]">
          <div className="bg-cream rounded-lg2 p-8">
            <NavLink to="/">
              <img src="/iefc-azul.svg" alt="IEFC" className="h-8 w-auto" />
            </NavLink>

            <h1 className="font-display font-bold text-navy text-xl mt-6 mb-1">Criar conta</h1>
            <p className="text-sm text-ink-soft mb-6">
              Cadastre-se para acessar os cursos e conteúdos do IEFC.
            </p>

            {erro && (
              <div className="flex items-start gap-2 bg-danger/10 border border-danger/30 rounded-md2 px-3 py-2.5 mb-4">
                <i className="ph ph-warning-circle text-danger text-[16px] mt-0.5" />
                <p className="text-[13px] text-danger m-0">{erro}</p>
              </div>
            )}

            {cadastroPendente ? (
              <div className="flex items-start gap-3 bg-teal-pale border border-teal-accessible/30 rounded-md2 px-4 py-4">
                <i className="ph ph-clock-countdown text-teal-accessible text-[20px] mt-0.5" />
                <div>
                  <p className="text-[14px] font-display font-medium text-navy m-0">
                    Cadastro enviado para aprovação
                  </p>
                  <p className="text-[13px] text-ink-soft mt-1 mb-0">
                    Seu cadastro de colaborador está aguardando aprovação do administrador. Você
                    receberá um e-mail assim que ele for analisado e poderá entrar após a aprovação.
                  </p>
                  <NavLink to="/login" className="inline-block mt-3 text-teal-accessible font-medium text-[13px]">
                    Ir para a tela de login
                  </NavLink>
                </div>
              </div>
            ) : (
            <form className="space-y-4" onSubmit={handleSubmit}>
              <label className="block">
                <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                  Nome completo
                </span>
                <input
                  type="text"
                  required
                  value={form.nome}
                  onChange={set('nome')}
                  placeholder="Seu nome"
                  className="w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none"
                />
              </label>

              <label className="block">
                <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                  E-mail
                </span>
                <input
                  type="email"
                  required
                  value={form.email}
                  onChange={set('email')}
                  placeholder="seu@email.com"
                  className="w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none"
                />
              </label>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <label className="block">
                  <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                    Senha
                  </span>
                  <input
                    type="password"
                    required
                    value={form.senha}
                    onChange={set('senha')}
                    placeholder="********"
                    className="w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none"
                  />
                </label>
                <label className="block">
                  <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                    Confirmar senha
                  </span>
                  <input
                    type="password"
                    required
                    value={form.confirmarSenha}
                    onChange={set('confirmarSenha')}
                    placeholder="********"
                    className="w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none"
                  />
                </label>
              </div>

              <div>
                <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                  Cadastro para
                </span>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 mt-1.5">
                  {TIPOS.map((t) => (
                    <label
                      key={t.valor}
                      className={`flex items-start gap-3 border rounded-md2 px-3.5 py-3 cursor-pointer transition-colors ${
                        form.tipo === t.valor
                          ? 'border-teal-accessible bg-teal-pale'
                          : 'border-input-border bg-surface-input'
                      }`}
                    >
                      <input
                        type="radio"
                        name="tipo"
                        value={t.valor}
                        checked={form.tipo === t.valor}
                        onChange={set('tipo')}
                        className="mt-1 accent-teal-accessible"
                      />
                      <span>
                        <span className="flex items-center gap-1.5 font-display font-medium text-navy text-[14px]">
                          <i className={`ph ${t.icon} text-[16px]`} />
                          {t.titulo}
                        </span>
                        <span className="block text-[12px] text-ink-soft mt-0.5">{t.desc}</span>
                      </span>
                    </label>
                  ))}
                </div>
              </div>

              <Button
                variant="amber"
                icon={null}
                className="w-full mt-2 disabled:opacity-60 disabled:cursor-not-allowed"
                type="submit"
                disabled={enviando}
              >
                {enviando ? 'Enviando...' : 'Criar conta'}
              </Button>
            </form>
            )}

            {!cadastroPendente && (
              <p className="text-center text-[12px] text-ink-caption mt-5">
                Já tem conta?{' '}
                <NavLink to="/login" className="text-teal-accessible font-medium">
                  Entrar
                </NavLink>
              </p>
            )}
          </div>

          <p className="text-center text-on-dark-muted text-xs mt-6">
            IEFC · Instituto Educacional Futuro da Ciência
          </p>
        </div>
      </div>
    </Layout>
  )
}
