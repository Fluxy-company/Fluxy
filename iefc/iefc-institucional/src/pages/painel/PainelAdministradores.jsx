import { useCallback, useEffect, useState } from 'react'
import Button from '../../components/Button'
import Layout from '../../components/Layout'
import PageHeader from '../../components/PageHeader'
import Skeleton from '../../components/ui/Skeleton'
import EmptyState from '../../components/ui/EmptyState'
import { useToast } from '../../contexts/ToastContext'
import { cadastrarAdministrador, listarAdministradores } from '../../services/usuarios'

const INPUT_CLS =
  'w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none'
const LABEL_CLS = 'font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption'
const FORM_VAZIO = { nome: '', email: '', senha: '', confirmarSenha: '' }

export default function PainelAdministradores() {
  const { mostrarToast } = useToast()
  const [admins, setAdmins] = useState(null)
  const [erro, setErro] = useState('')
  const [form, setForm] = useState(FORM_VAZIO)
  const [erroForm, setErroForm] = useState('')
  const [enviando, setEnviando] = useState(false)

  const carregar = useCallback(async () => {
    setErro('')
    try {
      setAdmins(await listarAdministradores())
    } catch (e) {
      setErro(e.message)
    }
  }, [])

  useEffect(() => {
    carregar()
  }, [carregar])

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }))

  async function handleSubmit(e) {
    e.preventDefault()
    setErroForm('')
    if (form.senha.length < 6) {
      setErroForm('A senha deve ter pelo menos 6 caracteres.')
      return
    }
    if (form.senha !== form.confirmarSenha) {
      setErroForm('As senhas não coincidem.')
      return
    }
    setEnviando(true)
    try {
      await cadastrarAdministrador(form)
      mostrarToast({ tipo: 'sucesso', mensagem: `${form.nome} agora é administrador(a).` })
      setForm(FORM_VAZIO)
      await carregar()
    } catch (e2) {
      if (e2.status !== 429) setErroForm(e2.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <Layout>
      <PageHeader
        eyebrow="Painel de gestão"
        title="Administradores"
        lead="Cadastre novos administradores e consulte quem já tem acesso total à plataforma."
      />
      <section className="px-6 md:px-10 py-14">
        <div className="max-w-content mx-auto grid grid-cols-1 lg:grid-cols-[1fr_1.2fr] gap-8 items-start">
          <form
            onSubmit={handleSubmit}
            className="bg-white border border-border rounded-lg2 shadow-card p-6 space-y-4"
          >
            <h2 className="font-display font-medium text-navy text-[18px]">Novo administrador</h2>

            {erroForm && (
              <div className="flex items-start gap-2 bg-danger/10 border border-danger/30 rounded-md2 px-3 py-2.5">
                <i className="ph-light ph-warning-circle text-danger text-[16px] mt-0.5" />
                <p className="text-[13px] text-danger m-0">{erroForm}</p>
              </div>
            )}

            <label className="block">
              <span className={LABEL_CLS}>Nome completo</span>
              <input required value={form.nome} onChange={set('nome')} className={INPUT_CLS} />
            </label>
            <label className="block">
              <span className={LABEL_CLS}>E-mail</span>
              <input
                type="email"
                required
                value={form.email}
                onChange={set('email')}
                className={INPUT_CLS}
              />
            </label>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <label className="block">
                <span className={LABEL_CLS}>Senha</span>
                <input
                  type="password"
                  required
                  value={form.senha}
                  onChange={set('senha')}
                  placeholder="********"
                  className={INPUT_CLS}
                />
              </label>
              <label className="block">
                <span className={LABEL_CLS}>Confirmar senha</span>
                <input
                  type="password"
                  required
                  value={form.confirmarSenha}
                  onChange={set('confirmarSenha')}
                  placeholder="********"
                  className={INPUT_CLS}
                />
              </label>
            </div>
            <Button
              variant="amber"
              icon="ph-plus"
              type="submit"
              disabled={enviando}
              className="w-full disabled:opacity-60 disabled:cursor-not-allowed"
            >
              {enviando ? 'Cadastrando...' : 'Cadastrar administrador'}
            </Button>
          </form>

          <div className="bg-white border border-border rounded-lg2 shadow-card overflow-hidden">
            <div className="flex items-center justify-between px-6 py-4 border-b border-border">
              <h2 className="font-display font-medium text-navy text-[18px]">Administradores ativos</h2>
              {admins && (
                <span className="font-label text-[12px] tracking-[0.08em] uppercase text-ink-soft">
                  {admins.length} {admins.length === 1 ? 'registro' : 'registros'}
                </span>
              )}
            </div>

            {!admins && !erro && (
              <div className="p-6 flex flex-col gap-3">
                <Skeleton className="h-14" />
                <Skeleton className="h-14" />
              </div>
            )}

            {erro && (
              <EmptyState
                icone="ph-light ph-warning-circle"
                titulo="Não foi possível carregar a lista"
                descricao={erro}
                acao={
                  <button
                    type="button"
                    onClick={carregar}
                    className="font-label font-bold text-[13px] tracking-[0.08em] uppercase text-teal-accessible"
                  >
                    Tentar novamente
                  </button>
                }
              />
            )}

            {admins && admins.length === 0 && (
              <EmptyState icone="ph-light ph-shield-star" titulo="Nenhum administrador encontrado" />
            )}

            {admins && admins.length > 0 && (
              <ul className="divide-y divide-border">
                {admins.map((a) => (
                  <li key={a.idUsuario} className="flex items-center gap-4 px-6 py-4 lift">
                    <div className="w-10 h-10 rounded-full bg-navy text-white font-display font-bold text-[14px] flex items-center justify-center shrink-0">
                      {(a.nome ?? '?').split(' ').filter(Boolean).slice(0, 2).map((p) => p[0].toUpperCase()).join('')}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="font-display font-medium text-navy text-[16px] truncate">{a.nome}</p>
                      <p className="text-[13px] text-ink-soft truncate">{a.email}</p>
                    </div>
                    <span className="font-label text-[11px] tracking-[0.1em] uppercase px-3 py-1.5 rounded-md2 bg-ice text-teal-accessible">
                      Admin
                    </span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </section>
    </Layout>
  )
}
