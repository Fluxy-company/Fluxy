import { useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { navLinks } from '../data/siteData'
import { useAuth } from '../contexts/AuthContext'

export default function Header() {
  const [menuOpen, setMenuOpen] = useState(false)
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const { estaLogado, usuario, ehAdmin, sair } = useAuth()
  const navigate = useNavigate()

  const itensMenu = [
    { to: '/perfil', label: 'Meu perfil', icone: 'ph-light ph-user-circle' },
    { to: '/meus-cursos', label: 'Meus cursos', icone: 'ph-light ph-books' },
    ...(ehAdmin
      ? [
          { to: '/painel/colaboradores/pendentes', label: 'Painel de colaboradores', icone: 'ph-light ph-users' },
          { to: '/painel/administradores', label: 'Cadastro de admins', icone: 'ph-light ph-shield-star' },
        ]
      : []),
  ]

  const linkCls = ({ isActive }) =>
    `navlink text-navy cursor-pointer ${isActive ? 'active' : ''}`

  function handleLogout() {
    setMenuOpen(false)
    setSidebarOpen(false)
    sair()
    navigate('/')
  }

  return (
    <>
      <header className="flex items-center justify-between gap-6 px-6 md:px-10 py-3.5 bg-cream/90 backdrop-blur-md border-b border-border-warm sticky top-0 z-[100]">
        <NavLink to="/" className="shrink-0" onClick={() => setMenuOpen(false)}>
          <img src="/iefc-azul.svg" alt="IEFC" className="h-6 md:h-[36px] w-auto cursor-pointer" />
        </NavLink>

        <nav className="hidden lg:flex flex-1 justify-center gap-5 xl:gap-7 whitespace-nowrap font-label text-[12px] tracking-[0.1em] uppercase font-medium">
          {navLinks.map((item) => (
            <NavLink key={item.to} to={item.to} className={linkCls}>
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="hidden lg:flex items-center gap-4 shrink-0 whitespace-nowrap">
          {estaLogado ? (
            <>
              <button
                type="button"
                onClick={() => setSidebarOpen((v) => !v)}
                title={usuario?.nome}
                aria-expanded={sidebarOpen}
                className="font-label text-[11px] tracking-[0.08em] uppercase text-teal-accessible cursor-pointer inline-flex items-center gap-1.5"
              >
                <i className="ph-light ph-user-circle text-[18px]" />
                {usuario?.nome ? usuario.nome.split(' ')[0] : 'Minha conta'}
                <i className="ph-light ph-caret-right text-[12px]" />
              </button>
              <button
                type="button"
                onClick={handleLogout}
                title="Sair"
                className="font-label text-[11px] tracking-[0.08em] uppercase text-ink-soft cursor-pointer inline-flex items-center gap-1.5"
              >
                <i className="ph-light ph-sign-out text-[15px]" />
                Sair
              </button>
            </>
          ) : (
            <NavLink
              to="/cadastro"
              title="Entrar ou cadastrar-se"
              className="font-label text-[11px] tracking-[0.08em] uppercase text-teal-accessible cursor-pointer inline-flex items-center gap-1.5"
            >
              <i className="ph-light ph-lock-key text-[15px]" />
              Login/Cadastre-se
            </NavLink>
          )}
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
          <i className={`ph-light ${menuOpen ? 'ph-x' : 'ph-list'} text-[26px]`} />
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
            {estaLogado ? (
              <>
                <button
                  type="button"
                  onClick={() => {
                    setMenuOpen(false)
                    setSidebarOpen(true)
                  }}
                  className="flex-1 text-center py-3 font-label text-[12px] tracking-[0.08em] uppercase text-teal-accessible border-[1.5px] border-teal-accessible rounded-md2 cursor-pointer"
                >
                  {usuario?.nome ? usuario.nome.split(' ')[0] : 'Minha conta'}
                </button>
                <button
                  type="button"
                  onClick={handleLogout}
                  className="flex-1 text-center py-3 font-label text-[12px] tracking-[0.08em] uppercase text-ink-soft border-[1.5px] border-border-warm rounded-md2 cursor-pointer"
                >
                  Sair
                </button>
              </>
            ) : (
              <NavLink
                to="/login"
                onClick={() => setMenuOpen(false)}
                className="flex-1 text-center py-3 font-label text-[12px] tracking-[0.08em] uppercase text-teal-accessible border-[1.5px] border-teal-accessible rounded-md2 cursor-pointer"
              >
                Login
              </NavLink>
            )}
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

      {estaLogado && (
        <>
          <div
            onClick={() => setSidebarOpen(false)}
            className={`fixed inset-0 z-[110] bg-navy/40 transition-opacity duration-300 ${
              sidebarOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
            }`}
          />
          <aside
            aria-hidden={!sidebarOpen}
            className={`fixed top-0 left-0 bottom-0 z-[120] w-[270px] bg-navy text-white px-4 py-6 flex flex-col transition-transform duration-300 ease-out ${
              sidebarOpen ? 'translate-x-0 shadow-2xl' : '-translate-x-full'
            }`}
          >
            <div className="flex items-center justify-between px-3 mb-4">
              <p className="font-label text-[10px] tracking-[0.14em] uppercase text-white/50">Minha área</p>
              <button
                type="button"
                aria-label="Fechar menu lateral"
                onClick={() => setSidebarOpen(false)}
                className="text-white/70 hover:text-white cursor-pointer"
              >
                <i className="ph-light ph-x text-[20px]" />
              </button>
            </div>
            <p className="px-3 pb-4 mb-2 border-b border-white/10 text-[14px] truncate" title={usuario?.email}>
              {usuario?.nome}
            </p>
            <nav className="flex flex-col gap-1">
              {itensMenu.map((item) => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  end
                  onClick={() => setSidebarOpen(false)}
                  className={({ isActive }) =>
                    `flex items-center gap-3 px-3 py-2.5 rounded-md2 text-[14px] transition-colors ${
                      isActive ? 'bg-white/15 text-white' : 'text-white/75 hover:bg-white/10 hover:text-white'
                    }`
                  }
                >
                  <i className={`${item.icone} text-[18px]`} />
                  {item.label}
                </NavLink>
              ))}
            </nav>
            <button
              type="button"
              onClick={handleLogout}
              className="mt-auto flex items-center gap-3 px-3 py-2.5 rounded-md2 text-[14px] text-white/75 hover:bg-white/10 hover:text-white cursor-pointer"
            >
              <i className="ph-light ph-sign-out text-[18px]" />
              Sair
            </button>
          </aside>
        </>
      )}
    </>
  )
}
