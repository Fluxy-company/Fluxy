import { useState, useEffect } from "react";
import logo from "../../assets/logotipo-azul.svg";
import { NavLink } from "react-router-dom";

export default function Navbar({isFooter = false, children }) {
  const [active, setActive] = useState("#inicio");

  const NAV_LINKS = [
    { label: "Início", href: "/#inicio" },
    { label: "O que fazemos", href: "/#fazemos" },
    { label: "Para quem é", href: "/#para-quem" },
    { label: "Calendário de eventos", href: "/#calendario" },
  ];

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <nav
      className={`
        ${isFooter ? "relative" : "fixed top-0 left-0 right-0"}
        z-100 flex items-center justify-between
        px-[clamp(1.5rem,5vw,3rem)] h-20
        bg-[#E4E4F2] text-[#032738]
      `}

    >
        <img src={logo} alt="Logo" className="h-8 w-auto object-contain" />

      <div className="mx-auto flex items-center justify-between px-[clamp(1.5rem,5vw,3rem)] h-16">


        <div className="hidden md:flex items-center gap-8">
          {NAV_LINKS.map((link) => {
            const isActive = active === link.href;

            return (
              <NavLink
                key={link.href}
                to={link.href}
                onClick={() => setActive(link.href)}
                className={`text-xl font-semibold tracking-[0.01em] transition-colors duration-200`} >
                  {link.label}
              </NavLink>
            );
          })}
        </div>

      </div>

        <div className="flex items-end gap-4">
          {children}
        </div>
    </nav>
  );
}