import Layout from "../layout/Layout";
import Titulo from "../ui/Titulo";

import guilherme from "../../src/assets/guilherme.jpg";
import lays from "../../src/assets/lays.jpg";
import leandro from "../../src/assets/leandro.jpg";
import maria from "../../src/assets/duda.jpg";
import matheus from "../../src/assets/matheus.jpg";
import rebeca from "../../src/assets/rebeca.jpg";

const integrantes = [
  {
    nome: "Guilherme Enrique",
    cargo: "Negócios & Back End",
    imagem: guilherme,
  },
  {
    nome: "Lays Abreu",
    cargo: "SCRUM Master & Banco de Dados",
    imagem: lays,
  },
  {
    nome: "Leandro Apolinário",
    cargo: "Product Owner & Full Stack",
    imagem: leandro,
  },
  {
    nome: "Maria Eduarda",
    cargo: "Back End",
    imagem: maria,
  },
  {
    nome: "Matheus Daniel",
    cargo: "Back End & Infraestrutura",
    imagem: matheus,
  },
  {
    nome: "Rebeca Oliveira",
    cargo: "Front End & Infraestrutura",
    imagem: rebeca,
  },
];

export default function QuemSomosSection() {
  return (
    <section className="bg-linear-to-b from-[#22344E] to-[#0E1116] py-8" id="quem-somos">
      <Layout>
        <div className="flex flex-col gap-8">

          <Titulo>Quem somos</Titulo>

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8">
            {integrantes.map((integrante, index) => (
              <div key={index} className="flex flex-col items-center text-center">

                <div className="w-56 h-44 rounded-lg border border-purple-500 overflow-hidden">
                  <img
                    src={integrante.imagem}
                    alt={integrante.nome}
                    className="w-full h-full object-cover"
                  />
                </div>

                <h3 className="mt-3 text-2xl font-semibold text-[#D6D6D6]">
                  {integrante.nome}
                </h3>

                <p className="text-xl text-[#D6D6D6]/80">
                  {integrante.cargo}
                </p>

              </div>
            ))}
          </div>

        </div>
      </Layout>
    </section>
  );
}