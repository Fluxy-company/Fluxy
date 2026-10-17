import { useState } from 'react'
import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import Button from '../components/Button'
import Layout from '../components/Layout'

export default function Login() {
  const navigate = useNavigate()
  const location = useLocation()

  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')

  const destino = location.state?.from || '/area'

  function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    const resultado = login(email, senha)
    if (!resultado.ok) {
      setErro(resultado.erro)
      return
    }
    navigate(destino, { replace: true })
  }

  return (
    <Layout >
      <div className="min-h-screen bg-navy flex items-center justify-center px-4 font-body py-10">
        <div className="w-full max-w-[400px]">
          <div className="bg-cream rounded-lg2 p-8">
            <NavLink to="/">
              <img src="/iefc-azul.svg" alt="IEFC" className="h-8 w-auto" />
            </NavLink>

            <h1 className="font-display font-bold text-navy text-xl mt-6 mb-1">Entrar</h1>
            <p className="text-sm text-ink-soft mb-6">
              Acesse sua conta de estudante ou colaborador do IEFC.
            </p>

            {erro && (
              <div className="flex items-start gap-2 bg-danger/10 border border-danger/30 rounded-md2 px-3 py-2.5 mb-4">
                <i className="ph ph-warning-circle text-danger text-[16px] mt-0.5" />
                <p className="text-[13px] text-danger m-0">{erro}</p>
              </div>
            )}

            <form className="space-y-4" onSubmit={handleSubmit}>
              <label className="block">
                <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                  E-mail
                </span>
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="seu@email.com"
                  className="w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none"
                />
              </label>
              <label className="block">
                <span className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption">
                  Senha
                </span>
                <input
                  type="password"
                  required
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  placeholder="********"
                  className="w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none"
                />
              </label>

              <Button variant="amber" icon={null} className="w-full mt-2" type="submit">
                Entrar
              </Button>
            </form>

            <p className="text-center text-[12px] text-ink-caption mt-5">
              Ainda não tem conta?{' '}
              <NavLink to="/cadastro" className="text-teal-accessible font-medium">
                Cadastre-se
              </NavLink>
            </p>

          </div>

          <p className="text-center text-on-dark-muted text-xs mt-6">
            IEFC · Instituto Educacional Futuro da Ciência
          </p>
        </div>
      </div>
    </Layout>
  )
}
