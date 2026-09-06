import { useState } from 'react'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Eyebrow from '../components/Eyebrow'

const valores = ['R$ 30', 'R$ 60', 'R$ 100', 'R$ 250']

export default function Apoie() {
  const [selected, setSelected] = useState(valores[1])

  return (
    <Layout>
      <PageHeader
        eyebrow="Apoie o IEFC"
        title="Apoie quem acende a LUZ da Ciência no Brasil"
        lead="Em 2025, cada R$1 investido no IEFC impactou diretamente 3 estudantes com Ciência de qualidade na escola pública."
      />

      <section className="px-6 md:px-10 py-16 bg-white">
        <div className="max-w-content mx-auto grid grid-cols-1 md:grid-cols-[1.3fr_1fr] gap-10">
          <div>
            <Eyebrow>Doação recorrente</Eyebrow>
            <h2 className="font-display font-bold text-[26px] text-navy mb-6">Escolha um valor mensal</h2>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 mb-6">
              {valores.map((v) => (
                <button
                  key={v}
                  onClick={() => setSelected(v)}
                  className={`font-label font-bold text-[15px] py-4 rounded-md2 border transition-colors ${
                    selected === v
                      ? 'bg-amber border-amber text-navy'
                      : 'bg-white border-border text-navy hover:border-teal-accessible'
                  }`}
                >
                  {v}
                </button>
              ))}
            </div>
            <button className="bg-navy text-white font-label font-bold text-[13px] tracking-[0.08em] uppercase px-6 py-3.5 rounded-md2 hover:bg-navy-hover">
              Quero apoiar com {selected}/mês
            </button>
          </div>

          <div className="bg-teal-accessible rounded-lg2 flex items-center justify-center p-10 text-center">
            <div>
              <div className="font-label font-bold text-[56px] text-white leading-none">1:3</div>
              <div className="font-label text-[11px] tracking-[0.18em] uppercase text-teal-pale mt-3">
                real por estudante
              </div>
            </div>
          </div>
        </div>
      </section>
    </Layout>
  )
}
