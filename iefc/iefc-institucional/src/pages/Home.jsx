import Layout from '../components/Layout'
import Button from '../components/Button'
import Eyebrow from '../components/Eyebrow'
import AnimatedStat from '../components/AnimatedStat'
import { stats, programs, featuredCourses, agendaHome } from '../data/siteData'

export default function Home() {
  return (
    <Layout>

      <section className="relative px-6 md:px-10 pt-20 md:pt-24 pb-16 md:pb-[84px] bg-navy overflow-hidden">
        <div
          className="absolute inset-0 opacity-[0.12]"
          style={{
            backgroundImage:
              'radial-gradient(circle at 20% 20%, var(--teal) 0%, transparent 45%), radial-gradient(circle at 80% 70%, var(--amber) 0%, transparent 40%)',
          }}
        />
        <div className="relative max-w-content mx-auto">
          <div className="font-label text-[12px] tracking-[0.28em] uppercase text-on-dark-accent mb-6">
            Instituto Educacional Futuro da Ciência
          </div>
          <h1 className="font-display font-bold text-[42px] md:text-[64px] leading-[1.03] tracking-heroLs text-white mb-[22px] max-w-[900px] text-balance">
            Mais <span className="text-amber">LUZ</span> sobre a Ciência
          </h1>
          <p className="text-[17px] md:text-lead leading-relaxed text-on-dark max-w-[660px] m-0">
            O IEFC é uma organização sem fins lucrativos que reúne cientistas, educadores e
            empreendedores com o propósito de desenvolver e promover o conhecimento científico
            para o mundo.
          </p>
          <div className="flex gap-3.5 mt-8 flex-wrap">
            <Button to="/apoie">Apoie o IEFC</Button>
          </div>
        </div>
      </section>

      <section className="px-6 md:px-10 py-14 bg-cream border-b border-border-warm">
        <div className="max-w-content mx-auto">
          <div className="flex items-center gap-2.5 mb-7">
            <span className="font-label text-[11px] tracking-[0.16em] uppercase text-teal-accessible">
              Números de impacto · atualizados via Power BI · há 2 h
            </span>
          </div>
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-6">
            {stats.map((s) => (
              <AnimatedStat key={s.label} {...s} />
            ))}
          </div>
        </div>
      </section>

      <section className="px-6 md:px-10 py-16 md:py-[72px] bg-white">
        <div className="max-w-content mx-auto">
          <Eyebrow>Nossos programas</Eyebrow>
          <h2 className="font-display font-bold text-[28px] md:text-[34px] text-navy mb-8">
            Onde a Ciência acontece
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            {programs.map((p) => (
              <div
                key={p.slug}
                className={`border border-border rounded-lg2 overflow-hidden bg-white ${p.link ? 'lift cursor-pointer' : ''}`}
              >
                <div className="h-2" style={{ background: p.tint }} />
                <div className="px-[26px] pt-[26px] pb-7">
                  <div className="flex items-center justify-between">
                    <span
                      className={`font-label text-[10px] tracking-[0.14em] uppercase ${
                        p.status === 'Aberto' ? 'text-teal-accessible' : 'text-ink-caption'
                      }`}
                    >
                      {p.status}
                    </span>
                    <i className={`ph ${p.icon} text-[24px]`} style={{ color: p.link ? 'var(--navy)' : p.tint }} />
                  </div>
                  <h3 className="font-display font-bold text-[24px] text-navy mt-3 mb-2">{p.title}</h3>
                  <p className="text-[15px] leading-[1.55] text-ink-soft m-0 mb-3.5">{p.desc}</p>
                  {p.link && (
                    <span className="font-label text-[12px] tracking-[0.06em] uppercase text-teal-accessible inline-flex items-center gap-1.5">
                      Saiba mais <i className="ph ph-arrow-right" />
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="px-6 md:px-10 pb-16 md:pb-18 bg-white">
        <div className="max-w-content mx-auto border border-border rounded-lg2 bg-cream p-8 md:p-10 grid grid-cols-1 md:grid-cols-[1.4fr_1fr] gap-8 items-center">
          <div>
            <Eyebrow>Plataforma de cursos</Eyebrow>
            <h2 className="font-display font-bold text-[26px] md:text-[30px] text-navy mb-3">
              Cursos e palestras
            </h2>
            <p className="text-[16px] leading-relaxed text-ink-soft mb-5 max-w-[460px]">
              Catálogo aberto, inscrição e certificação — tudo em um só lugar.
            </p>
            <Button to="/cursos" variant="navy">
              Ver catálogo
            </Button>
          </div>
          <div className="flex flex-col gap-3">
            {featuredCourses.map((c) => (
              <div
                key={c.title}
                className="bg-white border border-border rounded-md2 px-[18px] py-4 flex items-center gap-3.5"
              >
                <i className={`ph ${c.icon} text-[24px] text-teal-accessible`} />
                <div>
                  <div className="font-display font-medium text-[15px] text-navy">{c.title}</div>
                  <div className="text-[12px] text-ink-caption">{c.modules}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="px-6 md:px-10 pb-16 md:pb-20 bg-white">
        <div className="max-w-content mx-auto">
          <div className="flex items-end justify-between mb-6 flex-wrap gap-3">
            <div>
              <Eyebrow className="mb-2.5">Agenda</Eyebrow>
              <h2 className="font-display font-bold text-[28px] md:text-[30px] text-navy m-0">
                Próximas atividades
              </h2>
            </div>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {agendaHome.map((a) => (
              <div key={a.title} className="border border-border rounded-lg2 p-[22px] flex gap-4">
                <div className="text-center shrink-0">
                  <div className="font-label font-bold text-[26px] text-amber leading-none">{a.day}</div>
                  <div className="font-label text-[11px] tracking-[0.1em] uppercase text-ink-soft">{a.month}</div>
                </div>
                <div>
                  <div className="font-label text-[10px] tracking-[0.12em] uppercase text-teal-accessible">
                    {a.type}
                  </div>
                  <div className="font-display font-medium text-[16px] text-navy mt-1">{a.title}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="px-6 md:px-10 pb-16 md:pb-20 bg-white">
        <div className="max-w-content mx-auto rounded-xl2 overflow-hidden bg-navy grid grid-cols-1 md:grid-cols-[1.5fr_1fr] items-stretch">
          <div className="p-8 md:p-[52px_48px]">
            <i className="ph ph-hand-heart text-[34px] text-amber-light" />
            <h2 className="font-display font-bold text-[26px] md:text-[30px] text-white mt-4 mb-3 leading-[1.15]">
              Apoie quem acende a LUZ da Ciência no Brasil
            </h2>
            <p className="text-[16px] leading-relaxed text-on-dark mb-6 max-w-[480px]">
              Em 2025, cada R$1 investido no IEFC impactou diretamente 3 estudantes com Ciência de
              qualidade na escola pública.
            </p>
            <Button to="/apoie">Quero apoiar</Button>
          </div>
          <div className="bg-teal-accessible flex items-center justify-center p-10 text-center">
            <div>
              <div className="font-label font-bold text-[52px] md:text-[68px] text-white leading-none">1:3</div>
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
