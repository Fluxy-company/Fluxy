import { useState } from 'react'

import Layout from '../components/Layout'

export default function Contato() {
  const [sent, setSent] = useState(false)

  const [form, setForm] = useState({
    nome: '',
    email: '',
    mensagem: '',
  })

  const set = (campo) => (e) =>
    setForm((f) => ({
      ...f,
      [campo]: e.target.value,
    }))

  return (
    <Layout>
      <section className="px-6 md:px-10 pt-16 pb-[72px] bg-cream min-h-[60vh]">
        <div className="max-w-[900px] mx-auto">

          <div className="font-label text-[12px] tracking-[0.28em] uppercase text-teal-accessible mb-[18px]">
            Contato
          </div>

          <h1 className="font-display font-bold text-[38px] md:text-[42px] leading-[1.08] text-navy mb-7">
            Fale conosco
          </h1>

          <div className="grid grid-cols-1 md:grid-cols-[1.3fr_1fr] gap-6">

            {/* Formulário */}
            <div className="bg-white border border-border rounded-lg2 p-[30px]">
              {sent ? (
                <div className="flex flex-col items-center justify-center text-center py-10 gap-3">
                  <i className="ph ph-check-circle text-[36px] text-teal-accessible" />

                  <p className="font-display font-medium text-navy text-[17px] m-0">
                    Mensagem enviada
                  </p>

                  <p className="text-[14px] text-ink-soft m-0 max-w-[280px]">
                    A equipe do IEFC responde em até 2 dias úteis.
                  </p>
                </div>
              ) : (
                <form
                  onSubmit={(e) => {
                    e.preventDefault()
                    setSent(true)
                  }}
                >
                  <label className="font-label text-[11px] tracking-[0.1em] uppercase text-navy block mb-1.5">
                    Nome
                  </label>

                  <input
                    required
                    value={form.nome}
                    onChange={set('nome')}
                    className="w-full px-3.5 py-3 border-[1.5px] border-input-border rounded-md2 bg-surface-input text-[14px] font-body mb-4 outline-none focus:border-teal-accessible"
                  />

                  <label className="font-label text-[11px] tracking-[0.1em] uppercase text-navy block mb-1.5">
                    E-mail
                  </label>

                  <input
                    type="email"
                    required
                    value={form.email}
                    onChange={set('email')}
                    className="w-full px-3.5 py-3 border-[1.5px] border-input-border rounded-md2 bg-surface-input text-[14px] font-body mb-4 outline-none focus:border-teal-accessible"
                  />

                  <label className="font-label text-[11px] tracking-[0.1em] uppercase text-navy block mb-1.5">
                    Mensagem
                  </label>

                  <textarea
                    required
                    rows={4}
                    value={form.mensagem}
                    onChange={set('mensagem')}
                    className="w-full px-3.5 py-3 border-[1.5px] border-input-border rounded-md2 bg-surface-input text-[14px] font-body mb-5 outline-none focus:border-teal-accessible resize-y"
                  />

                  <button
                    type="submit"
                    className="inline-block bg-amber text-navy font-label font-bold text-[12px] tracking-[0.08em] uppercase px-6 py-3.5 rounded-md2 cursor-pointer hover:bg-amber-light"
                  >
                    Enviar mensagem
                  </button>
                </form>
              )}
            </div>

            <div className="flex flex-col gap-3.5">

              <a
                href="https://wa.me/5511933745663"
                target="_blank"
                rel="noreferrer"
                className="bg-white border border-border rounded-lg2 p-[22px] block hover:border-teal-accessible/50 transition-colors"
              >
                <i className="ph ph-whatsapp-logo text-[24px] text-teal-accessible" />

                <div className="font-display font-medium text-[15px] text-navy mt-2">
                  WhatsApp
                </div>

                <div className="text-[13px] text-ink-soft">
                  (11) 93374-5663
                </div>
              </a>

              <a
                href="mailto:iefc@iefc.com.br"
                className="bg-white border border-border rounded-lg2 p-[22px] block hover:border-teal-accessible/50 transition-colors"
              >
                <i className="ph ph-envelope-simple text-[24px] text-teal-accessible" />

                <div className="font-display font-medium text-[15px] text-navy mt-2">
                  E-mail
                </div>

                <div className="text-[13px] text-ink-soft">
                  iefc@iefc.com.br
                </div>
              </a>

              <div className="bg-white border border-border rounded-lg2 p-[22px] flex gap-4">

                <a
                  href="https://www.instagram.com/iefc_instituto/"
                  target="_blank"
                  rel="noreferrer"
                  aria-label="Instagram"
                  className="text-teal-accessible hover:text-navy transition-colors"
                >
                  <i className="ph ph-instagram-logo text-[22px]" />
                </a>

                <a
                  href="https://www.linkedin.com/company/instituto-educacional-futuro-da-ci%C3%AAncia/?originalSubdomain=br"
                  target="_blank"
                  rel="noreferrer"
                  aria-label="LinkedIn"
                  className="text-teal-accessible hover:text-navy transition-colors"
                >
                  <i className="ph ph-linkedin-logo text-[22px]" />
                </a>

                <a
                  href="COLE_AQUI_O_LINK_DO_TIKTOK"
                  target="_blank"
                  rel="noreferrer"
                  aria-label="TikTok"
                  className="text-teal-accessible hover:text-navy transition-colors"
                >
                  <i className="ph ph-tiktok-logo text-[22px]" />
                </a>

              </div>

            </div>
          </div>
        </div>
      </section>
    </Layout>
  )
}