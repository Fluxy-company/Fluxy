import { useState } from "react";
import Navbar from "./layout/NavBar";
import Footer from "./layout/Footer";

export default function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");

  function verificarEmail(email) {
    return email.includes("@");
  }

  async function login() {
    if (!verificarEmail(email)) {
      setMensagem("Email inválido");
      return;
    }

    const resposta = await fetch("http://localhost:8080/api/v1/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, senha }),
    });

    if (resposta.ok) {
      const data = await resposta.json();
      localStorage.setItem("token", data.token);

      setMensagem("Login realizado com sucesso!");

      setTimeout(() => {
        window.location.href = "/usuarios";
      }, 1000);
    } else {
      setMensagem("Email ou senha inválidos");
    }
  }

  return (
    <>
      <Navbar />

      <div className="min-h-screen flex justify-center items-center bg-gradient-to-b from-[#22344E] to-[#0E1116]">
        
        <div className="w-full max-w-lg p-10 rounded-lg border-4 border-[#CAF0F8] text-[#D6D6D6] flex flex-col gap-4">

          <h1 className="text-3xl font-bold">Login</h1>

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
            onClick={login}
            className="bg-[#3281F8] text-white py-3 rounded text-lg w-full hover:opacity-90 transition"
          >
            Entrar
          </button>

          <p>{mensagem}</p>

          <a href="/cadastro" className="text-sm underline">
            Ainda não tem cadastro? Cadastre-se
          </a>

        </div>

      </div>

      <Footer />
    </>
  );
}