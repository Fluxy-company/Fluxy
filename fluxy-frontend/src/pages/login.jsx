import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { verificarEmail } from "../js/util";
import "../css/style.css";

export default function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");
  const navigate = useNavigate();

  async function login() {
    if (!verificarEmail(email)) {
      setMensagem("Email inválido");
      return;
    }

    const usuario = { email, senha };

    const resposta = await fetch("http://localhost:8080/api/v1/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuario),
    });

    console.log("Resposta da API");
    console.log("Endpoint:", resposta.url);
    console.log("Método: POST");
    console.log("Status:", resposta.status);
    console.log(resposta.statusText);

    if (resposta.ok) {
      const data = await resposta.json();
      localStorage.setItem("token", data.token);
      setMensagem("Login realizado com sucesso!");
      setTimeout(() => navigate("/listagem-usuarios"), 1000);
    } else {
      console.log("Erro no login:", resposta.statusText);
      setMensagem("Email ou senha inválidos");
    }
  }

  return (
    <div className="main-section">
      <h1>Login</h1>

      <input
        type="text"
        id="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <br /><br />

      <input
        type="password"
        id="senha"
        placeholder="Senha"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
      />
      <br /><br />

      <button onClick={login}>Entrar</button>

      <p id="mensagem" className="mensagem">{mensagem}</p>

      <nav>
        <label htmlFor="cadastro-section">Ainda não tem cadastro?</label>
        <a className="link" href="/cadastro" id="cadastro-section">Cadastre-se</a>
      </nav>
    </div>
  );
}