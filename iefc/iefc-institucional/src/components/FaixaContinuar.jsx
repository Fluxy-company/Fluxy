import { useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom'
import Button from './Button'
import { buscarMeusCursos } from '../services/cursos'

export default function FaixaContinuar({ usuario }) {
  const [estado, setEstado] = useState('carregando')
  const [cursos, setCursos] = useState([])

  useEffect(() => {
    let cancelado = false
    buscarMeusCursos(usuario.idUsuario)
      .then((resposta) => {
        if (cancelado) return
        const lista = Array.isArray(resposta) ? resposta : []
        setCursos(lista.slice(0, 3))
        setEstado(lista.length > 0 ? 'sucesso' : 'vazio')
      })
      .catch(() => {
        if (!cancelado) setEstado('oculto')
      })
    return () => {
      cancelado = true
    }
  }, [usuario.idUsuario])

  if (estado === 'carregando' || estado === 'oculto') return null

  return (
    <section className="px-6 md:px-10 py-10 bg-ice/40 border-b border-border-warm">
      <div className="max-w-content mx-auto">
        <h2 className="font-display font-bold text-navy text-[20px] mb-5">Continue de onde parou</h2>

        {estado === 'vazio' ? (
          <div className="flex items-center justify-between flex-wrap gap-4 bg-white border border-border rounded-lg2 px-6 py-6">
            <p className="text-[14px] text-ink-soft m-0">
              Você ainda não iniciou nenhum curso.
            </p>
            <Button variant="outline" icon={null} to="/cursos">
              Explorar cursos
            </Button>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
              {cursos.map((curso) => {
                const percentual = curso.totalAulas
                  ? Math.round((curso.aulasConcluidas / curso.totalAulas) * 100)
                  : 0
                return (
                  <div key={curso.cursoId} className="bg-white border border-border rounded-lg2 p-5">
                    <span className="inline-block font-label text-[10px] tracking-[0.08em] uppercase text-teal-accessible bg-teal-pale rounded-pill px-2.5 py-1 mb-3">
                      {curso.tema}
                    </span>
                    <h3 className="font-display font-medium text-navy text-[16px] mb-2">{curso.titulo}</h3>
                    <div className="h-1 bg-ice rounded-full overflow-hidden mb-2">
                      <div
                        className="h-full bg-amber-soft"
                        style={{ width: `${percentual}%` }}
                      />
                    </div>
                    <p className="text-[12px] text-ink-caption mb-4">
                      {curso.aulasConcluidas} de {curso.totalAulas} aulas · {percentual}%
                    </p>
                    <Button
                      variant="outline"
                      icon={null}
                      className="w-full"
                      to={`/cursos/${curso.cursoId}`}
                    >
                      Continuar
                    </Button>
                  </div>
                )
              })}
            </div>
            <NavLink to="/meus-cursos" className="inline-block mt-5 text-teal-accessible text-[13px] font-medium">
              Ver todos os meus cursos
            </NavLink>
          </>
        )}
      </div>
    </section>
  )
}
