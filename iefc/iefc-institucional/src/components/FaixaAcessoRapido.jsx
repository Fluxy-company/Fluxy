import { useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom'
import { contarPendentes } from '../services/usuarios'

const ITEM_CLS =
  'flex items-center gap-3 border border-white/15 rounded-lg2 px-5 py-4 hover:bg-white/5 transition-colors cursor-pointer'

export default function FaixaAcessoRapido({ ehAdmin }) {
  const [pendentes, setPendentes] = useState(null)

  useEffect(() => {
    if (!ehAdmin) return
    let cancelado = false
    contarPendentes()
      .then((resposta) => {
        if (!cancelado) setPendentes(resposta?.totalElements ?? null)
      })
      .catch(() => {
        if (!cancelado) setPendentes(null)
      })
    return () => {
      cancelado = true
    }
  }, [ehAdmin])

  return (
    <section className="px-6 md:px-10 py-10 bg-navy border-b border-border-warm">
      <div className="max-w-content mx-auto">
        <h2 className="font-display font-bold text-white text-[20px] mb-5">Acesso rápido</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {ehAdmin && (
            <NavLink to="/painel/colaboradores/pendentes" className={ITEM_CLS}>
              <i className="ph-light ph-user-check text-amber-soft text-[22px]" />
              <span className="font-label text-[12px] tracking-[0.06em] uppercase text-white flex items-center gap-2">
                Colaboradores pendentes
                {typeof pendentes === 'number' && pendentes > 0 && (
                  <span className="inline-flex items-center justify-center min-w-[20px] h-5 px-1.5 rounded-full bg-peach text-navy text-[11px] font-bold">
                    {pendentes}
                  </span>
                )}
              </span>
            </NavLink>
          )}
        </div>
      </div>
    </section>
  )
}
