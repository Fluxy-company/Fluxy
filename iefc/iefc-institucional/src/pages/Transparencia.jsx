import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Eyebrow from '../components/Eyebrow'
import { docs, apoiadores, parceiros } from '../data/siteData'

export default function Transparencia() {
  return (
    <Layout>
      <PageHeader
        eyebrow="Transparência"
        title="Prestação de contas clara, para todo real investido"
        lead="Documentos institucionais, resultados de auditoria e a lista de quem torna o IEFC possível."
      />

      <section className="px-6 md:px-10 py-16 bg-white">
        <div className="max-w-content mx-auto">
          <Eyebrow>Documentos institucionais</Eyebrow>
          <h2 className="font-display font-bold text-[28px] text-navy mb-6">Governança</h2>
          <div className="border border-border rounded-lg2 overflow-hidden">
            {docs.map((d, i) => (
              <div
                key={d.name}
                className={`docrow flex items-center justify-between px-6 py-4 ${
                  i !== docs.length - 1 ? 'border-b border-border-hairline' : ''
                }`}
              >
                <span className="text-[15px] text-ink flex items-center gap-3">
                  <i className="ph ph-file-text text-[18px] text-ink-caption" />
                  {d.name}
                </span>
                <span className="font-label text-[11px] tracking-[0.1em] uppercase" style={{ color: d.color }}>
                  {d.status}
                </span>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="px-6 md:px-10 py-16 bg-cream border-y border-border-warm">
        <div className="max-w-content mx-auto">
          <Eyebrow>Quem apoia</Eyebrow>
          <h2 className="font-display font-bold text-[28px] text-navy mb-6">Apoiadores institucionais</h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4 mb-12">
            {apoiadores.map((a) => (
              <div
                key={a.name}
                className="bg-white border border-border rounded-md2 h-20 flex items-center justify-center text-center px-3 text-[12px] text-ink-soft font-label"
              >
                {a.name}
              </div>
            ))}
          </div>

          <Eyebrow>Quem colabora</Eyebrow>
          <h2 className="font-display font-bold text-[28px] text-navy mb-6">Parceiros</h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-4">
            {parceiros.map((p) => (
              <div
                key={p.name}
                className="bg-white border border-border rounded-md2 h-20 flex items-center justify-center text-center px-3 text-[12px] text-ink-soft font-label"
              >
                {p.name}
              </div>
            ))}
          </div>
        </div>
      </section>
    </Layout>
  )
}
