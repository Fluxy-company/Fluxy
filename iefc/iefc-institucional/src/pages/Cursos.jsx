import { useCallback, useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Button from '../components/Button'
import Skeleton from '../components/ui/Skeleton'
import EmptyState from '../components/ui/EmptyState'
import { useAuth } from '../contexts/AuthContext'
import { listarCursos } from '../services/cursos'

const TAMANHO_PAGINA = 9

export default function Cursos() {
  const { ehAdmin } = useAuth()
  const [pagina, setPagina] = useState(0)
  const [dados, setDados] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  const carregar = useCallback(async () => {
    setCarregando(true)
    setErro('')
    try {
      setDados(await listarCursos(pagina, TAMANHO_PAGINA))
    } catch (e) {
      setErro(e.message)
    } finally {
      setCarregando(false)
    }
  }, [pagina])

  useEffect(() => {
    carregar()
  }, [carregar])

  const cursos = dados?.content ?? []
  const totalPaginas = dados?.totalPages ?? 0

  return (
    <Layout>
      <PageHeader
        eyebrow="Plataforma de cursos"
        title="Catálogo de Cursos"
        lead="Cursos, palestras e oficinas gratuitas para estudantes, professores e entusiastas da Ciência."
      />

      <section className="px-6 md:px-10 py-16 bg-white">
        <div className="max-w-content mx-auto">
          {ehAdmin && (
            <div className="flex justify-end mb-6">
              <Button to="/cursos/novo" variant="amber" icon="ph-plus">
                Novo curso
              </Button>
            </div>
          )}

          {carregando && !dados && (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              <Skeleton className="h-44" />
              <Skeleton className="h-44" />
              <Skeleton className="h-44" />
            </div>
          )}

          {erro && (
            <EmptyState
              icone="ph-light ph-warning-circle"
              titulo="Não foi possível carregar os cursos"
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

          {!erro && dados && cursos.length === 0 && (
            <EmptyState
              icone="ph-light ph-books"
              titulo="Nenhum curso disponível"
              descricao="Os cursos cadastrados aparecerão aqui."
            />
          )}

          {!erro && cursos.length > 0 && (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {cursos.map((c) => (
                <NavLink
                  key={c.id}
                  to={`/cursos/${c.id}`}
                  className="lift border border-border rounded-lg2 overflow-hidden bg-white cursor-pointer block"
                >
                  <div className="h-2 bg-teal" />
                  <div className="p-6">
                    <div className="flex items-center justify-between mb-4">
                      <span className="font-label text-[10px] tracking-[0.14em] uppercase text-teal-accessible">
                        {c.tema?.nome ?? 'Curso'}
                      </span>
                      <i className="ph-light ph-graduation-cap text-[24px] text-teal-accessible" />
                    </div>
                    <h3 className="font-display font-bold text-[19px] text-navy mb-2">{c.titulo}</h3>
                    <p className="text-[13px] text-ink-soft m-0 mb-1 line-clamp-2">{c.descricao}</p>
                    <p className="text-[13px] text-ink-caption m-0 mb-4">
                      {c.instrutor} · {c.totalAulas ?? 0} {c.totalAulas === 1 ? 'aula' : 'aulas'}
                    </p>
                    <span className="font-label text-[12px] tracking-[0.06em] uppercase text-teal-accessible inline-flex items-center gap-1.5">
                      Ver curso <i className="ph ph-arrow-right" />
                    </span>
                  </div>
                </NavLink>
              ))}
            </div>
          )}

          {totalPaginas > 1 && (
            <nav aria-label="Paginação" className="flex items-center justify-between mt-8">
              <button
                type="button"
                disabled={pagina === 0 || carregando}
                onClick={() => setPagina(pagina - 1)}
                className="inline-flex items-center gap-2 font-label font-bold text-[12px] tracking-[0.08em] uppercase text-teal-accessible disabled:opacity-40"
              >
                <i className="ph-light ph-caret-left" /> Anterior
              </button>
              <span className="text-[13px] text-ink-soft">
                Página {pagina + 1} de {totalPaginas}
              </span>
              <button
                type="button"
                disabled={pagina + 1 >= totalPaginas || carregando}
                onClick={() => setPagina(pagina + 1)}
                className="inline-flex items-center gap-2 font-label font-bold text-[12px] tracking-[0.08em] uppercase text-teal-accessible disabled:opacity-40"
              >
                Próxima <i className="ph-light ph-caret-right" />
              </button>
            </nav>
          )}
        </div>
      </section>
    </Layout>
  )
}
