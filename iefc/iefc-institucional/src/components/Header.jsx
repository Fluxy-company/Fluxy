import { useState } from 'react'
import { NavLink } from 'react-router-dom'
import { navLinks } from '../data/siteData'

export default function Header() {
  const [menuOpen, setMenuOpen] = useState(false)

  const linkCls = ({ isActive }) =>
    `navlink text-navy cursor-pointer ${isActive ? 'active' : ''}`

  return (
    <>
      <header className="flex items-center justify-between gap-6 px-6 md:px-10 py-3.5 bg-cream/90 backdrop-blur-md border-b border-border-warm sticky top-0 z-[100]">
        <NavLink to="/" className="shrink-0" onClick={() => setMenuOpen(false)}>
          <img src="/iefc-azul.svg" alt="IEFC" className="h-6 md:h-[36px] w-auto cursor-pointer" />
        </NavLink>

        <nav className="hidden lg:flex gap-7 font-label text-[12px] tracking-[0.1em] uppercase font-medium">
          {navLinks.map((item) => (
            <NavLink key={item.to} to={item.to} className={linkCls}>
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="hidden lg:flex items-center gap-3">
          <span
            title="Área interna da equipe"
            className="font-label text-[11px] tracking-[0.08em] uppercase text-teal-accessible cursor-pointer inline-flex items-center gap-1.5"
          >
            <i className="ph ph-lock-key text-[15px]" />
            Login/Cadastre-se
          </span>
          <NavLink
            to="/apoie"
            className="inline-flex items-center gap-2 bg-amber text-navy font-label font-bold text-[12px] tracking-[0.08em] uppercase px-[18px] py-[11px] rounded-md2 cursor-pointer hover:bg-amber-light transition-colors"
          >
            Apoie o IEFC
          </NavLink>
        </div>

        <button
          aria-label="Abrir menu"
          onClick={() => setMenuOpen((v) => !v)}
          className="lg:hidden flex items-center justify-center w-11 h-11 rounded-md2 text-navy"
        >
          <i className={`ph ${menuOpen ? 'ph-x' : 'ph-list'} text-[26px]`} />
        </button>
      </header>

      {menuOpen && (
        <div className="lg:hidden flex flex-col gap-1 px-5 pt-3 pb-5 bg-cream border-b border-border-warm sticky top-[68px] z-[99]">
          {navLinks.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={() => setMenuOpen(false)}
              className="py-3 px-1 font-label text-[13px] tracking-[0.08em] uppercase text-navy border-b border-border-hairline cursor-pointer"
            >
              {item.label}
            </NavLink>
          ))}
          <div className="flex gap-2.5 mt-3">
            <NavLink
              to="/area-do-estudante"
              onClick={() => setMenuOpen(false)}
              className="flex-1 text-center py-3 font-label text-[12px] tracking-[0.08em] uppercase text-teal-accessible border-[1.5px] border-teal-accessible rounded-md2 cursor-pointer"
            >
              Login
            </NavLink>
            <NavLink
              to="/apoie"
              onClick={() => setMenuOpen(false)}
              className="flex-[2] text-center py-3 bg-amber text-navy font-label font-bold text-[12px] tracking-[0.08em] uppercase rounded-md2 cursor-pointer"
            >
              Apoie o IEFC
            </NavLink>
          </div>
        </div>
      )}
    </>
  )
}
