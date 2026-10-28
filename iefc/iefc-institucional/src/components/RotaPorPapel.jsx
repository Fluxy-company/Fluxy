import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import Pagina403 from '../pages/Pagina403'

export default function RotaPorPapel({ papeis = [], children }) {
  const { estaLogado, roles, carregando } = useAuth()
  const location = useLocation()

  if (carregando) return null

  if (!estaLogado) {
    const next = encodeURIComponent(location.pathname + location.search)
    return <Navigate to={`/login?next=${next}`} replace />
  }

  const temPermissao = papeis.length === 0 || papeis.some((papel) => roles.includes(papel))
  if (!temPermissao) {
    return <Pagina403 />
  }

  return children
}
