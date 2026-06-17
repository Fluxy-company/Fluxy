import { createBrowserRouter, Navigate } from "react-router-dom"
import Institucional from "../pages/Institucional"
import Login from "../pages/Login"
import Cadastro from "../pages/Cadastro"
import MeusCursos from "../pages/MeusCursos"
import Cursos from "../pages/Cursos"
import CursoPlayer from "../pages/CursoPlayer"
import Admin from "../pages/Admin"
import GerarRelatorio from "../pages/GerarRelatorio"
import { isAdmin } from "../services/api"

// Protege rotas que exigem login
function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");
  if (!token) return <Navigate to="/login" replace />;
  return children;
}

// Protege rotas que exigem role de admin
function AdminRoute({ children }) {
  const token = localStorage.getItem("token");
  if (!token) return <Navigate to="/login" replace />;
  if (!isAdmin()) return <Navigate to="/meus-cursos" replace />;
  return children;
}

export const routes = createBrowserRouter([
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
    element: <PrivateRoute><MeusCursos /></PrivateRoute>
  },
  {
    path: "/cursos",
    element: <PrivateRoute><Cursos /></PrivateRoute>
  },
  {
    path: "/curso/:id",
    element: <PrivateRoute><CursoPlayer /></PrivateRoute>
  },
  {
    path: "/admin",
    element: <AdminRoute><Admin /></AdminRoute>
  },
  {
    path: "/relatorio",
    element: <AdminRoute><GerarRelatorio /></AdminRoute>
  },
])