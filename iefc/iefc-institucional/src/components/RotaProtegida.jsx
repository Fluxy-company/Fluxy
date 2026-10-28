import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function RotaProtegida({ children }) {
  const { estaLogado, carregando } = useAuth()
  const location = useLocation()

  if (carregando) return null

  if (!estaLogado) {
    const next = encodeURIComponent(location.pathname + location.search)
    return <Navigate to={`/login?next=${next}`} replace />
  }

  return children
}
