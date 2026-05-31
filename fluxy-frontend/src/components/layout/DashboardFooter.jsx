export default function DashboardFooter() {
  return (
    <footer className="bg-[#1e2d3d] py-8 px-8 mt-auto w-full">
      <div className="max-w-7xl mx-auto flex flex-col sm:flex-row justify-between items-center gap-4">
        <div>
          <p className="text-white font-bold text-sm tracking-wide">FLUXY</p>
          <p className="text-gray-500 text-xs mt-1">
            © 2025 Fluxy. Todos os direitos reservados.
          </p>
        </div>
        <div className="flex gap-6">
          <a href="#" className="text-gray-400 hover:text-white text-sm transition-colors">
            Termos de Uso
          </a>
          <a href="#" className="text-gray-400 hover:text-white text-sm transition-colors">
            Privacidade
          </a>
          <a href="#" className="text-gray-400 hover:text-white text-sm transition-colors">
            Suporte
          </a>
        </div>
      </div>
    </footer>
  );
}
