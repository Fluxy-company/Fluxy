import { useState } from "react";
import { verificarEmail, senhaValida } from "../js/util";
import "../css/style.css";

export default function Cadastro() {
  const [nome, setNome] = useState("");
  const [sobrenome, setSobrenome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");

  async function cadastrar() {
    if (!verificarEmail(email)) {
      setMensagem("Email inválido");
      return;
    }

    if (!senhaValida(senha)) {
      setMensagem("Senha deve ter no mínimo 6 caracteres.");
      return;
    }

    const usuario = { nome, sobrenome, email, senha };

    const resposta = await fetch("http://localhost:8080/api/v1/usuarios", {
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
      setMensagem("Usuário cadastrado!");
    } else {
      console.log("Erro no cadastro:", resposta.statusText);
      setMensagem("Erro ao cadastrar");
    }
  }

  return (
    <div className="main-section">
      <h1>Cadastro</h1>

      <label htmlFor="nome">Insira seu nome</label>
      <br />
      <input
        type="text"
        id="nome"
        placeholder="Nome"
        value={nome}
        onChange={(e) => setNome(e.target.value)}
      />
      <br /><br />

      <label htmlFor="sobrenome">Insira seu sobrenome</label>
      <br />
      <input
        type="text"
        id="sobrenome"
        placeholder="Sobrenome"
        value={sobrenome}
        onChange={(e) => setSobrenome(e.target.value)}
      />
      <br /><br />

      <label htmlFor="email">Insira seu email</label>
      <br />
      <input
        type="text"
        id="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <br /><br />

      <label htmlFor="senha">Insira sua senha</label>
      <br />
      <input
        type="password"
        id="senha"
        placeholder="Senha"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
      />
      <br /><br />

      <button onClick={cadastrar}>Cadastrar</button>

      <p id="mensagem">{mensagem}</p>

      <nav>
        <label htmlFor="login-section">Já tem cadastro?</label>
        <a href="/login" id="login-section">Login</a>
      </nav>
    </div>
  );
}