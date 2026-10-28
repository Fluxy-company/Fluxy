import { NavLink } from 'react-router-dom'
import Button from '../components/Button'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import { useAuth } from '../contexts/AuthContext'

const NIVEIS = {
  ADMIN: { titulo: 'Administrador', descricao: 'Gerencia cursos, colaboradores e todo o conteúdo da plataforma.', icone: 'ph-light ph-shield-star' },
  COLABORADOR: { titulo: 'Colaborador', descricao: 'Membro da equipe IEFC: cria eventos e gera relatórios.', icone: 'ph-light ph-handshake' },
  ALUNO: { titulo: 'Aluno', descricao: 'Acessa os cursos, acompanha aulas e registra seu progresso.', icone: 'ph-light ph-graduation-cap' },
}

const STATUS = {
  APROVADO: { titulo: 'Aprovado', cls: 'bg-ice text-teal-accessible' },
  PENDENTE: { titulo: 'Pendente', cls: 'bg-amber-soft/30 text-navy' },
  REPROVADO: { titulo: 'Recusado', cls: 'bg-danger/10 text-danger' },
}

const LABEL_CLS = 'font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption'

export default function Perfil() {
  const { usuario, roles, sair } = useAuth()
  const status = STATUS[usuario?.status] ?? STATUS.APROVADO
  const iniciais = (usuario?.nome ?? '?')
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((p) => p[0].toUpperCase())
    .join('')

  return (
    <Layout>
      <PageHeader eyebrow="Minha conta" title="Meu perfil" lead="Seus dados e o seu nível de acesso no IEFC." />

      <section className="px-6 md:px-10 py-14 bg-white">
        <div className="max-w-[720px] mx-auto space-y-6">
          <div className="border border-border rounded-lg2 shadow-card p-6 flex items-center gap-5">
            <div className="w-16 h-16 rounded-full bg-navy text-white font-display font-bold text-[22px] flex items-center justify-center shrink-0">
              {iniciais}
            </div>
            <div className="min-w-0 flex-1">
              <h2 className="font-display font-bold text-navy text-[20px] truncate">{usuario?.nome}</h2>
              <p className="text-[14px] text-ink-soft truncate">{usuario?.email}</p>
            </div>
            <span className={`font-label text-[11px] tracking-[0.1em] uppercase px-3 py-1.5 rounded-md2 ${status.cls}`}>
              {status.titulo}
            </span>
          </div>

          <div className="border border-border rounded-lg2 p-6">
            <h2 className="font-display font-medium text-navy text-[18px] mb-4">Dados da conta</h2>
            <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <dt className={LABEL_CLS}>Nome</dt>
                <dd className="text-[14px] text-navy mt-1">{usuario?.nome}</dd>
              </div>
              <div>
                <dt className={LABEL_CLS}>E-mail</dt>
                <dd className="text-[14px] text-navy mt-1">{usuario?.email}</dd>
              </div>
              <div>
                <dt className={LABEL_CLS}>Situação do cadastro</dt>
                <dd className="text-[14px] text-navy mt-1">{status.titulo}</dd>
              </div>
            </dl>
          </div>

          <div className="border border-border rounded-lg2 p-6">
            <h2 className="font-display font-medium text-navy text-[18px] mb-4">Nível de acesso</h2>
            <ul className="space-y-3">
              {roles.map((papel) => {
                const nivel = NIVEIS[papel]
                return (
                  <li key={papel} className="flex items-start gap-3">
                    <i className={`${nivel?.icone ?? 'ph-light ph-user'} text-[24px] text-teal-accessible`} />
                    <div>
                      <p className="font-display font-medium text-navy text-[15px]">{nivel?.titulo ?? papel}</p>
                      {nivel && <p className="text-[13px] text-ink-soft">{nivel.descricao}</p>}
                    </div>
                  </li>
                )
              })}
            </ul>
          </div>

          <div className="flex flex-wrap items-center gap-4">
            <Button to="/cursos" variant="outline" icon={null}>Ver catálogo</Button>
            <NavLink
              to="/"
              onClick={sair}
              className="font-label font-bold text-[12px] tracking-[0.08em] uppercase text-ink-soft inline-flex items-center gap-1.5"
            >
              <i className="ph-light ph-sign-out" /> Sair
            </NavLink>
          </div>
        </div>
      </section>
    </Layout>
  )
}
