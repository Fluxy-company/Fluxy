import { useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom'
import Button from '../components/Button'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Skeleton from '../components/ui/Skeleton'
import EmptyState from '../components/ui/EmptyState'
import Tabs from '../components/ui/Tabs'
import { useAuth } from '../contexts/AuthContext'
import { buscarMeusCursos } from '../services/cursos'

const ABAS = [
  { valor: 'andamento', titulo: 'Em andamento', icone: 'ph-light ph-play-circle' },
  { valor: 'concluidos', titulo: 'Concluídos', icone: 'ph-light ph-seal-check' },
]

function percentualDe(c) {
  return c.totalAulas ? Math.round((c.aulasConcluidas / c.totalAulas) * 100) : 0
}

export default function MeusCursos() {
  const { usuario } = useAuth()
  const [cursos, setCursos] = useState(null)
  const [erro, setErro] = useState('')
  const [aba, setAba] = useState('andamento')

  async function carregar() {
    setErro('')
    setCursos(null)
    try {
      const resposta = await buscarMeusCursos(usuario.idUsuario)
      setCursos(Array.isArray(resposta) ? resposta : [])
    } catch (e) {
      setErro(e.message)
    }
  }

  useEffect(() => {
    carregar()
  }, [usuario?.idUsuario])

  const concluidos = (cursos ?? []).filter((c) => c.totalAulas > 0 && percentualDe(c) === 100)
  const emAndamento = (cursos ?? []).filter((c) => !concluidos.includes(c))
  const lista = aba === 'andamento' ? emAndamento : concluidos

  return (
    <Layout>
      <PageHeader
        eyebrow="Minha área"
        title="Meus cursos"
        lead="Acompanhe os cursos em que você está inscrito e continue de onde parou."
      />

      <section className="px-6 md:px-10 py-14 bg-white">
        <div className="max-w-content mx-auto">
          {cursos && (
            <div className="grid grid-cols-3 gap-4 mb-8 max-w-[560px]">
              {[
                { rotulo: 'Inscrições', valor: cursos.length },
                { rotulo: 'Em andamento', valor: emAndamento.length },
                { rotulo: 'Concluídos', valor: concluidos.length },
              ].map((s) => (
                <div key={s.rotulo} className="border border-border rounded-lg2 bg-cream px-4 py-4">
                  <p className="font-display font-bold text-navy text-[26px] m-0">{s.valor}</p>
                  <p className="font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption m-0">
                    {s.rotulo}
                  </p>
                </div>
              ))}
            </div>
          )}

          <Tabs abas={ABAS} ativa={aba} onChange={setAba} className="mb-6 overscroll-x-none" />

          {!cursos && !erro && (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              <Skeleton className="h-52" />
              <Skeleton className="h-52" />
              <Skeleton className="h-52" />
            </div>
          )}

          {erro && (
            <EmptyState
              icone="ph-light ph-warning-circle"
              titulo="Não foi possível carregar seus cursos"
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

          {!erro && cursos && lista.length === 0 && (
            <EmptyState
              icone={aba === 'andamento' ? 'ph-light ph-books' : 'ph-light ph-seal-check'}
              titulo={
                aba === 'andamento' ? 'Você não tem cursos em andamento' : 'Nenhum curso concluído ainda'
              }
              descricao="Explore o catálogo e inscreva-se em um curso."
              acao={
                <Button to="/cursos" variant="outline" icon={null}>
                  Explorar cursos
                </Button>
              }
            />
          )}

          {!erro && lista.length > 0 && (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {lista.map((c) => {
                const pct = percentualDe(c)
                return (
                  <NavLink
                    key={c.cursoId}
                    to={`/cursos/${c.cursoId}`}
                    className="lift border border-border rounded-lg2 overflow-hidden bg-white block"
                  >
                    <div className="h-2 bg-teal" />
                    <div className="p-6">
                      <div className="flex items-center justify-between mb-4">
                        <span className="font-label text-[10px] tracking-[0.14em] uppercase text-teal-accessible">
                          {c.tema ?? 'Curso'}
                        </span>
                        <i
                          className={`ph-light ${pct === 100 ? 'ph-seal-check' : 'ph-graduation-cap'} text-[24px] text-teal-accessible`}
                        />
                      </div>
                      <h3 className="font-display font-bold text-[19px] text-navy mb-1">{c.titulo}</h3>
                      <p className="text-[13px] text-ink-caption m-0 mb-4">{c.instrutor}</p>
                      <div
                        className="h-1.5 bg-ice rounded-full overflow-hidden mb-2"
                        role="progressbar"
                        aria-valuenow={pct}
                        aria-valuemin={0}
                        aria-valuemax={100}
                        aria-label={`Progresso em ${c.titulo}`}
                      >
                        <div className="h-full bg-amber-soft transition-all" style={{ width: `${pct}%` }} />
                      </div>
                      <p className="text-[12px] text-ink-caption mb-4">
                        {c.aulasConcluidas} de {c.totalAulas} aulas · {pct}%
                      </p>
                      <span className="font-label text-[12px] tracking-[0.06em] uppercase text-teal-accessible inline-flex items-center gap-1.5">
                        {pct === 100 ? 'Rever curso' : pct === 0 ? 'Começar' : 'Continuar'}
                        <i className="ph-light ph-arrow-right" />
                      </span>
                    </div>
                  </NavLink>
                )
              })}
            </div>
          )}
        </div>
      </section>
    </Layout>
  )
}
