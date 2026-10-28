import { useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import Button from '../components/Button'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import { useToast } from '../contexts/ToastContext'
import { criarCurso, listarTemas } from '../services/cursos'
import { useEffect } from 'react'

const INPUT_CLS =
  'w-full mt-1.5 border border-input-border bg-surface-input rounded-md2 px-3 py-2.5 text-sm focus:border-teal-accessible focus:outline-none'
const LABEL_CLS = 'font-label text-[10px] tracking-[0.1em] uppercase text-ink-caption'

export default function CursoNovo() {
  const navigate = useNavigate()
  const { mostrarToast } = useToast()
  const [temas, setTemas] = useState([])
  const [form, setForm] = useState({ titulo: '', descricao: '', instrutor: '', videoId: '', temaId: '' })
  const [erro, setErro] = useState('')
  const [enviando, setEnviando] = useState(false)

  useEffect(() => {
    listarTemas()
      .then((resposta) => setTemas(resposta.content ?? []))
      .catch((e) => setErro(e.message))
  }, [])

  function atualizar(campo, valor) {
    setForm((atual) => ({ ...atual, [campo]: valor }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setEnviando(true)
    try {
      const curso = await criarCurso({
        titulo: form.titulo,
        descricao: form.descricao,
        instrutor: form.instrutor,
        videoId: form.videoId || null,
        tema: { id: Number(form.temaId) },
      })
      mostrarToast({ tipo: 'sucesso', mensagem: 'Curso criado com sucesso.' })
      navigate(`/cursos/${curso.id}`)
    } catch (e) {
      setErro(e.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <Layout>
      <PageHeader
        eyebrow="Plataforma de cursos"
        title="Novo curso"
        lead="Cadastre o curso e depois adicione as aulas na página dele."
      />
      <section className="px-6 md:px-10 py-14 bg-white">
        <div className="max-w-[560px] mx-auto">
          {erro && (
            <div className="flex items-start gap-2 bg-danger/10 border border-danger/30 rounded-md2 px-3 py-2.5 mb-4">
              <i className="ph ph-warning-circle text-danger text-[16px] mt-0.5" />
              <p className="text-[13px] text-danger m-0">{erro}</p>
            </div>
          )}

          <form className="space-y-4" onSubmit={handleSubmit}>
            <label className="block">
              <span className={LABEL_CLS}>Título</span>
              <input
                required
                maxLength={100}
                value={form.titulo}
                onChange={(e) => atualizar('titulo', e.target.value)}
                className={INPUT_CLS}
              />
            </label>
            <label className="block">
              <span className={LABEL_CLS}>Descrição</span>
              <textarea
                required
                rows={3}
                maxLength={255}
                value={form.descricao}
                onChange={(e) => atualizar('descricao', e.target.value)}
                className={INPUT_CLS}
              />
            </label>
            <label className="block">
              <span className={LABEL_CLS}>Instrutor</span>
              <input
                required
                maxLength={100}
                value={form.instrutor}
                onChange={(e) => atualizar('instrutor', e.target.value)}
                className={INPUT_CLS}
              />
            </label>
            <label className="block">
              <span className={LABEL_CLS}>Tema</span>
              <select
                required
                value={form.temaId}
                onChange={(e) => atualizar('temaId', e.target.value)}
                className={INPUT_CLS}
              >
                <option value="">Selecione um tema</option>
                {temas.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.nome}
                  </option>
                ))}
              </select>
            </label>
            <label className="block">
              <span className={LABEL_CLS}>ID do vídeo de apresentação (YouTube, opcional)</span>
              <input
                maxLength={20}
                value={form.videoId}
                onChange={(e) => atualizar('videoId', e.target.value)}
                placeholder="dQw4w9WgXcQ"
                className={INPUT_CLS}
              />
            </label>

            <div className="flex items-center gap-4 pt-2">
              <Button
                variant="amber"
                icon={null}
                type="submit"
                disabled={enviando}
                className="disabled:opacity-60 disabled:cursor-not-allowed"
              >
                {enviando ? 'Salvando...' : 'Criar curso'}
              </Button>
              <NavLink to="/cursos" className="text-teal-accessible text-[13px] font-medium">
                Cancelar
              </NavLink>
            </div>
          </form>
        </div>
      </section>
    </Layout>
  )
}
