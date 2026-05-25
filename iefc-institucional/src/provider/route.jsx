import {createBrowserRouter} from "react-router-dom"
import Institucional from "../pages/Institucional"
import Login from "../pages/Login"
import Cadastro from "../pages/Cadastro"

export const routes = createBrowserRouter ([
    {path: "/",
    element: <Institucional />
  },
    {
      path: "/login",
      element: <Login />
  },
  {
    path: "/cadastro",
    element: <Cadastro />
  }

])