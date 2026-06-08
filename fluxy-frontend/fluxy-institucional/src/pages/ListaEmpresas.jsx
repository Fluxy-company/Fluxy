import { useEffect, useState } from "react";
import Navbar from "../components/layout/NavBar";
import Footer from "../components/layout/Footer";

export default function App() {
  return (
    <div>
      <Navbar></Navbar>
      <div className="min-h-screen bg-gradient-to-b from-[#22344E] to-[#0E1116] text-[#D6D6D6] font-['Inter'] p-8 flex flex-col items-center">
        <div className="w-full max-w-2xl py-20">
          
          <ListaEmpresas />
        </div>
      </div>
      <Footer />
    </div>
  );
}

function ListaEmpresas() {
  const [empresas, setEmpresas] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/empresas")
      .then((res) => res.json())
      .then((data) => setEmpresas(data))
      .catch((err) => console.log(err));
  }, []);

  return (
    <div className="w-full flex justify-center">
      <div className="bg-[#1a283c] rounded-2xl shadow-lg p-6 border-2 border-[#CAF0F8] w-full">
        <h2 className="text-2xl font-semibold mb-4 text-[#D6D6D6] text-center">
          Lista de Empresas
        </h2>

        <div className="space-y-4">
          {empresas.map((emp) => (
            <div
              key={emp.idEmpresa}
              className="bg-[#3281F8]/20 p-4 rounded-xl border border-[#3281F8]/40 hover:brightness-125 transition-all cursor-pointer"
            >
              <p className="text-lg font-semibold text-[#D6D6D6]">{emp.nome}</p>
              <p className="text-sm text-[#CAF0F8]">CNPJ: {emp.cnpj}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}