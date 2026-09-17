import { NavLink } from 'react-router-dom'

import { navLinks } from '../data/siteData'

export default function Footer() {
  return (
    <footer className="bg-navy text-on-dark px-6 md:px-10 pt-12 pb-8">

      <div className="max-w-content mx-auto grid grid-cols-1 md:grid-cols-[1.4fr_1fr_1fr_1fr] gap-8 pb-8 border-b border-white/10">

        <div>
          <img
            src="/iefc-branco.svg"
            alt="IEFC"
            className="h-[52px] mb-4"
          />

          <p className="text-[14px] leading-relaxed text-on-dark-muted max-w-[280px]">
            Instituto Educacional Futuro da Ciência — Ciência sem elitismo, com rigor e calor humano.
          </p>
        </div>

        <div>
          <div className="font-label text-[11px] tracking-[0.14em] uppercase text-on-dark-accent mb-3.5">
            Navegar
          </div>

          <div className="flex flex-col gap-2.5 text-[14px]">

            {navLinks.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                className="text-on-dark cursor-pointer hover:text-white"
              >
                {item.label}
              </NavLink>
            ))}

            <NavLink
              to="/apoie"
              className="text-on-dark cursor-pointer hover:text-white"
            >
              Apoie o IEFC
            </NavLink>

          </div>
        </div>

        <div>
          <div className="font-label text-[11px] tracking-[0.14em] uppercase text-on-dark-accent mb-3.5">
            Contato
          </div>

          <div className="flex flex-col gap-2.5 text-[14px] text-on-dark">

            <a
              href="mailto:iefc@iefc.org.br"
              className="text-on-dark hover:text-white"
            >
              iefc@iefc.org.br
            </a>

            <a
              href="https://wa.me/5511933745663"
              target="_blank"
              rel="noreferrer"
              className="text-on-dark hover:text-white inline-flex items-center gap-1.5"
            >
              <i className="ph ph-whatsapp-logo" />
              +55 11 93374-5663
            </a>

            <NavLink
              to="/contato"
              className="cursor-pointer text-on-dark hover:text-white"
            >
              Fale conosco
            </NavLink>

          </div>
        </div>

        <div>
          <div className="font-label text-[11px] tracking-[0.14em] uppercase text-on-dark-accent mb-3.5">
            Redes
          </div>

          <div className="flex gap-3.5 text-[22px]">

            <a
              href="https://www.instagram.com/iefc_instituto/"
              target="_blank"
              rel="noreferrer"
              aria-label="Instagram"
              className="text-on-dark cursor-pointer hover:text-white transition-colors"
            >
              <i className="ph ph-instagram-logo" />
            </a>

            <a
              href="https://www.linkedin.com/company/instituto-educacional-futuro-da-ciência/?originalSubdomain=br"
              target="_blank"
              rel="noreferrer"
              aria-label="LinkedIn"
              className="text-on-dark cursor-pointer hover:text-white transition-colors"
            >
              <i className="ph ph-linkedin-logo" />
            </a>

            <a
              href="https://www.tiktok.com/@SEU_PERFIL"
              target="_blank"
              rel="noreferrer"
              aria-label="TikTok"
              className="text-on-dark cursor-pointer hover:text-white transition-colors"
            >
              <i className="ph ph-tiktok-logo" />
            </a>

          </div>
        </div>

      </div>

      <div className="max-w-content mx-auto mt-5 flex items-center justify-between flex-wrap gap-2 font-label text-[12px] text-on-dark-muted">

        <span>
          IEFC · Instituto Educacional Futuro da Ciência · CNPJ 29.260.548/0001-52
        </span>

        <span>
          www.iefc.org.br
        </span>

      </div>

    </footer>
  )
}