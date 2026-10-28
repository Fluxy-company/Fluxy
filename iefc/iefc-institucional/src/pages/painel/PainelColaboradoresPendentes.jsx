import { useCallback, useEffect, useState } from 'react'
import Layout from '../../components/Layout'
import PageHeader from '../../components/PageHeader'
import Skeleton from '../../components/ui/Skeleton'
import EmptyState from '../../components/ui/EmptyState'
import Tabs from '../../components/ui/Tabs'
import { useToast } from '../../contexts/ToastContext'
import { aprovarColaborador, listarColaboradoresPorStatus, reprovarColaborador } from '../../services/usuarios'

const TAMANHO_PAGINA = 10

const ABAS = [
  { valor: 'PENDENTE', titulo: 'Pendentes', vazio: 'Nenhum cadastro pendente', icone: 'ph-light ph-users' },
  { valor: 'APROVADO', titulo: 'Aprovados', vazio: 'Nenhum colaborador aprovado ainda', icone: 'ph-light ph-user-check' },
  { valor: 'REPROVADO', titulo: 'Recusados', vazio: 'Nenhum cadastro recusado', icone: 'ph-light ph-user-minus' },
]

export default function PainelColaboradoresPendentes() {
  const [aba, setAba] = useState('PENDENTE')
  const [pagina, setPagina] = useState(0)
  const [dados, setDados] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')
  const [processandoId, setProcessandoId] = useState(null)
  const { mostrarToast } = useToast()

  const abaAtual = ABAS.find((a) => a.valor === aba)

  const carregar = useCallback(async () => {
    setCarregando(true)
    setErro('')
    try {
      const resposta = await listarColaboradoresPorStatus(aba, pagina, TAMANHO_PAGINA)
      if (resposta.content.length === 0 && pagina > 0) {
        setPagina(pagina - 1)
        return
      }
      setDados(resposta)
    } catch (e) {
      setErro(e.message)
    } finally {
      setCarregando(false)
    }
  }, [aba, pagina])

  useEffect(() => {
    carregar()
  }, [carregar])

  function trocarAba(valor) {
    setDados(null)
    setPagina(0)
    setAba(valor)
  }

  async function analisar(colaborador, aprovar) {
    setProcessandoId(colaborador.idUsuario)
    try {
      if (aprovar) await aprovarColaborador(colaborador.idUsuario)
      else await reprovarColaborador(colaborador.idUsuario)
      mostrarToast({
        tipo: 'sucesso',
        mensagem: `Cadastro de ${colaborador.nome} ${aprovar ? 'aprovado' : 'recusado'}.`,
      })
      await carregar()
    } catch (e) {
      mostrarToast({ tipo: 'erro', mensagem: e.message })
    } finally {
      setProcessandoId(null)
    }
  }

  const colaboradores = dados?.content ?? []
  const totalPaginas = dados?.totalPages ?? 0

  return (
    <Layout>
      <PageHeader
        eyebrow="Painel de gestão"
        title="Gestão de colaboradores"
        lead="Aprove ou recuse cadastros e consulte o histórico de quem já foi analisado."
      />
      <section className="px-6 md:px-10 py-14">
        <div className="max-w-content mx-auto">
          <Tabs abas={ABAS} ativa={aba} onChange={trocarAba} className="mb-6" />

          <div className="bg-white border border-border rounded-lg2 shadow-card overflow-hidden">
            <div className="flex items-center justify-between px-6 py-4 border-b border-border">
              <h2 className="font-display font-medium text-navy text-[18px]">{abaAtual.titulo}</h2>
              {dados && (
                <span className="font-label text-[12px] tracking-[0.08em] uppercase text-ink-soft">
                  {dados.totalElements} {dados.totalElements === 1 ? 'registro' : 'registros'}
                </span>
              )}
            </div>

            {carregando && !dados && (
              <div className="p-6 flex flex-col gap-3">
                <Skeleton className="h-14" />
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

            {!erro && dados && colaboradores.length === 0 && (
              <EmptyState icone={abaAtual.icone} titulo={abaAtual.vazio} />
            )}

            {!erro && colaboradores.length > 0 && (
              <ul className="divide-y divide-border">
                {colaboradores.map((c) => {
                  const ocupado = processandoId === c.idUsuario
                  return (
                    <li
                      key={c.idUsuario}
                      className="flex flex-col md:flex-row md:items-center gap-4 px-6 py-4 lift"
                    >
                      <div className="flex-1 min-w-0">
                        <p className="font-display font-medium text-navy text-[16px] truncate">{c.nome}</p>
                        <p className="text-[13px] text-ink-soft truncate">{c.email}</p>
                      </div>

                      {aba === 'PENDENTE' ? (
                        <div className="flex gap-3">
                          <button
                            type="button"
                            disabled={ocupado}
                            onClick={() => analisar(c, true)}
                            aria-label={`Aprovar ${c.nome}`}
                            className="inline-flex items-center gap-2 font-label font-bold text-[12px] tracking-[0.08em] uppercase px-4 py-2.5 rounded-md2 bg-amber text-navy hover:bg-amber-light transition-colors disabled:opacity-50"
                          >
                            <i className="ph-light ph-check" /> Aprovar
                          </button>
                          <button
                            type="button"
                            disabled={ocupado}
                            onClick={() => analisar(c, false)}
                            aria-label={`Recusar ${c.nome}`}
                            className="inline-flex items-center gap-2 font-label font-bold text-[12px] tracking-[0.08em] uppercase px-4 py-2.5 rounded-md2 border-[1.5px] border-danger text-danger hover:bg-danger hover:text-white transition-colors disabled:opacity-50"
                          >
                            <i className="ph-light ph-x" /> Recusar
                          </button>
                        </div>
                      ) : (
                        <span
                          className={`font-label text-[11px] tracking-[0.1em] uppercase px-3 py-1.5 rounded-md2 ${
                            aba === 'APROVADO' ? 'bg-ice text-teal-accessible' : 'bg-danger/10 text-danger'
                          }`}
                        >
                          {aba === 'APROVADO' ? 'Aprovado' : 'Recusado'}
                        </span>
                      )}
                    </li>
                  )
                })}
              </ul>
            )}

            {totalPaginas > 1 && (
              <nav
                aria-label="Paginação"
                className="flex items-center justify-between px-6 py-4 border-t border-border"
              >
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
        </div>
      </section>
    </Layout>
  )
}
