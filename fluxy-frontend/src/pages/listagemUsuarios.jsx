import { useState } from "react";
import "../css/style.css";

export default function ListaUsuarios() {
  const [usuarios, setUsuarios] = useState([]);

  async function buscarTodos() {
    const resposta = await fetch("http://localhost:8080/api/v1/usuarios");

    console.log("Resposta da API");
    console.log("Endpoint:", resposta.url);
    console.log("Método: GET");
    console.log("Status:", resposta.status);
    console.log(resposta.statusText);

    if (!resposta.ok) {
      console.log("Erro ao buscar usuários");
      return;
    }

    const dados = await resposta.json();
    setUsuarios(dados);
  }

  return (
    <div className="main-section">
      <h1>Lista de Usuários</h1>

      <button onClick={buscarTodos}>Listar Usuários</button>
      <br /><br />

      <ul className="listaUsuarios">
        {usuarios
          .filter((usuario) => usuario.nome !== "admin")
          .map((usuario, index) => (
            <li key={index}>
              {usuario.nome} {usuario.sobrenome} - {usuario.email}
            </li>
          ))}
      </ul>

      <a className="link" href="/login">Voltar para login</a>
    </div>
  );
}