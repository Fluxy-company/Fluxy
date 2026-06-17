import Layout from "../layout/Layout";

export default function Footer() {
  return (
    <footer className="bg-[#0A4A68] text-[#D9D9D9] py-12" id="contato">
      <Layout>
        
        <div className="flex flex-col md:flex-row md:justify-between md:items-center gap-10 md:gap-4">
        
          <div className="flex-shrink-0 flex justify-center md:justify-start">
            <img 
              src="/logotipo-amarelo.svg"
              alt="IEFC Logo" 
              className="h-10 w-auto opacity-80 brightness-200" 
            />
          </div>

          <div className="flex flex-col sm:flex-row items-center sm:items-center gap-28 md:gap-8 flex-grow justify-center text-center sm:text-left">
            
            <div className="flex flex-col gap-2 text-md font-normal items-center sm:items-start">
              <a href="#o-que-fazemos" className="hover:underline opacity-90 transition-all">O que fazemos</a>
              <a href="#eventos" className="hover:underline opacity-90 transition-all">Eventos</a>
              <a href="#para-quem-e" className="hover:underline opacity-90 transition-all">Para quem é</a>
            </div>

            <div className="flex flex-col gap-2 text-md font-normal items-center sm:items-start">
              <a href="#cursos" className="hover:underline opacity-90 transition-all">Nossos cursos</a>
              <a href="#calendario" className="hover:underline opacity-90 transition-all">Calendário de eventos</a>
            </div>

            <div className="hidden sm:block h-14 w-[2px] bg-[#D9D9D9] opacity-80 rounded-full"></div>

            <div className="flex items-center gap-4 text-[#D9D9D9]">

              <a 
                href="https://www.instagram.com/iefc_instituto/" 
                target="_blank" 
                rel="noopener noreferrer"
                className="hover:text-amber-400 opacity-90 hover:opacity-100 transition-all"
                aria-label="Instagram"
              >
                <svg className="w-6 h-6 fill-currentColor" viewBox="0 0 24 24">
                  <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zM12 0C8.741 0 8.333.014 7.053.072 2.695.272.273 2.69.073 7.051.014 8.333 0 8.741 0 12c0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98C15.668.014 15.259 0 12 0zm0 5.838a6.162 6.162 0 100 12.324 6.162 6.162 0 000-12.324zM12 16a4 4 0 110-8 4 4 0 010 8zm6.406-11.845a1.44 1.44 0 100 2.881 1.44 1.44 0 000-2.881z"/>
                </svg>
              </a>

              <a 
                href="https://linkedin.com/company/instituto-educacional-futuro-da-ciência?originalSubdomain=br" 
                target="_blank" 
                rel="noopener noreferrer"
                className="hover:text-amber-400 opacity-90 hover:opacity-100 transition-all"
                aria-label="LinkedIn"
              >
                <svg className="w-6 h-6 fill-currentColor" viewBox="0 0 24 24">
                  <path d="M19 0h-14c-2.761 0-5 2.239-5 5v14c0 2.761 2.239 5 5 5h14c2.762 0 5-2.239 5-5v-14c0-2.761-2.238-5-5-5zm-11 19h-3v-11h3v11zm-1.5-12.268c-.966 0-1.75-.779-1.75-1.75s.784-1.75 1.75-1.75 1.75.779 1.75 1.75-.784 1.75-1.75 1.75zm13.5 12.268h-3v-5.604c0-3.368-4-3.113-4 0v5.604h-3v-11h3v1.765c1.396-2.586 7-2.777 7 2.476v6.759z"/>
                </svg>
              </a>

              <a 
                href="https://www.youtube.com/@iefc-institutoeducacional" 
                target="_blank" 
                rel="noopener noreferrer"
                className="hover:text-amber-400 opacity-90 hover:opacity-100 transition-all"
                aria-label="YouTube"
              >
                <svg className="w-6 h-6 fill-currentColor" viewBox="0 0 24 24">
                  <path d="M23.498 6.186a3.016 3.016 0 00-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 00.502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 002.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 002.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"/>
                </svg>
              </a>
            </div>

          </div>
        </div>

        <div className="border-t border-[#D9D9D9]/10 mt-10 pt-4 text-center text-normal font-light opacity-60">
          © {new Date().getFullYear()} IEFC. Todos os direitos reservados.
        </div>
      </Layout>
    </footer>
  );
}