import { Routes, Route } from "react-router-dom";
import Institucional from "../components/Institucional";
import Login from "../components/Login";
import Cadastro from "../components/Cadastro";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Institucional />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
    </Routes>
  );
}