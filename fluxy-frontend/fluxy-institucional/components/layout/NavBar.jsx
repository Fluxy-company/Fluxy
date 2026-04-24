import { useState, useEffect } from "react";
import logo from "../../src/assets/fluxy-claro.svg";

export default function Navbar() {
  const [active, setActive] = useState("#inicio");
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);

  const NAV_LINKS = [
    { label: "Início", href: "#inicio" },
    { label: "Valores", href: "#valores" },
    { label: "Quem somos", href: "#quem-somos" },
    { label: "Quem transformamos", href: "#quem-transformamos" },
    { label: "Saiba mais", href: "#saiba-mais" },
  ];

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <nav className={`fixed top-0 left-0 right-0 z-[100]flex items-center justify-between px-[clamp(1.5rem,5vw,3rem)] h-16 backdrop-blur-md transition-all duration-300
        ${scrolled ? "bg-[rgba(15,17,23,0.92)] border-b border-[rgba(127,119,221,0.15)]": "bg-[rgba(15,17,23,0.7)] border-b border-transparent"}
      `}
    >

      <img src={logo} alt="Logo" className="h-8 w-auto object-contain"/>
      <div className="hidden md:flex items-center gap-8">
        {NAV_LINKS.map((link) => {
          const isActive = active === link.href;

          return (
            <a
              key={link.href}
              href={link.href}
              onClick={() => setActive(link.href)}
              className={`text-[13px] font-['Syne',sans-serif] tracking-[0.01em] transition-colors duration-200 ${isActive ? "text-white font-bold" : "text-white/55 hover:text-white/85"}`}>
              {link.label}
            </a>
          );
        })}
      </div>

      <button onClick={() => setMenuOpen(!menuOpen)} className="md:hidden text-white p-1" aria-label="Menu">
        <svg width="22" height="22"></svg>
      </button>
    </nav>
  );
}