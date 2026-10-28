import { NavLink } from 'react-router-dom'
import Layout from '../components/Layout'

export default function Pagina404() {
  return (
    <Layout>
      <div className="min-h-[60vh] flex items-center justify-center px-6 py-20">
        <div className="max-w-[420px] text-center">
          <i className="ph-light ph-compass text-navy text-[40px]" />
          <h1 className="font-display font-bold text-navy text-[30px] mt-4 mb-2">Página não encontrada</h1>
          <p className="text-[14px] text-ink-soft mb-8">
            O endereço acessado não existe ou foi movido.
          </p>
          <NavLink
            to="/"
            className="font-label font-bold text-[12px] tracking-[0.08em] uppercase px-5 py-3 rounded-md2 bg-amber text-navy cursor-pointer"
          >
            Ir para o início
          </NavLink>
        </div>
      </div>
    </Layout>
  )
}
