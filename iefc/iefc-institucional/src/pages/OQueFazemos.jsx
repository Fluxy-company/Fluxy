import { useSearchParams } from 'react-router-dom'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Tabs from '../components/ui/Tabs'
import { programs } from '../data/siteData'

const ABAS = programs.map((p) => ({ valor: p.slug, titulo: p.title }))

export default function OQueFazemos() {
  const [searchParams, setSearchParams] = useSearchParams()
  const aba = ABAS.some((a) => a.valor === searchParams.get('aba'))
    ? searchParams.get('aba')
    : ABAS[0].valor

  function mudarAba(valor) {
    setSearchParams(valor === ABAS[0].valor ? {} : { aba: valor })
  }

  const programa = programs.find((p) => p.slug === aba)

  return (
    <Layout>
      <PageHeader
        eyebrow="O que fazemos"
        title="Programas que levam Ciência a mais gente"
        lead="Lúmina, Vérita, Ânima e Bolsas — cada um com um jeito próprio de aproximar a Ciência da sociedade."
      />

      <section className="px-6 md:px-10 py-14">
        <div className="max-w-content mx-auto">
          <Tabs abas={ABAS} ativa={aba} onChange={mudarAba} className="mb-10 overscroll-x-none" />

          {programa && (
            <div className="max-w-[720px]">
              <div className="flex items-center gap-3 mb-4">
                <i className={`ph-light ${programa.icon} text-[28px]`} style={{ color: programa.tint }} />
                <span className="font-label text-[11px] tracking-[0.1em] uppercase text-teal-accessible">
                  {programa.status}
                </span>
              </div>
              <h2 className="font-display font-bold text-navy text-[28px] mb-4">{programa.title}</h2>
              <p className="text-[16px] text-ink-soft leading-relaxed">{programa.desc}</p>
            </div>
          )}
        </div>
      </section>
    </Layout>
  )
}
