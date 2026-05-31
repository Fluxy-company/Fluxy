import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { isAdmin } from "../../services/api";

const NAV_LINKS = [
  { label: "Cursos", href: "/cursos" },
  { label: "Meus Cursos", href: "/meus-cursos" },
  { label: "Calendário", href: "/calendario" },
];

export default function DashboardNavbar() {
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();
  const location = useLocation();

  function handleLogout() {
    localStorage.removeItem("token");
    navigate("/login");
  }

  return (
    <nav className="bg-[#1e2d3d] px-8 h-16 flex items-center justify-between sticky top-0 z-50 w-full">
      <div className="flex items-center gap-2">
        <span className="text-white font-bold text-lg tracking-wide">FLUXY</span>
        <span className="text-gray-400 text-xs tracking-widest uppercase hidden sm:inline">
          Education Portal
        </span>
      </div>

      <div className="hidden md:flex items-center gap-6">
        {NAV_LINKS.map((link) => {
          const isActive = location.pathname === link.href;
          return (
            <a
              key={link.href}
              href={link.href}
              className={`text-sm font-medium transition-colors ${
                isActive
                  ? "text-white border-b-2 border-white pb-1"
                  : "text-gray-400 hover:text-white"
              }`}
            >
              {link.label}
            </a>
          );
        })}
      </div>

      <div className="flex items-center gap-4">
        <div className="relative hidden sm:block">
          <input
            type="text"
            placeholder="Pesquisar..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="!bg-gray-700 text-white !text-sm pl-9 pr-4 py-2 w-48 focus:outline-none focus:ring-1 focus:ring-blue-500 placeholder-gray-500"
          />
          <svg
            className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            />
          </svg>
        </div>

        <button className="!bg-transparent !p-0 text-gray-400 hover:text-white transition-colors cursor-pointer">
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
            />
          </svg>
        </button>

        {isAdmin() && (
          <a
            href="/admin"
            className={`text-sm font-medium transition-colors ${
              location.pathname === "/admin"
                ? "text-white border-b-2 border-white pb-1"
                : "text-gray-400 hover:text-white"
            }`}
          >
            Admin
          </a>
        )}

        <div className="flex items-center gap-2">
          <div className="text-right hidden sm:block">
            <p className="text-sm text-white font-medium">Usuário</p>
          </div>
          <div className="w-9 h-9 rounded-full bg-gray-600 flex items-center justify-center">
            <span className="text-white text-sm font-semibold">U</span>
          </div>
          <button
            onClick={handleLogout}
            className="!bg-transparent !p-0 text-gray-400 hover:text-red-400 !text-xs ml-1 transition-colors cursor-pointer"
            title="Sair"
          >
            ✕
          </button>
        </div>
      </div>
    </nav>
  );
}
