import { useSearchParams } from 'react-router-dom'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Tabs from '../components/ui/Tabs'
import EmptyState from '../components/ui/EmptyState'
import { valores, equipe, parceiros, apoiadores } from '../data/siteData'

const ABAS = [
  { valor: 'missao', titulo: 'Missão e valores' },
  { valor: 'equipe', titulo: 'Equipe' },
  { valor: 'parceiros', titulo: 'Parceiros' },
]

export default function QuemSomos() {
  const [searchParams, setSearchParams] = useSearchParams()
  const aba = ABAS.some((a) => a.valor === searchParams.get('aba')) ? searchParams.get('aba') : 'missao'

  function mudarAba(valor) {
    setSearchParams(valor === 'missao' ? {} : { aba: valor })
  }

  return (
    <Layout>
      <PageHeader
        eyebrow="Quem somos"
        title="Cientistas, educadores e empreendedores pela Ciência"
        lead="Conheça a missão, os valores e as pessoas e organizações que tornam o IEFC possível."
      />

      <section className="px-6 md:px-10 py-14">
        <div className="max-w-content mx-auto">
          <Tabs abas={ABAS} ativa={aba} onChange={mudarAba} className="mb-10" />

          {aba === 'missao' && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
              {valores.map((v) => (
                <div key={v.title} className="border border-border rounded-lg2 p-6">
                  <i className={`ph-light ${v.icon} text-teal-accessible text-[24px]`} />
                  <h3 className="font-display font-medium text-navy text-[17px] mt-3 mb-1.5">
                    {v.title}
                  </h3>
                  <p className="text-[14px] text-ink-soft leading-relaxed m-0">{v.desc}</p>
                </div>
              ))}
            </div>
          )}

          {aba === 'equipe' && (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {equipe.map((pessoa) => (
                <div key={pessoa.nome} className="border border-border rounded-lg2 p-6 text-center">
                                    
                  <div className="w-24 h-24 rounded-full mx-auto mb-4 overflow-hidden bg-ice">
                    {pessoa.foto ? (
                      <img
                        src={pessoa.foto}
                        alt={`Foto de ${pessoa.nome}`}
                        className="w-full h-full object-cover"
                        loading="lazy"
                      />
                    ) : (
                      <div className="w-full h-full flex items-center justify-center">
                        <i
                          className="ph-light ph-user text-blue-gray text-[32px]"
                          aria-hidden="true"
                        />
                      </div>
                    )}
                  </div>
                  <h3 className="font-display font-medium text-navy text-[15px] mb-1">{pessoa.nome}</h3>
                  <p className="text-[13px] text-ink-soft m-0">{pessoa.papel}</p>
                </div>
              ))}
            </div>
          )}

          {aba === 'parceiros' && (
            <div className="space-y-10">
              <div>
                <h3 className="font-label text-[11px] tracking-[0.14em] uppercase text-ink-caption mb-4">
                  Apoiadores
                </h3>
                {apoiadores.length === 0 ? (
                  <EmptyState titulo="Nenhum apoiador cadastrado ainda." />
                ) : (
                  <div className="flex flex-wrap gap-3">
                    {apoiadores.map((a) => (
                      <span
                        key={a.name}
                        className="border border-border rounded-md2 px-4 py-2 text-[13px] text-ink-soft"
                      >
                        {a.name}
                      </span>
                    ))}
                  </div>
                )}
              </div>
              <div>
                <h3 className="font-label text-[11px] tracking-[0.14em] uppercase text-ink-caption mb-4">
                  Parceiros institucionais
                </h3>
                <div className="flex flex-wrap gap-3">
                  {parceiros.map((p) => (
                    <span
                      key={p.name}
                      className="border border-border rounded-md2 px-4 py-2 text-[13px] text-ink-soft"
                    >
                      {p.name}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>
      </section>
    </Layout>
  )
}
