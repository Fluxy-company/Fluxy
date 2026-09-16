import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import { courses } from '../data/siteData'

export default function Cursos() {
  return (
    <Layout>
      <PageHeader
        eyebrow="Plataforma de cursos"
        title="Catálogo aberto, inscrição e certificação"
        lead="Cursos, palestras e oficinas gratuitas para estudantes, professores e entusiastas da Ciência."
      />

      <section className="px-6 md:px-10 py-16 bg-white">
        <div className="max-w-content mx-auto grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {courses.map((c) => (
            <div key={c.title} className="lift border border-border rounded-lg2 overflow-hidden bg-white cursor-pointer">
              <div className="h-2" style={{ background: c.tint }} />
              <div className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <span className="font-label text-[10px] tracking-[0.14em] uppercase text-teal-accessible">
                    {c.tag}
                  </span>
                  <i className={`ph ${c.icon} text-[24px]`} style={{ color: c.tint }} />
                </div>
                <h3 className="font-display font-bold text-[19px] text-navy mb-2">{c.title}</h3>
                <p className="text-[13px] text-ink-caption m-0 mb-4">{c.modules} · certificado</p>
                <span className="font-label text-[12px] tracking-[0.06em] uppercase text-teal-accessible inline-flex items-center gap-1.5">
                  Ver curso <i className="ph ph-arrow-right" />
                </span>
              </div>
            </div>
          ))}
        </div>
      </section>
    </Layout>
  )
}
