import { NavLink, useNavigate } from 'react-router-dom'
import Layout from '../components/Layout'

export default function Pagina403() {
  const navigate = useNavigate()

  return (
    <Layout>
      <div className="min-h-[60vh] flex items-center justify-center px-6 py-20">
        <div className="max-w-[420px] text-center">
          <i className="ph-light ph-lock-key text-navy text-[40px]" />
          <h1 className="font-display font-bold text-navy text-[30px] mt-4 mb-2">Acesso restrito</h1>
          <p className="text-[14px] text-ink-soft mb-8">
            Você não tem permissão para acessar esta área.
          </p>
          <div className="flex items-center justify-center gap-3">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="font-label font-bold text-[12px] tracking-[0.08em] uppercase px-5 py-3 rounded-md2 border-[1.5px] border-teal-accessible text-teal-accessible cursor-pointer"
            >
              Voltar
            </button>
            <NavLink
              to="/"
              className="font-label font-bold text-[12px] tracking-[0.08em] uppercase px-5 py-3 rounded-md2 bg-amber text-navy cursor-pointer"
            >
              Ir para o início
            </NavLink>
          </div>
        </div>
      </div>
    </Layout>
  )
}
