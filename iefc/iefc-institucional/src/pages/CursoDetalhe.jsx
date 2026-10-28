import { useCallback, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Button from '../components/Button'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Skeleton from '../components/ui/Skeleton'
import EmptyState from '../components/ui/EmptyState'
import { useAuth } from '../contexts/AuthContext'
import { useToast } from '../contexts/ToastContext'
import {
  adicionarAula,
  buscarCurso,
  buscarMeusCursos,
  excluirCurso,
  inscreverEmCurso,
  listarAulas,
  listarProgresso,
  marcarAulaConcluida,
} from '../services/cursos'

const INPUT_CLS =
  'w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none'
const LABEL_CLS = 'font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption'
const AULA_VAZIA = { titulo: '', videoId: '', duracao: '', modulo: '', ordem: '' }

export default function CursoDetalhe() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { ehAdmin, usuario } = useAuth()
  const { mostrarToast } = useToast()
  const [inscrito, setInscrito] = useState(false)
  const [concluidas, setConcluidas] = useState(new Set())
  const [inscrevendo, setInscrevendo] = useState(false)

  const [curso, setCurso] = useState(null)
  const [aulas, setAulas] = useState([])
  const [aulaAtual, setAulaAtual] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')
  const [formAula, setFormAula] = useState(AULA_VAZIA)
  const [salvandoAula, setSalvandoAula] = useState(false)

  const carregar = useCallback(async () => {
    setCarregando(true)
    setErro('')
    try {
      const dadosCurso = await buscarCurso(id)
      setCurso(dadosCurso)
      let dadosAulas = []
      try {
        dadosAulas = await listarAulas(id)
      } catch (e) {
        if (e.status !== 404) throw e
      }
      dadosAulas.sort((a, b) => (a.ordem ?? 0) - (b.ordem ?? 0))
      setAulas(dadosAulas)
      setAulaAtual((atual) => atual ?? dadosAulas[0] ?? null)

      try {
        const meus = await buscarMeusCursos(usuario.idUsuario)
        const estaInscrito = Array.isArray(meus) && meus.some((c) => String(c.cursoId) === String(id))
        setInscrito(estaInscrito)
        if (estaInscrito) {
          const progresso = await listarProgresso(usuario.idUsuario, id)
          setConcluidas(new Set(progresso.filter((p) => p.concluida).map((p) => p.video?.id)))
        }
      } catch {
        setInscrito(false)
      }
    } catch (e) {
      setErro(e.message)
    } finally {
      setCarregando(false)
    }
  }, [id, usuario?.idUsuario])

  useEffect(() => {
    carregar()
  }, [carregar])

  async function handleAdicionarAula(e) {
    e.preventDefault()
    setSalvandoAula(true)
    try {
      await adicionarAula(id, {
        titulo: formAula.titulo,
        videoId: formAula.videoId,
        url: `https://www.youtube.com/watch?v=${formAula.videoId}`,
        duracao: formAula.duracao,
        modulo: formAula.modulo,
        ordem: Number(formAula.ordem) || aulas.length + 1,
      })
      mostrarToast({ tipo: 'sucesso', mensagem: 'Aula adicionada.' })
      setFormAula(AULA_VAZIA)
      await carregar()
    } catch (e) {
      mostrarToast({ tipo: 'erro', mensagem: e.message })
    } finally {
      setSalvandoAula(false)
    }
  }

  async function handleInscrever() {
    setInscrevendo(true)
    try {
      await inscreverEmCurso(usuario.idUsuario, id)
      setInscrito(true)
      mostrarToast({ tipo: 'sucesso', mensagem: 'Inscrição realizada! O curso está em Meus cursos.' })
    } catch (e) {
      mostrarToast({ tipo: 'erro', mensagem: e.message })
    } finally {
      setInscrevendo(false)
    }
  }

  async function alternarConclusao(aula) {
    const nova = !concluidas.has(aula.id)
    try {
      await marcarAulaConcluida(usuario.idUsuario, aula.id, nova)
      setConcluidas((atual) => {
        const proximo = new Set(atual)
        if (nova) proximo.add(aula.id)
        else proximo.delete(aula.id)
        return proximo
      })
    } catch (e) {
      mostrarToast({ tipo: 'erro', mensagem: e.message })
    }
  }

  async function handleExcluir() {
    if (!window.confirm(`Excluir o curso "${curso.titulo}"?`)) return
    try {
      await excluirCurso(id)
      mostrarToast({ tipo: 'sucesso', mensagem: 'Curso excluído.' })
      navigate('/cursos')
    } catch (e) {
      mostrarToast({ tipo: 'erro', mensagem: e.message })
    }
  }

  const videoExibido = aulaAtual?.videoId || curso?.videoId

  return (
    <Layout>
      <PageHeader
        eyebrow={curso?.tema?.nome ?? 'Plataforma de cursos'}
        title={curso?.titulo ?? 'Curso'}
        lead={curso ? `${curso.instrutor} · ${curso.descricao}` : undefined}
      />

      <section className="px-6 md:px-10 py-14 bg-white">
        <div className="max-w-content mx-auto">
          {carregando && !curso && <Skeleton className="h-72" />}

          {erro && (
            <EmptyState
              icone="ph-light ph-warning-circle"
              titulo="Não foi possível carregar o curso"
              descricao={erro}
              acao={<Button to="/cursos" variant="outline" icon={null}>Voltar aos cursos</Button>}
            />
          )}

          {!erro && curso && (
            <div className="grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-8">
              <div>
                {videoExibido ? (
                  <div className="aspect-video rounded-lg2 overflow-hidden border border-border bg-navy">
                    <iframe
                      key={videoExibido}
                      className="w-full h-full"
                      src={`https://www.youtube-nocookie.com/embed/${videoExibido}`}
                      title={aulaAtual?.titulo ?? curso.titulo}
                      allowFullScreen
                    />
                  </div>
                ) : (
                  <EmptyState
                    icone="ph-light ph-video-camera-slash"
                    titulo="Este curso ainda não tem aulas"
                    descricao="Volte em breve."
                  />
                )}
                {aulaAtual && (
                  <div className="flex flex-wrap items-center justify-between gap-3 mt-4">
                    <h2 className="font-display font-bold text-navy text-[20px]">{aulaAtual.titulo}</h2>
                    {inscrito && (
                      <button
                        type="button"
                        onClick={() => alternarConclusao(aulaAtual)}
                        className={`inline-flex items-center gap-2 font-label font-bold text-[12px] tracking-[0.08em] uppercase px-4 py-2.5 rounded-md2 transition-colors ${
                          concluidas.has(aulaAtual.id)
                            ? 'bg-ice text-teal-accessible'
                            : 'border-[1.5px] border-teal-accessible text-teal-accessible hover:bg-teal-accessible hover:text-white'
                        }`}
                      >
                        <i className={`ph-light ${concluidas.has(aulaAtual.id) ? 'ph-check-circle' : 'ph-circle'}`} />
                        {concluidas.has(aulaAtual.id) ? 'Aula concluída' : 'Marcar como concluída'}
                      </button>
                    )}
                  </div>
                )}
              </div>

              <aside>
                {!inscrito ? (
                  <div className="border border-border rounded-lg2 bg-cream p-5 mb-5">
                    <p className="font-display font-medium text-navy text-[16px] m-0 mb-1">
                      Inscreva-se neste curso
                    </p>
                    <p className="text-[13px] text-ink-soft m-0 mb-4">
                      Acompanhe seu progresso e retome de onde parou em Meus cursos.
                    </p>
                    <Button
                      variant="amber"
                      icon={null}
                      onClick={handleInscrever}
                      disabled={inscrevendo}
                      className="w-full disabled:opacity-60"
                    >
                      {inscrevendo ? 'Inscrevendo...' : 'Inscrever-me'}
                    </Button>
                  </div>
                ) : (
                  <div className="mb-5">
                    <div className="h-1.5 bg-ice rounded-full overflow-hidden mb-2">
                      <div
                        className="h-full bg-amber-soft transition-all"
                        style={{ width: `${aulas.length ? Math.round((concluidas.size / aulas.length) * 100) : 0}%` }}
                      />
                    </div>
                    <p className="text-[12px] text-ink-caption m-0">
                      {concluidas.size} de {aulas.length} aulas concluídas
                    </p>
                  </div>
                )}
                <h2 className="font-display font-medium text-navy text-[18px] mb-3">
                  Aulas ({aulas.length})
                </h2>
                <ul className="border border-border rounded-lg2 divide-y divide-border">
                  {aulas.map((a) => (
                    <li key={a.id}>
                      <button
                        type="button"
                        onClick={() => setAulaAtual(a)}
                        className={`w-full text-left px-4 py-3 flex items-center gap-3 lift ${
                          aulaAtual?.id === a.id ? 'bg-ice/50' : ''
                        }`}
                      >
                        <i
                          className={`ph-light ${concluidas.has(a.id) ? 'ph-check-circle' : 'ph-play-circle'} text-[22px] text-teal-accessible`}
                        />
                        <span className="flex-1 min-w-0">
                          <span className="block text-[14px] text-navy truncate">{a.titulo}</span>
                          <span className="block text-[12px] text-ink-soft">
                            {a.modulo} · {a.duracao}
                          </span>
                        </span>
                      </button>
                    </li>
                  ))}
                  {aulas.length === 0 && (
                    <li className="px-4 py-6 text-center text-[13px] text-ink-soft">Nenhuma aula cadastrada.</li>
                  )}
                </ul>
              </aside>
            </div>
          )}

          {!erro && curso && ehAdmin && (
            <div className="mt-12 border-t border-border pt-10 grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-8">
              <form onSubmit={handleAdicionarAula} className="space-y-4">
                <h2 className="font-display font-medium text-navy text-[18px]">Adicionar aula</h2>
                <label className="block">
                  <span className={LABEL_CLS}>Título</span>
                  <input
                    required
                    value={formAula.titulo}
                    onChange={(e) => setFormAula({ ...formAula, titulo: e.target.value })}
                    className={INPUT_CLS}
                  />
                </label>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <label className="block">
                    <span className={LABEL_CLS}>ID do vídeo (YouTube)</span>
                    <input
                      required
                      maxLength={20}
                      value={formAula.videoId}
                      onChange={(e) => setFormAula({ ...formAula, videoId: e.target.value })}
                      className={INPUT_CLS}
                    />
                  </label>
                  <label className="block">
                    <span className={LABEL_CLS}>Duração (ex.: 15:30)</span>
                    <input
                      required
                      maxLength={10}
                      value={formAula.duracao}
                      onChange={(e) => setFormAula({ ...formAula, duracao: e.target.value })}
                      className={INPUT_CLS}
                    />
                  </label>
                  <label className="block">
                    <span className={LABEL_CLS}>Módulo</span>
                    <input
                      required
                      value={formAula.modulo}
                      onChange={(e) => setFormAula({ ...formAula, modulo: e.target.value })}
                      className={INPUT_CLS}
                    />
                  </label>
                  <label className="block">
                    <span className={LABEL_CLS}>Ordem</span>
                    <input
                      type="number"
                      min="1"
                      value={formAula.ordem}
                      onChange={(e) => setFormAula({ ...formAula, ordem: e.target.value })}
                      className={INPUT_CLS}
                    />
                  </label>
                </div>
                <Button
                  variant="amber"
                  icon="ph-plus"
                  type="submit"
                  disabled={salvandoAula}
                  className="disabled:opacity-60 disabled:cursor-not-allowed"
                >
                  {salvandoAula ? 'Salvando...' : 'Adicionar aula'}
                </Button>
              </form>

              <div>
                <h2 className="font-display font-medium text-navy text-[18px] mb-3">Zona do admin</h2>
                <button
                  type="button"
                  onClick={handleExcluir}
                  className="inline-flex items-center gap-2 font-label font-bold text-[12px] tracking-[0.08em] uppercase px-4 py-2.5 rounded-md2 border-[1.5px] border-danger text-danger hover:bg-danger hover:text-white transition-colors"
                >
                  <i className="ph-light ph-trash" /> Excluir curso
                </button>
              </div>
            </div>
          )}
        </div>
      </section>
    </Layout>
  )
}
