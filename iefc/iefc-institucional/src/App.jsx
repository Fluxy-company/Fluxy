import { Navigate, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Cursos from './pages/Cursos'
import Perfil from './pages/Perfil'
import CursoDetalhe from './pages/CursoDetalhe'
import CursoNovo from './pages/CursoNovo'
import Contato from './pages/Contato'
import Apoie from './pages/Apoie'
import Transparencia from './pages/Transparencia'
import Login from './pages/Login'
import Cadastro from './pages/Cadastro'
import QuemSomos from './pages/QuemSomos'
import OQueFazemos from './pages/OQueFazemos'
import Blog from './pages/Blog'
import Pagina404 from './pages/Pagina404'
import PainelColaboradoresPendentes from './pages/painel/PainelColaboradoresPendentes'
import PainelAdministradores from './pages/painel/PainelAdministradores'
import MeusCursos from './pages/MeusCursos'
import RotaPorPapel from './components/RotaPorPapel'
import RotaProtegida from './components/RotaProtegida'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/quem-somos" element={<QuemSomos />} />
      <Route path="/o-que-fazemos" element={<OQueFazemos />} />
      <Route path="/blog" element={<Blog />} />
      <Route path="/perfil" element={<RotaProtegida><Perfil /></RotaProtegida>} />
      <Route path="/cursos" element={<RotaProtegida><Cursos /></RotaProtegida>} />
      <Route path="/meus-cursos" element={<RotaProtegida><MeusCursos /></RotaProtegida>} />
      <Route
        path="/cursos/novo"
        element={
          <RotaPorPapel papeis={['ADMIN']}>
            <CursoNovo />
          </RotaPorPapel>
        }
      />
      <Route path="/cursos/:id" element={<RotaProtegida><CursoDetalhe /></RotaProtegida>} />
      <Route path="/apoie" element={<Apoie />} />
      <Route path="/contato" element={<Contato />} />
      <Route path="/transparencia" element={<Transparencia />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/poslogin" element={<Navigate to="/" replace />} />

      <Route
        path="/painel/colaboradores/pendentes"
        element={
          <RotaPorPapel papeis={['ADMIN']}>
            <PainelColaboradoresPendentes />
          </RotaPorPapel>
        }
      />

      <Route
        path="/painel/administradores"
        element={
          <RotaPorPapel papeis={['ADMIN']}>
            <PainelAdministradores />
          </RotaPorPapel>
        }
      />

      <Route path="*" element={<Pagina404 />} />
    </Routes>
  )
}
