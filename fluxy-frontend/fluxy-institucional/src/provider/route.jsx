import {createBrowserRouter} from "react-router-dom"
import Institucional from "../pages/Institucional"
import Login from "../pages/Login"
import Cadastro from "../pages/Cadastro"
import ListaUsuarios from "../../../src/pages/listagemUsuarios"

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
  },
  {
    path:'/listagem',
    element: <ListaUsuarios />
  }


])

{/* <Route path='/login' element={<Login></Login>}></Route>
<Route path='/cadastro' element={<Cadastro></Cadastro>}></Route>
<Route path='/listagem' element={<Listagem></Listagem>}></Route> */}