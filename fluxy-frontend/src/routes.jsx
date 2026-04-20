import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Listagem from './pages/listagemUsuarios.jsx';
import Login from './pages/login.jsx';
import Cadastro from './pages/cadastro.jsx';

function RoutesApp() {
    return(
        <BrowserRouter>
            <Routes>
                <Route path='/login' element={<Login></Login>}></Route>
                <Route path='/cadastro' element={<Cadastro></Cadastro>}></Route>
                <Route path='/listagem' element={<Listagem></Listagem>}></Route>
            </Routes>
        </BrowserRouter>
    )
}

export default RoutesApp;