import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Cursos from './pages/Cursos'
import Contato from './pages/Contato'
import Apoie from './pages/Apoie'
import Transparencia from './pages/Transparencia'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/cursos" element={<Cursos />} />
      <Route path="/apoie" element={<Apoie />} />
      <Route path="/contato" element={<Contato />} />
      <Route path="/transparencia" element={<Transparencia />} />
      {/* <Route path="*" element={<Home />} /> */}
    </Routes>
  )
}
