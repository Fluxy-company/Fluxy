import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/layout/NavBar";
import Footer from "../components/layout/Footer";
import Button from "../components/ui/Button";
import { verificarEmail, senhaValida } from "../js/util";


export default function Cadastro(){
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [telefone, setTelefone] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");
  const navigate = useNavigate();

  async function cadastrar() {
    if (!verificarEmail(email)) {
      setMensagem("Email inválido");
      return;
    }

    if (!senhaValida(senha)) {
      setMensagem("Senha deve ter no mínimo 6 caracteres.");
      return;
    }

    const usuario = { nome, email, senha };

    const resposta = await fetch("/api/v1/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuario),
    });

    if (resposta.ok) {
      setMensagem("Cadastro realizado com sucesso!");
      setTimeout(() => navigate("/login"), 1500);
    } else {
      setMensagem("Erro ao cadastrar");
    }
  }

  return (
    <div>
      <Navbar>

        <Button href="/cadastro" className=" text-[#032738] border-2 border-[#0A4A68]">
            Cadastre-se
        </Button>

        <Button href="/login" className="bg-[#0A4A68] text-[#E4E4F2] border-[#0A4A68] border-2">
          Login
        </Button>

      </Navbar>
      <div className="bg-[url(/background-iefc.jpg)] bg-no-repeat min-h-screen bg-cover">
        <div className="grid grid-cols-1 md:grid-cols-2 h-screen ">
          <div className="bg-[#F9F9F9] mt-22 mb-2 rounded-md w-full max-w-lg p-10 flex flex-col gap-4">
            <div className="text-center">
              <h2 className="text-2xl font-bold text-[#032738] py-4">Seja bem-vindo!</h2>
              <h3 className="text-2xl font-normal text-[#8F8F9F]">Ainda não é aluno? Matricule-se já</h3>
            </div>

            <div className="flex flex-col gap-1">
              <label htmlFor="input_nome">Nome completo</label>
              <input
                id="input_nome"
                type="text"
                placeholder="Digite seu nome completo"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                className="w-full p-3 rounded border border-[#032738] bg-transparent outline-none focus:border-[#7F77DD]"
              />
            </div>

            <div className="flex flex-col gap-1">
              <label htmlFor="input_email">Email</label>
              <input
                id="input_email"
                type="text"
                placeholder="Digite seu email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full p-3 rounded border border-[#032738] bg-transparent outline-none focus:border-[#7F77DD]"
              />
            </div>

            <div className="flex flex-col gap-1">
              <label htmlFor="input_telefone">Telefone</label>
              <input
                id="input_telefone"
                type="text"
                placeholder="Digite seu telefone"
                value={telefone}
                onChange={(e) => setTelefone(e.target.value)}
                className="w-full p-3 rounded border border-[#032738] bg-transparent outline-none focus:border-[#7F77DD]"
              />
            </div>

            <div className="flex flex-col gap-1">
              <label htmlFor="input_senha">Senha</label>
              <input
                id="input_senha"
                type="password"
                placeholder="Digite sua senha"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                className="w-full p-3 rounded border border-[#032738] bg-transparent outline-none focus:border-[#7F77DD]"
              />
            </div>


            {mensagem && (
              <p className={`text-sm text-center ${mensagem.includes("sucesso") ? "text-green-600" : "text-red-500"}`}>
                {mensagem}
              </p>
            )}

            <Button
              onClick={cadastrar}
              className="bg-[#0A4A68] text-white text-lg w-full hover:opacity-90 transition"
            >
              Cadastrar
            </Button>

            <div className="w-full flex group text-center">
              <span>Já tem cadastro? {` `} </span>
              <a 
                href="/login" 
                className="text-[#1D9291] border-b-2 border-[#032738]"
              >
                 Entre na sua conta 
                <svg 
                  xmlns="http://www.w3.org/2000/svg" 
                  fill="none" 
                  viewBox="0 0 24 24" 
                  strokeWidth={2} 
                  stroke="currentColor" 
                  className="w-5 h-5 transform group-hover:translate-x-1 transition-transform inline"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5l6 6m0 0l-6 6M19.5 10.5h-14" />
                </svg>
              </a>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}