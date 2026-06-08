import {createBrowserRouter} from "react-router-dom"
import Institucional from "../pages/Institucional"
import Login from "../pages/Login"
import Cadastro from "../pages/Cadastro"
import MeusCursos from "../pages/MeusCursos"
import Cursos from "../pages/Cursos"
import CursoPlayer from "../pages/CursoPlayer"
import Admin from "../pages/Admin"
import GerarRelatorio from "../pages/GerarRelatorio"

export const routes = createBrowserRouter ([
  {
    path: "/",
    element: <Institucional />
  },
  {
    path: "/login",
    element: <Login />
  },
  {
    path: "/cadastro",
    element: <Cadastro />
  },
  {
    path: "/meus-cursos",
    element: <MeusCursos />
  },
  {
    path: "/cursos",
    element: <Cursos />
  },
  {
    path: "/curso/:id",
    element: <CursoPlayer />
  },
  {
    path: "/admin",
    element: <Admin />
  },
  {
    path: "/relatorio",
    element: <GerarRelatorio />
  },
])