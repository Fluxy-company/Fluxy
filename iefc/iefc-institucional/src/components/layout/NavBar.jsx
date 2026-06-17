import { useState, useEffect } from "react";
import logo from "../../assets/logotipo-azul.svg";
import { NavLink } from "react-router-dom";

export default function Navbar({ isFooter = false, children }) {
  const [active, setActive] = useState("/#inicio");
  const [isOpen, setIsOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);

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
        z-50 flex items-center justify-between
        px-[clamp(1rem,3vw,2rem)] h-16 md:h-20
        bg-[#E4E4F2] text-[#032738] transition-all duration-300
        ${scrolled ? "shadow-md bg-[#E4E4F2]/95 backdrop-blur-sm" : ""}
      `}
    >

      <img src={logo} alt="Logo" className="h-6 md:h-8 w-auto object-contain z-50" />

      <div className="hidden md:flex items-center gap-4 lg:gap-8">
        {NAV_LINKS.map((link) => {
          const isActive = active === link.href;

          return (
            <NavLink
              key={link.href}
              to={link.href}
              onClick={() => setActive(link.href)}
              className={`text-base lg:text-lg font-semibold tracking-[0.01em] transition-colors duration-200 hover:text-blue-900 ${
                isActive ? "text-blue-900 border-b-2 border-[#032738]" : ""
              }`}
            >
              {link.label}
            </NavLink>
          );
        })}
      </div>

      <div className="flex items-center gap-3 lg:gap-4 z-50">
        <div className="hidden sm:flex items-center gap-3 lg:gap-4 scale-90 lg:scale-100 origin-right">
          {children}
        </div>

        <button
          onClick={() => setIsOpen(!isOpen)}
          className="flex md:hidden flex-col justify-center items-center w-8 h-8 gap-1.5 text-[#032738] focus:outline-none"
          aria-label="Toggle Menu"
        >
          <span className={`h-0.5 w-6 bg-current transform transition duration-300 ${isOpen ? "rotate-45 translate-y-2" : ""}`} />
          <span className={`h-0.5 w-6 bg-current transition duration-300 ${isOpen ? "opacity-0" : ""}`} />
          <span className={`h-0.5 w-6 bg-current transform transition duration-300 ${isOpen ? "-rotate-45 -translate-y-2" : ""}`} />
        </button>
      </div>

      <div
        className={`
          fixed inset-0 bg-[#E4E4F2] flex flex-col pt-24 px-6 gap-6 transition-transform duration-300 ease-in-out md:hidden z-40
          ${isOpen ? "translate-x-0" : "translate-x-full"}
        `}
      >
        {NAV_LINKS.map((link) => (
          <NavLink
            key={link.href}
            to={link.href}
            onClick={() => {
              setActive(link.href);
              setIsOpen(false);
            }}
            className="text-2xl font-semibold tracking-[0.01em] py-2 border-b border-[#032738]/10"
          >
            {link.label}
          </NavLink>
        ))}

        <div className="flex sm:hidden flex-col gap-4 mt-4">
          {children}
        </div>
      </div>
    </nav>
  );
}