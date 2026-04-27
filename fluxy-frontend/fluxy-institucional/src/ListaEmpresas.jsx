import { useEffect, useState } from "react";

export default function App() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-[#0b0f1a] to-[#111827] text-white p-8">
      <h1 className="text-4xl font-bold mb-8 text-purple-400">Fluxy</h1>

      <div className="grid md:grid-cols-2 gap-8">
        <ListaEmpresas />
      </div>
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
    <div className="bg-[#0f172a] rounded-2xl shadow-lg p-6 border border-purple-500/20">
      <h2 className="text-2xl font-semibold mb-4 text-purple-300">
        Lista de Empresas
      </h2>

      <div className="space-y-4">
        {empresas.map((emp) => (
          <div
            key={emp.idEmpresa}
            className="bg-gradient-to-r from-purple-600/20 to-blue-500/20 p-4 rounded-xl hover:scale-[1.02] transition"
          >
            <p className="text-lg font-semibold">{emp.nome}</p>
            <p className="text-sm text-gray-300">CNPJ: {emp.cnpj}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

