import { Routes, Route } from "react-router-dom";
import Institucional from "../components/Institucional";
import Login from "../components/Login";
import Cadastro from "../components/Cadastro";
import ListaUsuarios from "../components/ListaUsuarios"

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Institucional />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/usuarios" element={<ListaUsuarios />}></Route>
    </Routes>
  );
}
