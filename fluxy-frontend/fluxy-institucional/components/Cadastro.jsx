import { useState } from "react";
import Navbar from "./layout/NavBar";
import Footer from "./layout/Footer";

export default function Cadastro() {
  const [nome, setNome] = useState("");
  const [sobrenome, setSobrenome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");

  function verificarEmail(email) {
    return email.includes("@");
  }

  function senhaValida(senha) {
    return senha.length >= 6;
  }

  async function cadastrar() {
    const usuario = { nome, sobrenome, email, senha };

    if (!verificarEmail(email)) {
      setMensagem("Email inválido");
      return;
    }

    if (!senhaValida(senha)) {
      setMensagem("Senha deve ter no mínimo 6 caracteres.");
      return;
    }

    const resposta = await fetch("http://localhost:8080/api/v1/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuario),
    });

    if (resposta.ok) {
      setMensagem("Usuário cadastrado!");
    } else {
      setMensagem("Erro ao cadastrar");
    }
  }

  return (
    <>
      <Navbar />

      <div className="min-h-screen flex justify-center items-center bg-gradient-to-b from-[#22344E] to-[#0E1116]">
        
        <div className="w-full max-w-lg p-10 rounded-lg border-4 border-[#CAF0F8] text-[#D6D6D6] flex flex-col gap-4">

          <h1 className="text-3xl font-bold">Cadastro</h1>

          <div className="flex flex-col gap-1">
            <label>Nome</label>
            <input
              type="text"
              placeholder="Digite seu nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              className="w-full p-3 rounded border border-[#CAF0F8] bg-transparent outline-none focus:border-[#7F77DD]"
            />
          </div>

          <div className="flex flex-col gap-1">
            <label>Sobrenome</label>
            <input
              type="text"
              placeholder="Digite seu sobrenome"
              value={sobrenome}
              onChange={(e) => setSobrenome(e.target.value)}
              className="w-full p-3 rounded border border-[#CAF0F8] bg-transparent outline-none focus:border-[#7F77DD]"
            />
          </div>

          <div className="flex flex-col gap-1">
            <label>Email</label>
            <input
              type="text"
              placeholder="Digite seu email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-3 rounded border border-[#CAF0F8] bg-transparent outline-none focus:border-[#7F77DD]"
            />
          </div>

          <div className="flex flex-col gap-1">
            <label>Senha</label>
            <input
              type="password"
              placeholder="Digite sua senha"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full p-3 rounded border border-[#CAF0F8] bg-transparent outline-none focus:border-[#7F77DD]"
            />
          </div>

          <button
            onClick={cadastrar}
            className="bg-[#3281F8] text-white py-3 rounded text-lg w-full hover:opacity-90 transition"
          >
            Cadastrar
          </button>

          <p
            className={`font-semibold ${
              mensagem.includes("sucesso")
                ? "text-green-500"
                : "text-red-500"
            }`}
          >
            {mensagem}
          </p>

          <a href="/login" className="text-sm underline">
            Já tem cadastro? Login
          </a>

        </div>

      </div>

      <Footer />
    </>
  );
}