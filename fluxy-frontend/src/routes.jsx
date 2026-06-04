import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Listagem from './pages/listagemUsuarios.jsx';
import Login from './pages/login.jsx';
import Cadastro from './pages/cadastro.jsx';
import MeusCursos from './pages/meusCursos.jsx';
import CursoPlayer from './pages/cursoPlayer.jsx';
import Cursos from './pages/cursos.jsx';
import Admin from './pages/admin.jsx';
import { getUserId, isAdmin } from './services/api.js';

function PrivateRoute({ children }) {
    return getUserId() ? children : <Navigate to="/login" />;
}

function AdminRoute({ children }) {
    if (!getUserId()) return <Navigate to="/login" />;
    if (!isAdmin()) return <Navigate to="/meus-cursos" />;
    return children;
}

function RoutesApp() {
    return(
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Navigate to='/login' />}></Route>
                <Route path='/login' element={<Login></Login>}></Route>
                <Route path='/cadastro' element={<Cadastro></Cadastro>}></Route>
                <Route path='/listagem' element={<PrivateRoute><Listagem /></PrivateRoute>}></Route>
                <Route path='/meus-cursos' element={<PrivateRoute><MeusCursos /></PrivateRoute>}></Route>
                <Route path='/cursos' element={<PrivateRoute><Cursos /></PrivateRoute>}></Route>
                <Route path='/curso/:id' element={<PrivateRoute><CursoPlayer /></PrivateRoute>}></Route>
                <Route path='/admin' element={<AdminRoute><Admin /></AdminRoute>}></Route>
            </Routes>
        </BrowserRouter>
    )
}

export default RoutesApp;